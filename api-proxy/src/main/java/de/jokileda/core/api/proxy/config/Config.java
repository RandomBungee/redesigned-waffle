package de.jokileda.core.api.proxy.config;

import com.google.common.io.Files;
import de.jokileda.core.api.shared.IPlugin;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Config {
    private final File file;
    private final String fileName;

    public Config(String pluginName, String fileName) {
        this.file = new File("plugins/" + pluginName, fileName + ".yml");
        this.fileName = fileName;
    }

    public Configuration loadConfig() {
        if (!file.exists()) {
            try {
                Files.createParentDirs(file);
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveConfig(Configuration config) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(Configuration configuration, String key, Object value) {
        configuration.set(key, value);
    }

    public <T> T get(Configuration configuration, String key, T defaultValue) {
        if(!configuration.contains(key)) {
            set(configuration, key, defaultValue);
            this.saveConfig(configuration);
        }
        return (T) configuration.get(key);
    }
}
