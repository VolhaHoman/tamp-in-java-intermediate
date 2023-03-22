package com.epam.mentoring.taf.util;

import java.util.concurrent.ConcurrentHashMap;

public class StorageHelper {

    private static final ConcurrentHashMap<String, String> STORAGE = new ConcurrentHashMap<>();

    public static String whatIsThe(String key) {
        return STORAGE.get(key);
    }

    public static void rememberThat(String key, String value) {
        STORAGE.put(key, value);
    }
}
