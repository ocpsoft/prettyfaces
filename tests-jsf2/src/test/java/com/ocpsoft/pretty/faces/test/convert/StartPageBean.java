package com.ocpsoft.pretty.faces.test.convert;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

@ManagedBean
@RequestScoped
public class StartPageBean
{
   
   @ManagedProperty("#{paramConversionBean}")
   private ParamConversionBean paramConversionBean;
   
   public String queryParameterDefaultConverter() {
      paramConversionBean.setNumber(2);
      return "pretty:queryParameterDefaultConverter";
   }
   
   public String pathParameterDefaultConverter() {
      paramConversionBean.setNumber(2);
      return "pretty:pathParameterDefaultConverter";
   }
   
   public String queryParameterCustomConverter() {
      paramConversionBean.setNumber(2);
      return "pretty:queryParameterCustomConverter";
   }
   
   public String pathParameterCustomConverter() {
      paramConversionBean.setNumber(2);
      return "pretty:pathParameterCustomConverter";
   }

   public ParamConversionBean getParamConversionBean()
   {
      return paramConversionBean;
   }

   public void setParamConversionBean(ParamConversionBean paramConversionBean)
   {
      this.paramConversionBean = paramConversionBean;
   }

}
