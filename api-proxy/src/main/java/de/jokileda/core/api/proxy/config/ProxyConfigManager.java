package de.jokileda.core.api.proxy.config;

import java.util.HashMap;
import java.util.List;
import net.md_5.bungee.config.Configuration;

import java.util.Arrays;
import java.util.Map;

public class ProxyConfigManager {
    private final Config config;
    private Configuration configuration;

    public ProxyConfigManager(String pluginName, String fileName) {
        config = new Config(pluginName, fileName);
        configuration = config.loadConfig();
    }

    public ProxyConfigManager(String pluginName, Class clazz) {
        config = new Config(pluginName, clazz.getSimpleName().toLowerCase());
    }

    public String getString(String path, String defaultValue, String... placeholder) {
        Map<String, String> placeholderMap = new HashMap<>();
        if (placeholder.length % 2 != 0) {
            throw new IllegalArgumentException("Placeholders must be provided in key-value pairs.");
        }
        for (int i = 0; i < placeholder.length; i += 2) {
            placeholderMap.put(placeholder[i], placeholder[i + 1]);
        }
        return getString(path, defaultValue, placeholderMap);
    }

    public String getString(String path, String defaultValue, Map<String, String> placeHolders) {
        String value = config.get(configuration, path, defaultValue);
        if(placeHolders == null)
            return config.get(configuration, path, defaultValue).replaceAll("%n", System.lineSeparator()).replaceAll(";", ":");
        for(Map.Entry<String, String> placeHolder : placeHolders.entrySet()) {
            value = value.replace(placeHolder.getKey(), placeHolder.getValue());
        }
        return value.replaceAll(";", ":").replaceAll("%n", System.lineSeparator());
    }

    public Integer getInt(String path, int defaultValue) {
        return config.get(configuration, path, defaultValue);
    }

    public Float getFloat(String path, float defaultValue) {
        return config.get(configuration, path, defaultValue);
    }

    public Long getLong(String path, long defaultValue) {
        return config.get(configuration, path, defaultValue);
    }

    public Boolean getBoolean(String path, boolean defaultValue) {
        return config.get(configuration, path, defaultValue);
    }

    public List<String> getStringList(String path, List<String> defaultValue) {
        return config.get(configuration, path, defaultValue);
    }

    public List<Integer> getIntegerList(String path, List<Integer> defaultValue) {
        return config.get(configuration, path, defaultValue);
    }

}
