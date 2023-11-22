package com.example.mobiletodoapp.phuc_activity.reusecode;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Function {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static <T> Boolean validateEmpty(T value, String message, Context context) {
        if (value == null || value.toString().isEmpty()) {
            showToast(context, message);
            return false;
        }
        return true;
    }

    public static <T> void saveSharedPref(Context context, String key, T value) {
        SharedPreferences sharedPref = context.getSharedPreferences("MobTodo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {
            throw new IllegalArgumentException("Unsupported data type");
        }

        editor.apply();
    }
    public static <T> T getSharedPref(Context context, String key, T defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences("MobTodo", Context.MODE_PRIVATE);

        if (defaultValue instanceof String) {
            return (T) sharedPref.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return (T) Integer.valueOf(sharedPref.getInt(key, (Integer) defaultValue));
        } else if (defaultValue instanceof Boolean) {
            return (T) Boolean.valueOf(sharedPref.getBoolean(key, (Boolean) defaultValue));
        } else if (defaultValue instanceof Float) {
            return (T) Float.valueOf(sharedPref.getFloat(key, (Float) defaultValue));
        } else if (defaultValue instanceof Long) {
            return (T) Long.valueOf(sharedPref.getLong(key, (Long) defaultValue));
        } else {
            throw new IllegalArgumentException("Unsupported data type");
        }
    }
    public static void clearSharedPref(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("MobTodo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}
