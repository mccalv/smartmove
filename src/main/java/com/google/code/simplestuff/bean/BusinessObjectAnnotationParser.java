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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Utility class which retrieves a &quot;business key&quot; from an object by
 * inspecting <code>BusinessField</code> annotations.
 * 
 * @author anph
 * @see com.tomtom.commons.annotation.BusinessObject
 * @see com.tomtom.commons.annotation.BusinessField
 * @since 6 Sep 2007
 * 
 */
class BusinessObjectAnnotationParser {

    /**
     * Retrieves a &quot;field-value&quot; map of all properties of the given
     * object annotated as <code>BusinessField</code>s.
     * <p>
     * These should constitute a &quot;business key&quot; for this object
     * 
     * @param object the object whose properties should be examined
     * @return a map of all appropriately annotated fields
     */
    static Map<String, Object> retrieveBusinessFields(Object object) {
        Map<String, Object> businessProperties = new HashMap<String, Object>();

        for (Field field : collectAnnotatedFields(object.getClass())) {
            String propertyName = field.getName();

            try {
                businessProperties.put(propertyName, PropertyUtils.getProperty(
                        object, propertyName));
            } catch (Exception exception) {

                // ignore any of the exceptions that can be thrown and simply
                // skip the property
            }

        }

        return businessProperties;
    }

    static Collection<Field> collectAnnotatedFields(
            Class<? extends Object> clazz) {
        final List<Field> businessFields = new ArrayList<Field>();

        ReflectionUtils.doWithFields(clazz, new FieldCallback() {

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
