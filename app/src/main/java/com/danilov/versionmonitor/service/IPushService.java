package com.danilov.versionmonitor.service;

import org.java_websocket.handshake.ServerHandshake;

import java.nio.ByteBuffer;

/**
 * Created by semyon on 29.10.15.
 */
public interface IPushService {

    void onOpen(final ServerHandshake handshakedata);

    void onMessage(final String message);

    void onClose(final int code, String reason, final boolean remote);

    void onError(final Exception ex);

}
