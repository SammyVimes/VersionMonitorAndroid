package com.danilov.versionmonitor.service;

import android.app.Service;

/**
 * Created by semyon on 18.08.15.
 */
public interface ServiceConnectionListener<T extends Service> {

    public void onServiceConnected(final T service);

    public void onServiceDisconnected(final T service);

}
