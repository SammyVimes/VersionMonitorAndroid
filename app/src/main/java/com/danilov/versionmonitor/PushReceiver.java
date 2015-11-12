package com.danilov.versionmonitor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by semyon on 30.10.15.
 */
public class PushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String body = intent.getStringExtra("body");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("PUSH", "Message from fugo: " + body);
        if (jsonObject == null) {
            return;
        }
        body = jsonObject.optString("data", "");

        Resources res = context.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle().bigText(body);
        String title = context.getString(R.string.app_name);
        PendingIntent goToAppPendingIntent = PendingIntent.getActivity(context, 1235091, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

        String ticker = body;
        if (ticker.length() > 50) {
            ticker = ticker.substring(0, 50) + "...";
        }

        boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        int smallIconId = whiteIcon ? R.drawable.ic_cloud : R.drawable.ic_cloud;

        builder.setSmallIcon(smallIconId)
                // большая картинка
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_cloud))
                .setTicker(ticker)
                .setContentIntent(goToAppPendingIntent)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(bigTextStyle);

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(1337, builder.build());
    }

}
