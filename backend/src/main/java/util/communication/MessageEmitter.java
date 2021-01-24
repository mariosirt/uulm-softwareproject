package util.communication;

/**
 * This class sends messages to the clients.
 *
 * @author Christian Wendlinger
 */

import NetworkStandard.MessageContainer.MessageContainer;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;
import NetworkStandard.Messages.*;
import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import util.Game;
import util.User;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MessageEmitter {
    Gson gson = new Gson();

    private List<User> clients;
    private List<User> viewers;

    private Game game;

    public MessageEmitter(Game game, List<User> clients, List<User> viewers) {
        this.game = game;
        this.clients = clients;
        this.viewers = viewers;
    }

    /**
     * Getter and Setter.
     */
    public List<User> getClients() {
        return clients;
    }

    public void setClients(List<User> clients) {
        this.clients = clients;
    }

    public List<User> getViewers() {
        return viewers;
    }

    public void setViewers(List<User> viewers) {
        this.viewers = viewers;
    }

    /**
     * Send a message to all clients.
     *
     * @param message json formatted message
     */
    private void sendMessageToClients(String message) {
        for (User client : clients) {
            try {
                MessageContainer messageContainer = gson.fromJson(message, MessageContainer.class);
                // Dummy message
                MessageContainer toSend = new MessageContainer(UUID.randomUUID(), MessageTypeEnum.STRIKE, Date.from(Instant.now()), "");

                if (messageContainer.type == MessageTypeEnum.GAME_STARTED) {
                    toSend = gson.fromJson(message, GameStartedMessage.class);
                } else if (messageContainer.type == MessageTypeEnum.GAME_STATUS) {
                    toSend = gson.fromJson(message, GameStatusMessage.class);
                    ((GameStatusMessage) toSend).getState().setMySafeCombinations(client.equals(game.getPlayers().get(0)) ? game.getPlayerOneSafeCombinations() : game.getPlayerTwoSafeCombinations());
                } else if (messageContainer.type == MessageTypeEnum.STATISTICS) {
                    toSend = gson.fromJson(message, StatisticsMessage.class);
                } else if (messageContainer.type == MessageTypeEnum.GAME_LEFT) {
                    toSend = gson.fromJson(message, GameLeftMessage.class);
                } else if (messageContainer.type == MessageTypeEnum.GAME_PAUSE) {
                    toSend = gson.fromJson(message, GamePauseMessage.class);
                }

                toSend.clientId = client.getPlayerId();
                message = gson.toJson(toSend);
                client.getConnection().send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Send a message to all viewers,
     *
     * @param message json formatted message
     */
    private void sendMessageToViewers(String message) {
        for (User viewer : viewers) {
            try {
                MessageContainer messageContainer = gson.fromJson(message, MessageContainer.class);
                // Dummy message
                MessageContainer toSend = new MessageContainer(UUID.randomUUID(), MessageTypeEnum.STRIKE, Date.from(Instant.now()), "");

                if (messageContainer.type == MessageTypeEnum.GAME_STARTED) {
                    toSend = gson.fromJson(message, GameStartedMessage.class);
                } else if (messageContainer.type == MessageTypeEnum.GAME_STATUS) {
                    toSend = gson.fromJson(message, GameStatusMessage.class);
                } else if (messageContainer.type == MessageTypeEnum.STATISTICS) {
                    toSend = gson.fromJson(message, StatisticsMessage.class);
                } else if (messageContainer.type == MessageTypeEnum.GAME_LEFT) {
                    toSend = gson.fromJson(message, GameLeftMessage.class);
                } else if (messageContainer.type == MessageTypeEnum.GAME_PAUSE) {
                    toSend = gson.fromJson(message, GamePauseMessage.class);
                }

                toSend.clientId = viewer.getPlayerId();
                message = gson.toJson(toSend);
                viewer.getConnection().send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Send a HelloReplyMessage.
     */
    public void sendHelloReplyMessage(WebSocket client, HelloReplyMessage message) {
        String jsonMessage = gson.toJson(message);
        try {
            client.send(jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("MESSAGE SENT: " + jsonMessage);


    }

    /**
     * Send a GameStartedMessage.
     */
    public void sendGameStartedMessage(GameStartedMessage message) {
        String jsonMessage = gson.toJson(message);
        sendMessageToClients(jsonMessage);
        sendMessageToViewers(jsonMessage);
        System.out.println("MESSAGE SENT: " + jsonMessage);

    }

    /**
     * Send a RequestItemChoiceMessage.
     */
    public void sendRequestItemChoiceMessage(WebSocket client, RequestItemChoiceMessage message) {
        String jsonMessage = gson.toJson(message);
        try {
            client.send(jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("MESSAGE SENT: " + jsonMessage);


    }

    /**
     * Send a RequestEquipmentChoiceMessage.
     */
    public void sendRequestEquipmentChoiceMessage(WebSocket client, RequestEquipmentChoiceMessage message) {
        String jsonMessage = gson.toJson(message);
        try {
            client.send(jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("MESSAGE SENT: " + jsonMessage);


    }

    /**
     * Send a GameStatusMessage.
     */
    public void sendGameStatusMessage(GameStatusMessage message) {
        String jsonMessage = gson.toJson(message);
        sendMessageToClients(jsonMessage);
        sendMessageToViewers(jsonMessage);
        System.out.println("MESSAGE SENT: " + jsonMessage);

    }

    /**
     * Send a RequestGameOperationMessage.
     */
    public void sendRequestGameOperationMessage(WebSocket client, RequestGameOperationMessage message) {
        String jsonMessage = gson.toJson(message);
        try {
            client.send(jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("MESSAGE SENT: " + jsonMessage);

    }

    /**
     * Send a StatisticsMessage.
     */
    public void sendStatisticsMessage(StatisticsMessage message) {
        String jsonMessage = gson.toJson(message);
        sendMessageToClients(jsonMessage);
        sendMessageToViewers(jsonMessage);
        System.out.println("MESSAGE SENT: " + jsonMessage);

    }

    /**
     * Send a GameLeftMessage.
     */
    public void sendGameLeftMessage(GameLeftMessage message) {
        String jsonMessage = gson.toJson(message);
        try {
            sendMessageToClients(jsonMessage);
            sendMessageToViewers(jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("MESSAGE SENT: " + jsonMessage);

    }

    /**
     * Send GameLeftMessage to a specific viewer.
     *
     * @param viewer
     * @param message
     */
    public void sendGameLeftMessageTo(WebSocket viewer, GameLeftMessage message) {
        String jsonMessage = gson.toJson(message);
        viewer.send(jsonMessage);
        System.out.println("MESSAGE SENT: " + jsonMessage);
    }

    /**
     * Send a GamePauseMessage.
     */
    public void sendGamePauseMessage(GamePauseMessage message) {
        String jsonMessage = gson.toJson(message);
        sendMessageToClients(jsonMessage);
        sendMessageToViewers(jsonMessage);
        System.out.println("MESSAGE SENT: " + jsonMessage);

    }

    /**
     * Send a StrikeMessage.
     */
    public void sendStrikeMessage(WebSocket client, Strike message) {
        String jsonMessage = gson.toJson(message);
        client.send(jsonMessage);
        System.out.println("MESSAGE SENT: " + jsonMessage);

    }

    /**
     * Send a ReplayMessage.
     */
    public void sendReplayMessage(WebSocket client, ReplayMessage message) {
        String jsonMessage = gson.toJson(message);
        try {
            client.send(jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("MESSAGE SENT: " + jsonMessage);

    }

    /**
     * Send ErrorMessage.
     */
    public void sendErrorMessage(WebSocket client, ErrorMessage message) {
        String jsonMessage = gson.toJson(message);
        try {
            client.send(jsonMessage);
        } catch (Exception ignored) {}
        System.out.println("MESSAGE SENT: " + jsonMessage);
    }


    /**
     * @author Luca Uihlein
     * @param client
     * @param message
     */
    public void sendMetaInformationMessage(WebSocket client, MetaInformationMessage message) {
        String jsonMessage = gson.toJson(message);
        try {
            client.send(jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("MESSAGE SENT: " + jsonMessage);
    }


}
