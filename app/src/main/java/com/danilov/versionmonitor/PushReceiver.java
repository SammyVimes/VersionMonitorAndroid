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

import com.danilov.acentrifugo.Info;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by semyon on 30.10.15.
 */
public class PushReceiver extends BroadcastReceiver {

    private static final String TAG = "PushReceiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Info info = intent.getParcelableExtra("info");
        if (info != null) {
            switch (info.getState()) {
                case Info.CONNECTED:
                    Log.i(TAG, "Connected to centrifugo");
                    break;
                case Info.SUBSCRIBED_TO_CHANNEL:
                    Log.i(TAG, "Subscribed to channel " + info.getValue());
                    break;
            }
            return;
        }
        String body = intent.getStringExtra("body");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("PUSH", "Message from centrifugo: " + body);
        if (jsonObject == null) {
            return;
        }
        body = jsonObject.optString("data", "");
        JSONObject data = null;
        try {
            data = new JSONObject(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (data == null) {
            return;
        }
        String text = data.optString("text", "");
        Long projectId = Long.valueOf(data.optString("project_id", "0"));

        Resources res = context.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle().bigText(text);
        String title = context.getString(R.string.app_name);
        Intent startApp = new Intent(context, DetailsActivity.class);
        startApp.putExtra(DetailsActivity.PROJECT_ID_EXTRA, projectId);
        PendingIntent goToAppPendingIntent = PendingIntent.getActivity(context, 1235091, startApp, PendingIntent.FLAG_UPDATE_CURRENT);

        String ticker = text;
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
                .setContentText(text)
                .setAutoCancel(true)
                .setStyle(bigTextStyle);

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(1337, builder.build());
    }

}
