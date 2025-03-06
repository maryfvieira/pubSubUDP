package org.example.sbe.quotation.subscriber;

import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(ConfigLoader.class.getClassLoader().getResourceAsStream("config/market-data-config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar configurações", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}


