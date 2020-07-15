package ru.job4j.dreamjob.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
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

    private void initConfig() {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("db.properties")))) {
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
