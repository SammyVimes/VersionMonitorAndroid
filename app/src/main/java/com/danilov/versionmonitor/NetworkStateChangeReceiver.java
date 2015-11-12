package com.danilov.versionmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.danilov.acentrifugo.PushService;

/**
 * Created by semyon on 11.11.15.
 */
public class NetworkStateChangeReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkStateChangeReceiver";


    public NetworkStateChangeReceiver() {
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d(TAG, "Network connectivity change");
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean newMobileEnabled = false;
        if (mobile != null) {
            newMobileEnabled = mobile.isConnectedOrConnecting();
        }
        boolean newWifiEnabled = wifi.isConnectedOrConnecting();
        String msg = "WiFi turned " + (newWifiEnabled ? "on" : "off");
        Log.d(TAG, msg);
        if (newWifiEnabled) {
            doRoutine();
        }
        msg = "Mobile network turned " + (newMobileEnabled ? "on" : "off");
        Log.d(TAG, msg);
        if (newMobileEnabled) {
            doRoutine();
        }
    }

    private void doRoutine() {
        CHelper.startCentrifugoService();
    }

}