package com.danilov.versionmonitor;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.Collections;

/**
 * Created by Semyon on 14.10.2015.
 */
public class BaseActivity extends AppCompatActivity {

    protected Context context = null;
    protected Picasso picasso = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        OkHttpClient client = new OkHttpClient();
        client.setProtocols(Collections.singletonList(Protocol.HTTP_1_1));
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttpDownloader(client));
        picasso = builder.build();
    }

    public <T extends View> T view(final int id) {
        return (T) findViewById(id);
    }

}
