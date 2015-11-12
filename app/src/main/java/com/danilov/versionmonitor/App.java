package com.danilov.versionmonitor;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.danilov.acentrifugo.PushService;

/**
 * Created by Semyon on 14.10.2015.
 */
public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        final Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread thread, final Throwable ex) {
                Log.e("VM", "error:", ex);
                defaultUncaughtExceptionHandler.uncaughtException(thread, ex);
            }
        });
        CHelper.startCentrifugoService();
    }

    public static Context getContext() {
        return context;
    }

}
