package com.sopra.ntts.controller;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.sopra.ntts.model.NetworkStandard.Characters.Character;
import com.sopra.ntts.model.NetworkStandard.Characters.CharacterDescription;
import com.sopra.ntts.model.NetworkStandard.Characters.CharacterInformation;
import com.sopra.ntts.model.NetworkStandard.Characters.GenderEnum;
import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;
import com.sopra.ntts.model.NetworkStandard.DataTypes.MatchConfig.Matchconfig;
import com.sopra.ntts.model.NetworkStandard.DataTypes.Operations.BaseOperation;
import com.sopra.ntts.model.NetworkStandard.DataTypes.Operations.ErrorTypeEnum;
import com.sopra.ntts.model.NetworkStandard.DataTypes.Operations.Operation;
import com.sopra.ntts.model.NetworkStandard.DataTypes.Operations.VictoryEnum;
import com.sopra.ntts.model.NetworkStandard.DataTypes.Szenario.Scenario;
import com.sopra.ntts.model.NetworkStandard.DataTypes.Szenario.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Class handles the game cycle and game screen.
 *
 * @author Sedat Qaja
 */
public class GameHandler {

  /**
   * Logger to emmit logger messages.
   */
  private final Logger logger = LoggerFactory.getLogger(GameHandler.class);

  /**
   * A messageEmitter ise needed to emit messages to the Server.
   */
  private MessageEmitter messageEmitter;

  /**
   * The clients unique identifier. Duplicate of userId in ConnectionHandler.
   */
  public UUID userId;

  /**
   * The unique identifier representing the games id.
   */
  public UUID sessionId;

  /**
   * This attribute indicates whether the game is active or not.
   * true = active
   * false = closed (for whatever reason)
   */
  public boolean isSessionActive;

  /**
   * Indicates when the game was started.
   */
  public Date creationDate;

  /**
   * The unique identifier representing the player one.
   */
  public UUID playerOneId;

  /**
   * The unique identifier representing the player two.
   */
  public UUID playerTwoId;

  /**
   * Well, if player one should have a name, here is the place to store it.
   */
  public String playerOneName;

  /**
   * This attribute stores the user name of player two.
   */
  public String playerTwoName;

  /**
   * This attribute indicates whether the client is player one or not.
   * true = client is player one
   * false = not
   */
  public boolean clientIsPlayerOne;

  /**
   * This attribute indicates whether the client is player two or not.
   * true = client is player two
   * false = not
   */
  public boolean clientIsPlayerTwo;

  /**
   * Boolean value which signals if opponent left the game.
   */
  public boolean opponentLeft;

  /**
   * UUID of the active player.
   */
  public UUID activeCharacterId;

  /**
   * List of operations which are to animate.
   */
  public List<BaseOperation> operations;

  /**
   * Representing the level in game.
   */
  public State state;

  /**
   * Value if game is over.
   * true = game is over
   * false = game is not over
   */
  public boolean isGameOver;

  /**
   * String for the winner name and reason.
   */
  public String winnerString;
  public String winnerReason;

  /**
   * Value if game is paused.
   * true = game is paused.
   * false = game is not paused.
   */
  public Boolean gamePause;
  public Boolean serverEnforcedPause;

  /**
   * Value if level is updated.
   */
  public Boolean stateUpdated;

  /**
   * Tells if the clients should change to game screen.
   */
  public Integer gameStatusMessages;

  /**
   * Scenario value for the game stage design.
   */
  public Scenario level;

  /**
   * MatchConfig value for the game settings.
   */
  public Matchconfig settings;

  /**
   * Array of CharacterInformation for the default game characters.
   */
  public CharacterInformation[] character_settings;

  /**
   * Boolean which tells if the activeCharacter is part of the clients Team.
   */
  public Boolean isClientActivePlayer;

  /**
   * CharacterMap to know which texture is for a character.
   */
  public HashMap<UUID, CharacterDescription> characterMap;

  /**
   * Map GadgetEnum to GadgetString
   */
  public HashMap<GadgetEnum, String> gadgetMap;

  /**
   * List of chosen characters UUIDs.
   */
  public List<UUID> chosenCharacters;

  /**
   * List of opponents character.
   */
  public List<UUID> opponentCharacters;

  /**
   * Bind the textures to the objects.
   */
  public HashMap<GadgetEnum, TextureRegionDrawable> gadgetEnumTextureHashMap;
  public HashMap<UUID, TextureRegionDrawable> characterToTextureMap;

  /**
   * Value if server implements the replay function.
   */
  public boolean hasReplay;

  /**
   * True if strike handling is started.
   */
  public Boolean strikeReceived;
  public Integer strikeNr;
  public Integer strikeMax;
  public String strikeReason;

  /**
   * True if strike handling is started.
   */
  public Boolean errorReceived;
  public String errorType;

  /**
   * List of opened safes for each player.
   */
  public Set<Point> playerOpenedSafes;
  public Set<Integer> safeCombinations;

  /**
   * Setter for the private attribute messageEmitter.
   *
   * @param messageEmitter The set messageEmitter to the game.
   */
  public void setMessageEmitter(MessageEmitter messageEmitter) {
    this.messageEmitter = messageEmitter;
  }

  /**
   * Default constructor of class GameHandler.
   * Constructor sets default values for all class attributes.
   */
  public GameHandler() {
    // set all values to their defaults, except messageEmitter
    setDefaultValues();
  }

  /**
   * Setter used if game is started. Information which is send by the server.
   *
   * @param userId       UserId which is identified with the game.
   * @param sessionId    GameId which is the unique sessionId.
   * @param creationDate Date of the game creation.
   */
  public void sessionStarted(UUID sessionId, UUID userId, Date creationDate, Scenario level, Matchconfig settings, CharacterInformation[] character_settings) {
    setDefaultValues();

    this.userId = userId;
    this.sessionId = sessionId;
    this.creationDate = creationDate;
    this.level = level;
    this.settings = settings;
    this.character_settings = character_settings;

    this.operations = new LinkedList<>();

    characterMap = new HashMap<>();
    characterToTextureMap = new HashMap<>();
    gadgetEnumTextureHashMap = new HashMap<>();

    for (CharacterInformation character_setting : character_settings) {
      if (character_setting.getGender().equals(GenderEnum.MALE)) {
        characterToTextureMap.put(character_setting.getCharacterId(), new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.male)));
      } else if (character_setting.getGender().equals(GenderEnum.FEMALE)) {
        characterToTextureMap.put(character_setting.getCharacterId(), new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.female)));
      }
      else if (character_setting.getGender().equals(GenderEnum.DIVERSE)) {
        characterToTextureMap.put(character_setting.getCharacterId(), new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.diverse)));
      }
      characterMap.put(character_setting.getCharacterId(), character_setting);
    }

    assignTexturesToGadget();

    gadgetMap = new HashMap<>();
    gadgetMap.put(GadgetEnum.HAIRDRYER, "Hairdryer");
    gadgetMap.put(GadgetEnum.MOLEDIE, "Moledie");
    gadgetMap.put(GadgetEnum.TECHNICOLOUR_PRISM, "Technicolour Prism");
    gadgetMap.put(GadgetEnum.BOWLER_BLADE, "Bowler Blade");
    gadgetMap.put(GadgetEnum.MAGNETIC_WATCH, "Magnetic Watch");
    gadgetMap.put(GadgetEnum.POISON_PILLS, "Poison Pills");
    gadgetMap.put(GadgetEnum.LASER_COMPACT, "Laser Compact");
    gadgetMap.put(GadgetEnum.ROCKET_PEN, "Rocket Pen");
    gadgetMap.put(GadgetEnum.GAS_GLOSS, "Gas Gloss");
    gadgetMap.put(GadgetEnum.MOTHBALL_POUCH, "Mothball Pouch");
    gadgetMap.put(GadgetEnum.FOG_TIN, "Fog Tin");
    gadgetMap.put(GadgetEnum.GRAPPLE, "Grapple");
    gadgetMap.put(GadgetEnum.WIRETAP_WITH_EARPLUGS, "Wiretap With Earplugs");
    gadgetMap.put(GadgetEnum.DIAMOND_COLLAR, "Diamond Collar");
    gadgetMap.put(GadgetEnum.JETPACK, "Jetpack");
    gadgetMap.put(GadgetEnum.CHICKEN_FEED, "Chicken Feed");
    gadgetMap.put(GadgetEnum.NUGGET, "Nugget");
    gadgetMap.put(GadgetEnum.MIRROR_OF_WILDERNESS, "Mirror Of Wilderness");
    gadgetMap.put(GadgetEnum.POCKET_LITTER, "Pocket Litter");
    gadgetMap.put(GadgetEnum.COCKTAIL, "Cocktail");
    gadgetMap.put(GadgetEnum.MASK, "Anti Seuchen Maske");
    isSessionActive = true;
  }

  /**
   * Initializer for the game.
   *
   * @param playerOneId        UUID who is playerOne.
   * @param playerTwoId        UUID who is playerTwo.
   * @param playerOneName      String how playerOne is called.
   * @param playerTwoName      String how playerTwo is called.
   */
  public void gameInitialized(UUID playerOneId, UUID playerTwoId, String playerOneName, String playerTwoName, UUID sessionId) {

    this.playerOneId = playerOneId;
    this.playerTwoId = playerTwoId;
    this.playerOneName = playerOneName;
    this.playerTwoName = playerTwoName;
    this.sessionId = sessionId;

    playerOpenedSafes = new HashSet<Point>();

    clientIsPlayerOne = userId.equals(playerOneId);
    clientIsPlayerTwo = userId.equals(playerTwoId);
  }

  /**
   * Method which is used to update the game status after every operation.
   *
   * @param activeCharacterId The active character.
   * @param operations        The done operations.
   * @param state             The state of the level.
   * @param isGameOver        Value if game is over.
   */
  public void gameStatusUpdate(UUID activeCharacterId, List<BaseOperation> operations, State state, Boolean isGameOver) {

    this.activeCharacterId = activeCharacterId;

    this.operations.addAll(operations);

    this.state = state;
  }

  /**
   * Setter for the case the opponent left the game.
   */
  public void opponentLeftGame() {
    opponentLeft = true;
  }

  /**
   * Method to handle gameEnd.
   *
   * @param winner UUID of the winner.
   * @param reason Reason of winning.
   */
  public void gameOver(UUID winner, VictoryEnum reason) {

    if (winner.equals(playerOneId)){
      this.winnerString = playerOneName;
    }
    else {
      this.winnerString = playerTwoName;
    }

    if (opponentLeft) {
      this.winnerReason = "Player left the Game.";
    }
    else {
      this.winnerReason = reason.toString();
    }

    this.isGameOver = true;

    // if statistics implemented show statistics
    // if replay implemented enable replayButton
  }

  /**
   * Handler if the client is leaving the game.
   */
  public void leaveGame() {
    if (sessionId != null && !isGameOver) {
      messageEmitter.sendGameLeaveMessage();
    }
  }

  /**
   * Sends the chosen operation to the server.
   *
   * @param operation The chosen operation.
   */
  public void sendGameOperationMessage(Operation operation) {

    messageEmitter.sendGameOperationMessage(operation);

    resetIsClientActivePlayer();

    operations.clear();
  }

  /**
   * Handles the gamePause.
   *
   * @param gamePause Boolean if game is paused.
   */
  public void gamePaused(Boolean gamePause, Boolean serverEnforcedPause) {

    this.gamePause = gamePause;
    this.serverEnforcedPause = serverEnforcedPause;
  }

  public void sendGamePause() {

    messageEmitter.sendRequestGamePauseMessage(!gamePause);
  }

  /**
   * Informs the player about received Strike.
   * @param strikeNr How many strikes have been received in a row.
   * @param strikeMax Maximal strike count.
   * @param reason The reason for the strike.
   */
  public void strike(Integer strikeNr, Integer strikeMax, String reason) {

    strikeReceived = true;
    this.strikeNr = strikeNr;
    this.strikeMax = strikeMax;
    this.strikeReason = reason;

  }

  public void error(final ErrorTypeEnum errorType) {

    errorReceived = true;
    this.errorType = errorType.toString();
  }

  /**
   * Assigns texture to the gadget in a hashMap.
   */
  public void assignTexturesToGadget() {

    gadgetEnumTextureHashMap.put(GadgetEnum.BOWLER_BLADE, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.bowlerBlade)));
    gadgetEnumTextureHashMap.put(GadgetEnum.CHICKEN_FEED, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.chickenfeed)));
    gadgetEnumTextureHashMap.put(GadgetEnum.DIAMOND_COLLAR, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.diamondCollar)));
    gadgetEnumTextureHashMap.put(GadgetEnum.FOG_TIN, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.fogTin)));
    gadgetEnumTextureHashMap.put(GadgetEnum.GAS_GLOSS, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.gasGloss)));
    gadgetEnumTextureHashMap.put(GadgetEnum.GRAPPLE, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.grapple)));
    gadgetEnumTextureHashMap.put(GadgetEnum.HAIRDRYER, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.hairdryer)));
    gadgetEnumTextureHashMap.put(GadgetEnum.JETPACK, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.jetPack)));
    gadgetEnumTextureHashMap.put(GadgetEnum.LASER_COMPACT, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.laserCompact)));
    gadgetEnumTextureHashMap.put(GadgetEnum.MAGNETIC_WATCH, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.magneticWatch)));
    gadgetEnumTextureHashMap.put(GadgetEnum.MIRROR_OF_WILDERNESS, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.mirrorOfWilderness)));
    gadgetEnumTextureHashMap.put(GadgetEnum.MOLEDIE, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.moleDie)));
    gadgetEnumTextureHashMap.put(GadgetEnum.MOTHBALL_POUCH, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.mothPouch)));
    gadgetEnumTextureHashMap.put(GadgetEnum.NUGGET, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.nugget)));
    gadgetEnumTextureHashMap.put(GadgetEnum.POCKET_LITTER, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.pocketLitter)));
    gadgetEnumTextureHashMap.put(GadgetEnum.POISON_PILLS, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.poisonPills)));
    gadgetEnumTextureHashMap.put(GadgetEnum.ROCKET_PEN, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.rocketPen)));
    gadgetEnumTextureHashMap.put(GadgetEnum.TECHNICOLOUR_PRISM, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.technicolorPrism)));
    gadgetEnumTextureHashMap.put(GadgetEnum.WIRETAP_WITH_EARPLUGS, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.wireTapsWithEarPlug)));
    gadgetEnumTextureHashMap.put(GadgetEnum.MASK, new TextureRegionDrawable(new TextureRegion(NoTimeToSpy.mask)));

  }

  /**
   * Sets the client as active player.
   * @param activeCharacterId The active character.
   */
  public void setIsClientActivePlayer(UUID activeCharacterId) {

    this.activeCharacterId = activeCharacterId;
    isClientActivePlayer = true;
  }

  public void resetIsClientActivePlayer() {
    isClientActivePlayer = false;
  }

  /**
   * Auxiliary method to get a character from a set of characters by the UUID.
   * @param characters The set of characters, which is to be searched in.
   * @param searchedCharacterUUID The character UUID who is searched.
   * @return The found character.
   */
  public Character getCharacterByUUID(Set<Character> characters, UUID searchedCharacterUUID) {

    for (Character searchedCharacter : characters) {

      if (searchedCharacter.getCharacterId().equals(searchedCharacterUUID)){
        return searchedCharacter;
      }
    }
    return null;
  }

  /**
   * Auxiliary method to get a character from a set of characters by the point.
   * @param characters The set of characters, which is to be searched in.
   * @param searchedCharacterCoordinates The Coordinates searched for.
   * @return The found character.
   */
  public Character getCharacterByPoint(Set<Character> characters, Point searchedCharacterCoordinates) {

    for (Character searchedCharacter : characters) {

      if (searchedCharacter.getCoordinates().equals(searchedCharacterCoordinates)){
        return searchedCharacter;
      }
    }
    return null;
  }

  /**
   * Default constructor.
   */
  public void setDefaultValues() {
    userId = null;

    sessionId = null;
    isSessionActive = false;
    creationDate = null;

    playerOneId = null;
    playerTwoId = null;

    playerOneName = null;
    playerTwoName = null;

    clientIsPlayerOne = false;
    clientIsPlayerTwo = false;

    activeCharacterId = null;

    operations = null;
    state = null;

    isClientActivePlayer = false;

    isGameOver = false;

    level = null;
    settings = null;
    character_settings = null;
    characterMap = null;
    gadgetMap = null;

    gadgetEnumTextureHashMap = null;
    characterToTextureMap = null;

    opponentLeft = false;

    stateUpdated = false;

    gameStatusMessages = 0;

    strikeReceived = false;
    strikeMax = null;
    strikeNr = null;
    strikeReason = null;

    errorReceived = false;
    errorType = null;

    winnerReason = null;
    winnerString = null;

    gamePause = false;
    serverEnforcedPause = false;

    hasReplay = false;

    chosenCharacters = new LinkedList<UUID>();

    opponentCharacters = new LinkedList<UUID>();

    safeCombinations = new HashSet<Integer>();

    playerOpenedSafes = null;
  }
}
