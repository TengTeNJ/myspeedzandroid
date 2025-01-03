package com.potent.util;

import android.util.Log;

/**
 * Created by Linzl on 2014/10/20.
 */
public class MLog {
    private static boolean DEBUG = true;

    public static void e(String tag, String msg) {
        if (DEBUG) Log.e(tag, msg);
    }
}
