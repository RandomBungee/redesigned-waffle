package de.jokileda.core.api.shared.exception;

public class MissingPluginInfoAnnotationException extends RuntimeException {
  public MissingPluginInfoAnnotationException() {
    super("@PluginInfo Annotation is missing but necessary. Please add @PluginInfo Annotation!");
  }
}
