package com.danilov.versionmonitor.service;

import android.app.Service;
import android.os.Binder;

/**
 * Created by semyon on 18.08.15.
 */
public class ServiceHolderBinder<T extends Service> extends Binder {

    private T service;

    public ServiceHolderBinder(final T service) {
        this.service = service;
    }

    public T getService() {
        return service;
    }

}