package org.ocpsoft.prettyfaces.annotation.jaas;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.ocpsoft.prettyfaces.annotation.ForwardTo;
import org.ocpsoft.prettyfaces.annotation.RolesRequired;
import org.ocpsoft.prettyfaces.annotation.PathPattern;

@ManagedBean
@RequestScoped
@PathPattern("/admin/something")
@RolesRequired("admin")
@ForwardTo("/faces/protected-page.xhtml")
public class JaasRolesBean
{

}
