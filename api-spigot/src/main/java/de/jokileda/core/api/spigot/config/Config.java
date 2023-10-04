package de.jokileda.core.api.spigot.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

  private final File file;
  private final YamlConfiguration config;

  public Config(String pluginName, String configName) {
    this.file = new File("plugins/" + pluginName, configName + ".yml");
    config = YamlConfiguration.loadConfiguration(file);
  }

  public void safeConfig() {
    try {
      config.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public <T> T get(String key, T defaultValue) {
    if (!config.contains(key)) {
      set(key, defaultValue);
      this.safeConfig();
    }
    return (T) config.get(key);
  }

  public void set(String key, Object value) {
    config.set(key, value);
    this.safeConfig();
  }
}
