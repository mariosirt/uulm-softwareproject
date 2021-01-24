package com.sopra.ntts.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.sopra.ntts.controller.GameHandler;
import com.sopra.ntts.controller.MusicPlayer;
import com.sopra.ntts.controller.NoTimeToSpy;
import com.sopra.ntts.model.GameScreenFieldStateEnum;
import com.sopra.ntts.model.GameScreenState;
import com.sopra.ntts.model.NetworkStandard.Characters.Character;
import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;
import com.sopra.ntts.model.NetworkStandard.DataTypes.Gadgets.Gadget;
import com.sopra.ntts.model.NetworkStandard.DataTypes.Operations.*;
import com.sopra.ntts.model.NetworkStandard.DataTypes.PropertyEnum;
import com.sopra.ntts.model.NetworkStandard.DataTypes.Szenario.FieldStateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Game screen is used to draw the game elements.
 *
 * @author Sedat Qaja
 */
public class GameScreen implements Screen {

    final Logger logger = LoggerFactory.getLogger(GameScreen.class);

    private NoTimeToSpy parent;
    private GameHandler gameHandler;

    private Label versionLabel;

    private Label gameIsPausedLabel;

    private Label currentRound;

    private Label currentCharacterHp;
    private Label currentCharacterMp;
    private Label currentCharacterAp;
    private Label currentCharacterIp;
    private Label currentCharacterChips;

    private SelectBox<Object> currentCharacterProperty;

    private TextButton gameLeaveButton;
    private TextButton gamePauseButton;

    private InputListener gameLeaveListener;
    private InputListener gamePauseListener;

    private TextButton useGadgetButton;
    private SelectBox<Object> gadgetOperation;
    private TextButton gambleButton;
    private TextButton propertyButton;
    private TextButton moveButton;
    private TextButton retireButton;
    private TextButton spySafeButton;

    private Image keyImage;
    private SelectBox<Object> keyValues;

    private InputListener useGadgetButtonListener;
    private InputListener gambleButtonListener;
    private InputListener propertyButtonListener;
    private InputListener moveButtonListener;
    private InputListener retireButtonListener;
    private InputListener spySafeButtonListener;

    private HashMap<Point, GameScreenState> stateMap;

    private Boolean tileChosen;

    private Integer characterAp = -1;

    private Point tileChosenCoordinates;
    private Point characterPoint;

    private Texture textureChosen;

    private TextField stakeTextField;

    private Label timerLabel;
    private Timer.Task timerTask;
    private Integer timePast;

    private Boolean dialogOpen = false;

    public Boolean gameStateInitialized = false;

    private OrthographicCamera camera;
    private StretchViewport viewport;
    private Stage stage;
    private MusicPlayer musicPlayer;

    /**
     * Initializes basic graphical elements of the game screen and and logic.
     * @param parent Base game for every client.
     * @param gameHandler Handles the logical part of the game.
     *
     * @author Sedat Qaja
     */
    public GameScreen(final NoTimeToSpy parent, final GameHandler gameHandler) {

        this.parent = parent;
        this.gameHandler = gameHandler;

        camera = new OrthographicCamera(1280, 720);
        viewport = new StretchViewport(1280, 720, camera);
        stage = new Stage(viewport);

        this.stateMap = new HashMap<>();

        this.tileChosen = false;

        this.tileChosenCoordinates = new Point();
        this.musicPlayer = new MusicPlayer();

        // initiates the listeners for all interactive elements
        gameLeaveListener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                musicPlayer.playSound(musicPlayer.button_sound);
                gameHandler.leaveGame();

            }
        };

        gamePauseListener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                musicPlayer.playSound(musicPlayer.button_sound);
                gameHandler.sendGamePause();
            }
        };

        useGadgetButtonListener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return gameHandler.isClientActivePlayer && !gameHandler.gamePause;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                musicPlayer.playSound(musicPlayer.button_sound);
                boolean cocktailAction = false;
                if (gadgetOperation.getSelected() == null) {
                    cocktailAction = true;
                } else {
                    if (!gadgetOperation.getSelected().equals(GadgetEnum.GRAPPLE) && !gadgetOperation.getSelected().equals(GadgetEnum.LASER_COMPACT) && !gadgetOperation.getSelected().equals(GadgetEnum.POISON_PILLS)) {
                        cocktailAction = true;
                    }
                }

                if (stateMap.get(tileChosenCoordinates).getFieldState().equals(GameScreenFieldStateEnum.BAR_TABLE_COCKTAIL) && cocktailAction) {

                    GadgetAction gadgetAction = new GadgetAction(OperationEnum.GADGET_ACTION, null, tileChosenCoordinates, gameHandler.activeCharacterId, GadgetEnum.COCKTAIL);
                    gameHandler.sendGameOperationMessage(gadgetAction);
                    resetChoice();
                } else {

                    GadgetAction gadgetAction = new GadgetAction(OperationEnum.GADGET_ACTION, null, tileChosenCoordinates, gameHandler.activeCharacterId, (GadgetEnum) gadgetOperation.getSelected());
                    gameHandler.sendGameOperationMessage(gadgetAction);
                    resetChoice();
                }
            }
        };

        gambleButtonListener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return gameHandler.isClientActivePlayer && !gameHandler.gamePause;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                musicPlayer.playSound(musicPlayer.button_sound);
                Dialog gambleDialog = new Dialog("How much do you want to bet?", NoTimeToSpy.skin) {

                    @Override
                    protected void result(Object object) {

                        if (object.equals(true)) {

                            GambleAction gambleAction = new GambleAction(OperationEnum.GAMBLE_ACTION, null, tileChosenCoordinates, gameHandler.activeCharacterId, Integer.parseInt(stakeTextField.getText()));
                            gameHandler.sendGameOperationMessage(gambleAction);
                            resetChoice();
                        }
                    }
                };
                gambleDialog.button("Confirm the stake", true);
                gambleDialog.button("Decline stake", false);
                gambleDialog.getContentTable().row();
                gambleDialog.getContentTable().add(stakeTextField = new TextField("10", NoTimeToSpy.skin)).width(100);
                gambleDialog.show(stage);
            }
        };

        propertyButtonListener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return gameHandler.isClientActivePlayer && !gameHandler.gamePause;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                musicPlayer.playSound(musicPlayer.button_sound);
                Operation propertyAction = new PropertyAction(OperationEnum.PROPERTY_ACTION, null, tileChosenCoordinates, gameHandler.activeCharacterId, (PropertyEnum) currentCharacterProperty.getSelected(), null);
                gameHandler.sendGameOperationMessage(propertyAction);
                resetChoice();
            }
        };

        moveButtonListener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return gameHandler.isClientActivePlayer && !gameHandler.gamePause;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                musicPlayer.playSound(musicPlayer.button_sound);
                Movement movement = new Movement(OperationEnum.MOVEMENT, null, tileChosenCoordinates, gameHandler.activeCharacterId, characterPoint);
                gameHandler.sendGameOperationMessage(movement);
                resetChoice();
            }
        };

        retireButtonListener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return gameHandler.isClientActivePlayer && !gameHandler.gamePause;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                musicPlayer.playSound(musicPlayer.button_sound);
                Retire retire = new Retire(OperationEnum.RETIRE, null, tileChosenCoordinates, gameHandler.activeCharacterId);
                gameHandler.sendGameOperationMessage(retire);
                resetChoice();
            }
        };

        spySafeButtonListener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return gameHandler.isClientActivePlayer && !gameHandler.gamePause;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                musicPlayer.playSound(musicPlayer.button_sound);
                // checks if safe is opened
                if (gameHandler.state.getMap().getField(tileChosenCoordinates.x, tileChosenCoordinates.y).getState().equals(FieldStateEnum.SAFE)) {
                    if (gameHandler.safeCombinations.contains(gameHandler.state.getMap().getField(tileChosenCoordinates.x, tileChosenCoordinates.y).getSafeIndex()) && characterAp > 0) {

                        Point point = new Point(tileChosenCoordinates.x, tileChosenCoordinates.y);

                        gameHandler.playerOpenedSafes.add(point);
                    }
                }

                Spy spySafe = new Spy(OperationEnum.SPY_ACTION, null, tileChosenCoordinates, gameHandler.activeCharacterId);
                gameHandler.sendGameOperationMessage(spySafe);
                resetChoice();
            }
        };
    }

    /**
     * Basic show method of libGdx, is called when the showScreen method is called.
     * Initiates all screen elements.
     *
     * @author Sedat Qaja
     */
    @Override
    public void show() {

        stage.clear();

        versionLabel = new Label("Version " + NoTimeToSpy.versionNumber, NoTimeToSpy.skin);
        versionLabel.setPosition(1100, 680);

        gameLeaveButton = new TextButton("Leave Game", NoTimeToSpy.skin);
        gameLeaveButton.addListener(gameLeaveListener);
        gameLeaveButton.setPosition(850, 670);

        gamePauseButton = new TextButton("Pause Game", NoTimeToSpy.skin);
        gamePauseButton.addListener(gamePauseListener);
        gamePauseButton.setPosition(650, 670);

        gameIsPausedLabel = new Label("Paused: to resume click again.", NoTimeToSpy.skin);
        gameIsPausedLabel.setPosition(50, 670);
        gameIsPausedLabel.setVisible(false);

        retireButton = new TextButton("Retire", NoTimeToSpy.skin);
        retireButton.addListener(retireButtonListener);
        retireButton.setPosition(550, 670);

        spySafeButton = new TextButton("Spy", NoTimeToSpy.skin);
        spySafeButton.addListener(spySafeButtonListener);
        spySafeButton.setPosition(450, 670);

        keyImage = new Image();
        keyImage.setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.keys)));
        keyImage.setSize(40,40);
        keyImage.setPosition(10,616);

        keyValues = new SelectBox<Object>(NoTimeToSpy.skin);
        keyValues.setSize(20,20);
        keyValues.setPosition(20,570);

        useGadgetButton = new TextButton("Use Gadget", NoTimeToSpy.skin);
        useGadgetButton.addListener(useGadgetButtonListener);
        useGadgetButton.setPosition(240, 15);
        useGadgetButton.setSize(200, 28);

        gambleButton = new TextButton("Gamble", NoTimeToSpy.skin);
        gambleButton.addListener(gambleButtonListener);
        gambleButton.setPosition(30, 45);
        gambleButton.setSize(200, 28);

        moveButton = new TextButton("Move to Position", NoTimeToSpy.skin);
        moveButton.addListener(moveButtonListener);
        moveButton.setPosition(30, 15);
        moveButton.setSize(200, 28);

        propertyButton = new TextButton("Use Property", NoTimeToSpy.skin);
        propertyButton.addListener(propertyButtonListener);
        propertyButton.setPosition(240, 45);
        propertyButton.setSize(200, 28);

        gadgetOperation = new SelectBox<Object>(NoTimeToSpy.skin);
        gadgetOperation.setPosition(450, 20);
        gadgetOperation.setSize(300, 15);

        currentRound = new Label("Round : ", NoTimeToSpy.skin);
        currentRound.setPosition(1050, 15);

        currentCharacterChips = new Label("Chips : ", NoTimeToSpy.skin);
        currentCharacterChips.setPosition(1050, 45);

        currentCharacterIp = new Label("IP : ", NoTimeToSpy.skin);
        currentCharacterIp.setPosition(900, 45);

        currentCharacterProperty = new SelectBox<Object>(NoTimeToSpy.skin);
        currentCharacterProperty.setPosition(450, 50);
        currentCharacterProperty.setSize(300, 15);

        currentCharacterMp = new Label("MP : ", NoTimeToSpy.skin);
        currentCharacterMp.setPosition(750, 45);

        currentCharacterAp = new Label("AP : ", NoTimeToSpy.skin);
        currentCharacterAp.setPosition(750, 15);

        currentCharacterHp = new Label("HP : ", NoTimeToSpy.skin);
        currentCharacterHp.setPosition(900, 15);

        timerLabel = new Label("Timer : " + timePast, NoTimeToSpy.skin);
        timerLabel.setPosition(50, 690);

        initializeGameScreen();
        initializeGadgetsForGameMove();

    }

    /**
     * Draws the screen every frame. Checks if Dialogs are to be shown because of game cause.
     * Shows the Buttons for the active player.
     *
     * @param delta time passed.
     *
     * @author Sedat Qaja
     */
    @Override
    public void render(final float delta) {

        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        NoTimeToSpy.spriteBatch.begin();
        NoTimeToSpy.spriteBatch.end();

        if (gameHandler.isClientActivePlayer && gameStateInitialized) {

            // activates the propertyButton
            propertyButton.setVisible(currentCharacterProperty.getSelected().equals(PropertyEnum.BANG_AND_BURN) || currentCharacterProperty.getSelected().equals(PropertyEnum.OBSERVATION));

            // activates spy safe button
            spySafeButton.setVisible(stateMap.get(tileChosenCoordinates).getFieldState().equals(GameScreenFieldStateEnum.SAFE) || (stateMap.get(tileChosenCoordinates).getCharacter() != null && !gameHandler.chosenCharacters.contains(stateMap.get(tileChosenCoordinates).getCharacter().getCharacterId())));

            // the gambleButton activation
            gambleButton.setVisible(stateMap.get(tileChosenCoordinates).getFieldState().equals(GameScreenFieldStateEnum.ROULETTE_TABLE));

            // deactivates the useGadgetButton
            useGadgetButton.setVisible(!stateMap.get(characterPoint).getCharacter().getAp().equals(0) && (!stateMap.get(characterPoint).getCharacter().getGadgets().isEmpty() || stateMap.get(tileChosenCoordinates).getFieldState().equals(GameScreenFieldStateEnum.BAR_TABLE_COCKTAIL)));

            // deactivates the moveButton
            moveButton.setVisible(!stateMap.get(characterPoint).getCharacter().getMp().equals(0));

        }

        if (gameHandler.clientIsPlayerTwo || gameHandler.clientIsPlayerOne) {

            // activates pause label/button
            gameIsPausedLabel.setVisible(gameHandler.gamePause && gameStateInitialized);
            gamePauseButton.setVisible(gameHandler.clientIsPlayerOne || gameHandler.clientIsPlayerTwo);
        }

        if (gameHandler.strikeReceived && !dialogOpen) {

            Dialog strikeDialog = new Dialog("You received a Strike!", NoTimeToSpy.skin) {

                @Override
                protected void result(Object object) {

                    if (object.equals(true)) {
                        dialogOpen = false;
                        gameHandler.strikeReceived = false;
                    }
                }
            };
            strikeDialog.button("Confirm", true);
            strikeDialog.getContentTable().row();
            strikeDialog.getContentTable().add(new Label("You received Strike-Nr.: " + gameHandler.strikeNr + " from maximal " + gameHandler.strikeMax + " Strikes. \n The reason is:" + gameHandler.strikeReason, NoTimeToSpy.skin)).width(800);
            strikeDialog.show(stage);
            dialogOpen = true;
        }

        if (gameHandler.errorReceived && !dialogOpen) {

            Dialog errorDialog = new Dialog("You received an ErrorMessage!", NoTimeToSpy.skin) {

                @Override
                protected void result(Object object) {

                    if (object.equals(true)) {

                        dialogOpen = false;

                        gameHandler.errorReceived = false;

                        gameHandler.setDefaultValues();

                        parent.showLoginScreen();
                    }
                }
            };
            errorDialog.button("Confirm", true);
            errorDialog.getContentTable().row();
            errorDialog.getContentTable().add(new Label("You received an Error.\n The reason is:" + gameHandler.errorType, NoTimeToSpy.skin)).width(800);
            errorDialog.show(stage);
            dialogOpen = true;
        }

        if (gameHandler.isGameOver && !dialogOpen) {

            Dialog gameOverDialog = new Dialog("The game has ended!", NoTimeToSpy.skin) {

                @Override
                protected void result(Object object) {

                    if (object.equals(true)) {

                        dialogOpen = false;

                        gameHandler.setDefaultValues();

                        parent.showLoginScreen();
                    }
                }
            };
            gameOverDialog.button("Confirm", true);
            gameOverDialog.getContentTable().row();
            gameOverDialog.getContentTable().add(new Label("The Winner is: " + gameHandler.winnerString + " \n The reason is:" + gameHandler.winnerReason, NoTimeToSpy.skin)).width(800);
            gameOverDialog.show(stage);
            dialogOpen = true;
        }

        if (gameHandler.stateUpdated) {

            initializeGameScreen();
            initializeGadgetsForGameMove();
            gameHandler.stateUpdated = false;
        }


        stage.act();
        stage.draw();
    }

    /**
     * Method from libgdx.
     *
     * @param width  of the screen.
     * @param height of the screen.
     *
     * @author Sedat Qaja
     */
    @Override
    public void resize(int width, int height) {
    }

    /**
     * Method from libgdx.
     *
     * @author Sedat Qaja
     */
    @Override
    public void pause() {

    }

    /**
     * Method from libgdx.
     *
     * @author Sedat Qaja
     */
    @Override
    public void resume() {

    }

    /**
     * Method from libgdx.
     *
     * @author Sedat Qaja
     */
    @Override
    public void hide() {

    }

    /**
     * Method from libgdx. Disposes the resources used in the screen.
     *
     * @author Sedat Qaja
     */
    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * Initializes the game tiles.
     *
     * @author Sedat Qaja
     */
    private void initializeGameScreen() {

        stage.clear();

        // iterates through every possible field position
        for (int xd = 0; xd < gameHandler.level.getScenario().length; xd++) {

            for (int yd = 0; yd < gameHandler.level.getScenario()[xd].length; yd++) {

                //start positions in the left upper corner
                int startPosX = 60;
                int startPosY = 616;

                //image size
                int sizeY = gameHandler.level.getScenario().length;
                int sizeX = gameHandler.level.getScenario()[1].length;
                int sizeXY;

                // cuts size per tile to half if field to big for screen
                if (sizeX > 24) {
                    sizeXY = 24;
                } else if (sizeY > 12) {
                    sizeXY = 24;
                } else {
                    sizeXY = 48;
                }


                final int posX, posY;

                posX = startPosX + sizeXY * yd;
                posY = startPosY - sizeXY * xd;

                Image image = new Image();
                Image image1 = new Image();
                image.setPosition(posX, posY);
                image.setSize(sizeXY, sizeXY);

                final int finalXd = xd;
                final int finalYd = yd;

                // Initiates listener for all tiles
                InputListener inputListener = new InputListener() {

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        //blocks action from non active player
                        return gameHandler.isClientActivePlayer;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                        Point point = new Point(finalXd, finalYd);

                        if (!point.equals(characterPoint)) {

                            if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.FREE)) {

                                // resets the before chosen
                                resetChoice();

                                // new image is drawn -> chosen
                                stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.freeSelected)));

                                // activate the choice boolean
                                tileChosen = true;
                                tileChosenCoordinates.setLocation(finalXd, finalYd);
                                textureChosen = NoTimeToSpy.free;

                            } else if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.WALL)) {

                                resetChoice();
                                stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.wallSelected)));
                                tileChosen = true;
                                tileChosenCoordinates.setLocation(finalXd, finalYd);
                                textureChosen = NoTimeToSpy.wall;
                            } else if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.BAR_SEAT)) {

                                resetChoice();
                                stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.barSeatSelected)));
                                tileChosen = true;
                                tileChosenCoordinates.setLocation(finalXd, finalYd);
                                textureChosen = NoTimeToSpy.barSeat;
                            } else if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.BAR_TABLE)) {

                                resetChoice();
                                stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.barTableSelected)));
                                tileChosen = true;
                                tileChosenCoordinates.setLocation(finalXd, finalYd);
                                textureChosen = NoTimeToSpy.barTable;
                            } else if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.BAR_TABLE_COCKTAIL)) {

                                resetChoice();
                                stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.barTableCocktailSelected)));
                                tileChosen = true;
                                tileChosenCoordinates.setLocation(finalXd, finalYd);
                                textureChosen = NoTimeToSpy.barTableCocktail;
                            } else if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.ROULETTE_TABLE)) {

                                resetChoice();
                                stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.rouletteSelected)));
                                tileChosen = true;
                                tileChosenCoordinates.setLocation(finalXd, finalYd);
                                textureChosen = NoTimeToSpy.roulette;
                            } else if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.FIREPLACE)) {

                                resetChoice();
                                stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.fireplaceSelected)));
                                tileChosen = true;
                                tileChosenCoordinates.setLocation(finalXd, finalYd);
                                textureChosen = NoTimeToSpy.fireplace;
                            } else if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.SAFE)) {

                                resetChoice();
                                stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.safeSelected)));
                                tileChosen = true;
                                tileChosenCoordinates.setLocation(finalXd, finalYd);
                                textureChosen = NoTimeToSpy.safe;
                            } else if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.SAFEOPENED)) {

                                resetChoice();
                                stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.safeOpenedSelected)));
                                tileChosen = true;
                                tileChosenCoordinates.setLocation(finalXd, finalYd);
                                textureChosen = NoTimeToSpy.safeOpened;
                            } else if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.PLAYERSELECTEDFREEFRIEND)) {

                                resetChoice();
                                stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.freeSelected)));
                                tileChosen = true;
                                tileChosenCoordinates.setLocation(finalXd, finalYd);
                                textureChosen = NoTimeToSpy.freeSelectedFriend;
                            } else if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.PLAYERSELECTEDBARSEATFRIEND)) {

                                resetChoice();
                                stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.barSeatSelected)));
                                tileChosen = true;
                                tileChosenCoordinates.setLocation(finalXd, finalYd);
                                textureChosen = NoTimeToSpy.barSeatSelectedFriend;
                            } else if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.PLAYERSELECTEDFREEFOE)) {

                                resetChoice();
                                stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.freeSelected)));
                                tileChosen = true;
                                tileChosenCoordinates.setLocation(finalXd, finalYd);
                                textureChosen = NoTimeToSpy.freeSelectedFoe;
                            } else if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.PLAYERSELECTEDBARSEATFOE)) {

                                resetChoice();
                                stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.barSeatSelected)));
                                tileChosen = true;
                                tileChosenCoordinates.setLocation(finalXd, finalYd);
                                textureChosen = NoTimeToSpy.barSeatSelectedFoe;
                            } else if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.FOGGY)) {

                                resetChoice();
                                stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.foggySelected)));
                                tileChosen = true;
                                tileChosenCoordinates.setLocation(finalXd, finalYd);
                                textureChosen = NoTimeToSpy.foggy;
                            }
                        } else {

                            resetChoice();
                            tileChosenCoordinates.setLocation(finalXd, finalYd);
                        }
                    }
                };

                boolean characterIsDrawn = false;

                Point integerPoint = new Point(xd, yd);

                GameScreenState gameScreenState = new GameScreenState();

                // fills the hashmap for the data change and draws basic images from given field
                if (gameHandler.state.getMap().getField(xd, yd).getState().equals(FieldStateEnum.FREE)) {
                    image.setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.free)));
                    gameScreenState.setFieldState(GameScreenFieldStateEnum.FREE);
                    gameScreenState.setImage(image);
                    stateMap.put(integerPoint, gameScreenState);
                }
                else if (gameHandler.state.getMap().getField(xd, yd).getState().equals(FieldStateEnum.WALL)) {
                    image.setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.wall)));
                    gameScreenState.setFieldState(GameScreenFieldStateEnum.WALL);
                    gameScreenState.setImage(image);
                    stateMap.put(integerPoint, gameScreenState);
                }
                else if (gameHandler.state.getMap().getField(xd, yd).getState().equals(FieldStateEnum.BAR_SEAT)) {
                    image.setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.barSeat)));
                    gameScreenState.setFieldState(GameScreenFieldStateEnum.BAR_SEAT);
                    gameScreenState.setImage(image);
                    stateMap.put(integerPoint, gameScreenState);
                }
                else if (gameHandler.state.getMap().getField(xd, yd).getState().equals(FieldStateEnum.BAR_TABLE)) {
                    // draw the cocktail
                    if (gameHandler.state.getMap().getField(xd, yd).getGadget() != null && gameHandler.state.getMap().getField(xd, yd).getGadget().getGadget().equals(GadgetEnum.COCKTAIL)) {

                        image.setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.barTableCocktail)));
                        gameScreenState.setFieldState(GameScreenFieldStateEnum.BAR_TABLE_COCKTAIL);
                    } else {

                        image.setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.barTable)));
                        gameScreenState.setFieldState(GameScreenFieldStateEnum.BAR_TABLE);
                    }
                    gameScreenState.setImage(image);
                    stateMap.put(integerPoint, gameScreenState);

                }
                else if (gameHandler.state.getMap().getField(xd, yd).getState().equals(FieldStateEnum.ROULETTE_TABLE)) {
                    image.setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.roulette)));
                    gameScreenState.setFieldState(GameScreenFieldStateEnum.ROULETTE_TABLE);
                    gameScreenState.setImage(image);
                    stateMap.put(integerPoint, gameScreenState);
                }
                else if (gameHandler.state.getMap().getField(xd, yd).getState().equals(FieldStateEnum.FIREPLACE)) {
                    image.setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.fireplace)));
                    gameScreenState.setFieldState(GameScreenFieldStateEnum.FIREPLACE);
                    gameScreenState.setImage(image);
                    stateMap.put(integerPoint, gameScreenState);
                }
                else if (gameHandler.state.getMap().getField(xd, yd).getState().equals(FieldStateEnum.SAFE)) {

                    // checking if
                    if (gameHandler.playerOpenedSafes.contains(integerPoint)) {

                        image.setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.safeOpened)));
                        gameScreenState.setFieldState(GameScreenFieldStateEnum.SAFEOPENED);
                    } else {

                        image.setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.safe)));
                        gameScreenState.setFieldState(GameScreenFieldStateEnum.SAFE);
                    }
                    gameScreenState.setImage(image);
                    stateMap.put(integerPoint, gameScreenState);
                }
                // foggy is after initialization, because its not a state of the hashmap -> temporal change
                if (gameHandler.state.getMap().getField(xd, yd).isFoggy()) {
                    image.setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.foggy)));
                    gameScreenState.setFieldState(GameScreenFieldStateEnum.FOGGY);
                    gameScreenState.setImage(image);
                    stateMap.put(integerPoint, gameScreenState);
                }

                // draws characters
                for (Character character : gameHandler.state.getCharacters()) {

                    if (xd == character.getCoordinates().getX() && yd == character.getCoordinates().getY()) {

                        image1.setPosition(posX, posY);
                        image1.setSize(sizeXY, sizeXY);
                        image1.setDrawable(gameHandler.characterToTextureMap.get(character.getCharacterId()));
                        gameScreenState.setCharacter(character);

                        characterIsDrawn = true;
                    }
                }

                // draws gadget
                if (gameHandler.state.getMap().getField(xd, yd).getGadget() != null && !gameHandler.state.getMap().getField(xd, yd).getState().equals(FieldStateEnum.SAFE)) {

                    image1.setPosition(posX, posY);
                    image1.setSize(sizeXY, sizeXY);
                    image1.setDrawable(gameHandler.gadgetEnumTextureHashMap.get(gameHandler.state.getMap().getField(xd, yd).getGadget().getGadget()));
                    gameScreenState.setGadget(gameHandler.state.getMap().getField(xd, yd).getGadget());

                    characterIsDrawn = true;
                }

                // draws cat
                if (xd == gameHandler.state.getCatCoordinates().x && yd == gameHandler.state.getCatCoordinates().y) {

                    image1.setPosition(posX, posY);
                    image1.setSize(sizeXY, sizeXY);
                    image1.setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.cat)));
                    characterIsDrawn = true;
                }

                // draws janitor
                if (xd == gameHandler.state.getJanitorCoordinates().x && yd == gameHandler.state.getJanitorCoordinates().y) {

                    image1.setPosition(posX, posY);
                    image1.setSize(sizeXY, sizeXY);
                    image1.setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.diverse)));
                    characterIsDrawn = true;
                }

                // sets listener not to tile image, rather to character image
                if (characterIsDrawn) {
                    image1.addListener(inputListener);
                } else {
                    image.addListener(inputListener);
                }

                stage.addActor(image);
                stage.addActor(image1);

            }
        }

        // initializes all labels and buttons
        initializeCharacterLabels();

        currentRound.setText("Round : " + gameHandler.state.getCurrentRound());

        gameStateInitialized = true;

        stage.addActor(timerLabel);
        stage.addActor(spySafeButton);
        stage.addActor(retireButton);
        stage.addActor(gameIsPausedLabel);
        stage.addActor(moveButton);
        stage.addActor(propertyButton);
        stage.addActor(gambleButton);
        stage.addActor(useGadgetButton);
        stage.addActor(gadgetOperation);
        stage.addActor(currentCharacterHp);
        stage.addActor(currentCharacterAp);
        stage.addActor(currentCharacterMp);
        stage.addActor(currentCharacterProperty);
        stage.addActor(currentCharacterIp);
        stage.addActor(currentCharacterChips);
        stage.addActor(currentRound);
        stage.addActor(gameLeaveButton);
        stage.addActor(gamePauseButton);
        stage.addActor(versionLabel);
        stage.addActor(keyImage);
        stage.addActor(keyValues);

        // disables the buttons, if player is not active
        if (!gameHandler.isClientActivePlayer) {

            disableBottomLevelHUD();
        }

        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Initializes the bottom level HUD elements.
     * Activates the propertyButton, if the needed properties are available.
     *
     * @author Sedat Qaja
     */
    public void initializeCharacterLabels() {

        for (Character f : gameHandler.state.getCharacters()) {

            if (f.getCharacterId().equals(gameHandler.activeCharacterId)) {
                currentCharacterHp.setText("HP : " + f.getHp());
                currentCharacterMp.setText("MP : " + f.getMp());
                currentCharacterAp.setText("AP : " + f.getAp());

                // set ap for checking
                characterAp = f.getAp();

                currentCharacterIp.setText("IP : " + f.getIp());
                currentCharacterChips.setText("Chips : " + f.getChips());
                currentCharacterProperty.setItems(f.getProperties().toArray());
            }
        }
    }

    /**
     * Initializes the GadgetEnum for the client to choose for a GameMove.
     * Initializes the image which character is active and friends.
     *
     * @author Sedat Qaja
     */
    public void initializeGadgetsForGameMove() {

        // initializes gadgets and keys, if active character or player in possession of some
        for (Character f : gameHandler.state.getCharacters()) {

            if (f.getCharacterId().equals(gameHandler.activeCharacterId)) {

                List<Object> gadgets = new LinkedList<Object>();

                for (Gadget gadget : f.getGadgets()) {
                    if (gadget.getUsages() != 0) {

                        gadgets.add(gadget.getGadget());
                    }
                }
                gadgetOperation.setItems(gadgets.toArray());

                characterPoint = new Point(f.getCoordinates().x, f.getCoordinates().y);

                if (!characterPoint.equals(new Point(-1, -1))) {

                    // sets  the character
                    if (stateMap.get(characterPoint).getFieldState().equals(GameScreenFieldStateEnum.FREE) && gameHandler.isClientActivePlayer) {

                        stateMap.get(characterPoint).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.freeSelected)));
                    } else if (stateMap.get(characterPoint).getFieldState().equals(GameScreenFieldStateEnum.BAR_SEAT) && gameHandler.isClientActivePlayer) {

                        stateMap.get(characterPoint).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.barSeatSelected)));
                    }
                }

                // sets keys
                keyValues.setItems(gameHandler.safeCombinations.toArray());
            }

            // sets the team characters
            else if (gameHandler.chosenCharacters.contains(f.getCharacterId())) {

                Point point = new Point(f.getCoordinates().x, f.getCoordinates().y);

                if (!point.equals(new Point(-1, -1))) {

                    if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.FREE)) {

                        stateMap.get(point).setFieldState(GameScreenFieldStateEnum.PLAYERSELECTEDFREEFRIEND);
                        stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.freeSelectedFriend)));
                    } else if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.BAR_SEAT)) {

                        stateMap.get(point).setFieldState(GameScreenFieldStateEnum.PLAYERSELECTEDBARSEATFRIEND);
                        stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.barSeatSelectedFriend)));
                    }
                }
            }
            // sets the opponent characters
            else if (gameHandler.opponentCharacters.contains(f.getCharacterId())) {

                Point point = new Point(f.getCoordinates().x, f.getCoordinates().y);

                if (!point.equals(new Point(-1, -1))) {

                    if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.FREE)) {

                        stateMap.get(point).setFieldState(GameScreenFieldStateEnum.PLAYERSELECTEDFREEFOE);
                        stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.freeSelectedFoe)));
                    } else if (stateMap.get(point).getFieldState().equals(GameScreenFieldStateEnum.BAR_SEAT)) {

                        stateMap.get(point).setFieldState(GameScreenFieldStateEnum.PLAYERSELECTEDBARSEATFOE);
                        stateMap.get(point).getImage().setDrawable(new SpriteDrawable(new Sprite(NoTimeToSpy.barSeatSelectedFoe)));
                    }
                }
            }
        }
    }

    /**
     * Starts the timer for a game Round.
     *
     * @author Sedat Qaja
     */
    public void startTimer() {

        timePast = gameHandler.settings.getTurnPhaseLimit();
        if (timerTask != null) {
            timerTask.cancel();
        }

        timerTask = Timer.schedule(new Timer.Task() {
                                       @Override
                                       public void run() {

                                           if (!gameHandler.gamePause) {
                                               timerLabel.setText("Timer : " + timePast);
                                               timePast--;
                                           }
                                       }
                                   }
                , 0
                , 1
        );
    }

    /**
     * Resets the chosen images.
     *
     * @author Sedat Qaja
     */
    public void resetChoice() {

        if (tileChosen) {

            stateMap.get(tileChosenCoordinates).getImage().setDrawable(new SpriteDrawable(new Sprite(textureChosen)));

            tileChosen = false;
        }
    }

    /**
     * Sets all the items of the lower HUD to invisible, if active character is not clients property.
     *
     * @author Sedat Qaja
     */
    public void disableBottomLevelHUD() {

        timerLabel.setVisible(false);
        retireButton.setVisible(false);
        spySafeButton.setVisible(false);
        currentCharacterProperty.setVisible(false);
        currentCharacterChips.setVisible(false);
        currentCharacterIp.setVisible(false);
        currentCharacterAp.setVisible(false);
        currentCharacterHp.setVisible(false);
        currentCharacterMp.setVisible(false);
        currentRound.setVisible(false);
        useGadgetButton.setVisible(false);
        gadgetOperation.setVisible(false);
        gambleButton.setVisible(false);
        propertyButton.setVisible(false);
        moveButton.setVisible(false);
        keyValues.setVisible(false);
        keyImage.setVisible(false);
    }

    /**
     * Sets all the items of the lower HUD to visible, if active character is clients property.
     *
     * @author Sedat Qaja
     */
    public void enableBottomLevelHUD() {

        timerLabel.setVisible(true);
        currentCharacterProperty.setVisible(true);
        currentCharacterChips.setVisible(true);
        currentCharacterIp.setVisible(true);
        currentCharacterAp.setVisible(true);
        currentCharacterHp.setVisible(true);
        currentCharacterMp.setVisible(true);
        currentRound.setVisible(true);
        gadgetOperation.setVisible(true);
        useGadgetButton.setVisible(true);
        moveButton.setVisible(true);
        retireButton.setVisible(true);
        keyValues.setVisible(true);
        keyImage.setVisible(true);
    }
}
