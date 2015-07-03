/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 by rumatoest at github.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE. 
 */
package com.github.jneat.jsf;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Base class for all Java 8 time converters.
 * Implements setters for formatter and pattern attributes.
 * This attributes allows you setup your date format and should not be used together.
 */
public abstract class DateTimeConverter extends javax.faces.convert.DateTimeConverter implements Converter {

    private DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    /**
     * You can set any custom date format string.
     *
     * @param pattern format string for {@link DateTimeFormatter}
     */
    @Override
    public void setPattern(String pattern) {
        super.setPattern(pattern);
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    protected DateTimeFormatter getFormatter() {
        return this.formatter;
    }

    protected void setFormatter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    public DateTimeFormatter getFormatterWithUiComponent(UIComponent component) {
        String pattern = this.getPatternFromUiComponent(component);
        if (pattern != null) {
            this.setPattern(pattern);
        }

        return this.formatter;
    }

    /**
     * Set predefined formatter {@link DateTimeFormatter} to output/parse date.
     *
     * @param value Formatter constant name from {@link DateTimeFormatter}
     */
    public void setFormatter(String value) {
        try {
            Class<DateTimeFormatter> dtfCls = DateTimeFormatter.class;
            Field field = dtfCls.getField(value);

            if (field.getType().getCanonicalName().equals("java.time.format.DateTimeFormatter")) {
                this.formatter = (DateTimeFormatter)field.get(null);
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    /**
     * Set the {@link ZoneId} for the {@link DateTimeFormatter}
     *
     * @param zoneIdName format string for {@link DateTimeFormatter}
     */
    public void setZoneId(String zoneIdName) {
        ZoneId zoneId = ZoneId.of(zoneIdName);
        if (this.getPattern() != null) {
            this.formatter = DateTimeFormatter.ofPattern(this.getPattern());
        }
        this.formatter = formatter.withZone(zoneId);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String pattern = getPatternFromUiComponent(component);
        if (pattern != null) {
            setPattern(pattern);
        }

        if (!(value instanceof Temporal)) {
            return super.getAsString(context, component, value);
        }
        Temporal temporal = (Temporal)value;
        return formatter.format(temporal);
    }

    /**
     * This method is useful for, for example, org.primefaces.component.calendar.Calendar.
     * It allows to write in xhtml something like that: <br>
     * {@code
     * <p:calendar value="#{managedBean.localDate}" showOn="button"
     * showButtonPanel="true" pattern="dd-MM-yyyy" converter="jsfplugs.LocalDate"/>
     * }
     *
     * @param component UIComponent
     * @return pattern if method String getPattern() is available on UIComponent or null
     */
    protected String getPatternFromUiComponent(UIComponent component) {
        Method[] methods = component.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals("getPattern") && String.class.isAssignableFrom(method.getReturnType()) &&
                method.getParameters().length == 0) {

                try {
                    return (String) method.invoke(component);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return null;
                }
            }
        }
        return null;
    }
}
