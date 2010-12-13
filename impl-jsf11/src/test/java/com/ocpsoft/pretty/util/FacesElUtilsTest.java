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
 * along with this program. If not, see the file COPYING.LESSER3 or visit the
 * GNU website at <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.util;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.lang.reflect.Method;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;

import org.junit.Test;

/**
 * @author Rafael Carvalho
 * @since 13/09/2010
 */
public class FacesElUtilsTest
{

   @Test
   public void testCallElMethodWithNoArguments()
            throws SecurityException, NoSuchMethodException
   {

      FacesContext context =
               createMock(FacesContext.class, new Method[] {
                        FacesContext.class.getMethod("getApplication") });
      Application application =
               createMock(Application.class, new Method[] {
                        Application.class.getMethod("createMethodBinding", String.class, Class[].class) });
      MethodBinding methodBinding = createMock(MethodBinding.class);

      String expression = "aGivenExpression";

      // Set expectations
      expect(context.getApplication()).andReturn(application);
      expect(application.createMethodBinding(expression, null)).andReturn(methodBinding);
      expect(methodBinding.invoke(context, null)).andReturn(new Object());
      replay(context, application, methodBinding);

      // Execute behavior
      FacesElUtils facesElUtils = new FacesElUtils();
      facesElUtils.invokeMethod(context, expression);

      // Verify expectations
      verify(context);
   }
}
