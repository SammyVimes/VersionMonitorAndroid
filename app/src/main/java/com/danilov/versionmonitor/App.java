package com.danilov.versionmonitor;

import android.app.Application;
import android.util.Log;

/**
 * Created by Semyon on 14.10.2015.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        final Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread thread, final Throwable ex) {
                Log.e("VM", "error:", ex);
                defaultUncaughtExceptionHandler.uncaughtException(thread, ex);
            }
        });
    }
}
