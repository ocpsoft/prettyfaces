/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com> This program
 * is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. You should have received a copy of the GNU Lesser General
 * Public License along with this program. If not, see the file COPYING.LESSER
 * or visit the GNU website at <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.faces.config.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ocpsoft.pretty.faces.el.ConstantExpression;
import com.ocpsoft.pretty.faces.el.PrettyExpression;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class QueryParameter extends RequestParameter
{
    private String validatorIds = "";
    private PrettyExpression validatorExpression;
    private String onError = "";
    private boolean onPostback = true;

    public QueryParameter()
    {
        super();
    }

    public QueryParameter(final String name, final String value)
    {
        super(name, value);
    }

    public QueryParameter(final String name, final String value, final PrettyExpression expression)
    {
        super(name, value, expression);
    }

    public boolean hasValidators()
    {
        return validatorIds.trim().length() > 0;
    }

    public List<String> getValidatorIdList()
    {
        List<String> result = new ArrayList<String>();
        if (hasValidators())
        {
            String[] ids = validatorIds.split(" ");
            Collections.addAll(result, ids);
        }
        return result;
    }

    public String getValidatorIds()
    {
        return validatorIds;
    }

    public void setValidatorIds(final String validatorIds)
    {
        this.validatorIds = validatorIds;
    }

    public String getOnError()
    {
        return onError;
    }

    public void setOnError(final String onError)
    {
        this.onError = onError;
    }

    public PrettyExpression getValidatorExpression()
    {
       return validatorExpression;
    }

    public void setValidatorExpression(PrettyExpression validatorExpression)
    {
       this.validatorExpression = validatorExpression;
    }

    /**
     * Extra setter method creating a {@link ConstantExpression} for the
     * validatorExpression. Used only for Digester only.
     */
    public void setValidator(String validator)
    {
       this.validatorExpression = new ConstantExpression(validator);
    }

    public boolean isOnPostback()
    {
       return onPostback;
    }

    public void setOnPostback(boolean onPostback)
    {
       this.onPostback = onPostback;
    }

}
