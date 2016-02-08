package com.danilov.versionmonitor;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.danilov.acentrifugo.PushService;

/**
 * Created by semyon on 12.11.15.
 */
public class CHelper {

    public static void startCentrifugoService() {
        final Context context = App.getContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String pi = sharedPreferences.getString("PUSH_ID", "");
        String token = sharedPreferences.getString("PUSH_TOKEN", "");
        String ts = sharedPreferences.getLong("PUSH_TIMESTAMP", 0) + "";
        String channel = context.getString(R.string.centrifugo_channel) + pi;
        if (!"".equals(pi)) {
            PushService.subscribe(context, context.getString(R.string.centrifugo_host), channel, null, pi, token, ts);
        }
    }

}
