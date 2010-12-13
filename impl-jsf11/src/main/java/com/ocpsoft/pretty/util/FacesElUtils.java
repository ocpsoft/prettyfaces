/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * 
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see the file COPYING.LESSER or visit the GNU
 * website at <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.util;

import java.util.regex.Pattern;

import javax.el.ELException;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

/**
 * @author lb3
 */
public class FacesElUtils
{
    public static final String EL_REGEX = "\\#\\{[\\w\\.]{1,}\\}";
    public static final Pattern elPattern = Pattern.compile(EL_REGEX);

    public boolean isEl(final String viewId)
    {
        if (viewId == null)
        {
            return false;
        }
        return elPattern.matcher(viewId).matches();
    }

    public Object invokeMethod(final FacesContext context, final String expression) throws ELException
    {       
        MethodBinding methodBinding = context.getApplication().createMethodBinding(expression, null);
        return methodBinding.invoke(context, null);
    }

    public void setValue(final FacesContext context, final String expression, final Object value) throws ELException
    {
        ValueBinding valueBinding = context.getApplication().createValueBinding(expression);
        Class<?> type = valueBinding.getType(context);
        Converter converter = ConvertUtils.lookup(type);
        if (converter == null)
        {
            throw new UnsupportedOperationException("Cant deal with " + type);
        }
        valueBinding.setValue(context, converter.convert(type, value));
    }

    public Object getValue(final FacesContext context, final String expression) throws ELException
    {
        ValueBinding valueBinding = context.getApplication().createValueBinding(expression);
        return valueBinding.getValue(context);
    }

    public Class<?> getExpectedType(final FacesContext context, final String expression) throws ELException
    {
        ValueBinding valueBinding = context.getApplication().createValueBinding(expression);
        return valueBinding.getType(context);
    }
}
