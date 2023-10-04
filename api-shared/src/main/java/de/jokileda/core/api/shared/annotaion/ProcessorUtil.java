package de.jokileda.core.api.shared.annotaion;

import de.jokileda.core.api.shared.exception.MissingPluginInfoAnnotationException;
import java.lang.annotation.Annotation;

public class ProcessorUtil {

  public ProcessorUtil() {}

  public void checkForAnnotation(Class<?> clazz) {
    Class<? extends Annotation> annotationToCheck = PluginInfo.class;
    if(!clazz.isAnnotationPresent(annotationToCheck))
      throw new MissingPluginInfoAnnotationException();
  }

}
