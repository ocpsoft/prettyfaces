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
package com.ocpsoft.pretty.component;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import com.ocpsoft.pretty.util.FacesElUtils;

/**
 * @author lb3
 */
abstract public class PrettyTagBase extends UIComponentTag
{
    private static final FacesElUtils elUtils = new FacesElUtils();

    protected void setAttributeProperites(final UIComponent component, final String attributeName,
            final String attribute)
    {
        if (attribute != null)
        {
            if (elUtils.isEl(attribute))
            {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                
                ValueBinding valueBinding = app.createValueBinding(attribute);
                component.setValueBinding(attributeName, valueBinding);
            }
            else
            {
                component.getAttributes().put(attributeName, attribute);
            }
        }
    }
}
