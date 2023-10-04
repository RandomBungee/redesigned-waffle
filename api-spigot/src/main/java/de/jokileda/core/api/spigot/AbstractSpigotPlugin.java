package de.jokileda.core.api.spigot;

import de.jokileda.core.api.shared.annotaion.ProcessorUtil;
import de.jokileda.core.api.spigot.refelction.LoginListener;
import java.util.Arrays;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractSpigotPlugin extends JavaPlugin implements
    ISpigotPlugin {

  public AbstractSpigotPlugin() {
  }

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
    getServer().getPluginManager().registerEvents(new LoginListener(), this);
  }

  @Override
  public void onDisable() {
    this.disable();
  }

}
