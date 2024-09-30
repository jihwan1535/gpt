package org.delivery.config;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.Map;

public class YamlConfig {

    private Map<String, Object> properties;

    public YamlConfig(final String yamlFile) {
        final Yaml yaml = new Yaml();
        try (final InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(yamlFile)
        ) {
            properties = yaml.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("error => " + e);
        }
    }

    public String getString(final String key) {
        final Object value = getValueByPath(key);
        return (value != null) ? String.valueOf(value) : null;
    }

    public Integer getInteger(final String key) {
        final Object value = getValueByPath(key);
        return (value != null) ? Integer.valueOf(String.valueOf(value)) : null;
    }

    private Object getValueByPath(final String key) {
        final String[] parts = key.split("\\.");
        Map<String, Object> currentMap = properties;
        for (int i = 0; i < parts.length - 1; i++) {
            final Object value = currentMap.get(parts[i]);
            if (!(value instanceof Map)) {
                return null;
            }
            currentMap = (Map<String, Object>) value;
        }
        return currentMap.get(parts[parts.length - 1]);
    }

}