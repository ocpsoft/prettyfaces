package org.ocpsoft.prettyfaces.annotation.scan;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ocpsoft.rewrite.bind.El;
import com.ocpsoft.rewrite.bind.RegexConditionParameterBuilder;
import com.ocpsoft.rewrite.config.And;
import com.ocpsoft.rewrite.config.Condition;
import com.ocpsoft.rewrite.config.Operation;
import com.ocpsoft.rewrite.config.Rule;
import com.ocpsoft.rewrite.config.RuleBuilder;
import com.ocpsoft.rewrite.param.Constraint;
import com.ocpsoft.rewrite.servlet.config.Path;

public class MappingBuilderImpl implements MappingBuilder
{

   private Operation operation;

   private final List<Condition> conditions = new ArrayList<Condition>();

   private final Map<String, ParameterConfig> paramConfigMap = new HashMap<String, ParameterConfig>();

   @Override
   public MappingBuilder addCondition(Condition condition)
   {
      this.conditions.add(condition);
      return this;
   }

   @Override
   public MappingBuilder setOperation(Operation operation)
   {
      this.operation = operation;
      return this;
   }

   @Override
   public MappingBuilder addParameterConstraint(String fieldName, Constraint<String> constraint)
   {
      ParameterConfig fieldConfig = getOrCreateFieldConfigEntry(fieldName);
      fieldConfig.setConstraint(constraint);
      return this;
   }

   @Override
   public MappingBuilder addParameterInjection(Field field, String paramName)
   {
      ParameterConfig fieldConfig = getOrCreateFieldConfigEntry(field.getName());
      fieldConfig.setParamName(paramName);
      fieldConfig.setFieldName(field.getName());
      fieldConfig.setTargetClass(field.getDeclaringClass());
      return this;
   }

   @Override
   public Rule toRule()
   {

      // we need this to get a working rule
      if (operation == null && conditions.isEmpty()) {
         return null;
      }

      // prepare an array of constraints
      Condition[] conditionArray = new Condition[conditions.size()];
      for (int i = 0; i < conditions.size(); i++) {

         Condition condition = conditions.get(i);
         conditionArray[i] = condition;

         // Path conditions may need parameters to be added
         if (condition instanceof Path) {
            Path path = (Path) condition;

            // we add all parameters we know about
            for (ParameterConfig paramConfig : paramConfigMap.values()) {

               // all attributes for this parameter set? If not we ignore it
               if (paramConfig.isValid()) {

                  // .where('name')
                  RegexConditionParameterBuilder parameter = path.where(paramConfig.getParamName());

                  // .constraintBy(...)
                  if (paramConfig.getConstraint() != null) {
                     parameter.constrainedBy(paramConfig.getConstraint());
                  }

                  // FIXME: dirty way to build the EL expression
                  String simpleClassName = paramConfig.getTargetClass().getSimpleName();
                  String beanName = String.valueOf(simpleClassName.charAt(0)).toLowerCase()
                           + simpleClassName.substring(1);
                  String el = "#{" + beanName + "." + paramConfig.getFieldName() + "}";

                  // .bindsTo()
                  parameter.bindsTo(El.property(el));

               }
            }

         }

      }

      // use the builder to create the rule
      RuleBuilder builder = new RuleBuilder();
      builder.when(And.all(conditionArray));
      builder.perform(operation);
      return builder;

   }

   private ParameterConfig getOrCreateFieldConfigEntry(String fieldName)
   {
      ParameterConfig fieldConfig = paramConfigMap.get(fieldName);
      if (fieldConfig == null) {
         fieldConfig = new ParameterConfig();
         paramConfigMap.put(fieldName, fieldConfig);
      }
      return fieldConfig;
   }

   private static class ParameterConfig
   {

      private String paramName;

      private Class<?> targetClass;

      private String fieldName;

      private Constraint<String> constraint;

      public boolean isValid()
      {
         return paramName != null && targetClass != null && fieldName != null;
      }

      public String getParamName()
      {
         return paramName;
      }

      public void setParamName(String paramName)
      {
         this.paramName = paramName;
      }

      public String getFieldName()
      {
         return fieldName;
      }

      public void setFieldName(String fieldName)
      {
         this.fieldName = fieldName;
      }

      public Constraint<String> getConstraint()
      {
         return constraint;
      }

      public void setConstraint(Constraint<String> constraint)
      {
         this.constraint = constraint;
      }

      public Class<?> getTargetClass()
      {
         return targetClass;
      }

      public void setTargetClass(Class<?> targetClass)
      {
         this.targetClass = targetClass;
      }

   }

}
