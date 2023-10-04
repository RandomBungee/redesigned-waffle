package de.jokileda.core.api.proxy;

import de.jokileda.core.api.shared.annotaion.ProcessorUtil;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Arrays;

public abstract class AbstractProxyPlugin extends Plugin implements IProxyPlugin {


  public AbstractProxyPlugin() {}

  @Override
  public void onLoad() {
    this.load();
    ProcessorUtil processorUtil = new ProcessorUtil();
    processorUtil.checkForAnnotation(this.getClass());
  }

  @Override
  public void onEnable() {
    this.enable();
    System.out.println("Plugin: " + getAnnotation().name());
    System.out.println("Version: " + Arrays.toString(getAnnotation().version()));
    System.out.println("Authors: " + Arrays.toString(getAnnotation().authors()));
    System.out.println("PluginType: " + getAnnotation().pluginType());
  }

  @Override
  public void onDisable() {
    this.disable();
  }
}
