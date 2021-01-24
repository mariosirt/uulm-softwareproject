package com.sopra.ntts.controller;

import org.java_websocket.WebSocket;

import java.util.UUID;

/**
 * The class manages a webSocket connection to the game server.
 *
 * @author Sedat Qaja
 */
public class ConnectionHandler {

    private NoTimeToSpy parent;

    private SimpleClient simpleClient;

    private WebSocket connection;

    private UUID userId;

    /**
     * Method for setting a client to the ConnectionHandler.
     *
     * @param simpleClient the client is set for the ConnectionHandler.
     */
    public void setSimpleClient(SimpleClient simpleClient) {
        this.simpleClient = simpleClient;
    }

    /**
     * Method returns the SimpleClient, if not available the constructor Method is enforced.
     *
     * @return The SimpleClient that is returned.
     */
    public SimpleClient getSimpleClient() {
        if (simpleClient == null) {
            reset();
        }

        return simpleClient;
    }

    /**
     * Method for setting a WebSocket to the ConnectionHandler.
     *
     * @param connection WebSocket which is set for the ConnectionHandler.
     */
    public void setConnection(WebSocket connection) {
        if (this.connection == null) {
            this.userId = null;
            this.connection = connection;
            this.connection.setAttachment(null);
        } else {
            closeConnection();
            setConnection(connection);
        }
    }

    /**
     * Method to get the WebSocket to the ConnectionHandler.
     *
     * @return The WebSocket that is used for the ConnectionHandler.
     */
    public WebSocket getConnection() {
        if (connection == null) {
            return null;
        }
        return connection;
    }

    /**
     * Setter for the private attribute UserID.
     *
     * @param userId The user specific ID.
     */
    public void setUserId(UUID userId) {
        this.userId = userId;
        this.connection.setAttachment(this.userId);
    }

    /**
     * Getter for the UserId.
     *
     * @return UserId that is set for the ConnectionHandler.
     */
    public UUID getUserId() {
        return userId;
    }

    public NoTimeToSpy getParent() {
        return parent;
    }

    public void setParent(NoTimeToSpy parent) {
        this.parent = parent;
    }

    /**
     * Constructor for the ConnectionHandler.
     */
    public ConnectionHandler() {
        reset();
    }

    /**
     * Method to check if the connection is open.
     *
     * @return Boolean value if connection is open.
     */
    public boolean connectionOpen() {
        if (connection == null) {
            return false;
        }

        if (connection.isOpen()) {
            return true;
        } else {
            closeConnection();
            return false;
        }
    }

    /**
     * Method to close the connection after the use.
     */
    public void closeConnection() {
        if (connection == null) {
            return;
        }
        connection.close();
        reset();
    }

    /**
     * Helper method for the constructor.
     */
    private void reset() {
        this.simpleClient = null;
        this.connection = null;
        this.userId = null;
    }
}
