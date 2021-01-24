/**
 * This class implements a basic WebsocketServer.
 *
 * @author Christian Wendlinger
 */

import NetworkStandard.Characters.CharacterInformation;
import NetworkStandard.DataTypes.MatchConfig.Matchconfig;
import NetworkStandard.DataTypes.Operations.ErrorTypeEnum;
import NetworkStandard.DataTypes.RoleEnum;
import NetworkStandard.DataTypes.Szenario.Scenario;
import NetworkStandard.MessageContainer.MessageContainer;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;
import NetworkStandard.Messages.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import util.Game;
import util.User;
import util.communication.MessageEmitter;
import util.logic.ingame.OperationTimer;
import util.logic.ingame.ReconnectTimer;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class NTTSServer extends WebSocketServer {
    private ConcurrentHashMap<WebSocket, Game> connectionToGameMap;
    private Queue<User> playersInQueue;
    private Queue<User> viewersInQueue;
    private MessageEmitter emitter;

    // Gson instance to parse JSON
    private final static Gson gson = new Gson();

    // read through shell
    private static Scenario level;
    private static Matchconfig settings;
    private static CharacterInformation[] character_settings;

    // server start information
    private static int port = 7007;

    /**
     * constructor
     */
    public NTTSServer(InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress);
        connectionToGameMap = new ConcurrentHashMap<>();
        playersInQueue = new ConcurrentLinkedQueue<>();
        viewersInQueue = new ConcurrentLinkedQueue<>();
        emitter = new MessageEmitter(null, null, null);

        /*
        // print information about current server population (every 10 sec)
        Thread t = new Thread(() -> {
            while (true) {
                System.out.println("Players in game: " + connectionToGameMap.size() + " - " + connectionToGameMap.size() / 2 + " active Games");
                System.out.println("Players in Queue: " + playersInQueue.size());
                System.out.println("Viewers in Queue: " + viewersInQueue.size());
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
         */

        // matches player + viewers every 10 seconds
        Thread matchmaker = new Thread(() -> {
            try {
                while (true) {
                    // clean games, that are over
                    for (WebSocket conn : connectionToGameMap.keySet()) {
                        if (!connectionToGameMap.get(conn).isActive()) {
                            connectionToGameMap.remove(conn);
                        }
                    }

                    // generate match, if possible
                    if (playersInQueue.size() >= 2) {

                        // create new game and add two players
                        Game game = new Game(UUID.randomUUID(), new LinkedList<>(), new LinkedList<>(), level, settings, character_settings);
                        game.addPlayer(playersInQueue.poll());
                        game.addPlayer(playersInQueue.poll());

                        while (!viewersInQueue.isEmpty()) {
                            game.addViewer(viewersInQueue.poll());
                        }

                        // add players and viewers to the game
                        for (User player : game.getPlayers()) {
                            connectionToGameMap.put(player.getConnection(), game);
                        }

                        for (User viewer : game.getViewers()) {
                            connectionToGameMap.put(viewer.getConnection(), game);
                        }

                        // send GameStartedMessage to Viewers and Players
                        game.emitter.sendGameStartedMessage(new GameStartedMessage(null, MessageTypeEnum.GAME_STARTED, Date.from(Instant.now()), "Game Started", game.getPlayers().get(0).getPlayerId(), game.getPlayers().get(1).getPlayerId(), game.getPlayers().get(0).getUsername(), game.getPlayers().get(1).getUsername(), game.getSessionId()));
                        game.startGame();
                    }

                    // sleep
                    Thread.sleep(10000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        matchmaker.start();
    }

    /**
     * connections is opened
     *
     * @param conn
     * @param handshake
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Client " + conn.getRemoteSocketAddress() + " connected!");
        conn.send("Welcome to the NTTSServer!");
    }

    /**
     * removes client from queues if they are not in a game and triggers the reconnect timer if they are in a game
     *
     * @param conn
     * @param code
     * @param reason
     * @param remote
     */
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        // player not in game - remove from queue
        if (connectionToGameMap.get(conn) == null) {
            //remove Player from Queues if connection gets closed
            playersInQueue.removeIf(player -> player.getConnection() == conn);
            viewersInQueue.removeIf(viewer -> viewer.getConnection() == conn);
        }
        // player in game = disconnect
        else {
            Game game = connectionToGameMap.get(conn);

            // player disconnected (viewers do not matter)
            if (game.getPlayers().get(0).getConnection().equals(conn) || game.getPlayers().get(1).getConnection().equals(conn)) {

                // player 1 disconnected
                if (game.getPlayers().get(0).getConnection().equals(conn)) {
                    game.getPlayers().get(0).setDisconnected(true);

                    // if the other player is disconnected as well - close game
                    if (game.getPlayers().get(1).isDisconnected()) {
                        game.setActive(false);
                    }
                }
                // player 2 disconnected
                else {
                    game.getPlayers().get(1).setDisconnected(true);

                    // if the other player is disconnected as well - close game
                    if (game.getPlayers().get(0).isDisconnected()) {
                        game.setActive(false);
                    }
                }

                // unpause game if paused
                if (game.isPaused()) {
                    // try - catch to avoid errors because of closed websocket or because the pause timer may be null
                    try {
                        game.receiver.resume(true);
                    } catch (Exception ignored) {
                    }
                }

                // stop operation timer
                game.getGameHandler().setSecondsPassed(game.getGameHandler().getSecondsPassed() + game.getGameHandler().getTimer().stop());

                // one player disconnected - so the game is still active
                if (game.isActive()) {
                    // start reconnect timer
                    game.getGameHandler().setReconnectTimer(new ReconnectTimer(game, game.getPlayers().get(0).isDisconnected() ? 1 : 2));
                    game.getGameHandler().getReconnectTimer().schedule(game.getSettings().getReconnectLimit());
                }
                // both players disconnected
                else {
                    // stop time
                    if (game.getGameHandler().getReconnectTimer() != null) {
                        game.getGameHandler().getReconnectTimer().stop();
                    }
                }
            }

            // remove key
            connectionToGameMap.remove(conn);
        }
    }

    /**
     * handles incoming messages
     *
     * @param conn
     * @param message
     */
    @Override
    public void onMessage(WebSocket conn, String message) {
        //user not in game
        if (connectionToGameMap.get(conn) == null) {
            // check for helloMessage
            try {
                MessageContainer container = gson.fromJson(message, MessageContainer.class);

                if (container.type == MessageTypeEnum.HELLO) {
                    HelloMessage helloMessage = gson.fromJson(message, HelloMessage.class);

                    // check players and viewers for duplicate names
                    boolean playerWithSameName = false;

                    for (User p : playersInQueue) {
                        if (p.getUsername().equals(helloMessage.name)) {
                            playerWithSameName = true;
                            break;
                        }
                    }

                    for (User p : viewersInQueue) {
                        if (p.getUsername().equals(helloMessage.name)) {
                            playerWithSameName = true;
                            break;
                        }
                    }

                    // duplicate - error
                    if (playerWithSameName) {
                        emitter.sendErrorMessage(conn, new ErrorMessage(null, MessageTypeEnum.ERROR, Date.from(Instant.now()), "Name already in use", ErrorTypeEnum.NAME_NOT_AVAILABLE));
                    }

                    // name is free
                    else {
                        User user = new User(UUID.randomUUID(), conn, helloMessage.name, helloMessage.role);

                        // player looking for Game -> add Player to Queue
                        if (helloMessage.role == RoleEnum.PLAYER) {
                            playersInQueue.add(user);
                        }

                        // viewer looking for Game -> add Viewer to Queue
                        else if (helloMessage.role == RoleEnum.SPECTATOR) {
                            viewersInQueue.add(user);
                        }

                        // send HelloReplyMessage
                        emitter.sendHelloReplyMessage(conn, new HelloReplyMessage(user.getPlayerId(), MessageTypeEnum.HELLO_REPLY, Date.from(Instant.now()), "added Player to Queue", null, level, settings, character_settings));
                    }
                }

                // reconnecting to a game
                else if (container.type == MessageTypeEnum.RECONNECT) {
                    ReconnectMessage reconnectMessage = gson.fromJson(message, ReconnectMessage.class);

                    Game game = null;

                    for (Game g : connectionToGameMap.values()) {
                        // found the correct game
                        if (g.getSessionId().equals(reconnectMessage.getSessionId())) {
                            game = g;
                            break;
                        }
                    }

                    // game was found
                    if (game != null) {

                        // game is still active
                        if (game.isActive()) {
                            boolean newOffer = false;

                            // player 1 wants to reconnect
                            if (game.getPlayers().get(0).isDisconnected() && game.getPlayers().get(0).getPlayerId().equals(reconnectMessage.clientId)) {
                                // map new connection to the game
                                connectionToGameMap.put(conn, game);

                                //update values
                                game.getPlayers().get(0).setDisconnected(false);
                                game.getPlayers().get(0).setConnection(conn);

                                // player 1 turn
                                if (game.getPlayerOneCharacters().contains(game.getGameHandler().getCurrentCharacter())) {
                                    newOffer = true;
                                }
                            }
                            // player 2 wants to reconnect
                            else if (game.getPlayers().get(1).isDisconnected() && game.getPlayers().get(1).getPlayerId().equals(reconnectMessage.clientId)) {
                                // map new connection to the game
                                connectionToGameMap.put(conn, game);

                                //update values
                                game.getPlayers().get(1).setDisconnected(false);
                                game.getPlayers().get(1).setConnection(conn);

                                // player 2 turn
                                if (game.getPlayerTwoCharacters().contains(game.getGameHandler().getCurrentCharacter())) {
                                    newOffer = true;
                                }
                            }
                            // wrong userId
                            else {
                                sendReconnectError(conn, reconnectMessage);
                            }

                            // if both players are connected now
                            if (!game.getPlayers().get(0).isDisconnected() && !game.getPlayers().get(1).isDisconnected()) {
                                // stop reconnect timer
                                game.getGameHandler().getReconnectTimer().stop();

                                // send game status message (for the reconnected player)
                                game.emitter.sendGameStatusMessage(new GameStatusMessage(null, MessageTypeEnum.GAME_STATUS, Date.from(Instant.now()), "game status after successful reconnect", game.getGameHandler().getCurrentCharacter().getCharacterId(), game.getCurrentOperations(), game.getState(), false));

                                // save seconds because they get reset if when a new operation is requested
                                int secondsPassed = game.receiver.getOperationSecondsPassed();

                                // send new request if necessary
                                if (newOffer) {
                                    game.getGameHandler().requestOperation(game.getGameHandler().getCurrentCharacter());
                                }

                                // start new operation timer with remaining time
                                if (game.getGameHandler().getTimer() != null) {
                                    game.getGameHandler().getTimer().stop();
                                }
                                game.getGameHandler().setTimer(new OperationTimer(game));
                                game.getGameHandler().getTimer().schedule(game.getSettings().getTurnPhaseLimit() - secondsPassed);
                            }
                        }
                        // game is already over
                        else {
                            sendReconnectError(conn, reconnectMessage);
                        }
                    }
                    // game was not found
                    else {
                        sendReconnectError(conn, reconnectMessage);
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();

                // Illegal Syntax received
                emitter.sendErrorMessage(conn, new ErrorMessage(null, MessageTypeEnum.ERROR, java.sql.Date.from(Instant.now()), "Message could not be parsed", ErrorTypeEnum.ILLEGAL_MESSAGE));
            }
        }
        // game related message - let corresponding handler take care of the message
        else {
            try {
                if (connectionToGameMap.get(conn) != null) {
                    connectionToGameMap.get(conn).receiver.handleMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * handles errors
     *
     * @param conn
     * @param ex
     */
    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Error on session: " + conn.getRemoteSocketAddress());
        System.err.println(ex);

        //remove Player from Queues if an error occurs
        playersInQueue.removeIf(player -> player.getConnection() == conn);
        viewersInQueue.removeIf(viewer -> viewer.getConnection() == conn);
    }

    /**
     * handles the server start
     */
    @Override
    public void onStart() {
        System.out.println("Server is running on IP: " + getAddress() + "!");
    }

    /**
     * assign UUIDs to all characters
     */
    private void assignCharacterUUIDs() {
        for (CharacterInformation character_setting : character_settings) {
            character_setting.setCharacterId(UUID.randomUUID());
        }
    }

    /**
     * Send Error message if reconnect fails
     *
     * @param conn
     * @param message
     */
    private void sendReconnectError(WebSocket conn, ReconnectMessage message) {
        ErrorMessage error = new ErrorMessage(message.clientId, MessageTypeEnum.ERROR, Date.from(Instant.now()), "Game to recconect not found!", ErrorTypeEnum.SESSION_DOES_NOT_EXIST);
        conn.send(gson.toJson(error));
    }

    /**
     * read the complete content of a file;
     *
     * @param path -path to the file
     * @return - String with content of the file
     * @throws FileNotFoundException
     */
    private static String readFile(String path) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        try {
            StringBuilder str = new StringBuilder();
            String content;

            while ((content = reader.readLine()) != null) {
                str.append(content);
            }
            return str.toString();
        } catch (IOException ignored) {
        }
        return null;
    }

    /**
     * read level data
     *
     * @param path - path to .scenario file
     * @return Scenario object created from file
     */
    private static Scenario readLevel(String path) {
        try {
            String settings = readFile(path);
            return gson.fromJson(settings, Scenario.class);
        } catch (FileNotFoundException ignored) {
        }
        return null;
    }

    /**
     * read match config data
     *
     * @param path - path to .match file
     * @return Matchconfig object created from file
     */
    private static Matchconfig readSettings(String path) {
        try {
            String settings = readFile(path);
            return gson.fromJson(settings, Matchconfig.class);
        } catch (FileNotFoundException ignored) {
        }
        return null;
    }

    /**
     * read character settings data
     *
     * @param path - path to .json file
     * @return CharacterInformation Array created from file
     */
    private static CharacterInformation[] readCharacter_settings(String path) {
        try {
            String chars = readFile(path);
            return gson.fromJson(chars, CharacterInformation[].class);
        } catch (FileNotFoundException ignored) {
        }
        return null;
    }

    /**
     * main Point to start the server
     *
     * @param args
     */
    public static void main(String[] args) {
        shell(); // start command: server002 -c src\main\java\config\characters.json -s src\main\java\config\level.scenario -m src\main\java\config\matchconfig.match -p 8080
    }

    /**
     * Shell to start the server.
     */
    private static void shell() {
        Scanner scanner = new Scanner(System.in);

        HashMap<String, String> params = new HashMap<>();

        System.out.println("NTTS shell - use \"server002 --help\" or \"server002 -h\" for further information");

        start:
        while (true) {
            System.out.print("~");
            String command = scanner.nextLine();

            // reset hashMap
            params.clear();

            // parse input
            String[] parts = command.split("\\s+");

            switch (parts[0].toLowerCase()) {
                // command to start the server
                case "server002":
                    // show help
                    if (parts.length == 2 && (parts[1].toLowerCase().equals("--help") || parts[1].equals("-h"))) {
                        System.out.println("server002 starts the server with the following parameters:\n --config-charset [PATH] ||| Alias -c [PATH] ||| REQUIRED\n --config-match [PATH] ||| Alias -m [PATH] ||| REQUIRED\n --config-scenario [PATH] ||| Alias -s [PATH] ||| REQUIRED\n --port [NUM] ||| Alias -p [NUM] ||| OPTIONAL - Default: 7007");
                    }

                    // invalid syntax
                    else if (parts.length == 1) {
                        System.err.println("Invalid syntax - provide parameters");
                    }

                    // read parameters
                    else {
                        // invalid syntax (word count)
                        if (parts.length % 2 != 1) {
                            System.err.println("The amount of params does not equal the amount of values");
                            break;
                        }

                        for (int i = 1; i < parts.length - 1; i += 2) {
                            params.put(parts[i].toLowerCase(), parts[i + 1]);
                        }

                        // parse input
                        if (parseInput(params)) {
                            scanner.close();
                            String host = null;
                            try {
                                host = Inet4Address.getLocalHost().getHostAddress();
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            }

                            if (host != null) {
                                NTTSServer server = new NTTSServer(new InetSocketAddress(port));
                                server.assignCharacterUUIDs();
                                server.run();
                                break start;
                            } else System.err.println("Could not resolve IP-Address, check your router Settings.");
                        }
                    }
                    break;

                // pressing only enter
                case "":
                    break;

                //close
                case "exit":
                case "quit":
                    System.exit(0);
                    break;

                // invalid command
                default:
                    System.err.println("Unknown command " + parts[0]);
            }
        }
    }

    /**
     * check if every parameter is provided and whether the given paths are readable
     */
    private static boolean parseInput(HashMap<String, String> params) {
        // rest values
        level = null;
        settings = null;
        character_settings = null;
        port = 7007;

        /* check all constraints */

        // charset not provided
        if (params.get("-c") == null && params.get("--config-charset") == null) {
            System.err.println("param -c / --config-charset is missing");
            return false;
        }
        // charset provided
        else {
            String key = params.get("-c") != null ? "-c" : "--config-charset";
            try {
                NTTSServer.character_settings = readCharacter_settings(params.get(key));
                params.remove(key);

                // unsuccessful read
                if (character_settings == null) {
                    System.err.println("Could not read character settings - invalid path");
                    return false;
                }
            } catch (Exception e) {
                System.err.println(params.get(key) + " is an invalid path to a character settings file");
                return false;
            }
        }

        // scenario not provided
        if (params.get("-s") == null && params.get("--config-scenario") == null) {
            System.err.println("param -s / --config-scenario is missing");
            return false;
        }
        // scenario provided
        else {
            String key = params.get("-s") != null ? "-s" : "--config-scenario";
            try {
                NTTSServer.level = readLevel(params.get(key));
                params.remove(key);

                // unsuccessful read
                if (level == null) {
                    System.err.println("Could not read scenario - invalid path");
                    return false;
                }
            } catch (Exception e) {
                System.err.println(params.get(key) + " is an invalid path to a scenario file");
                return false;
            }
        }

        // match config not provided
        if (params.get("-m") == null && params.get("--config-match") == null) {
            System.err.println("param -m / --config-match is missing");
            return false;
        }
        // scenario provided
        else {
            String key = params.get("-m") != null ? "-m" : "--config-match";
            try {
                NTTSServer.settings = readSettings(params.get(key));
                params.remove(key);

                // unsuccessful read
                if (settings == null) {
                    System.err.println("Could not read match config - invalid path");
                    return false;
                }
            } catch (Exception e) {
                System.err.println(params.get(key) + " is an invalid path to a match config file");
                return false;
            }
        }

        // check port
        if (params.get("-p") != null || params.get("--port") != null) {
            String key = params.get("-p") != null ? "-p" : "--port";
            try {
                NTTSServer.port = Integer.parseInt(params.get(key));
                params.remove(key);

                // negative port provided
                if (!(NTTSServer.port > 0)) {
                    System.err.println(params.get(key) + " could not be parsed as a valid port");
                    return false;
                }
            } catch (Exception e) {
                System.err.println(params.get(key) + " could not be parsed as a valid port");
                return false;
            }
        }

        // unknown parameters
        if (params.keySet().size() != 0) {
            System.err.println("invalid parameters were provided " + params.keySet());
            return false;
        }

        // everything read successfully
        return true;
    }
}
