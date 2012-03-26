package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;

import org.ocpsoft.prettyfaces.annotation.api.URLPattern;
import org.ocpsoft.prettyfaces.annotation.scan.MappingBuilder;
import org.ocpsoft.prettyfaces.annotation.spi.AnnotationHandler;

import com.ocpsoft.rewrite.servlet.config.Path;

public class URLPatternHandler implements AnnotationHandler<URLPattern> {

    @Override
    public Class<URLPattern> handles() {
        return URLPattern.class;
    }
    
    @Override
    public void process(URLPattern annotation, AnnotatedElement element, MappingBuilder builder) {
        builder.addCondition(Path.matches(annotation.value()));
    }

}
