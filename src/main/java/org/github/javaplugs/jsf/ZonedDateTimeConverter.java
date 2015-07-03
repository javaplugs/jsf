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
package org.github.javaplugs.jsf;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * Converter for Java 8 ZonedDateTime.
 */
public class ZonedDateTimeConverter implements Converter {

    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    String formatterName = "ISO_DATE_TIME";

    /**
     * Return name for used now {@link DateTimeFormatter}
     */
    public String getFormatter() {
        return formatterName;
    }

    /**
     * Set name for {@link DateTimeFormatter} for output/parse date
     */
    public void setFormatter(String value) {
        try {
            Class<DateTimeFormatter> dtfCls = DateTimeFormatter.class;
            Field field = dtfCls.getField(value);

            if (field.getType().getCanonicalName().equals("DateTimeFormatter")) {
                this.formatterName = field.getName();
                this.formatter = (DateTimeFormatter)field.get(null);
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        ZonedDateTime zdt = ZonedDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
        return zdt;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (!(value instanceof ChronoZonedDateTime)) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        ChronoZonedDateTime dateTime = (ChronoZonedDateTime)value;

        return dateTime.format(formatter);
    }
}
