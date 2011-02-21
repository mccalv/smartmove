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
import java.util.Collection;

import com.google.code.simplestuff.annotation.BusinessObject;

/**
 * Container maintaining "Business Object" information for an object.
 * 
 * @author Vincenzo Vitale (vita)
 * 
 */
public class BusinessObjectDescriptor {

    /**
     * <code>true</code> if the class has to be considered in the equals
     * comparison.
     */
    private Boolean classToBeConsideredInComparison;

    /**
     * The first annotated class in the object comparison. If this field is
     * <code>null</code> than the object is not a {@link BusinessObject}.
     */
    private Class<? extends Object> nearestBusinessObjectClass;

    /**
     * The collection of BusinessField annotated fields.
     */
    private Collection<Field> annotatedFields;

    /**
     * @return the classToBeConsideredInComparison
     */
    public Boolean isClassToBeConsideredInComparison() {
        return classToBeConsideredInComparison;
    }

    /**
     * @param classToBeConsideredInComparison the
     *        classToBeConsideredInComparison to set
     */
    public void setClassToBeConsideredInComparison(
            Boolean classToBeConsideredInComparison) {
        this.classToBeConsideredInComparison = classToBeConsideredInComparison;
    }

    /**
     * @return the nearestBusinessObjectClass
     */
    public Class<? extends Object> getNearestBusinessObjectClass() {
        return nearestBusinessObjectClass;
    }

    /**
     * @param nearestBusinessObjectClass the nearestBusinessObjectClass to set
     */
    public void setNearestBusinessObjectClass(
            Class<? extends Object> nearestBusinessObjectClass) {
        this.nearestBusinessObjectClass = nearestBusinessObjectClass;
    }

    /**
     * @return the annotatedFields
     */
    public Collection<Field> getAnnotatedFields() {
        return annotatedFields;
    }

    /**
     * @param annotatedFields the annotatedFields to set
     */
    public void setAnnotatedFields(Collection<Field> annotatedFields) {
        this.annotatedFields = annotatedFields;
    }
}
