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
