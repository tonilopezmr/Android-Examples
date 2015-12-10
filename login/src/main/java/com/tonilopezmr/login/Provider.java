package com.tonilopezmr.login;

/**
 * @author Antonio López.
 */
public interface Provider {
    boolean isConnected();
    void connect();
    void disconnect();
    String getName();
}
