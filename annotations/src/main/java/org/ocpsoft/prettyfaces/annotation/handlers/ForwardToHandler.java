package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;

import org.ocpsoft.prettyfaces.annotation.api.ForwardTo;
import org.ocpsoft.prettyfaces.annotation.scan.MappingBuilder;
import org.ocpsoft.prettyfaces.annotation.spi.AnnotationHandler;

import com.ocpsoft.rewrite.servlet.config.Forward;

public class ForwardToHandler implements AnnotationHandler<ForwardTo> {

    @Override
    public Class<ForwardTo> handles() {
        return ForwardTo.class;
    }

    @Override
    public void process(ForwardTo annotation, AnnotatedElement element, MappingBuilder builder) {
        builder.setOperation(Forward.to(annotation.value()));
    }

}
