/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 *
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com>
 *
 * This program is free software: you can redistribute it and/or modify it
under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see the file COPYING.LESSER or visit the
GNU
 * website at <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.faces.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIParameter;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.faces.component.Link;
import com.ocpsoft.pretty.faces.config.mapping.QueryParameter;
import com.ocpsoft.pretty.faces.config.mapping.UrlAction;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;

/**
 * @author lb3
 */
public class PrettyURLBuilderTest
{
   static Link link = new Link();
   static UrlMapping mapping = new UrlMapping();
   static UIParameter param1 = new UIParameter();
   static UIParameter param2 = new UIParameter();
   static UIParameter param3 = new UIParameter();
   static UIParameter param4 = new UIParameter();
   static UIParameter param5 = new UIParameter();
   static String expectedPath = "";

   static List<Object> values = new ArrayList<Object>();
   static Object[] valuesArray;

   @BeforeClass
   public static void setUpBeforeClass() throws Exception
   {
      mapping.addAction(new UrlAction("#{bean.action}"));
      mapping.setId("testMapping");
      mapping.setPattern("/test/#{bean.param1}/mapping/#{bean.param2}");
      mapping.addQueryParam(new QueryParameter("key1", "#{bean.qp1}"));
      mapping.addQueryParam(new QueryParameter("key2", "#{bean.qp2}"));

      param1.setName("key1");
      param1.setValue("qp1");
      link.getChildren().add(param1);

      param2.setValue("up1");
      link.getChildren().add(param2);
      values.add(param2.getValue());

      param3.setName("key2");
      param3.setValue("qp2");
      link.getChildren().add(param3);

      param4.setValue("up2");
      link.getChildren().add(param4);
      values.add(param4.getValue());
      valuesArray = values.toArray();

      param5.setName("double");
      param5.setValue(new Object[] { "12", "34" });
      link.getChildren().add(param5);

      expectedPath = "/test/" + param2.getValue() + "/mapping/" + param4.getValue();

      link.getAttributes().put("mappingId", mapping.getId());
   }

   private final PrettyURLBuilder builder = new PrettyURLBuilder();

   @Test
   public void testExtractParameters()
   {
      List<UIParameter> parameters = builder.extractParameters(link);
      assertEquals(5, parameters.size());
      assertTrue(parameters.contains(param1));
      assertTrue(parameters.contains(param2));
      assertTrue(parameters.contains(param3));
      assertTrue(parameters.contains(param4));
      assertTrue(parameters.contains(param5));
   }

   @Test
   public void testBuildMappedUrlPrettyUrlMappingListOfUIParameter()
   {
      List<UIParameter> parameters = builder.extractParameters(link);
      String mappedUrl = builder.build(mapping, parameters);
      assertTrue(mappedUrl.startsWith(expectedPath));
      assertTrue(mappedUrl.contains(param1.getName() + "=" + param1.getValue()));
      assertTrue(mappedUrl.contains(param3.getName() + "=" + param3.getValue()));
   }

   @Test
   public void testBuildMappedUrlPrettyUrlMappingListOfUIParameterContainsArrayQueryParam()
   {
      List<UIParameter> parameters = builder.extractParameters(link);
      String mappedUrl = builder.build(mapping, parameters);
      assertTrue(mappedUrl.startsWith(expectedPath));
      assertTrue(mappedUrl.contains(param5.getName() + "=" + ((Object[]) param5.getValue())[0]));
      assertTrue(mappedUrl.contains(param5.getName() + "=" + ((Object[]) param5.getValue())[1]));
   }

   @Test
   public void testBuildMappedUrlPrettyUrlMappingSingleParameterContainingList()
   {
      List<UIParameter> parameters = new ArrayList<UIParameter>();
      UIParameter param = new UIParameter();
      param.setValue(values);
      parameters.add(param);

      String mappedUrl = builder.build(mapping, parameters);
      assertTrue(mappedUrl.startsWith(expectedPath));
   }

   @Test
   public void testBuildMappedUrlPrettyUrlMappingSingleParameterContainingArray()
   {
      List<UIParameter> parameters = new ArrayList<UIParameter>();
      UIParameter param = new UIParameter();
      param.setValue(valuesArray);
      parameters.add(param);

      String mappedUrl = builder.build(mapping, parameters);
      assertTrue(mappedUrl.startsWith(expectedPath));
   }

   @Test(expected = PrettyException.class)
   public void testBuildMappedUrlPrettyUrlMappingSingleNamedParameterDefaultsToNonListBuild()
   {
      List<UIParameter> parameters = new ArrayList<UIParameter>();
      UIParameter param = new UIParameter();
      param.setValue(values);
      param.setName("something");
      parameters.add(param);

      builder.build(mapping, parameters);
   }

}
