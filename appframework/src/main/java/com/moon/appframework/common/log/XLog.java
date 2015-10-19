package com.moon.appframework.common.log;

import android.util.Log;

/**
 * @author kidcrazequ
 *
 */

public final class XLog {

    private static final int CHUNK_SIZE = 4000;
    private static LogLevel logLevel = LogLevel.FULL;

    private static final String TAG = "Logger";

    public static void d(String message) {
        log(Log.DEBUG, message);
    }

    public static void e(String message) {
        log(Log.ERROR, message);
    }

    public static void w(String message) {
        log(Log.WARN, message);
    }

    public static void i(String message) {
        log(Log.INFO, message);
    }

    public static void v(String message) {
        log(Log.VERBOSE, message);
    }

    public static void wtf(String message) {
        log(Log.ASSERT, message);
    }

    private static void log(int logType, String message) {

        if (logLevel == LogLevel.NONE) {
            return;
        }
        int length = message.length();
        if (length <= CHUNK_SIZE) {
            logChunk(logType, message);
            return;
        }

        for (int i = 0; i < length; i += CHUNK_SIZE) {
            int end = Math.min(length, i + CHUNK_SIZE);
            logChunk(logType, message.substring(i, end));
        }

    }

    private static void logChunk(int logType, String chunk) {
        switch (logType) {
            case Log.ERROR:
                Log.e(TAG, chunk);
                break;
            case Log.INFO:
                Log.i(TAG, chunk);
                break;
            case Log.VERBOSE:
                Log.v(TAG, chunk);
                break;
            case Log.WARN:
                Log.w(TAG, chunk);
                break;
            case Log.ASSERT:
                Log.wtf(TAG, chunk);
                break;
            case Log.DEBUG:
            default:
                Log.d(TAG, chunk);
                break;
        }
    }

    public static LogLevel getLogLevel() {
        return logLevel;
    }

    public static void setLogLevel(LogLevel logLevel) {
        XLog.logLevel = logLevel;
    }

}
