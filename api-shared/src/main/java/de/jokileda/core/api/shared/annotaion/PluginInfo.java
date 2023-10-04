package de.jokileda.core.api.shared.annotaion;

import de.jokileda.core.api.shared.util.PluginType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginInfo {
  String name();

  String[] authors();

  int[] version();

  PluginType pluginType();
}
