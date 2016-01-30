package com.tonilopezmr.login.providers;

/**
 * @author Antonio López.
 */
public interface Provider {
    boolean isConnected();
    void connect();
    void disconnect();
    String getName();
}
