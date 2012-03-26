package org.ocpsoft.prettyfaces.annotation.scan;

import java.lang.reflect.Field;

import com.ocpsoft.rewrite.config.Condition;
import com.ocpsoft.rewrite.config.Operation;
import com.ocpsoft.rewrite.config.Rule;
import com.ocpsoft.rewrite.param.Constraint;

public interface MappingBuilder
{

   /**
    * Adds a {@link Condition} to the mapping
    */
   MappingBuilder addCondition(Condition condition);

   /**
    * Add the resulting {@link Operation} to the mapping
    */
   MappingBuilder setOperation(Operation operation);

   /**
    * Adds a parameter to the mapping which is bound to a field using an EL expression.
    */
   MappingBuilder addParameterInjection(Field field, String paramName);

   /**
    * Adds an additional constraint to check for a parameter
    */
   MappingBuilder addParameterConstraint(String fieldName, Constraint<String> constraint);

   /**
    * Build a {@link Rule} from the mapping configuration
    */
   Rule toRule();

}