package com.jackpan.schoolintroductory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferncesHelp {

    public static final String  NAME = "MySharedPrefernces";


    public static final String KEY_IS_FRACTION ="isFraction";
    // 設定分數
    public static void saveIsFraction(Context context, long token) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        sp.edit().putLong(KEY_IS_FRACTION, token).apply();
    }
//  取得分數

    public static long getIsFraction(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Activity.MODE_PRIVATE);
        return sp.getLong(KEY_IS_FRACTION, 100);
    }
}
