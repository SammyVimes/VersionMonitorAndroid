package com.danilov.versionmonitor.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.drafts.Draft_75;
import org.java_websocket.drafts.Draft_76;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.UUID;

/**
 * Created by semyon on 29.10.15.
 */
public class PushService extends Service implements IPushService {

    private Handler handler = new Handler();

    @Override
    public IBinder onBind(final Intent intent) {
        return new ServiceHolderBinder<PushService>(this);
    }

    public static Connection<PushService> connectService(final Context context, final ServiceConnectionListener<PushService> listener) {
        Intent intent = new Intent(context, PushService.class);
        Connection<PushService> streamingServiceConnection = new Connection<>(listener);
        context.bindService(intent, streamingServiceConnection, BIND_AUTO_CREATE);
        return streamingServiceConnection;
    }

    private String host = "ws://192.168.0.73:8001/connection/websocket";

    private PushClient client;

    @Override
    public void onCreate() {
        client = new PushClient(URI.create(host), new Draft_17());
        client.start();
    }

    @Override
    public void onOpen(final ServerHandshake handshakedata) {
        //sending connect
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid", UUID.randomUUID().toString());
            jsonObject.put("method", "connect");

            JSONObject params = new JSONObject();
            params.put("user", "my-client-id");
            params.put("timestamp", "1446134472610");
            params.put("info", "");
            params.put("token", "f6ead68483cc078cde1df20fde625a265030983d297a9ca0a9cb4e6347bcb18a");
            jsonObject.put("params", params);

            JSONArray messages = new JSONArray();
            messages.put(jsonObject);

            client.send(messages.toString());

        } catch (JSONException e) {

        }
    }

    public void onConnected() {

    }

    @Override
    public void onMessage(final String message) {
        Log.d("PUSH", "Message from fugo: " + message);
        try {
            JSONArray messageArray = new JSONArray(message);
            for (int i = 0; i < messageArray.length(); i++) {
                JSONObject messageObj = messageArray.optJSONObject(i);
                JSONObject body = messageObj.optJSONObject("body");
                if (messageObj.optString("method", "").equals("connect")) {
                    subscribe();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void subscribe() {
        //sending connect
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid", UUID.randomUUID().toString());
            jsonObject.put("method", "subscribe");

            JSONObject params = new JSONObject();
            params.put("channel", "python");
            jsonObject.put("params", params);

            JSONArray messages = new JSONArray();
            messages.put(jsonObject);

            client.send(messages.toString());

        } catch (JSONException e) {

        }
    }

    @Override
    public void onClose(final int code, final String reason, final boolean remote) {
        Log.d("PUSH", "onClose: " + code + ", " + reason + ", " + remote);
    }

    @Override
    public void onError(final Exception ex) {
        Log.e("PUSH", "onError: ", ex);
    }

    private class PushClient extends WebSocketClient {

        private Thread clientThread;

        public PushClient(final URI serverURI, final Draft draft) {
            super(serverURI, draft);
            clientThread = new Thread() {
                @Override
                public void run() {
                    String error = "";
                    try {
                        boolean success = connectBlocking();
                        if (success) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    onConnected();
                                }
                            });
                            return;
                        }
                        error = "Unknow error";
                    } catch (InterruptedException e) {
                        error = e.getMessage();
                    }
                    Log.e("PUSH", "Error while connecting: " + error);
                }
            };
        }

        @Override
        public void onOpen(final ServerHandshake handshakedata) {
            PushService.this.onOpen(handshakedata);
        }

        @Override
        public void onMessage(final String message) {
            PushService.this.onMessage(message);
        }

        @Override
        public void onClose(final int code, final String reason, final boolean remote) {
            PushService.this.onClose(code, reason, remote);
        }

        @Override
        public void onError(final Exception ex) {
            PushService.this.onError(ex);
        }

        public void start() {
            clientThread.start();
        }

    }

}
