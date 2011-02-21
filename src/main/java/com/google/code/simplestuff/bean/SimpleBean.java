/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.code.simplestuff.bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.code.simplestuff.annotation.BusinessField;
import com.google.code.simplestuff.annotation.BusinessObject;

/**
 * Simple utility class for Java bean enhancement.
 * 
 * @author Vincenzo Vitale
 * @author Salomo Petrus
 * @author Andrew Phillips
 * @since Jul 08, 2008
 * 
 */
public class SimpleBean {

    /** Logger for this class */
    private static Log log = LogFactory.getLog(SimpleBean.class);

    /**
     * Compare two bean basing the comparison only on the {@link BusinessField}
     * annotated fields.
     * 
     * @param firstBean First bean to compare.
     * @param secondBean Second bean to compare.
     * @return The equals result.
     * @throws IllegalArgumentException If one of the beans compared is not an
     *         instance of a {@link BusinessObject} annotated class.
     */
    public static boolean equals(Object firstBean, Object secondBean) {

        // null + !null = false
        // null + null = true
        if ((firstBean == null) || (secondBean == null)) {
            if ((firstBean == null) && (secondBean == null)) {
                return true;
            } else {
                return false;
            }
        }

        final BusinessObjectDescriptor firstBusinessObjectInfo =
                BusinessObjectContext.getBusinessObjectDescriptor(firstBean
                        .getClass());
        final BusinessObjectDescriptor secondBusinessObjectInfo =
                BusinessObjectContext.getBusinessObjectDescriptor(secondBean
                        .getClass());

        // We don't need here a not null check since by contract the
        // getBusinessObjectDescriptor method always returns an abject.
        
        // All this conditions are to support the case in which
        // SimpleBean.equals is used in not Business Object. Than the rules are:
        // !BO.equals(!BO) = The objects are equals if one of them is assignable
        // to the other (the or is used for the respect the symmetric rule)
        // !BO.eqauls(BO) = The equals of the !BO is used.
        // BO.equals(!BO) = The equals of the !BO is used.
        if (firstBusinessObjectInfo.getNearestBusinessObjectClass() == null) {
            if (secondBusinessObjectInfo.getNearestBusinessObjectClass() == null) {
                return firstBean.getClass().isAssignableFrom(
                        secondBean.getClass())
                        || secondBean.getClass().isAssignableFrom(
                                firstBean.getClass());
            } else {
                return firstBean.equals(secondBean);
            }
        } else if (secondBusinessObjectInfo.getNearestBusinessObjectClass() == null) {
            return secondBean.equals(firstBean);
        }

        // TODO: Revise this code in order to make it more readable...
        // If one of the two bean has the class with Business relevance then
        // we need to compare the lowest hierarchical annotated classes of
        // the two beans.
        if ((firstBusinessObjectInfo.isClassToBeConsideredInComparison() || secondBusinessObjectInfo
                .isClassToBeConsideredInComparison())
                && (!firstBusinessObjectInfo.getNearestBusinessObjectClass()
                        .equals(
                                secondBusinessObjectInfo
                                        .getNearestBusinessObjectClass()))) {
            // If the comparison fails than we can already return false.
            return false;
        }

        // Then we continue with the annotated fields, first checking
        // if the two objects contain the same annotated fields. A paranoid
        // comparison (both sides) is done only if the two objects are not on
        // the same class in order to handle tricky cases.
        final boolean performParanoidComparison = false;
        if (!compareAnnotatedFieldsByName(firstBusinessObjectInfo
                .getAnnotatedFields(), secondBusinessObjectInfo
                .getAnnotatedFields(), performParanoidComparison)) {
            // If the comparison fails than we can already return false.
            return false;
        }

        // Then we continue with the values of the annotated fields.
        Collection<Field> firstBeanAnnotatedFields =
                firstBusinessObjectInfo.getAnnotatedFields();

        for (Field field : firstBeanAnnotatedFields) {

            final BusinessField fieldAnnotation =
                    field.getAnnotation(BusinessField.class);
            // Since the cycle is on the annotated Field we are sure that
            // fieldAnnotation will always be not null.
            if (fieldAnnotation.includeInEquals()) {

                Object firstBeanFieldValue = null;
                Object secondBeanFieldValue = null;

                try {
                    firstBeanFieldValue =
                            PropertyUtils.getProperty(firstBean, field
                                    .getName());
                    // Also in this case, since before of the cycle we
                    // compare the annotated fields, we can be sure that the
                    // field exists.
                    secondBeanFieldValue =
                            PropertyUtils.getProperty(secondBean, field
                                    .getName());

                    // If there were problems (like when we compare
                    // different Business Object with different Business
                    // Fields), then we return false.
                } catch (IllegalAccessException e) {
                    if (log.isDebugEnabled()) {
                        log.debug(
                                "IllegalAccessException exception when comparing class "
                                        + firstBean.getClass().toString()
                                        + " with class"
                                        + secondBean.getClass().toString(), e);
                    }
                    return false;
                } catch (InvocationTargetException e) {
                    if (log.isDebugEnabled()) {
                        log.debug(
                                "InvocationTargetException exceptionwhen comparing class "
                                        + firstBean.getClass().toString()
                                        + " with class"
                                        + secondBean.getClass().toString(), e);
                    }
                    return false;
                } catch (NoSuchMethodException e) {
                    if (log.isDebugEnabled()) {
                        log.debug(
                                "NoSuchMethodException exception when comparing class "
                                        + firstBean.getClass().toString()
                                        + " with class"
                                        + secondBean.getClass().toString(), e);
                    }
                    return false;
                }

                // Some date implementations give not exact
                // comparison...
                if ((ClassUtils.isAssignable(field.getType(), Date.class))
                        || (ClassUtils.isAssignable(field.getType(),
                                Calendar.class))) {

                    if (firstBeanFieldValue != null) {
                        firstBeanFieldValue =
                                DateUtils.round(firstBeanFieldValue,
                                        Calendar.MILLISECOND);
                    }

                    if (secondBeanFieldValue != null) {
                        secondBeanFieldValue =
                                DateUtils.round(secondBeanFieldValue,
                                        Calendar.MILLISECOND);
                    }

                }

                // We use always EqualsBuilder since we can get also
                // primitive arrays and they need ot be internally
                // compared.
                EqualsBuilder equalsBuilder = new EqualsBuilder();
                equalsBuilder.append(firstBeanFieldValue, secondBeanFieldValue);
                if (!equalsBuilder.isEquals()) {
                    return false;
                } else {

                    // If we are here the bean are both not null and
                    // equals or both null (then equals)... the cycle
                    // can
                    // continue...
                    continue;
                }

            }
        }

        // If we finally arrive here, then all the comparison were
        // successful and the two beans are equals.
        return true;

    }

    /**
     * Returns true if the two {@link Field} object collections are equals,
     * basing the comparison only on the name and not on the class of the
     * fields.
     * 
     * @param firstAnnotatedFields The first collection to compare
     * @param secondAnnotatedFields The second collection to compare
     * @param paranoidComparison If a double comparison has to be done
     *        (comparing first and second and then second and first.
     * @return If the two collection contain the same fields by name.
     */
    private static boolean compareAnnotatedFieldsByName(
            Collection<Field> firstAnnotatedFields,
            Collection<Field> secondAnnotatedFields, boolean paranoidComparison) {

        // TODO: Probably this code can be improved in performances. BTW
        // consider that it could be possible that parent and child have the
        // same private property and this will result in two identical (from the
        // business point of view) fields in the collection. This also means we
        // cannot speed up performances first comparing the collections sizes.
        for (Field firstField : firstAnnotatedFields) {
            boolean fieldFound = false;
            for (Field secondField : secondAnnotatedFields) {
                if (firstField.getName().equals(secondField.getName())) {
                    fieldFound = true;
                    // Field found we can exit the second inner cycle.
                    break;
                }
            }

            // If we are the current field of the first collection wasn't found
            // in the second one... we can directly return false
            if (!fieldFound) {
                return false;
            }
        }

        // If the paranoidComparison is true, we repeat the same operation
        // comparing the second collection with the first one. The
        // paranoicComparison prevent tricky cases in which for example for
        // the first bean parent a child have the same annotated field (same
        // name) and for the second one we have the same number of annotated
        // fields of the first one. In this case neither comparing size of
        // the second collection and successful matches would help.
        if (paranoidComparison) {
            compareAnnotatedFieldsByName(secondAnnotatedFields,
                    firstAnnotatedFields, false);
        }

        // If we are here all the fields of the first collection were
        // successfully found in the second collection and vice versa.
        return true;
    }

    /**
     * 
     * Returns the hashCode basing considering only the {@link BusinessField}
     * annotated fields.
     * 
     * @param bean The bean.
     * @return The hashCode result.
     * @throws IllegalArgumentException If the bean is not a Business Object.
     */
    public static int hashCode(Object bean) {

        if (bean == null) {
            throw new IllegalArgumentException("The bean passed is null!!!");
        }

        BusinessObjectDescriptor businessObjectInfo =
                BusinessObjectContext.getBusinessObjectDescriptor(bean
                        .getClass());

        // We don't need here a not null check since by contract the
        // getBusinessObjectDescriptor method always returns an abject.
        if (businessObjectInfo.getNearestBusinessObjectClass() == null) {
            return bean.hashCode();
            // throw new IllegalArgumentException(
            // "The bean passed is not annotated in the hierarchy as Business Object!!!");
        }

        Collection<Field> annotatedField =
                businessObjectInfo.getAnnotatedFields();

        HashCodeBuilder builder = new HashCodeBuilder();
        for (Field field : annotatedField) {
            field.setAccessible(true);
            final BusinessField fieldAnnotation =
                    field.getAnnotation(BusinessField.class);
            if ((fieldAnnotation != null)
                    && (fieldAnnotation.includeInHashCode())) {
                try {
                    // Vincenzo Vitale(vita) May 23, 2007 2:39:26 PM: some
                    // date implementations give not equals values...
                    if ((ClassUtils.isAssignable(field.getType(), Date.class))
                            || (ClassUtils.isAssignable(field.getType(),
                                    Calendar.class))) {
                        Object fieldValue =
                                PropertyUtils
                                        .getProperty(bean, field.getName());
                        if (fieldValue != null) {
                            builder.append(DateUtils.round(fieldValue,
                                    Calendar.MILLISECOND));
                        }

                    } else {
                        builder.append(PropertyUtils.getProperty(bean, field
                                .getName()));
                    }
                } catch (IllegalAccessException e) {
                    if (log.isDebugEnabled()) {
                        log.debug(
                                "IllegalAccessException exception when calculating the hashcode of class"
                                        + bean.getClass().toString(), e);
                    }
                } catch (InvocationTargetException e) {
                    if (log.isDebugEnabled()) {
                        log.debug(
                                "InvocationTargetException exception when calculating the hashcode of class"
                                        + bean.getClass().toString(), e);
                    }
                } catch (NoSuchMethodException e) {
                    if (log.isDebugEnabled()) {
                        log.debug(
                                "NoSuchMethodException exception when calculating the hashcode of class"
                                        + bean.getClass().toString(), e);
                    }
                }
            }
        }

        return builder.toHashCode();

    }

    /**
     * 
     * Returns the description of a bean considering only the
     * {@link BusinessField} annotated fields.
     * 
     * @param bean The bean to describe.
     * @return The description of the bean.
     * @throws IllegalArgumentException If the bean is not a Business Object.
     */
    public static String toString(Object bean) {

        if (bean == null) {
            throw new IllegalArgumentException("The bean passed is null!!!");
        }

        BusinessObjectDescriptor businessObjectInfo =
                BusinessObjectContext.getBusinessObjectDescriptor(bean
                        .getClass());

        if (businessObjectInfo == null) {
            return bean.toString();
            // throw new IllegalArgumentException(
            // "The bean passed is not annotated in the hierarchy as Business Object!!!");
        }

        Collection<Field> annotatedField =
                businessObjectInfo.getAnnotatedFields();
        ToStringBuilder builder =
                new ToStringBuilder(bean, ToStringStyle.MULTI_LINE_STYLE);
        for (Field field : annotatedField) {
            final BusinessField fieldAnnotation =
                    field.getAnnotation(BusinessField.class);
            if ((fieldAnnotation != null)
                    && (fieldAnnotation.includeInToString())) {
                try {
                    // Vincenzo Vitale(vita) May 23, 2007 2:39:26 PM: some
                    // date implementations give not equals values...
                    if ((ClassUtils.isAssignable(field.getType(), Date.class))
                            || (ClassUtils.isAssignable(field.getType(),
                                    Calendar.class))) {
                        Object fieldValue =
                                PropertyUtils
                                        .getProperty(bean, field.getName());
                        if (fieldValue != null) {
                            builder.append(DateUtils.round(fieldValue,
                                    Calendar.SECOND));
                        }

                    } else {
                        builder.append(PropertyUtils.getProperty(bean, field
                                .getName()));
                    }
                } catch (IllegalAccessException e) {
                    if (log.isDebugEnabled()) {
                        log
                                .debug(
                                        "IllegalAccessException exception when calculating the string representation of class"
                                                + bean.getClass().toString(), e);
                    }
                } catch (InvocationTargetException e) {
                    if (log.isDebugEnabled()) {
                        log
                                .debug(
                                        "InvocationTargetException exception when calculating the string representation of class"
                                                + bean.getClass().toString(), e);
                    }
                } catch (NoSuchMethodException e) {
                    if (log.isDebugEnabled()) {
                        log
                                .debug(
                                        "NoSuchMethodException exception when calculating the string representation of a bean of class"
                                                + bean.getClass().toString(), e);
                    }
                }
            }
        }

        return builder.toString();

    }

    /**
     * 
     * Returns a test object with all the {@link BusinessField} annotated fields
     * set to a test value. TODO At the moment only the String field are
     * considered and the collection are not considered.
     * 
     * @param bean The class of the bean to fill.
     * @param suffix The suffix to append in the string field.
     * @return The bean with test values.
     */
    public static <T> T getTestBean(Class<T> beanClass, String suffix) {
        if (beanClass == null) {
            throw new IllegalArgumentException(
                    "The bean class passed is null!!!");
        }

        T testBean = null;
        try {
            testBean = beanClass.newInstance();
        } catch (InstantiationException e1) {
            if (log.isDebugEnabled()) {
                log.debug(e1.getMessage());
            }
        } catch (IllegalAccessException e1) {
            if (log.isDebugEnabled()) {
                log.debug(e1.getMessage());
            }
        }

        BusinessObjectDescriptor businessObjectInfo =
                BusinessObjectContext.getBusinessObjectDescriptor(beanClass);

        // We don't need here a not null check since by contract the
        // getBusinessObjectDescriptor method always returns an abject.
        if (businessObjectInfo.getNearestBusinessObjectClass() != null) {

            Collection<Field> annotatedField =
                    businessObjectInfo.getAnnotatedFields();
            for (Field field : annotatedField) {
                final BusinessField fieldAnnotation =
                        field.getAnnotation(BusinessField.class);
                if (fieldAnnotation != null) {
                    try {
                        if (field.getType().equals(String.class)) {
                            String stringValue =
                                    "test"
                                            + StringUtils.capitalize(field
                                                    .getName())
                                            + (suffix == null ? "" : suffix);
                            PropertyUtils.setProperty(testBean,
                                    field.getName(), stringValue);

                        } else if ((field.getType().equals(boolean.class))
                                || (field.getType().equals(Boolean.class))) {
                            PropertyUtils.setProperty(testBean,
                                    field.getName(), true);
                        } else if ((field.getType().equals(int.class))
                                || (field.getType().equals(Integer.class))) {
                            PropertyUtils.setProperty(testBean,
                                    field.getName(), 10);
                        } else if ((field.getType().equals(char.class))
                                || (field.getType().equals(Character.class))) {
                            PropertyUtils.setProperty(testBean,
                                    field.getName(), 't');
                        } else if ((field.getType().equals(long.class))
                                || (field.getType().equals(Long.class))) {
                            PropertyUtils.setProperty(testBean,
                                    field.getName(), 10L);
                        } else if ((field.getType().equals(float.class))
                                || (field.getType().equals(Float.class))) {
                            PropertyUtils.setProperty(testBean,
                                    field.getName(), 10F);
                        } else if ((field.getType().equals(byte.class))
                                || (field.getType().equals(Byte.class))) {
                            PropertyUtils.setProperty(testBean,
                                    field.getName(), (byte) 10);
                        } else if (field.getType().equals(Date.class)) {
                            PropertyUtils.setProperty(testBean,
                                    field.getName(), new Date());
                        } else if (field.getType().equals(Collection.class)) {
                            // TODO: create a test object of the collection
                            // class specified (if one is specified and
                            // recursively call this method.
                        }

                    } catch (IllegalAccessException e) {
                        if (log.isDebugEnabled()) {
                            log.debug(e.getMessage());
                        }
                    } catch (InvocationTargetException e) {
                        if (log.isDebugEnabled()) {
                            log.debug(e.getMessage());
                        }
                    } catch (NoSuchMethodException e) {
                        if (log.isDebugEnabled()) {
                            log.debug(e.getMessage());
                        }
                    }
                }
            }
        }

        return testBean;
    }
}
