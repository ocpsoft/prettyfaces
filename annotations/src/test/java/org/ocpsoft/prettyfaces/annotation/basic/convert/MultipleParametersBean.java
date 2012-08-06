package org.ocpsoft.prettyfaces.annotation.basic.convert;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.ocpsoft.prettyfaces.annotation.Hostname;
import org.ocpsoft.prettyfaces.annotation.JSFConverter;
import org.ocpsoft.prettyfaces.annotation.Join;
import org.ocpsoft.prettyfaces.annotation.ParameterBinding;

@ManagedBean
@RequestScoped
@Hostname("{hostname}")
@Join(path = "/path/{path}", to = "/faces/multiple-parameters.xhtml")
public class MultipleParametersBean
{

   @ParameterBinding
   @JSFConverter(converterId = "ReverseConverter")
   private String hostname;

   @ParameterBinding
   @JSFConverter(converterId = "UppercaseConverter")
   private String path;

   public String getPath()
   {
      return path;
   }

   public void setPath(String path)
   {
      this.path = path;
   }

   public String getHostname()
   {
      return hostname;
   }

   public void setHostname(String hostname)
   {
      this.hostname = hostname;
   }

}
