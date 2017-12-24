package com.shakenbeer.wolttest.convert;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.shakenbeer.wolttest.model.Hour;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public class Json2Hours {

    private final Gson gson;
    private final Type type;

    private Json2Hours() {
        gson = new Gson();
        type = new TypeToken<Map<String, Hour[]>>() {
        }.getType();
    }

    public static Json2Hours getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public Map<String, Hour[]> convert(String json) {
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            return Collections.emptyMap();
        }
    }

    private static class SingletonHelper {
        private static final Json2Hours INSTANCE = new Json2Hours();
    }
}
