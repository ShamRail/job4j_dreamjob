package ru.job4j.dreamjob.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class JSONUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JSONUtils() { }

    public static String serialize(Object object) {
        String json = "";
        try {
            json = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static <T> Optional<T> deserialize(String json, Class<T> cls) {
        Optional<T> object = Optional.empty();
        try {
            object = Optional.ofNullable(MAPPER.readValue(json, cls));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

}
