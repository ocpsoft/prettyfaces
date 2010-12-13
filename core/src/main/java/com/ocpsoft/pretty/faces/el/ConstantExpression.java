package com.ocpsoft.pretty.faces.el;

/**
 * Implementation of {@link PrettyExpression} used when the EL expression is
 * known in advance.
 * 
 * @author Christian Kaltepoth
 */
public class ConstantExpression implements PrettyExpression
{

   /**
    * Holds the expression
    */
   private final String expression;

   /**
    * Creates a new {@link ConstantExpression}.
    * 
    * @param expression
    *            The EL expressions
    */
   public ConstantExpression(String expression)
   {
      this.expression = expression;
   }

   /*
    * @see com.ocpsoft.pretty.expression.PrettyExpression#getELExpression()
    */
   public String getELExpression()
   {
      return expression;
   }

   @Override
   public String toString()
   {
      return expression;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((expression == null) ? 0 : expression.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      ConstantExpression other = (ConstantExpression) obj;
      if (expression == null)
      {
         if (other.expression != null)
            return false;
      }
      else if (!expression.equals(other.expression))
         return false;
      return true;
   }

}
