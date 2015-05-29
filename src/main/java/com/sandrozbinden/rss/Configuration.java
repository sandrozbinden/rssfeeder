package com.sandrozbinden.rss;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.primitives.Ints;

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

    public File getIndexInputDirectory() {
        return new File(getProperties().getProperty("index.input.directory"));
    }

    public File getIndexProcessedDirectory() {
        return new File(getProperties().getProperty("index.processed.directory"));
    }

    public File getIndexErrorDirectory() {
        return new File(getProperties().getProperty("index.error.directory"));
    }

    public File getQueryResultDirectory() {
        return new File(getProperties().getProperty("query.result.directory"));
    }

    public Optional<String> getQueryParameter(String parameterName) {
        String parameter = getProperties().getProperty("query.parameter." + parameterName);
        if (Strings.isNullOrEmpty(parameter)) {
            return Optional.absent();
        } else {
            return Optional.of(parameter);
        }
    }

    public Optional<Boolean> getQueryBooleanParameter(String parameterName) {
        String parameter = getProperties().getProperty("query.parameter." + parameterName);
        if (Strings.isNullOrEmpty(parameter)) {
            return Optional.absent();
        } else {
            return Optional.of(Boolean.valueOf(parameter));
        }
    }

    public Optional<Integer> getQueryIntParameter(String parameterName) {
        String parameter = getProperties().getProperty("query.parameter." + parameterName);
        if (Strings.isNullOrEmpty(parameter)) {
            return Optional.absent();
        } else {
            return Optional.of(Ints.tryParse(parameter));
        }
    }

    public String getQueryResultFieldSeperator() {
        return getProperties().getProperty("query.result.field.seperator");
    }
}
