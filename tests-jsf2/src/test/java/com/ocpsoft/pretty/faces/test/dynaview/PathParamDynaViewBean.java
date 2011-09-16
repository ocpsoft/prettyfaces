package com.ocpsoft.pretty.faces.test.dynaview;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean
@RequestScoped
@URLMapping(id = "dynaview", pattern = "/dynaview/#{ pathParamDynaViewBean.value }", 
      viewId = "#{pathParamDynaViewBean.computeViewId}")
public class PathParamDynaViewBean
{

   /**
    * Injected value of the path parameter
    */
   private String value;

   /**
    * This method is used to compute the view id! It will return
    * <code>/correct.jsf</code> if the path parameter property contains the
    * string <code>correct</code>. In all other cases it will return
    * <code>/wrong.jsf</code>.
    */
   public String computeViewId()
   {

      if (value != null && value.equals("correct"))
      {
         return "/correct.jsf";
      }
      else
      {
         return "/wrong.jsf";
      }

   }

   public String getValue()
   {
      return value;
   }

   public void setValue(String value)
   {
      this.value = value;
   }

}
