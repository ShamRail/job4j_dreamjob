package ru.job4j.dreamjob.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ResourceScriptReader {
    public static String read(String fileName) {
        String text = "";
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(ResourceScriptReader.class.getClassLoader().getResourceAsStream(fileName)))) {
            text = br.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }
}
