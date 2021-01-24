package com.sopra.ntts.controller;

import com.google.gson.Gson;
import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;
import com.sopra.ntts.model.NetworkStandard.DataTypes.Operations.Operation;
import com.sopra.ntts.model.NetworkStandard.DataTypes.RoleEnum;
import com.sopra.ntts.model.NetworkStandard.MessageTypeEnum.MessageTypeEnum;
import com.sopra.ntts.model.NetworkStandard.Messages.*;
import org.java_websocket.WebSocket;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * This class emits messages to the game server.
 *
 * @author Sedat Qaja
 */
public class MessageEmitter {

  Gson gson = new Gson();

  private ConnectionHandler connectionHandler;

  public void setConnectionHandler(ConnectionHandler connectionHandler) {
    this.connectionHandler = connectionHandler;
  }

  public MessageEmitter() {
    connectionHandler = null;
  }

  /**
   * Send a message to the server.
   *
   * @param jsonMessage Message to send.
   */
  private void sendMessage(String jsonMessage) {
    WebSocket connection = connectionHandler.getConnection();
    if (connection == null) {
      return;
    }
    connection.send(jsonMessage);
  }

  /**
   * Sends the first message to establish the connection.
   *
   * @param name User chosen name.
   * @param role User chosen name.
   */
  public void sendHelloMessage(String name, RoleEnum role) {
    Date date = new Date();
    HelloMessage helloMessage = new HelloMessage(null, MessageTypeEnum.HELLO, date, null, name, role);
    String jsonMessage = gson.toJson(helloMessage);
    sendMessage(jsonMessage);
    System.out.println("MESSAGE SENT:" + jsonMessage);

  }

  /**
   * Sends message to reconnect to the server.
   *
   * @param sessionId UUID known from the server.
   */
  public void sendReconnectMessage(UUID sessionId) {
    UUID userId = connectionHandler.getUserId();
    Date date = new Date();
    ReconnectMessage reconnectMessage = new ReconnectMessage(userId, MessageTypeEnum.RECONNECT, date, "sending reconnect message", sessionId);
    String jsonMessage = gson.toJson(reconnectMessage);

    sendMessage(jsonMessage);
    System.out.println("MESSAGE SENT:" + jsonMessage);
  }

  /**
   * Sends message about the chosen item or character.
   *
   * @param chosenCharacterId UUID which character is chosen.
   * @param gadget            Gadget which character is chosen.
   */
  public void sendItemChoiceMessage(UUID chosenCharacterId, GadgetEnum gadget) {
    ItemChoiceMessage itemChoiceMessage = new ItemChoiceMessage(connectionHandler.getUserId(), MessageTypeEnum.ITEM_CHOICE, Date.from(Instant.now()), null, chosenCharacterId, gadget);
    String jsonMessage = gson.toJson(itemChoiceMessage);
    sendMessage(jsonMessage);
    System.out.println("MESSAGE SENT:" + jsonMessage);
  }

  /**
   * Sends redundant message for safety reasons on the chosen equipment.
   *
   * @param equipment Map for classification which character and gadgets are chosen and equip.
   */
  public void sendEquipmentChoiceMessage(Map<UUID, Set<GadgetEnum>> equipment) {
    UUID userId = connectionHandler.getUserId();
    Date date = new Date();
    EquipmentChoiceMessage equipmentChoiceMessage = new EquipmentChoiceMessage(userId, MessageTypeEnum.EQUIPMENT_CHOICE, date, null, equipment);
    String jsonMessage = gson.toJson(equipmentChoiceMessage);
    sendMessage(jsonMessage);
    System.out.println("MESSAGE SENT:" + jsonMessage);
  }

  /**
   * Sends message which operation is done by the player.
   *
   * @param operation Operation chosen.
   */
  public void sendGameOperationMessage(Operation operation) {
    UUID userId = connectionHandler.getUserId();
    Date date = new Date();
    GameOperationMessage gameOperationMessage = new GameOperationMessage(userId, MessageTypeEnum.GAME_OPERATION, date, null, operation);
    String jsonMessage = gson.toJson(gameOperationMessage);
    sendMessage(jsonMessage);
    System.out.println("MESSAGE SENT:" + jsonMessage);
  }

  /**
   * Sends message to the server that the client is leaving the game.
   */
  public void sendGameLeaveMessage() {
    UUID userId = connectionHandler.getUserId();
    Date date = new Date();
    GameLeaveMessage gameLeaveMessage = new GameLeaveMessage(userId, MessageTypeEnum.GAME_LEAVE, date, null);
    String jsonMessage = gson.toJson(gameLeaveMessage);
    sendMessage(jsonMessage);
    System.out.println("MESSAGE SENT:" + jsonMessage);
  }

  /**
   * Send message that the game ist to be paused or continued.
   *
   * @param gamePause Tells if game is Paused (true) or continued (false).
   */
  public void sendRequestGamePauseMessage(boolean gamePause) {
    UUID userId = connectionHandler.getUserId();
    Date date = new Date();
    RequestGamePauseMessage requestGamePauseMessage = new RequestGamePauseMessage(userId, MessageTypeEnum.REQUEST_GAME_PAUSE, date, null, gamePause);
    String jsonMessage = gson.toJson(requestGamePauseMessage);
    sendMessage(jsonMessage);
    System.out.println("MESSAGE SENT:" + jsonMessage);
  }

  /**
   * Send a request to the server for the configuration file.
   */
  public void sendRequestMetaInformationMessage(String[] keys) {
    UUID userId = connectionHandler.getUserId();
    Date date = new Date();
    RequestMetaInformationMessage requestMetaInformationMessage = new RequestMetaInformationMessage(userId, MessageTypeEnum.REQUEST_META_INFORMATION, date, null, keys);
    String jsonMessage = gson.toJson(requestMetaInformationMessage);
    sendMessage(jsonMessage);
    System.out.println("MESSAGE SENT:" + jsonMessage);
  }

  /**
   * Send a request to the server for a game replay.
   */
  public void sendRequestReplayMessage() {
    UUID userId = connectionHandler.getUserId();
    Date date = new Date();
    RequestReplayMessage requestReplayMessage = new RequestReplayMessage(userId, MessageTypeEnum.REQUEST_REPLAY, date, null);
    String jsonMessage = gson.toJson(requestReplayMessage);
    sendMessage(jsonMessage);
    System.out.println("MESSAGE SENT:" + jsonMessage);
  }
}
