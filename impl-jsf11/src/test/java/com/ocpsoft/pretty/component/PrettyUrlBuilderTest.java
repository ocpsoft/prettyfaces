// /*
// * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
// *
// * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com>
// *
// * This program is free software: you can redistribute it and/or modify it
// under
// * the terms of the GNU Lesser General Public License as published by the Free
// * Software Foundation, either version 3 of the License, or (at your option)
// any
// * later version.
// *
// * This program is distributed in the hope that it will be useful, but WITHOUT
// * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS
// * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
// more
// * details.
// *
// * You should have received a copy of the GNU Lesser General Public License
// * along with this program. If not, see the file COPYING.LESSER or visit the
// GNU
// * website at <http://www.gnu.org/licenses/>.
// */
// package com.ocpsoft.pretty.component;
//
// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertTrue;
//
// import java.util.List;
//
// import javax.faces.component.UIParameter;
//
// import org.junit.BeforeClass;
// import org.junit.Test;
//
// import com.ocpsoft.pretty.config.PrettyUrlMapping;
// import com.ocpsoft.pretty.config.QueryParameter;
// import com.ocpsoft.pretty.config.mapping.UrlAction;
//
// /**
// * @author lb3
// */
// public class PrettyUrlBuilderTest
// {
// static Link link = new Link();
// static PrettyUrlMapping mapping = new PrettyUrlMapping();
// static UIParameter param = new UIParameter();
// static UIParameter param2 = new UIParameter();
// static UIParameter param3 = new UIParameter();
// static UIParameter param4 = new UIParameter();
// static String expected = "";
//
// @BeforeClass
// public static void setUpBeforeClass() throws Exception
// {
// mapping.addAction(new UrlAction("#{bean.action}"));
// mapping.setId("testMapping");
// mapping.setPattern("/test/#{bean.param1}/mapping/#{bean.param2}");
// mapping.addQueryParam(new QueryParameter("key1", "#{bean.qp1}"));
// mapping.addQueryParam(new QueryParameter("key2", "#{bean.qp2}"));
//
// param.setName("key1");
// param.setValue("qp1");
// link.getChildren().add(param);
//
// param2.setValue("up1");
// link.getChildren().add(param2);
//
// param3.setName("key2");
// param3.setValue("qp2");
// link.getChildren().add(param3);
//
// param4.setValue("up2");
// link.getChildren().add(param4);
//
// expected = "/test/" + param2.getValue() + "/mapping/" + param4.getValue() +
// "?" + param.getName() + "="
// + param.getValue() + "&" + param3.getName() + "=" + param3.getValue();
//
// link.getAttributes().put("mappingId", mapping.getId());
// }
//
// private final PrettyUrlBuilder builder = new PrettyUrlBuilder();
//
// @Test
// public void testExtractParameters()
// {
// List<UIParameter> parameters = builder.extractParameters(link);
// assertEquals(4, parameters.size());
// assertTrue(parameters.contains(param));
// assertTrue(parameters.contains(param2));
// assertTrue(parameters.contains(param3));
// assertTrue(parameters.contains(param4));
// }
//
// @Test
// public void testBuildMappedUrlPrettyUrlMappingListOfUIParameter()
// {
// String mappedUrl = builder.buildMappedUrl(mapping,
// builder.extractParameters(link));
// assertEquals(expected, mappedUrl);
// }
//
// }
