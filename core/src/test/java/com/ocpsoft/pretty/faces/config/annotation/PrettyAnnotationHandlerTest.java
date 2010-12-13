package com.ocpsoft.pretty.faces.config.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLAction.PhaseId;
import com.ocpsoft.pretty.faces.annotation.URLActions;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLQueryParameter;
import com.ocpsoft.pretty.faces.annotation.URLValidator;
import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.PrettyConfigBuilder;
import com.ocpsoft.pretty.faces.config.mapping.PathValidator;
import com.ocpsoft.pretty.faces.config.mapping.QueryParameter;
import com.ocpsoft.pretty.faces.config.mapping.UrlAction;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.el.ConstantExpression;
import com.ocpsoft.pretty.faces.el.LazyExpression;

public class PrettyAnnotationHandlerTest
{

   @Test
   public void testNotAnnotatedClass()
   {

      // create handler and process class
      PrettyAnnotationHandler handler = new PrettyAnnotationHandler(null);
      handler.processClass(NotAnnotatedClass.class);

      // build configuration
      PrettyConfigBuilder configBuilder = new PrettyConfigBuilder();
      handler.build(configBuilder);
      PrettyConfig config = configBuilder.build();

      // no mappings added
      assertNotNull(config.getMappings());
      assertEquals(0, config.getMappings().size());

   }

   @Test
   public void testSimpleMapping()
   {

      // create handler and process class
      PrettyAnnotationHandler handler = new PrettyAnnotationHandler(null);
      handler.processClass(ClassWithSimpleMapping.class);

      // build configuration
      PrettyConfigBuilder configBuilder = new PrettyConfigBuilder();
      handler.build(configBuilder);
      PrettyConfig config = configBuilder.build();

      // no mappings added
      assertNotNull(config.getMappings());
      assertEquals(1, config.getMappings().size());

      // validate mapping properties
      UrlMapping mapping = config.getMappings().get(0);
      assertEquals("simple", mapping.getId());
      assertEquals("/some/url", mapping.getPattern());
      assertEquals("/view.jsf", mapping.getViewId());
      assertEquals(false, mapping.isOutbound());
      assertEquals(false, mapping.isOnPostback());
      assertEquals(0, mapping.getActions().size());
      assertEquals(0, mapping.getQueryParams().size());
      assertEquals(0, mapping.getPathValidators().size());

   }

   @Test
   public void testMappingWithPathValidation()
   {

      // create handler and process class
      PrettyAnnotationHandler handler = new PrettyAnnotationHandler(null);
      handler.processClass(ClassWithMappingAndPathValidation.class);

      // build configuration
      PrettyConfigBuilder configBuilder = new PrettyConfigBuilder();
      handler.build(configBuilder);
      PrettyConfig config = configBuilder.build();

      // no mappings added
      assertNotNull(config.getMappings());
      assertEquals(1, config.getMappings().size());

      // validate mapping properties
      UrlMapping mapping = config.getMappings().get(0);
      assertEquals("simple", mapping.getId());
      assertEquals("/some/url", mapping.getPattern());
      assertEquals("/view.jsf", mapping.getViewId());
      assertEquals(true, mapping.isOutbound());
      assertEquals(true, mapping.isOnPostback());
      assertEquals(0, mapping.getActions().size());
      assertEquals(0, mapping.getQueryParams().size());
      assertEquals(1, mapping.getPathValidators().size());

      // check path validation
      PathValidator validator = mapping.getPathValidators().get(0);
      assertEquals(0, validator.getIndex());
      assertEquals("#{bean.action}", validator.getOnError());
      assertEquals("myValidator myOtherValidator", validator.getValidatorIds());

   }

   @Test
   public void testMappingWithOneLazyExpressionAction()
   {

      // create handler and process class
      PrettyAnnotationHandler handler = new PrettyAnnotationHandler(null);
      handler.processClass(ClassWithSingleLazyExpressionAction.class);

      // build configuration
      PrettyConfigBuilder configBuilder = new PrettyConfigBuilder();
      handler.build(configBuilder);
      PrettyConfig config = configBuilder.build();

      // no mappings added
      assertNotNull(config.getMappings());
      assertEquals(1, config.getMappings().size());

      // validate mapping properties
      UrlMapping mapping = config.getMappings().get(0);
      assertEquals("singleLazyExpressionAction", mapping.getId());
      assertEquals("/some/url", mapping.getPattern());
      assertEquals("/view.jsf", mapping.getViewId());
      assertEquals(true, mapping.isOutbound());
      assertEquals(true, mapping.isOnPostback());
      assertEquals(0, mapping.getQueryParams().size());
      assertEquals(1, mapping.getActions().size());
      assertEquals(0, mapping.getPathValidators().size());

      // validate action
      UrlAction action = mapping.getActions().get(0);
      assertEquals(PhaseId.RESTORE_VIEW, action.getPhaseId());

      // validate PrettyExpression
      assertNotNull(action.getAction());
      assertEquals(LazyExpression.class, action.getAction().getClass());
      LazyExpression actionExpression = (LazyExpression) action.getAction();
      assertEquals(ClassWithSingleLazyExpressionAction.class, actionExpression.getBeanClass());
      assertEquals("actionMethod", actionExpression.getComponent());

   }

   @Test
   public void testMappingWithOneConstantExpressionAction()
   {

      // create handler and process class
      PrettyAnnotationHandler handler = new PrettyAnnotationHandler(null);
      handler.processClass(ClassWithSingleConstantExpressionAction.class);

      // build configuration
      PrettyConfigBuilder configBuilder = new PrettyConfigBuilder();
      handler.build(configBuilder);
      PrettyConfig config = configBuilder.build();

      // no mappings added
      assertNotNull(config.getMappings());
      assertEquals(1, config.getMappings().size());

      // validate mapping properties
      UrlMapping mapping = config.getMappings().get(0);
      assertEquals("singleConstantExpressionAction", mapping.getId());
      assertEquals("/some/url", mapping.getPattern());
      assertEquals("/view.jsf", mapping.getViewId());
      assertEquals(true, mapping.isOutbound());
      assertEquals(true, mapping.isOnPostback());
      assertEquals(0, mapping.getQueryParams().size());
      assertEquals(1, mapping.getActions().size());
      assertEquals(0, mapping.getPathValidators().size());

      // validate action
      UrlAction action = mapping.getActions().get(0);
      assertEquals(PhaseId.RESTORE_VIEW, action.getPhaseId());

      // validate PrettyExpression
      assertNotNull(action.getAction());
      assertEquals(ConstantExpression.class, action.getAction().getClass());
      assertEquals("#{someBean.actionMethod}", action.getAction().getELExpression());

   }

   @Test
   public void testMappingWithMultipleActions()
   {

      // create handler and process class
      PrettyAnnotationHandler handler = new PrettyAnnotationHandler(null);
      handler.processClass(ClassWithMultipleActions.class);

      // build configuration
      PrettyConfigBuilder configBuilder = new PrettyConfigBuilder();
      handler.build(configBuilder);
      PrettyConfig config = configBuilder.build();

      // no mappings added
      assertNotNull(config.getMappings());
      assertEquals(1, config.getMappings().size());

      // validate mapping properties
      UrlMapping mapping = config.getMappings().get(0);
      assertEquals("multipleActions", mapping.getId());
      assertEquals("/some/url", mapping.getPattern());
      assertEquals("/view.jsf", mapping.getViewId());
      assertEquals(true, mapping.isOutbound());
      assertEquals(true, mapping.isOnPostback());
      assertEquals(0, mapping.getQueryParams().size());
      assertEquals(2, mapping.getActions().size());
      assertEquals(0, mapping.getPathValidators().size());

      // validate first action
      UrlAction firstAction = mapping.getActions().get(0);
      assertEquals(PhaseId.RENDER_RESPONSE, firstAction.getPhaseId());
      assertNotNull(firstAction.getAction());
      assertEquals(LazyExpression.class, firstAction.getAction().getClass());
      LazyExpression firstActionExpression = (LazyExpression) firstAction.getAction();
      assertEquals(ClassWithMultipleActions.class, firstActionExpression.getBeanClass());
      assertEquals("actionMethod", firstActionExpression.getComponent());

      // validate second action
      UrlAction secondAction = mapping.getActions().get(1);
      assertEquals(PhaseId.INVOKE_APPLICATION, secondAction.getPhaseId());
      assertNotNull(secondAction.getAction());
      assertEquals(LazyExpression.class, secondAction.getAction().getClass());
      LazyExpression secondActionExpression = (LazyExpression) secondAction.getAction();
      assertEquals(ClassWithMultipleActions.class, secondActionExpression.getBeanClass());
      assertEquals("actionMethod", secondActionExpression.getComponent());

   }

   @Test
   public void testMappingWithSingleQueryParameter()
   {

      // create handler and process class
      PrettyAnnotationHandler handler = new PrettyAnnotationHandler(null);
      handler.processClass(ClassWithSingleQueryParameter.class);

      // build configuration
      PrettyConfigBuilder configBuilder = new PrettyConfigBuilder();
      handler.build(configBuilder);
      PrettyConfig config = configBuilder.build();

      // no mappings added
      assertNotNull(config.getMappings());
      assertEquals(1, config.getMappings().size());

      // validate mapping properties
      UrlMapping mapping = config.getMappings().get(0);
      assertEquals("singleQueryParamater", mapping.getId());
      assertEquals("/some/url", mapping.getPattern());
      assertEquals("/view.jsf", mapping.getViewId());
      assertEquals(true, mapping.isOutbound());
      assertEquals(true, mapping.isOnPostback());
      assertEquals(0, mapping.getActions().size());
      assertEquals(1, mapping.getQueryParams().size());
      assertEquals(0, mapping.getPathValidators().size());

      // validate query parameter
      QueryParameter queryParameter = mapping.getQueryParams().get(0);
      assertEquals("myQueryParam", queryParameter.getName());
      assertEquals(true, queryParameter.isOnPostback());

      // validate PrettyExpression
      assertNotNull(queryParameter.getExpression());
      assertEquals(ConstantExpression.class, queryParameter.getExpression().getClass());
      assertEquals("#{myQueryParamBean.someParameter}", queryParameter.getExpression().getELExpression());

   }

   @Test
   public void testMappingWithSingleQueryParameterAndValidation()
   {

      // create handler and process class
      PrettyAnnotationHandler handler = new PrettyAnnotationHandler(null);
      handler.processClass(ClassWithSingleQueryParameterAndValidation.class);

      // build configuration
      PrettyConfigBuilder configBuilder = new PrettyConfigBuilder();
      handler.build(configBuilder);
      PrettyConfig config = configBuilder.build();

      // no mappings added
      assertNotNull(config.getMappings());
      assertEquals(1, config.getMappings().size());

      // validate mapping properties
      UrlMapping mapping = config.getMappings().get(0);
      assertEquals("singleQueryParamater", mapping.getId());
      assertEquals("/some/url", mapping.getPattern());
      assertEquals("/view.jsf", mapping.getViewId());
      assertEquals(true, mapping.isOutbound());
      assertEquals(true, mapping.isOnPostback());
      assertEquals(0, mapping.getActions().size());
      assertEquals(1, mapping.getQueryParams().size());
      assertEquals(0, mapping.getPathValidators().size());

      // validate query parameter
      QueryParameter queryParameter = mapping.getQueryParams().get(0);
      assertEquals("myQueryParam", queryParameter.getName());
      assertEquals(false, queryParameter.isOnPostback());
      assertEquals("#{bean.action}", queryParameter.getOnError());
      assertEquals(2, queryParameter.getValidatorIdList().size());
      assertEquals("myValidator myOtherValidator", queryParameter.getValidatorIds());

      // validate PrettyExpression
      assertNotNull(queryParameter.getExpression());
      assertEquals(ConstantExpression.class, queryParameter.getExpression().getClass());
      assertEquals("#{myQueryParamBean.someParameter}", queryParameter.getExpression().getELExpression());

   }

   /**
    * Simple class without any PrettyFaces annotations
    */
   public class NotAnnotatedClass
   {
      // nothing
   }

   /**
    * Simple class with a single {@link URLMapping} annotation
    */
   @URLMapping(id = "simple", pattern = "/some/url", viewId = "/view.jsf",
         outbound = false, onPostback = false)
   public class ClassWithSimpleMapping
   {
      // nothing
   }

   /**
    * Simple class with a single {@link URLMapping} annotation and a validation
    * rule
    */
   @URLMapping(id = "simple", pattern = "/some/url", viewId = "/view.jsf",
         validation = @URLValidator(index = 0, onError = "#{bean.action}",
               validatorIds = { "myValidator", "myOtherValidator" }))
   public class ClassWithMappingAndPathValidation
   {
      // nothing
   }

   /**
    * Simple class with a mapping and one annotated action method (lazy
    * expression)
    */
   @URLMapping(id = "singleLazyExpressionAction", pattern = "/some/url", viewId = "/view.jsf")
   public class ClassWithSingleLazyExpressionAction
   {

      @URLAction
      public void actionMethod()
      {
         // nothing
      }

   }

   /**
    * Simple class with a mapping and one annotated action method (constant
    * expression)
    */
   @URLMapping(id = "singleConstantExpressionAction", pattern = "/some/url", viewId = "/view.jsf")
   @URLBeanName("someBean")
   public class ClassWithSingleConstantExpressionAction
   {

      @URLAction
      public void actionMethod()
      {
         // nothing
      }

   }

   /**
    * Simple class with a mapping and an action method referenced by multiple
    * {@link URLAction}s.
    */
   @URLMapping(id = "multipleActions", pattern = "/some/url", viewId = "/view.jsf")
   public class ClassWithMultipleActions
   {

      @URLActions(actions = {
            @URLAction(phaseId = URLAction.PhaseId.RENDER_RESPONSE),
            @URLAction(phaseId = URLAction.PhaseId.INVOKE_APPLICATION)
      })
      public void actionMethod()
      {
         // nothing
      }

   }

   /**
    * Simple class with a single mapping and a query parameter
    */
   @URLMapping(id = "singleQueryParamater", pattern = "/some/url", viewId = "/view.jsf")
   @URLBeanName("myQueryParamBean")
   public class ClassWithSingleQueryParameter
   {

      @URLQueryParameter("myQueryParam")
      @SuppressWarnings("unused")
      private String someParameter;

   }

   /**
    * Simple class with a single mapping and a query parameter with validation
    */
   @URLMapping(id = "singleQueryParamater", pattern = "/some/url", viewId = "/view.jsf")
   @URLBeanName("myQueryParamBean")
   public class ClassWithSingleQueryParameterAndValidation
   {

      @URLQueryParameter(value = "myQueryParam", onPostback = false)
      @URLValidator(index = 0, onError = "#{bean.action}",
            validatorIds = { "myValidator", "myOtherValidator" })
      @SuppressWarnings("unused")
      private String someParameter;

   }

}
