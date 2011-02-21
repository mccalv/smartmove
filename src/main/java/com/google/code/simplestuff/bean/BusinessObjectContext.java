/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.code.simplestuff.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import com.google.code.simplestuff.annotation.BusinessField;
import com.google.code.simplestuff.annotation.BusinessObject;

/**
 * A cache of <code>{@link BusinessObjectDescriptor}</code>s.
 * <p>
 * Thread-safe as long as there are not two different classes with the same
 * (full) name in the environment in which the <code>BusinessObjectUtils</code>
 * are used.
 * 
 * @author Andrew Phillips
 * @author Vincenzo Vitale
 * @see BusinessObjectUtils
 * @since 9 Jul 2008
 * 
 */
class BusinessObjectContext {
    private static final Map<Class<? extends Object>, BusinessObjectDescriptor> BUSINESS_OBJECT_DESCRIPTORS =
            new ConcurrentHashMap<Class<? extends Object>, BusinessObjectDescriptor>();

    /**
     * Private so that this class cannot be instantiated.
     */
    private BusinessObjectContext() {
        throw new AssertionError("Don't instantiate me.");
    }

    /**
     * Retrieve a {@link BusinessObjectDescriptor} object for a
     * {@link BusinessObject} class bean.
     * 
     * @param objectClass The class of the bean to check.
     * @return A {@link BusinessObjectDescriptor} for the bean class passed.
     */
    static BusinessObjectDescriptor getBusinessObjectDescriptor(
            Class<? extends Object> objectClass) {

        if (!BUSINESS_OBJECT_DESCRIPTORS.containsKey(objectClass)) {
            BUSINESS_OBJECT_DESCRIPTORS.put(objectClass,
                    getBusinessObjectDescriptor(objectClass, objectClass));
        }

        return BUSINESS_OBJECT_DESCRIPTORS.get(objectClass);
    }

    /**
     * Retrieve a {@link BusinessObjectDescriptor} object for a
     * {@link BusinessObject} class bean.
     * 
     * @param objectClass The class of the bean to check.
     * @param annotationObjectClass The class to use for retrieving the
     *        {@link BusinessField} annotated field.
     * @return A {@link BusinessObjectDescriptor} for the bean class passed.
     */
    private static BusinessObjectDescriptor getBusinessObjectDescriptor(
            Class<? extends Object> objectClass,
            Class<? extends Object> annotationObjectClass) {

        BusinessObjectDescriptor businessObjectDescriptor =
                new BusinessObjectDescriptor();

        if (objectClass == null) {
            businessObjectDescriptor.setNearestBusinessObjectClass(null);
            return businessObjectDescriptor;
        } else {
            if (objectClass.isAnnotationPresent(BusinessObject.class)) {

                businessObjectDescriptor
                        .setAnnotatedFields(getAnnotatedFields(annotationObjectClass));
                businessObjectDescriptor
                        .setClassToBeConsideredInComparison(objectClass
                                .getAnnotation(BusinessObject.class)
                                .includeClassAsBusinessField());
                businessObjectDescriptor
                        .setNearestBusinessObjectClass(objectClass);
                return businessObjectDescriptor;
            } else {
                return getBusinessObjectDescriptor(objectClass.getSuperclass(),
                        annotationObjectClass);
            }

        }
    }

    /**
     * Returns an array of all fields used by this object from it's class and
     * all super classes.
     * 
     * @author Andrew Phillips (anph)
     * @param objectClass the class
     * @param fields the current field list
     * @return an array of fields
     * 
     */
    private static Collection<Field> getAnnotatedFields(
            Class<? extends Object> objectClass) {
        final List<Field> businessFields = new ArrayList<Field>();

        ReflectionUtils.doWithFields(objectClass, new FieldCallback() {

            // simply add each found field to the list
            public void doWith(Field field) throws IllegalArgumentException,
                    IllegalAccessException {
                businessFields.add(field);
            }

        }, new FieldFilter() {

            // match fields with the "@BusinessField" annotation
            public boolean matches(Field field) {
                return (field.getAnnotation(BusinessField.class) != null);
            }

        });

        return businessFields;
    }

}