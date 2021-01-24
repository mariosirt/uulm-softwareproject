package com.sopra.ntts.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.sopra.ntts.controller.*;
import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.UUID;

/**
 * Drafting phase screen is used to draw the draft elements.
 *
 * @author Sedat Qaja
 */
public class DraftingPhaseScreen implements Screen {

    private final Logger logger = LoggerFactory.getLogger(LoginScreen.class);

    private NoTimeToSpy parent;

    private ConnectionHandler connectionHandler;
    private MessageReceiver messageReceiver;
    private DraftingHandler draftingHandler;
    private GameHandler gameHandler;
    private MessageEmitter messageEmitter;

    private OrthographicCamera camera;
    private StretchViewport viewport;
    private Stage stage;

    private Table layoutTable;

    private ImageTextButton offeredItemButton1;
    private ImageTextButton offeredItemButton2;
    private ImageTextButton offeredItemButton3;
    private ImageTextButton offeredItemButton4;
    private ImageTextButton offeredItemButton5;
    private ImageTextButton offeredItemButton6;

    private Image offeredImage1;
    private Image offeredImage2;
    private Image offeredImage3;
    private Image offeredImage4;
    private Image offeredImage5;
    private Image offeredImage6;

    private InputListener offeredButton1Listener;
    private InputListener offeredButton2Listener;
    private InputListener offeredButton3Listener;
    private InputListener offeredButton4Listener;
    private InputListener offeredButton5Listener;
    private InputListener offeredButton6Listener;

    private Label versionLabel;
    private Label inDraftingPhase;

    private Object[] draftingValues;

    private TextButton gameLeaveButton;
    private InputListener gameLeaveListener;

    private Boolean dialogOpen = false;

    /**
     * Constructor for the draft screen. Necessary attributes are initialized.
     *
     * @param parent            The basic game is linked to the screen.
     * @param connectionHandler The connectionHandler is needed to send or evaluate connection.
     * @param messageReceiver   The messageReceiver is needed to get the messages from the server
     * @param gameHandler       The gameHandler is needed because its the framework of the actual game.
     * @param draftingHandler   The draftingHandler is needed because its the framework of the drafting phase.
     * @param messageEmitter    The messageEmitter is needed to send messages.
     *
     * @author Sedat Qaja
     */
    public DraftingPhaseScreen(NoTimeToSpy parent, ConnectionHandler connectionHandler, MessageReceiver messageReceiver, final DraftingHandler draftingHandler, final GameHandler gameHandler, final MessageEmitter messageEmitter) {

        this.parent = parent;
        this.connectionHandler = connectionHandler;
        this.messageReceiver = messageReceiver;
        this.draftingHandler = draftingHandler;
        this.gameHandler = gameHandler;
        this.messageEmitter = messageEmitter;

        setDefaultValues();

        // initiates the listeners for all interactive elements
        offeredButton1Listener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                sendItemChoice(0);
            }
        };

        offeredButton2Listener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                sendItemChoice(1);
            }
        };

        offeredButton3Listener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                sendItemChoice(2);
            }
        };

        offeredButton4Listener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                sendItemChoice(3);
            }
        };

        offeredButton5Listener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                sendItemChoice(4);
            }
        };

        offeredButton6Listener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                sendItemChoice(5);
            }
        };

        gameLeaveListener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                gameHandler.leaveGame();
            }
        };

        camera = new OrthographicCamera(1280, 720);
        viewport = new StretchViewport(1280, 720, camera);
        stage = new Stage(viewport);
    }

    /**
     * Basic show method of libGdx, is called when the showScreen method is called.
     * Initiates all screen elements.
     *
     * @author Sedat Qaja
     */
    @Override
    public void show() {
        setDefaultValues();
        stage.clear();

        offeredItemButton1 = new ImageTextButton("", NoTimeToSpy.skin);
        offeredItemButton1.addListener(offeredButton1Listener);

        offeredItemButton2 = new ImageTextButton("", NoTimeToSpy.skin);
        offeredItemButton2.addListener(offeredButton2Listener);

        offeredItemButton3 = new ImageTextButton("", NoTimeToSpy.skin);
        offeredItemButton3.addListener(offeredButton3Listener);

        offeredItemButton4 = new ImageTextButton("", NoTimeToSpy.skin);
        offeredItemButton4.addListener(offeredButton4Listener);

        offeredItemButton5 = new ImageTextButton("", NoTimeToSpy.skin);
        offeredItemButton5.addListener(offeredButton5Listener);

        offeredItemButton6 = new ImageTextButton("", NoTimeToSpy.skin);
        offeredItemButton6.addListener(offeredButton6Listener);

        enableAllButtons();

        offeredImage1 = new Image(NoTimeToSpy.cat);
        offeredImage2 = new Image(NoTimeToSpy.cat);
        offeredImage3 = new Image(NoTimeToSpy.cat);
        offeredImage4 = new Image(NoTimeToSpy.cat);
        offeredImage5 = new Image(NoTimeToSpy.cat);
        offeredImage6 = new Image(NoTimeToSpy.cat);


        versionLabel = new Label("Version " + NoTimeToSpy.versionNumber, NoTimeToSpy.skin);
        versionLabel.setPosition(1100, 680);

        inDraftingPhase = new Label("You are in the DraftingPhase. Choose an Item.", NoTimeToSpy.skin);
        inDraftingPhase.setPosition(15, 15);

        layoutTable = new Table();
        layoutTable.setWidth(stage.getWidth());
        layoutTable.align(Align.top | Align.center);
        layoutTable.setPosition(0, Gdx.graphics.getHeight());
        layoutTable.padTop(300);
        layoutTable.add(offeredItemButton1).minSize(200, 50);
        layoutTable.add(offeredImage1);
        layoutTable.row();
        layoutTable.add(offeredItemButton2).minSize(200, 50);
        layoutTable.add(offeredImage2);
        layoutTable.row();
        layoutTable.add(offeredItemButton3).minSize(200, 50);
        layoutTable.add(offeredImage3);
        layoutTable.row();
        layoutTable.add(offeredItemButton4).minSize(200, 50);
        layoutTable.add(offeredImage4);
        layoutTable.row();
        layoutTable.add(offeredItemButton5).minSize(200, 50);
        layoutTable.add(offeredImage5);
        layoutTable.row();
        layoutTable.add(offeredItemButton6).minSize(200, 50);
        layoutTable.add(offeredImage6);
        layoutTable.row();

        gameLeaveButton = new TextButton("Leave Game", NoTimeToSpy.skin);
        gameLeaveButton.addListener(gameLeaveListener);
        gameLeaveButton.setPosition(850, 670);

        stage.addActor(gameLeaveButton);
        stage.addActor(layoutTable);
        stage.addActor(versionLabel);
        stage.addActor(inDraftingPhase);

        draftingHandler.draftingInitialized = true;

        Gdx.input.setInputProcessor(stage);

    }

    /**
     * Draws the screen every frame. Checks if Dialogs are to be shown because of game cause.
     *
     * @param delta time passed.
     *
     * @author Sedat Qaja
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        NoTimeToSpy.spriteBatch.begin();
        NoTimeToSpy.spriteBatch.draw(NoTimeToSpy.nttsPicture, 0, 0, 1280, 720);
        NoTimeToSpy.spriteBatch.end();

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
     * Fills the auxiliary object array with all values.
     *
     * @author Sedat Qaja
     */
    public void fillingObjectArray() {

        draftingValues = new Object[6];

        int runningInt = 0;

        for (UUID listValues : draftingHandler.offeredCharacterIds) {
            draftingValues[runningInt] = listValues;
            runningInt++;
        }

        for (GadgetEnum gadgetEnum : draftingHandler.offeredGadgets) {
            draftingValues[runningInt] = gadgetEnum;
            runningInt++;
        }

        showChoices();
    }

    /**
     * Puts the according labels on the buttons.
     *
     * @author Sedat Qaja
     */
    private void showChoices() {
        offeredItemButton1.setText(draftingValues[0] instanceof UUID ? gameHandler.characterMap.get(draftingValues[0]).getName() : gameHandler.gadgetMap.get(draftingValues[0]));
        offeredItemButton2.setText(draftingValues[1] instanceof UUID ? gameHandler.characterMap.get(draftingValues[1]).getName() : gameHandler.gadgetMap.get(draftingValues[1]));
        offeredItemButton3.setText(draftingValues[2] instanceof UUID ? gameHandler.characterMap.get(draftingValues[2]).getName() : gameHandler.gadgetMap.get(draftingValues[2]));
        offeredItemButton4.setText(draftingValues[3] instanceof UUID ? gameHandler.characterMap.get(draftingValues[3]).getName() : gameHandler.gadgetMap.get(draftingValues[3]));
        offeredItemButton5.setText(draftingValues[4] instanceof UUID ? gameHandler.characterMap.get(draftingValues[4]).getName() : gameHandler.gadgetMap.get(draftingValues[4]));
        offeredItemButton6.setText(draftingValues[5] instanceof UUID ? gameHandler.characterMap.get(draftingValues[5]).getName() : gameHandler.gadgetMap.get(draftingValues[5]));

        offeredImage1.setDrawable(draftingValues[0] instanceof UUID ? gameHandler.characterToTextureMap.get(draftingValues[0]) : gameHandler.gadgetEnumTextureHashMap.get(draftingValues[0]));
        offeredImage2.setDrawable(draftingValues[1] instanceof UUID ? gameHandler.characterToTextureMap.get(draftingValues[1]) : gameHandler.gadgetEnumTextureHashMap.get(draftingValues[1]));
        offeredImage3.setDrawable(draftingValues[2] instanceof UUID ? gameHandler.characterToTextureMap.get(draftingValues[2]) : gameHandler.gadgetEnumTextureHashMap.get(draftingValues[2]));
        offeredImage4.setDrawable(draftingValues[3] instanceof UUID ? gameHandler.characterToTextureMap.get(draftingValues[3]) : gameHandler.gadgetEnumTextureHashMap.get(draftingValues[3]));
        offeredImage5.setDrawable(draftingValues[4] instanceof UUID ? gameHandler.characterToTextureMap.get(draftingValues[4]) : gameHandler.gadgetEnumTextureHashMap.get(draftingValues[4]));
        offeredImage6.setDrawable(draftingValues[5] instanceof UUID ? gameHandler.characterToTextureMap.get(draftingValues[5]) : gameHandler.gadgetEnumTextureHashMap.get(draftingValues[5]));
    }

    /**
     * Auxiliary method to decide if gadget or character is to send.
     *
     * @param instance Value for the draftingValues array.
     *
     * @author Sedat Qaja
     */
    private void sendItemChoice(int instance) {
        if (draftingHandler.draftingInitialized && draftingValues[instance] instanceof GadgetEnum) {
            disableAllButtons();
            draftingHandler.sendItemChoice(null, (GadgetEnum) draftingValues[instance]);
        } else if (draftingHandler.draftingInitialized && draftingValues[instance] instanceof UUID) {
            disableAllButtons();
            draftingHandler.sendItemChoice((UUID) draftingValues[instance], null);
        }
    }

    /**
     * Auxiliary method to disable all Buttons.
     *
     * @author Sedat Qaja
     */
    public void disableAllButtons() {
        offeredItemButton1.setTouchable(Touchable.disabled);
        offeredItemButton2.setTouchable(Touchable.disabled);
        offeredItemButton3.setTouchable(Touchable.disabled);
        offeredItemButton4.setTouchable(Touchable.disabled);
        offeredItemButton5.setTouchable(Touchable.disabled);
        offeredItemButton6.setTouchable(Touchable.disabled);
    }

    /**
     * Auxiliary method to enable all Buttons.
     *
     * @author Sedat Qaja
     */
    public void enableAllButtons() {
        offeredItemButton1.setTouchable(Touchable.enabled);
        offeredItemButton2.setTouchable(Touchable.enabled);
        offeredItemButton3.setTouchable(Touchable.enabled);
        offeredItemButton4.setTouchable(Touchable.enabled);
        offeredItemButton5.setTouchable(Touchable.enabled);
        offeredItemButton6.setTouchable(Touchable.enabled);
    }

    /**
     * Default Constructor.
     *
     * @author Sedat Qaja
     */
    private void setDefaultValues() {
        draftingValues = new Object[6];
    }
}
