package de.jokileda.core.api.shared;

import de.jokileda.core.api.shared.annotaion.PluginInfo;
import de.jokileda.core.api.shared.annotaion.ProcessorUtil;
import de.jokileda.core.api.shared.exception.MissingPluginInfoAnnotationException;
import de.jokileda.core.api.shared.util.PluginType;
import java.lang.annotation.Annotation;

public interface IPlugin {

  default String getName() throws MissingPluginInfoAnnotationException {
    return getAnnotation().name();
  }

  default String[] getAuthors() throws MissingPluginInfoAnnotationException {
    return getAnnotation().authors();
  }

  default int[] getVersion() throws MissingPluginInfoAnnotationException {
    return getAnnotation().version();
  }

  default PluginType getPluginType() throws MissingPluginInfoAnnotationException {
    return getAnnotation().pluginType();
  }

  default PluginInfo getAnnotation() throws MissingPluginInfoAnnotationException {
    for(Annotation pluginInfoAnnotation : getAllAnnotations()) {
      if(isPluginInfo(pluginInfoAnnotation))
        return (PluginInfo) pluginInfoAnnotation;
    }
    throw new MissingPluginInfoAnnotationException();
  }

  default Annotation[] getAllAnnotations() {
    return this.getClass().getAnnotations();
  }

  default boolean isPluginInfo(Annotation annotation) {
    return annotation instanceof PluginInfo;
  }
}
