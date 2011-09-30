package com.ocpsoft.pretty.faces.test.convert;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.ocpsoft.pretty.faces.annotation.URLConverter;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLQueryParameter;

@ManagedBean
@RequestScoped
@URLMapping(id = "queryParamMapping", pattern = "/annotation/queryparam/custom", viewId = "/convert.jsf")
public class AnnotationQueryParamConversionBean
{

   @URLQueryParameter(value="number")
   @URLConverter(converterId="customNumberConverter")
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
