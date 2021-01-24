package com.sopra.ntts.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sopra.ntts.model.NetworkStandard.Characters.CharacterInformation;
import com.sopra.ntts.model.NetworkStandard.DataTypes.MatchConfig.Matchconfig;
import com.sopra.ntts.model.NetworkStandard.DataTypes.Operations.*;
import com.sopra.ntts.model.NetworkStandard.DataTypes.PropertyEnum;
import com.sopra.ntts.model.NetworkStandard.DataTypes.Szenario.FieldStateEnum;
import com.sopra.ntts.model.NetworkStandard.DataTypes.Szenario.Scenario;
import com.sopra.ntts.model.NetworkStandard.MessageContainer.MessageContainer;
import com.sopra.ntts.model.NetworkStandard.MessageTypeEnum.MessageTypeEnum;
import com.sopra.ntts.model.NetworkStandard.Messages.*;
import com.sopra.ntts.model.RuntimeTypeAdapterFactory;
import com.sopra.ntts.view.DraftingPhaseScreen;
import com.sopra.ntts.view.GameScreen;
import com.sopra.ntts.view.SelectionPhaseScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles the message received by the server.
 *
 * @author Sedat Qaja
 */
public class MessageReceiver {

  final Logger logger = LoggerFactory.getLogger(MessageReceiver.class);
  final ExecutorService executorService = Executors.newFixedThreadPool(1);

  RuntimeTypeAdapterFactory<BaseOperation> baseOperationAdapterFactory = RuntimeTypeAdapterFactory.of(BaseOperation.class, "type")
    .registerSubtype(Operation.class)
    .registerSubtype(Movement.class, "MOVEMENT")
    .registerSubtype(GadgetAction.class, "GADGET_ACTION")
    .registerSubtype(GambleAction.class, "GAMBLE_ACTION")
    .registerSubtype(PropertyAction.class, "PROPERTY_ACTION")
    .registerSubtype(Retire.class, "RETIRE")
    .registerSubtype(Spy.class, "SPY_ACTION")
    .registerSubtype(JanitorAction.class, "JANITOR_ACTION");

  Gson gson;

  private NoTimeToSpy parent;
  private ConnectionHandler connectionHandler;
  private GameHandler gameHandler;
  private DraftingHandler draftingHandler;
  private SelectionHandler selectionHandler;

  private GameScreen gameScreen;
  private DraftingPhaseScreen draftingPhaseScreen;
  private SelectionPhaseScreen selectionPhaseScreen;

  /**
   * Setter for the parent.
   */
  public void setParent(NoTimeToSpy parent) {
    this.parent = parent;
  }

  /**
   * Setter for the connectionHandler.
   *
   * @param connectionHandler The set handler.
   */
  public void setConnectionHandler(ConnectionHandler connectionHandler) {
    this.connectionHandler = connectionHandler;
  }

  /**
   * Setter for the gameHandler.
   *
   * @param gameHandler The set handler.
   */
  public void setGameHandler(GameHandler gameHandler) {
    this.gameHandler = gameHandler;
  }

  /**
   * Setter for the draftingHandler.
   *
   * @param draftingHandler The set handler.
   */
  public void setDraftingHandler(DraftingHandler draftingHandler) {
    this.draftingHandler = draftingHandler;
  }

  /**
   * Setter for the selectionHandler.
   *
   * @param selectionHandler The set handler.
   */
  public void setSelectionHandler(SelectionHandler selectionHandler) {
    this.selectionHandler = selectionHandler;
  }

  /**
   * Setter for the gameScreen.
   *
   * @param gameScreen The set screen.
   */
  public void setGameScreen(GameScreen gameScreen) {
    this.gameScreen = gameScreen;
  }

  /**
   * Setter for the draftingPhaseScreen.
   *
   * @param draftingPhaseScreen The set screen.
   */
  public void setDraftingPhaseScreen(DraftingPhaseScreen draftingPhaseScreen) {
    this.draftingPhaseScreen = draftingPhaseScreen;
  }

  /**
   * Setter for the selectionPhaseScreen.
   *
   * @param selectionPhaseScreen The set screen.
   */
  public void setSelectionPhaseScreen(SelectionPhaseScreen selectionPhaseScreen) {
    this.selectionPhaseScreen = selectionPhaseScreen;
  }

  /**
   * Default constructor.
   */
  public MessageReceiver() {
    connectionHandler = null;
    gameHandler = null;
    draftingHandler = null;
    selectionHandler = null;

    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapterFactory(baseOperationAdapterFactory);
    this.gson = gsonBuilder.create();
  }

  /**
   * Splits message into different types and directs them to specific auxiliary method.
   *
   * @param json Json message from the server.
   */
  public void handleMessage(final String json) {

    executorService.execute(new Runnable() {
      @Override
      public void run() {

        logger.info("Received message: " + json);

        try {

          MessageContainer messageContainer = gson.fromJson(json, MessageContainer.class);
          MessageTypeEnum messageType = messageContainer.type;

          if (messageType.equals(MessageTypeEnum.HELLO_REPLY)) {
            receiveHelloReplyMessage(gson.fromJson(json, HelloReplyMessage.class));
          } else if (messageType.equals(MessageTypeEnum.ERROR)) {
            receiveErrorMessage(gson.fromJson(json, ErrorMessage.class));
          } else if (messageType.equals(MessageTypeEnum.GAME_STARTED)) {
            receiveGameStartedMessage(gson.fromJson(json, GameStartedMessage.class));
          } else if (messageType.equals(MessageTypeEnum.REQUEST_ITEM_CHOICE)) {
            receiveRequestItemChoiceMessage(gson.fromJson(json, RequestItemChoiceMessage.class));
          } else if (messageType.equals(MessageTypeEnum.REQUEST_EQUIPMENT_CHOICE)) {
            receiveRequestEquipmentChoiceMessage(gson.fromJson(json, RequestEquipmentChoiceMessage.class));
          } else if (messageType.equals(MessageTypeEnum.GAME_STATUS)) {
            receiveGameStatusMessage(gson.fromJson(json, GameStatusMessage.class));
          } else if (messageType.equals(MessageTypeEnum.REQUEST_GAME_OPERATION)) {
            receiveRequestGameOperationMessage(gson.fromJson(json, RequestGameOperationMessage.class));
          } else if (messageType.equals(MessageTypeEnum.STATISTICS)) {
            receiveStatisticsMessage(gson.fromJson(json, StatisticsMessage.class));
          } else if (messageType.equals(MessageTypeEnum.GAME_LEFT)) {
            receiveGameLeftMessage(gson.fromJson(json, GameLeftMessage.class));
          } else if (messageType.equals(MessageTypeEnum.GAME_PAUSE)) {
            receiveGamePauseMessage(gson.fromJson(json, GamePauseMessage.class));
          } else if (messageType.equals(MessageTypeEnum.META_INFORMATION)) {
            receiveMetaInformationMessage(gson.fromJson(json, MetaInformationMessage.class));
          } else if (messageType.equals(MessageTypeEnum.STRIKE)) {
            receiveStrikeMessage(gson.fromJson(json, Strike.class));
          } else if (messageType.equals(MessageTypeEnum.REPLAY)) {
            receiveReplayMessage(gson.fromJson(json, ReplayMessage.class));
          }
        } catch (JsonSyntaxException jse) {
          logger.error("JsonSyntaxException: " + jse.getMessage());
        }
      }
    });
  }

  /**
   * OPTIONAL
   * Auxiliary Method to further handle the incoming message.
   *
   * @param fromJson Message with as defined in the network standard.
   */
  private void receiveReplayMessage(ReplayMessage fromJson) {

    // OPTIONAL replay the whole game with given information
  }

  /**
   * Auxiliary Method to further handle the incoming message.
   *
   * @param fromJson Message with as defined in the network standard.
   */
  private void receiveStrikeMessage(Strike fromJson) {
    System.out.println("MESSAGE RECEIVED: "+ fromJson);

    gameHandler.strike(fromJson.getStrikeNr(), fromJson.getStrikeMax(), fromJson.getReason());
  }

  /**
   * Auxiliary Method to further handle the incoming message.
   *
   * @param fromJson Message with as defined in the network standard.
   */
  private void receiveMetaInformationMessage(MetaInformationMessage fromJson) {
    System.out.println("MESSAGE RECEIVED: "+ fromJson);

    writeScenario((Scenario) fromJson.getInformation().get("Configuration.Scenario"), gameHandler.sessionId.toString());
    writeMatchConfig((Matchconfig) fromJson.getInformation().get("Configuration.Matchconfig"), gameHandler.sessionId.toString());
    writeCharacterInformation((CharacterInformation[]) fromJson.getInformation().get("Configuration.CharacterDescriptions"), gameHandler.sessionId.toString());

  }

  /**
   * Auxiliary Method to further handle the incoming message.
   *
   * @param fromJson Message with as defined in the network standard.
   */
  private void receiveGamePauseMessage(GamePauseMessage fromJson) {
    System.out.println("MESSAGE RECEIVED: "+ fromJson);

    gameHandler.gamePaused(fromJson.getGamePaused(), fromJson.getServerEnforced());

    if (fromJson.getGamePaused() && fromJson.getServerEnforced()) {
      parent.messageEmitter.sendReconnectMessage(gameHandler.sessionId);
    }
  }

  /**
   * Auxiliary Method to further handle the incoming message.
   *
   * @param fromJson Message with as defined in the network standard.
   */
  private void receiveGameLeftMessage(GameLeftMessage fromJson) {
    System.out.println("MESSAGE RECEIVED: "+ fromJson);

    if (fromJson.clientId.equals(gameHandler.playerOneId) || fromJson.clientId.equals(gameHandler.playerTwoId)) {
      gameHandler.opponentLeftGame();
    }
    else if (fromJson.clientId.equals(gameHandler.userId)) {
      gameHandler.setDefaultValues();

      // viewer action
      if (!(fromJson.clientId.equals(gameHandler.playerOneId) || fromJson.clientId.equals(gameHandler.playerTwoId))) {
        gameHandler.isGameOver = true;
      }

      parent.showLoginScreen();
    }
  }

  /**
   * Auxiliary Method to further handle the incoming message.
   *
   * @param fromJson Message with as defined in the network standard.
   */
  private void receiveStatisticsMessage(StatisticsMessage fromJson) {
    System.out.println("MESSAGE RECEIVED: "+ fromJson);

    gameHandler.gameOver(fromJson.getWinner(), fromJson.getReason());
  }

  /**
   * Auxiliary Method to further handle the incoming message.
   *
   * @param fromJson Message with as defined in the network standard.
   */
  private void receiveRequestGameOperationMessage(RequestGameOperationMessage fromJson) {
    System.out.println("MESSAGE RECEIVED: "+ fromJson);

    gameHandler.setIsClientActivePlayer(fromJson.characterId);

    gameScreen.initializeGadgetsForGameMove();

    gameScreen.enableBottomLevelHUD();

    gameScreen.startTimer();
  }

  /**
   * Auxiliary Method to further handle the incoming message.
   *
   * @param fromJson Message with as defined in the network standard.
   */
  private void receiveGameStatusMessage(GameStatusMessage fromJson) {
    System.out.println("MESSAGE RECEIVED: "+ fromJson);

    // parses Operations if needed
    for (BaseOperation baseoperation : fromJson.getOperations()) {

      if (baseoperation instanceof PropertyAction) {
        PropertyAction propertyAction = (PropertyAction) baseoperation;
        // observation
        if (propertyAction.getSuccessful() && !fromJson.getState().getMap().getField(propertyAction.getTarget().x, propertyAction.getTarget().y).getState().equals(FieldStateEnum.ROULETTE_TABLE)) {

          // adds opponent to the List
          gameHandler.opponentCharacters.add(gameHandler.getCharacterByPoint(fromJson.getState().getCharacters() , propertyAction.getTarget()).getCharacterId());
        }
      }
      else if (baseoperation instanceof GadgetAction) {
        GadgetAction gadgetAction = (GadgetAction) baseoperation;

        // nugget Information
      }
    }

    if (gameHandler.gameStatusMessages == 0) {
      gameHandler.gameStatusUpdate(fromJson.getActiveCharacterId(), fromJson.getOperations(), fromJson.getState(), fromJson.getGameOver());
      parent.showGameScreen();
    }
    else {
      gameHandler.gameStatusUpdate(fromJson.getActiveCharacterId(), fromJson.getOperations(), fromJson.getState(), fromJson.getGameOver());
      if (fromJson.getState().getMySafeCombinations() != null) {
        gameHandler.safeCombinations.addAll(fromJson.getState().getMySafeCombinations());
      }
      gameScreen.gameStateInitialized = false;
      gameHandler.stateUpdated = true;
    }
    gameHandler.gameStatusMessages++;
  }

  /**
   * Auxiliary Method to further handle the incoming message.
   *
   * @param fromJson Message with as defined in the network standard.
   */
  private void receiveRequestEquipmentChoiceMessage(RequestEquipmentChoiceMessage fromJson) {
    System.out.println("MESSAGE RECEIVED: "+ fromJson);

    // is needed to set the characters first and create lists in maps, for the display
    selectionHandler.setList(fromJson.getChosenCharacterIds(), fromJson.getChosenGadgets());
    selectionHandler.fillHashMap();

    gameHandler.chosenCharacters.addAll(fromJson.getChosenCharacterIds());

    parent.showSelectionPhaseScreen();
  }

  /**
   * Auxiliary Method to further handle the incoming message.
   *
   * @param fromJson Message with as defined in the network standard.
   */
  private void receiveRequestItemChoiceMessage(RequestItemChoiceMessage fromJson) {
    System.out.println("MESSAGE RECEIVED: "+ fromJson);

    draftingPhaseScreen.enableAllButtons();
    draftingHandler.setItemChoiceLists(fromJson.getOfferedCharacterIds(), fromJson.getOfferedGadgets());
    draftingPhaseScreen.fillingObjectArray();
    draftingPhaseScreen.enableAllButtons();
  }

  /**
   * Auxiliary Method to further handle the incoming message.
   *
   * @param fromJson Message with as defined in the network standard.
   */
  private void receiveGameStartedMessage(GameStartedMessage fromJson) {
    System.out.println("MESSAGE RECEIVED: "+ fromJson);

    gameHandler.gameInitialized(fromJson.getPlayerOneId(), fromJson.getPlayerTwoId(), fromJson.getPlayerOneName(), fromJson.getPlayerTwoName(), fromJson.getSessionId());

    connectionHandler.setUserId(fromJson.clientId);

    if (fromJson.getPlayerOneId().equals(fromJson.clientId) || fromJson.getPlayerTwoId().equals(fromJson.clientId)) {

      parent.showDraftingPhaseScreen();
    }
  }

  /**
   * Auxiliary Method to further handle the incoming message.
   *
   * @param fromJson Message with as defined in the network standard.
   */
  private void receiveErrorMessage(ErrorMessage fromJson) {
    System.out.println("MESSAGE RECEIVED: "+ fromJson);

    if (fromJson.getReason().equals(ErrorTypeEnum.NAME_NOT_AVAILABLE)) {
      logger.info("Name not available.");
      gameHandler.error(fromJson.getReason());
      connectionHandler.closeConnection();
    } else if (fromJson.getReason().equals(ErrorTypeEnum.ALREADY_SERVING)) {
      logger.info("Already serving.");
      gameHandler.error(fromJson.getReason());
      connectionHandler.closeConnection();
    } else if (fromJson.getReason().equals(ErrorTypeEnum.SESSION_DOES_NOT_EXIST)) {
      logger.info("Session does not exist.");
      gameHandler.error(fromJson.getReason());
      connectionHandler.closeConnection();
    } else if (fromJson.getReason().equals(ErrorTypeEnum.ILLEGAL_MESSAGE)) {
      logger.info("Illegal message.");
      gameHandler.error(fromJson.getReason());
    } else if (fromJson.getReason().equals(ErrorTypeEnum.TOO_MANY_STRIKES)) {
      logger.info("Too many strikes.");
      gameHandler.error(fromJson.getReason());
      connectionHandler.closeConnection();
    } else if (fromJson.getReason().equals(ErrorTypeEnum.GENERAL)) {
      logger.info("General.");
      gameHandler.error(fromJson.getReason());
      connectionHandler.closeConnection();
    }
  }

  /**
   * Auxiliary Method to further handle the incoming message.
   *
   * @param fromJson Message with as defined in the network standard.
   */
  private void receiveHelloReplyMessage(HelloReplyMessage fromJson) {
    System.out.println("MESSAGE RECEIVED: "+ fromJson);

    gameHandler.sessionStarted(fromJson.getSessionId(), fromJson.clientId, fromJson.creationDate, fromJson.getLevel(), fromJson.getSettings(), fromJson.getCharacter_settings());
    connectionHandler.setUserId(fromJson.clientId);
  }


  /**
   * Auxiliary method to write json files in directory.
   *
   * @param level     The level to write down.
   * @param sessionId Different sessionId for every game.
   */
  private void writeScenario(Scenario level, String sessionId) {
    try {
      File file = new File("savedMetaInformation/" + sessionId + "scenario");
      gson.toJson(level, new FileWriter(file.getAbsolutePath()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Auxiliary method to write json files in directory.
   *
   * @param settings  The settings to write down.
   * @param sessionId Different sessionId for every game.
   */
  private void writeMatchConfig(Matchconfig settings, String sessionId) {
    try {
      File file = new File("savedMetaInformation/" + sessionId + "scenario");
      gson.toJson(settings, new FileWriter(file.getAbsolutePath()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Auxiliary method to write json files in directory.
   *
   * @param characterSettings The characters to write down.
   * @param sessionId         Different sessionId for every game.
   */
  private void writeCharacterInformation(CharacterInformation[] characterSettings, String sessionId) {
    try {
      File file = new File("savedMetaInformation/" + sessionId + "scenario");
      gson.toJson(characterSettings, new FileWriter(file.getAbsolutePath()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
