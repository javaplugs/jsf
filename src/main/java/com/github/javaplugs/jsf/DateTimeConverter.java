/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Vladislav Zablotsky
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
package com.github.javaplugs.jsf;

import java.lang.reflect.Field;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * Base class for all Java 8 time converters.
 * Implements setters for formatter and pattern attributes.
 * This attributes allows you setup your date format and should not be used together.
 */
public abstract class DateTimeConverter implements Converter {

    protected DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    protected String pattern = null;

    /**
     * You can set any custom date format string.
     *
     * @param pattern format string for {@link DateTimeFormatter}
     */
    public void setPattern(String pattern) {
    	this.pattern = pattern;
    	this.formatter = DateTimeFormatter.ofPattern(pattern);
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
     * @param zoneId format string for {@link DateTimeFormatter}
     */
    public void setZoneId(String value) {
    	ZoneId zoneId = ZoneId.of(value);
    	if (this.pattern != null) {
        	this.formatter = DateTimeFormatter.ofPattern(this.pattern);
    	}
    	this.formatter = formatter.withZone(zoneId);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (!(value instanceof Temporal)) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        Temporal temporal = (Temporal)value;
        return formatter.format(temporal);
    }
}
