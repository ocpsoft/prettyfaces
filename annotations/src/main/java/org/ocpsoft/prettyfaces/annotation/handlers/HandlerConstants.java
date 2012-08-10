package org.ocpsoft.prettyfaces.annotation.handlers;

public class HandlerConstants
{

   /**
    * Suggested weight for handlers which build the basic structure of the rule. This is for example the case for
    * handlers that define the rule itself, add parameter bindings or operations.
    */
   public static final int WEIGHT_TYPE_STRUCTURAL = 100;

   /**
    * Suggested weight for handlers which enrich the structure built by other handlers. Typical examples are handles
    * that add conditions to the rule or that add validation or conversion to parameter bindings.
    */
   public static final int WEIGHT_TYPE_ENRICHING = 200;

}
