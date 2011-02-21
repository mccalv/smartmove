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
package com.google.code.simplestuff.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that specifies if a field have a business meaning.
 * 
 * @author Vincenzo Vitale (vincenzo.vitale)
 * @since Jul 08, 2008
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BusinessField {

    /**
     * Returns if this field has to be considered in the equals() method.
     * 
     * @return <code>true</code> if the field has to be considered when
     *         evaluating the equals().
     */
    boolean includeInEquals() default true;

    /**
     * Returns if this field has to be considered in the hashCode() method.
     * 
     * @return <code>true</code> if the field has to be considered when
     *         evaluating the hashCode().
     */
    boolean includeInHashCode() default true;

    /**
     * Returns if this field has to be considered in the toString() method.
     * 
     * @return <code>true</code> if the field has to be considered when
     *         evaluating the toString().
     */
    boolean includeInToString() default true;

}
