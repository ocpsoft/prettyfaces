package com.ocpsoft.pretty.faces.test.convert;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.ocpsoft.pretty.faces.annotation.URLConverter;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean
@RequestScoped
@URLMapping(id = "pathParamMapping", pattern = "/annotation/pathparam/custom/#{annotationPathParamConversionBean.number}", 
      viewId = "/convert.jsf", converter=@URLConverter(index=0, converterId="customNumberConverter")
)
public class AnnotationPathParamConversionBean
{

   private Integer number;

   public Integer getNumber()
   {
      return number;
   }

   public void setNumber(Integer number)
   {
      this.number = number;
   }
   
}
