package com.sopra.ntts.controller;

import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * This classes inherits from the org.java_websocket.client.WebSocketClient.
 * It's needed to establish a connection to the server.
 *
 * @author Sedat Qaja
 */
public class SimpleClient extends WebSocketClient {

  final Logger logger = LoggerFactory.getLogger(SimpleClient.class);

  final static Gson gson = new Gson();

  private ConnectionHandler connectionHandler;
  private MessageReceiver messageReceiver;

  public SimpleClient(URI serverUri, ConnectionHandler connectionHandler, MessageReceiver messageReceiver) {
    super(serverUri);
    this.connectionHandler = connectionHandler;
    this.messageReceiver = messageReceiver;
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {
    logger.info("SimpleClient connected to: " + this.getConnection().getRemoteSocketAddress().toString());

    connectionHandler.setConnection(this);
  }

  @Override
  public void onMessage(String message) {
    messageReceiver.handleMessage(message);
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    logger.info("SimpleClient disconnected from: " + this.getConnection().getRemoteSocketAddress().toString());

    if (!connectionHandler.getParent().getGameHandler().isGameOver) {

      // sets the connection again and sends reconnect message
      this.connect();
      connectionHandler.getParent().messageEmitter.sendReconnectMessage(connectionHandler.getParent().getGameHandler().sessionId);
    }
  }

  @Override
  public void onError(Exception ex) {
    logger.error("SimpleClient, onError executed: " + ex.getMessage());
  }
}
