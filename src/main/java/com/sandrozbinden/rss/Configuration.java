package com.sandrozbinden.rss;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

    private File file;

    public Configuration(File file) {
        this.file = file;
    }

    public Properties getProperties() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(file)) {
            properties.load(fis);
        } catch (IOException e) {
            try {
                throw new RuntimeException("Can't get configuration file: " + file.getCanonicalPath(), e);
            } catch (IOException e1) {
                throw new RuntimeException("Can't get configuration file: " + file.getAbsolutePath(), e);
            }
        }
        return properties;
    }

    public String getSolrURL() {
        return getProperties().getProperty("solr.url");
    }

    public File getQueryResultDirectory() {
        return new File(getProperties().getProperty("query.result.directory"));
    }

    public File getIndexInputDirectory() {
        return new File(getProperties().getProperty("index.input.directory"));
    }

    public File getIndexProcessedDirectory() {
        return new File(getProperties().getProperty("index.processed.directory"));
    }

    public File getIndexErrorDirectory() {
        return new File(getProperties().getProperty("index.error.directory"));
    }
}
