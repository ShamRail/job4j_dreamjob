package ru.job4j.dreamjob.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private final Properties properties = new Properties();

    private final static AppConfig INSTANCE = new AppConfig();

    public static AppConfig getConfig() {
        return INSTANCE;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    private AppConfig() {
        this.initConfig();
    }

    private synchronized void initConfig() {
        try (InputStream in = new FileInputStream("db.properties")) {
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
