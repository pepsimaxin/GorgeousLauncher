package com.gorgeous.launcher3.util;

import android.util.Log;

/**
 * <pre>
 *     author: Marco
 *     date  : 2024.09.14
 *     desc  : utils about log
 * </pre>
 */
public class LogUtils {
    private static final String APP_TAG = "Gorgeous_Launcher";
    private static final String SPIT = "===> ";

    /**
     * 定义全局的标志
     */
    private static final boolean mIsOpen = true;

    public static void i(String tag, String msg) {
        if (mIsOpen) {
            Log.i(APP_TAG, "[ " + tag + " ]" + " 日志输出：" + SPIT + msg);
        }
    }

    public static void i(String tag, String msg, Throwable e) {
        if (mIsOpen) {
            Log.i(APP_TAG, "[ " + tag + " ]" + " 日志输出：" + SPIT + msg, e);
        }
    }

    public static void w(String tag, String msg) {
        if (mIsOpen) {
            Log.w(APP_TAG, "[ " + tag + " ]" + " 日志输出：" + SPIT + msg);
        }
    }

    public static void w(String tag, String msg, Throwable e) {
        if (mIsOpen) {
            Log.w(APP_TAG, "[ " + tag + " ]" + " 日志输出：" + SPIT + msg, e);
        }
    }

    public static void e(String tag, String msg) {
        if (mIsOpen) {
            Log.e(APP_TAG, "[ " + tag + " ]" + " 日志输出：" + SPIT + msg);
        }
    }

    public static void e(String tag, String msg, Throwable e) {
        if (mIsOpen) {
            Log.e(APP_TAG, "[ " + tag + " ]" + " 日志输出：" + SPIT + msg, e);
        }
    }

    public static void d(String tag, String msg) {
        if (mIsOpen) {
            Log.d(APP_TAG, "[ " + tag + " ]" + " 日志输出：" + SPIT + msg);
        }
    }

    public static void d(String tag, String msg, Throwable e) {
        if (mIsOpen) {
            Log.d(APP_TAG, "[ " + tag + " ]" + " 日志输出：" + SPIT + msg, e);
        }
    }

    public static void v(String tag, String msg) {
        if (mIsOpen) {
            Log.v(APP_TAG, "[ " + tag + " ]" + " 日志输出：" + SPIT + msg);
        }
    }
}

