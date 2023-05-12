package com.epam.mentoring.taf.util;

import io.restassured.response.Response;

import java.util.concurrent.ConcurrentHashMap;

public class StorageHelper {

    private static final ConcurrentHashMap<String, String> STORAGE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Response> RESPONSE_STORAGE = new ConcurrentHashMap<>();


    public static String whatIsThe(String key) {
        return STORAGE.get(key);
    }

    public static void rememberThat(String key, String value) {
        STORAGE.put(key, value);
    }

    public static void rememberTheResponse(String key, Response value) {
        RESPONSE_STORAGE.put(key, value);
    }

    public static Response whatIsTheResponse(String key) {
        return RESPONSE_STORAGE.get(key);
    }
}
