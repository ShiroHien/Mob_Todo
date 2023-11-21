package com.example.mobiletodoapp.phuc_activity.reusecode;

import android.content.Context;
import android.widget.Toast;

public class Function {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static Boolean validateEmpty(String value, String message, Context context) {
        if (value.isEmpty()) {
            showToast(context, message);
            return false;
        }
        return true;
    }
}
