package com.example.good_lodging_service.utils;

public class Value {
    public static <T> T getOrDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

}
