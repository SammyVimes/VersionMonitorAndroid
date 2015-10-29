package com.danilov.versionmonitor.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by semyon on 18.08.15.
 */
public class Connection<T extends Service> implements ServiceConnection {

    private ServiceConnectionListener<T> listener;
    private T service;

    public Connection(final ServiceConnectionListener<T> listener) {
        this.listener = listener;
    }

    @Override
    public void onServiceConnected(final ComponentName name, final IBinder service) {
        ServiceHolderBinder<T> binder = (ServiceHolderBinder<T>) service;
        this.service = binder.getService();
        listener.onServiceConnected(this.service);
    }

    @Override
    public void onServiceDisconnected(final ComponentName name) {
        listener.onServiceDisconnected(this.service);
    }

}
