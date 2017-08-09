package com.georgeren.myboring.utils;

import android.util.Log;

public class ULog {

    private static boolean isDebug = true;
    public final static String TAG = "调试应用";

    public static void setDebug(boolean isDebug) {
        ULog.isDebug = isDebug;
    }

    public static boolean getDebug() {
        return isDebug;
    }
    public static void v(String tag, String type, String msg) {
        if (isDebug) {
            String des = String.format("[%s][%s]%s", tag, type, msg);
            Log.v(TAG, des);
        }
    }
    public static void i(String tag, String type, String msg) {
        if (isDebug) {
            String des = String.format("[%s][%s]%s", tag, type, msg);
            Log.i(TAG, des);
        }
    }


    public static void e(String tag, String type, String msg) {
        if (isDebug) {
            String des = String.format("[%s][%s]%s", tag, type, msg);
            Log.e(TAG, des);
        }
    }

    public static void e(Throwable tr) {
        if (isDebug) {
            Log.e(TAG, "", tr);
        }
    }

    public static void syso(String tag, String type, String msg) {
        if (isDebug) {
            String des = String.format("[%s][%s]%s", tag, type, msg);
            System.out.println(des);
        }
    }
    public static void d(String tag, String type, String msg) {
        String des = String.format("[%s][%s]%s", tag, type, msg);
        Log.d(TAG, des);
    }
}
