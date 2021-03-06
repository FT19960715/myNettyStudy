package com.ARPC.entity;

import java.util.HashMap;
import java.util.Map;

public class MessageRegistry {
    private static Map<String, Class<?>> clazzes = new HashMap<>();

    public static void register(String type, Class<?> clazz) {
        clazzes.put(type, clazz);
    }

    public static Class<?> get(String type) {
        return clazzes.get(type);
    }
}
