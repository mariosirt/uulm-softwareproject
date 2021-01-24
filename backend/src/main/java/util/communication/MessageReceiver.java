package util.communication;

import NetworkStandard.Characters.Character;
import NetworkStandard.DataTypes.GadgetEnum;
import NetworkStandard.DataTypes.Gadgets.Gadget;
import NetworkStandard.DataTypes.Operations.*;
import NetworkStandard.MessageContainer.MessageContainer;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;
import NetworkStandard.Messages.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.java_websocket.WebSocket;
import util.Game;
import util.RuntimeTypeAdapterFactory;
import util.User;
import util.logic.ingame.OperationTimer;
import util.logic.ingame.PauseTimer;

import java.sql.Date;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles the incoming messages and redistributes them to other handlers.
 *
 * @author Christian Wendlinger & Sedat Qaja
 */
public class MessageReceiver {

    final ExecutorService executorService = Executors.newFixedThreadPool(1);

    /**
     * timer to handle pauses
     */
    private PauseTimer pauseTimer;
    private int operationSecondsPassed;

    public PauseTimer getPauseTimer() {
        return pauseTimer;
    }

    public void setPauseTimer(PauseTimer pauseTimer) {
        this.pauseTimer = pauseTimer;
    }

    public int getOperationSecondsPassed() {
        return operationSecondsPassed;
    }

    public void setOperationSecondsPassed(int operationSecondsPassed) {
        this.operationSecondsPassed = operationSecondsPassed;
    }

    /**
     * additional information for gson
     */
    RuntimeTypeAdapterFactory<Operation> operationAdapterFactory = RuntimeTypeAdapterFactory.of(Operation.class, "type")
            .registerSubtype(Movement.class, "MOVEMENT")
            .registerSubtype(GadgetAction.class, "GADGET_ACTION")
            .registerSubtype(GambleAction.class, "GAMBLE_ACTION")
            .registerSubtype(PropertyAction.class, "PROPERTY_ACTION")
            .registerSubtype(Retire.class, "RETIRE")
            .registerSubtype(Spy.class, "SPY_ACTION");

    Gson gson = new GsonBuilder().registerTypeAdapterFactory(operationAdapterFactory).create();

    private Game game;

    /**
     * constructor
     */

    public MessageReceiver(Game game) {
        this.game = game;
    }

    /**
     * find the correct type of a message and call the specified method
     *
     * @param json
     */
    public void handleMessage(String json) {
        System.out.println("RECEIVED JSON MESSAGE: "+json);
        executorService.execute(() -> {

            try {
                MessageContainer message = gson.fromJson(json, MessageContainer.class);
                MessageTypeEnum messageType = message.type;

                /* HELLO AND RECONNECT ARE HANDLED BY NTTSServer*/

                if (messageType.equals(MessageTypeEnum.ITEM_CHOICE)) {
                    receiveItemChoiceMessage(gson.fromJson(json, ItemChoiceMessage.class));
                } else if (messageType.equals(MessageTypeEnum.EQUIPMENT_CHOICE)) {
                    receiveEquipmentChoiceMessage(gson.fromJson(json, EquipmentChoiceMessage.class));
                } else if (messageType.equals(MessageTypeEnum.GAME_OPERATION)) {
                    receiveGameOperationMessage(gson.fromJson(json, GameOperationMessage.class));
                } else if (messageType.equals(MessageTypeEnum.GAME_LEAVE)) {
                    receiveGameLeaveMessage(gson.fromJson(json, GameLeaveMessage.class));
                } else if (messageType.equals(MessageTypeEnum.REQUEST_GAME_PAUSE)) {
                    receiveRequestGamePauseMessage(gson.fromJson(json, RequestGamePauseMessage.class));
                } else if (messageType.equals(MessageTypeEnum.REQUEST_REPLAY)) {
                    receiveRequestReplayMessage(gson.fromJson(json, RequestReplayMessage.class));
                } else if (messageType.equals(MessageTypeEnum.REQUEST_META_INFORMATION)) {
                    receiveRequestMetaInformationMessage(gson.fromJson(json, RequestMetaInformationMessage.class));
                } else {
                    handleIllegalMessage(message);
                }
            } catch (JsonSyntaxException jse) {
                jse.printStackTrace();
            }
        });
    }

    /**
     * send an error message if the message could not be identified
     *
     * @param message
     */
    private void handleIllegalMessage(MessageContainer message) {

        // find corresponding user
        User messageSender = null;

        for (User p : game.getPlayers()) {
            if (p.getPlayerId().equals(message.clientId)) {
                messageSender = p;
                break;
            }
        }

        for (User p : game.getViewers()) {
            if (p.getPlayerId().equals(message.clientId)) {
                messageSender = p;
                break;
            }
        }

        // send error
        if (messageSender != null) {
            game.emitter.sendErrorMessage(messageSender.getConnection(), new ErrorMessage(message.clientId, MessageTypeEnum.ERROR, Date.from(Instant.now()), "Message could not be parsed", ErrorTypeEnum.ILLEGAL_MESSAGE));
        }
    }

    /**
     * not implemented
     *
     * @param message
     */
    private void receiveRequestReplayMessage(RequestReplayMessage message) {
        System.out.println("RECEIVED MESSAGE:" + message.toString());
    }

    /**
     * handles game pauses requested by clients
     *
     * @param message
     */
    private void receiveRequestGamePauseMessage(RequestGamePauseMessage message) {
        System.out.println("RECEIVED MESSAGE:" + message.toString());

        if (game.getSettings().getTurnPhaseLimit() > 0 && (game.getPlayers().get(0).getPlayerId().equals(message.getClientId()) || game.getPlayers().get(1).getPlayerId().equals(message.getClientId()))) {
            // client requests pause
            if (message.isGamePause()) {
                // if game is not paused
                if (!game.isPaused()) {
                    // stop OperationTimer
                    operationSecondsPassed += game.getGameHandler().getTimer().stop();

                    // pause game
                    game.setPaused(true);
                    game.emitter.sendGamePauseMessage(new GamePauseMessage(null, MessageTypeEnum.GAME_PAUSE, Date.from(Instant.now()), "game paused", true, false));

                    // start pause timer
                    pauseTimer = new PauseTimer(this.game);
                    pauseTimer.schedule(game.getSettings().getPauseLimit());
                }
                // game already paused - Strike
                else {
                    sendStrike(game.getPlayers().get(0).getPlayerId().equals(message.clientId) ? 1 : 2, "game is already paused", "Game is already paused");
                }
            }
            // client requests unpause
            else if (!message.isGamePause() && game.isPaused()) {
                // game is paused
                if (game.isPaused()) {
                    resume(false);
                }
                // game is already running again - strike
                else {
                    sendStrike(game.getPlayers().get(0).getPlayerId().equals(message.clientId) ? 1 : 2, "game was already resumed", "game was already resumed");
                }
            }
        }
    }


    /**
     * continues the game
     *
     * @param enforced
     */
    public void resume(boolean enforced) {
        // unpause
        game.setPaused(false);

        // stop pause timer
        if (!enforced) {
            pauseTimer.stop();
        }

        // restart timer with remaining time
        game.getGameHandler().setTimer(new OperationTimer(this.game));
        game.getGameHandler().getTimer().schedule(game.getSettings().getTurnPhaseLimit() - operationSecondsPassed);

        // send unpause message to client and viewers
        game.emitter.sendGamePauseMessage(new GamePauseMessage(null, MessageTypeEnum.GAME_PAUSE, Date.from(Instant.now()), "game unpaused", false, enforced));
    }

    /**
     * handles a viewer or a player leaving the game
     *
     * @param message
     */
    private void receiveGameLeaveMessage(GameLeaveMessage message) {
        User left = null;

        for (User viewer : game.getViewers()) {
            if (viewer.getPlayerId().equals(message.getClientId())) {
                left = viewer;
            }
        }

        // viewer left
        if (left != null) {
            game.getViewers().removeIf(viewer -> viewer.getPlayerId().equals(message.getClientId()));
            game.emitter.sendGameLeftMessageTo(left.getConnection(), new GameLeftMessage(left.getPlayerId(), MessageTypeEnum.GAME_LEFT, Date.from(Instant.now()), "a viewer left the game", left.getPlayerId()));
        }

        // player left
        else {

            // find player that left
            for (User player : game.getPlayers()) {
                if (player.getPlayerId().equals(message.getClientId())) {
                    left = player;
                }
            }

            if (left != null) {
                // send left message
                game.emitter.sendGameLeftMessage(new GameLeftMessage(null, MessageTypeEnum.GAME_LEFT, Date.from(Instant.now()), "a player left the game", left.getPlayerId()));

                // send statistics message
                game.emitter.sendStatisticsMessage(new StatisticsMessage(null, MessageTypeEnum.STATISTICS, Date.from(Instant.now()), "a victory because one opponent left", Statistics.StatisticsEntry, game.getPlayers().get(0).getPlayerId(), VictoryEnum.VICTORY_BY_RANDOMNESS, false));

                // set active to false, so that matchmaker removes game from the HashMap
                game.setActive(false);
            }
        }
    }

    /**
     * handling game operations
     *
     * @param message
     */
    private void receiveGameOperationMessage(GameOperationMessage message) {
        System.out.println("RECEIVED MESSAGE:" + message.toString());

        // check if every player is connected
        boolean allConnected = true;

        for (User player : game.getPlayers()) {
            if (player.isDisconnected()) {
                allConnected = false;
                break;
            }
        }

        // parse Operation if the game is not paused and every player is connected
        if (!game.isPaused() && allConnected) {
            Character currentCharacter = game.getGameHandler().getCurrentCharacter();

            // message from Player 1
            if (message.clientId.equals(game.getPlayers().get(0).getPlayerId())) {
                // wrong character to operate
                if (!currentCharacter.getCharacterId().equals(message.getOperation().getCharacterId())) {
                    // its player 1 turn
                    if (game.getPlayerOneCharacters().contains(currentCharacter)) {
                        sendStrike(1, "Chose wrong character to operate", "You chose the wrong character to operate!");
                    }
                    // its not player 1 turn
                    else {
                        sendStrike(1, "not clients turn", "It is not your turn!");
                    }
                }
                // correct character to operate
                else {
                    // not player 1 turn
                    if (!game.getPlayerOneCharacters().contains(currentCharacter)) {
                        sendStrike(1, "not clients turn", "It is not your turn!");
                    } else {
                        // player 1 turn
                        game.getActionHandler().handleIncomingMessage(message);
                    }
                }
            }
            // Message from Player 2
            else if (message.clientId.equals(game.getPlayers().get(1).getPlayerId())) {
                // wrong character to operate
                if (!currentCharacter.getCharacterId().equals(message.getOperation().getCharacterId())) {
                    // its player 2 turn
                    if (game.getPlayerTwoCharacters().contains(currentCharacter)) {
                        sendStrike(2, "Chose wrong character to operate", "You chose the wrong character to operate!");
                    }
                    // its not player 2 turn
                    else {
                        sendStrike(2, "not clients turn", "It is not your turn!");
                    }
                }

                // correct character to operate
                else {
                    // not player 2 turn
                    if (!game.getPlayerTwoCharacters().contains(currentCharacter)) {
                        sendStrike(2, "not clients turn", "It is not your turn!");
                    } else {
                        //player 2 turn
                        game.getActionHandler().handleIncomingMessage(message);
                    }
                }
            }
        }
        //send strikes
        else {
            if (game.isPaused()) {
                sendStrike(game.getPlayers().get(0).getPlayerId().equals(message.clientId) ? 1 : 2, "game is paused and client tries to send an operation", "game is currently paused");
            } else if (!allConnected) {
                sendStrike(game.getPlayers().get(0).getPlayerId().equals(message.clientId) ? 1 : 2, "enemy is disconnected, so no operations allowed", "a player is disconnected");
            }
        }
    }

    /**
     * handling equipment choices
     *
     * @param message
     */
    private void receiveEquipmentChoiceMessage(EquipmentChoiceMessage message) {
        System.out.println("RECEIVED MESSAGE:" + message.toString());

        // player 1
        if (message.clientId.equals(game.getPlayers().get(0).getPlayerId())) {
            Map<UUID, Set<GadgetEnum>> mapping = message.getEquipment();

            // nothing was sent
            if (mapping == null) {
                sendStrike(1, "Client has not sent any Equipment or invalid data", "You have sent no Equipment choices!");
                return;
            }

            // iterate through map
            for (UUID character : mapping.keySet()) {
                Character toEquip = null;

                // find the right character
                for (Character ch : game.getAllCharacters()) {
                    if (ch.getCharacterId().equals(character)) {
                        toEquip = ch;
                    }
                }

                // invalid character from client
                if (!game.getPlayerOneCharacters().contains(toEquip)) {
                    sendStrike(1, "Client has not sent any Equipment or invalid data", "You have sent no Equipment choices!");
                    return;
                }
                // valid character
                else {
                    // generate GadgetList
                    Set<Gadget> equipment = new HashSet<>();

                    for (GadgetEnum gadget : mapping.get(character)) {
                        Gadget toAdd = null;

                        for (Gadget g : game.getPlayerOneGadgetChoices()) {
                            if (g.getGadget().equals(gadget)) {
                                toAdd = g;
                            }
                        }

                        // valid Gadget equipped
                        if (toAdd != null) {
                            equipment.add(toAdd);
                        } else {
                            sendStrike(1, "Client has not sent any Equipment or invalid data", "You have sent no Equipment choices!");
                            return;
                        }
                    }

                    // if all Gadgets were in the choice
                    if (equipment.size() == mapping.get(character).size()) {
                        toEquip.setGadgets(equipment);
                    }
                    // invalid choices in equipment
                    else {
                        sendStrike(1, "Client has not sent any Equipment or invalid data", "You have sent no Equipment choices!");
                        return;
                    }
                }
            }
            game.setPlayerOneCharOffer(null);
            game.setPlayerOneGadgetOffer(null);
            // game.setAllGadgets(null);

            game.ready[0] = true;
            game.getGameHandler().startGame();
        }

        // player 2
        else if (message.clientId.equals(game.getPlayers().get(1).getPlayerId())) {
            Map<UUID, Set<GadgetEnum>> mapping = message.getEquipment();

            // nothing was sent
            if (mapping == null) {
                sendStrike(2, "Client has not sent any Equipment or invalid data", "You have sent no Equipment choices!");
                return;
            }

            // iterate through map
            for (UUID character : mapping.keySet()) {
                Character toEquip = null;

                // find the right character
                for (Character ch : game.getAllCharacters()) {
                    if (ch.getCharacterId().equals(character)) {
                        toEquip = ch;
                    }
                }

                // invalid character from client
                if (!game.getPlayerTwoCharacters().contains(toEquip)) {
                    sendStrike(2, "Client has not sent any Equipment or invalid data", "You have sent no Equipment choices!");
                    return;
                }
                // valid character
                else {
                    // generate GadgetList
                    Set<Gadget> equipment = new HashSet<>();

                    for (GadgetEnum gadget : mapping.get(character)) {
                        Gadget toAdd = null;

                        for (Gadget g : game.getPlayerTwoGadgetChoices()) {
                            if (g.getGadget().equals(gadget)) {
                                toAdd = g;
                            }
                        }

                        // valid Gadget equipped
                        if (toAdd != null) {
                            equipment.add(toAdd);
                        } else {
                            sendStrike(2, "Client has not sent any Equipment or invalid data", "You have sent no Equipment choices!");
                            return;
                        }
                    }

                    // if all Gadgets were in the choice
                    if (equipment.size() == mapping.get(character).size()) {
                        toEquip.setGadgets(equipment);
                    }
                    // invalid choices in equipment
                    else {
                        sendStrike(2, "Client has not sent any Equipment or invalid data", "You have sent no Equipment choices!");
                        return;
                    }
                }
            }
            game.setPlayerTwoCharOffer(null);
            game.setPlayerTwoGadgetOffer(null);

            game.ready[1] = true;
            game.getGameHandler().startGame();
        }
    }

    /**
     * handling item choices
     *
     * @param message
     */
    private void receiveItemChoiceMessage(ItemChoiceMessage message) {
        System.out.println("RECEIVED MESSAGE:" + message.toString());

        // Choice from Player 1
        if (message.clientId.equals(game.getPlayers().get(0).getPlayerId())) {
            // ungültige Auswahl
            if (message.getChosenCharacterId() == null && message.getChosenGadget() == null || message.getChosenCharacterId() != null && message.getChosenGadget() != null) {
                sendStrike(1, "Client has not sent any ItemChoice or invalid data", "You have sent no Item choices or invalid Data!");
            } else {
                // character choice
                if (message.getChosenCharacterId() != null && message.getChosenGadget() == null) {
                    boolean valid = false;
                    Character chosen = null;

                    // check if character is in the offered list
                    for (Character character : game.getPlayerOneCharOffer()) {
                        if (character.getCharacterId().equals(message.getChosenCharacterId())) {
                            valid = true;
                            chosen = character;
                        }
                    }

                    // valid character choice
                    if (valid) {
                        game.getPlayerOneCharacters().add(chosen);
                        game.getNpcs().remove(chosen);
                        game.getItemChoicePhase().sendOffer();
                    }
                    // invalid character choice
                    else {
                        sendStrike(1, "Client has not sent any ItemChoice or invalid data", "You have sent no Item choices or invalid Data!");
                    }
                }
                // Gadget choice
                else {
                    boolean valid = false;
                    Gadget chosen = null;

                    // check if Gadget is in the offered list
                    for (Gadget gadget : game.getPlayerOneGadgetOffer()) {
                        if (gadget.getGadget().equals(message.getChosenGadget())) {
                            valid = true;
                            chosen = gadget;
                        }
                    }

                    // valid gadget choice
                    if (valid) {
                        game.getPlayerOneGadgetChoices().add(chosen);
                        game.getAllGadgets().remove(chosen);
                        game.getItemChoicePhase().sendOffer();
                    }
                    // invalid gadget choice
                    else {
                        sendStrike(1, "Client has not sent any ItemChoice or invalid data", "You have sent no Item choices or invalid Data!");
                    }
                }
            }
        }
        // Choice from Player 2
        else if (message.clientId.equals(game.getPlayers().get(1).getPlayerId())) {
            // ungültige Auswahl
            if (message.getChosenCharacterId() == null && message.getChosenGadget() == null || message.getChosenCharacterId() != null && message.getChosenGadget() != null) {
                sendStrike(2, "Client has not sent any ItemChoice or invalid data", "You have sent no Item choices or invalid Data!");
            } else {
                // character choice
                if (message.getChosenCharacterId() != null && message.getChosenGadget() == null) {
                    boolean valid = false;
                    Character chosen = null;

                    // check if character is in the offered list
                    for (Character character : game.getPlayerTwoCharOffer()) {
                        if (character.getCharacterId().equals(message.getChosenCharacterId())) {
                            valid = true;
                            chosen = character;
                        }
                    }

                    // valid character choice
                    if (valid) {
                        game.getPlayerTwoCharacters().add(chosen);
                        game.getNpcs().remove(chosen);
                        game.getItemChoicePhase().sendOffer();
                    }
                    // invalid character choice
                    else {
                        sendStrike(2, "Client has not sent any ItemChoice or invalid data", "You have sent no Item choices or invalid Data!");
                    }
                }
                // Gadget choice
                else {
                    boolean valid = false;
                    Gadget chosen = null;

                    // check if Gadget is in the offered list
                    for (Gadget gadget : game.getPlayerTwoGadgetOffer()) {
                        if (gadget.getGadget().equals(message.getChosenGadget())) {
                            valid = true;
                            chosen = gadget;
                        }
                    }

                    // valid gadget choice
                    if (valid) {
                        game.getPlayerTwoGadgetChoices().add(chosen);
                        game.getAllGadgets().remove(chosen);
                        game.getItemChoicePhase().sendOffer();
                    }
                    // invalid gadget choice
                    else {
                        sendStrike(2, "Client has not sent any ItemChoice or invalid data", "You have sent no Item choices or invalid Data!");
                    }
                }
            }
        }
    }

    /**
     * send a strike to the specified player and disqualifies them if they reached the maximum strike count
     *
     * @param player
     * @param debugMessage
     * @param reason
     */
    private void sendStrike(int player, String debugMessage, String reason) {
        if (game.getPlayers().get(player - 1).getStrikeCount() < game.getSettings().getStrikeMaximum() - 1) {
            game.getPlayers().get(player - 1).setStrikeCount(game.getPlayers().get(player - 1).getStrikeCount() + 1);
            game.emitter.sendStrikeMessage(game.getPlayers().get(player - 1).getConnection(), new Strike(game.getPlayers().get(player - 1).getPlayerId(), MessageTypeEnum.STRIKE, Date.from(Instant.now()), debugMessage, game.getPlayers().get(player - 1).getStrikeCount(), game.getSettings().getStrikeMaximum(), reason));
        } else {
            game.getGameHandler().disqualify(player);
        }
    }


    /**
     * @author Luca Uihlein
     * send the requested information to the client
     */
    private void receiveRequestMetaInformationMessage(RequestMetaInformationMessage message) {
        Map<String, Object> information = new HashMap<>();

        List<UUID> viewers = new LinkedList<>();
        game.getViewers().forEach(id -> viewers.add(id.getPlayerId()));
        boolean checkViewers = viewers.contains(message.clientId);

        for (int i = 0; i < message.getKeys().length; i++) {
            switch (message.getKeys()[i]) {
                case "Spectator.Count":
                    information.put(message.getKeys()[i], game.getViewers().size());
                    break;
                case "Spectator.Members":
                    List<String> names = new LinkedList<>();
                    game.getViewers().forEach(nam -> names.add(nam.getUsername()));
                    information.put(message.getKeys()[i], names.toArray());
                    break;
                case "Configuration.Scenario":
                    information.put(message.getKeys()[i], game.getLevel());
                    break;
                case "Configuration.Matchconfig":
                    information.put(message.getKeys()[i], game.getSettings());
                    break;
                case "Configuration.CharacterInformation":
                    information.put(message.getKeys()[i], game.getCharacter_settings());
                    break;
                case "Faction.Player1":
                    if (game.getPlayers().get(0).getPlayerId().equals(message.clientId) || checkViewers) {
                        List<UUID> p1 = new LinkedList<>();
                        game.getPlayerOneCharacters().forEach(id -> p1.add(id.getCharacterId()));
                        information.put(message.getKeys()[i], p1.toArray());
                    }
                    information.put(message.getKeys()[i], null);
                    break;
                case "Faction.Player2":
                    if (game.getPlayers().get(1).getPlayerId().equals(message.clientId) || checkViewers) {
                        List<UUID> p2 = new LinkedList<>();
                        game.getPlayerTwoCharacters().forEach(id -> p2.add(id.getCharacterId()));
                        information.put(message.getKeys()[i], p2.toArray());
                    }
                    information.put(message.getKeys()[i], null);
                    break;
                case "Faction.Neutral":
                    if (checkViewers) {
                        List<UUID> neutrals = new LinkedList<>();
                        game.getNpcs().forEach(id -> neutrals.add(id.getCharacterId()));
                        information.put(message.getKeys()[i], neutrals.toArray());
                    }
                    information.put(message.getKeys()[i], null);
                    break;
                case "Gadgets.Player1":
                    if (game.getPlayers().get(0).getPlayerId().equals(message.clientId) || checkViewers) {
                        Set<Gadget> gadgets = new HashSet<>();
                        game.getPlayerOneCharacters().forEach(g1 -> gadgets.addAll(g1.getGadgets()));

                        List<GadgetEnum> gadgetIds = new LinkedList<>();
                        gadgets.forEach(id -> gadgetIds.add(id.getGadget()));

                        information.put(message.getKeys()[i], gadgetIds.toArray());
                    }
                    information.put(message.getKeys()[i], null);
                    break;
                case "Gadgets.Player2":
                    if (game.getPlayers().get(1).getPlayerId().equals(message.clientId) || checkViewers) {
                        Set<Gadget> gadgets = new HashSet<>();
                        game.getPlayerTwoCharacters().forEach(g1 -> gadgets.addAll(g1.getGadgets()));

                        List<GadgetEnum> gadgetIds = new LinkedList<>();
                        gadgets.forEach(id -> gadgetIds.add(id.getGadget()));

                        information.put(message.getKeys()[i], gadgetIds.toArray());
                    }
                    information.put(message.getKeys()[i], null);
                    break;
            }
        }


        WebSocket socket;
        MetaInformationMessage metaInformationMessage;

        if (message.clientId.equals(game.getPlayers().get(0))) {
            metaInformationMessage = new MetaInformationMessage(message.clientId, MessageTypeEnum.REQUEST_META_INFORMATION, Date.from(Instant.now()), "sending  to player1");
            metaInformationMessage.setInformation(information);

            socket = game.getPlayers().get(0).getConnection();
            game.emitter.sendMetaInformationMessage(socket, metaInformationMessage);
        } else if (message.clientId.equals(game.getPlayers().get(1))) {
            metaInformationMessage = new MetaInformationMessage(message.clientId, MessageTypeEnum.REQUEST_META_INFORMATION, Date.from(Instant.now()), "sending  to player2");
            metaInformationMessage.setInformation(information);

            socket = game.getPlayers().get(1).getConnection();
            game.emitter.sendMetaInformationMessage(socket, metaInformationMessage);
        } else {
            int viewerCheck = game.getViewers().indexOf(message.clientId);
            if (viewerCheck != -1) {
                metaInformationMessage = new MetaInformationMessage(message.clientId, MessageTypeEnum.REQUEST_META_INFORMATION, Date.from(Instant.now()), "sending  to a viewer");
                metaInformationMessage.setInformation(information);

                socket = game.getViewers().get(viewerCheck).getConnection();
                game.emitter.sendMetaInformationMessage(socket, metaInformationMessage);
            }
        }
    }
}
