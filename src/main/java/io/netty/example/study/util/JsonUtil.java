package io.netty.example.study.util;

import com.google.gson.Gson;
import io.netty.example.study.common.MessageBody;

public final class JsonUtil {

    private static final Gson GSON = new Gson();

    private JsonUtil(){
        //no instance
    }

    public static <T extends MessageBody> T fromJson(String jsonStr, Class<T> bodyClazz) {
        return GSON.fromJson(jsonStr, bodyClazz);
    }

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }
}