package com.ocpsoft.pretty.faces.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Container annotation for multiple {@link URLAction} annotations placed on
 * a single action method.
 * </p>
 * 
 * @author Christian Kaltepoth
 * @see URLAction
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface URLActions {

   /**
    * <p>
    * List of {@link URLAction} annotations for the action method.
    * </p>
    */
   URLAction[] actions() default {};
}
