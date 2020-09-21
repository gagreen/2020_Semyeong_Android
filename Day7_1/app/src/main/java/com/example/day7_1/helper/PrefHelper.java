package com.example.day7_1.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefHelper {
    public static void setWish(Context ctx, long productId, boolean b) {
        SharedPreferences pref = ctx.getSharedPreferences("wish", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("wish_"+productId, b);
        editor.apply();
    }

    public static boolean isWish(Context ctx, long productId) {
        SharedPreferences pref = ctx.getSharedPreferences("wish",0);
        boolean b = pref.getBoolean("wish_"+productId, false);
        return b;
    }
}
