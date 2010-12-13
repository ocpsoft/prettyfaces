package com.ocpsoft.pretty.faces.el;

/**
 * 
 * Interface representing a single EL method or value binding expression.
 * 
 * @author Christian Kaltepoth
 *
 */
public interface PrettyExpression
{
   /**
    * Returns the string representation of the expression.
    * 
    * @return The string representation.
    */
   public String getELExpression();

}
