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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 *
 * @author rumatoest
 */
public class LocalDateConverter extends DateTimeConverter {

    public LocalDateConverter() {
        this.formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        LocalDate ld = LocalDate.parse(value, formatter);
        return ld;
    }
}
