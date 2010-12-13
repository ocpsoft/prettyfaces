/*
 * Copyright 2010 Lincoln Baxter, III
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
