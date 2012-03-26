package org.ocpsoft.prettyfaces.annotation.spi;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.ocpsoft.prettyfaces.annotation.scan.MappingBuilder;

public interface AnnotationHandler<A extends Annotation> {

    Class<A> handles();
    
    void process(A annotation, AnnotatedElement element, MappingBuilder builder);

}
