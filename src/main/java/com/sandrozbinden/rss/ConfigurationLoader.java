package com.sandrozbinden.rss;

import java.io.File;

public class ConfigurationLoader {

    private Configuration configuration;

    public static final String CONFIG_FILE_LOCATION = "config/configuration.properties";
    private static final ConfigurationLoader INSTANCE = new ConfigurationLoader();

    public static Configuration getConfig() {
        if (INSTANCE.configuration == null) {
            INSTANCE.configuration = new Configuration(new File(new File("."), CONFIG_FILE_LOCATION));
        }
        return INSTANCE.configuration;
    }

    public static void setConfiguration(Configuration configuration) {
        INSTANCE.configuration = configuration;
    }
}
