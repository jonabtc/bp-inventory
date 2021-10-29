package com.example.apistock.utils;

import java.util.List;
import java.util.Map;

public class DataUtils {

    public static <T> boolean dataIsNotValid(List<Map<String, T>> data) {
        for (Map<String, T> item : data) {
            if (hasNullValue(item)) {
                return true;
            }
        }

        return false;
    }

    public static <T> boolean hasNullValue(Map<String, T> data) {
        if (data == null || data.isEmpty()) {
            return true;
        }
        for (Map.Entry<String, T> entry : data.entrySet()) {
            if (entry.getValue() == null) {
                return true;
            }
        }

        return false;
    }

    public static <T> boolean hasNullValue(List<T> data) {
        if (data == null || data.isEmpty()) {
            return true;
        }
        for (T entry : data) {
            if (entry ==  null) {
                return true;
            }
        }

        return false;
    }   
    
}
