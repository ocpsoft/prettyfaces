package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;

import org.ocpsoft.prettyfaces.annotation.Join;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.servlet.config.rule.IJoin;

public class JoinHandler implements AnnotationHandler<Join>
{

   @Override
   public Class<Join> handles()
   {
      return Join.class;
   }

   @Override
   public void process(ClassContext context, AnnotatedElement element, Join annotation)
   {

      IJoin join = org.ocpsoft.rewrite.servlet.config.rule.Join
               .path(annotation.path())
               .to(annotation.to());

      context.setBaseRule(join);

   }

}
