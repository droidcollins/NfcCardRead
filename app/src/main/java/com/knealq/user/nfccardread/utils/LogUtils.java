package com.knealq.user.nfccardread.utils;

import android.util.Log;

/**
 * Created by user on 7/5/17.
 */

public final class LogUtils {

    private static final boolean DEBUGABLE = true;
    private static final String DEBUG_TAG = "NfcReadCard-";

    public static void v(String tag, String msg) {
        if (DEBUGABLE) {
            Log.v(DEBUG_TAG + tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable thr) {
        if (DEBUGABLE) {
            Log.v(DEBUG_TAG + tag, msg, thr);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUGABLE) {
            Log.d(DEBUG_TAG + tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable thr) {
        if (DEBUGABLE) {
            Log.d(DEBUG_TAG + tag, msg, thr);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUGABLE) {
            Log.e(DEBUG_TAG + tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable thr) {
        if (DEBUGABLE) {
            Log.e(DEBUG_TAG + tag, msg, thr);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUGABLE) {
            Log.i(DEBUG_TAG + tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable thr) {
        if (DEBUGABLE) {
            Log.i(DEBUG_TAG + tag, msg, thr);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUGABLE) {
            Log.i(DEBUG_TAG + tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable thr) {
        if (DEBUGABLE) {
            Log.i(DEBUG_TAG + tag, msg, thr);
        }
    }
}
