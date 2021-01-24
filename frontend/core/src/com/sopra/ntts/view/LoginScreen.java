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
import com.sopra.ntts.model.NameGenerator;
import com.sopra.ntts.model.NetworkStandard.DataTypes.RoleEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.concurrent.TimeUnit;

/**
 * Login screen is used to draw the login elements.
 *
 * @author Sedat Qaja
 */
public class LoginScreen implements Screen {

    private final Logger logger = LoggerFactory.getLogger(LoginScreen.class);

    private NoTimeToSpy parent;

    private ConnectionHandler connectionHandler;
    private MessageReceiver messageReceiver;
    private GameHandler gameHandler;
    private MessageEmitter messageEmitter;

    OrthographicCamera camera = new OrthographicCamera(1024, 576);
    StretchViewport viewport = new StretchViewport(1024, 576, camera);
    Stage stage = new Stage(viewport);

    private Table layoutTable;

    private Button connectButton;
    private Button endGameButton;

    private TextField playerNameTextField;
    private TextField serverNameTextField;
    private TextField serverPortTextField;
    private SelectBox<RoleEnum> roleEnumSelection;

    private Label versionLabel;
    private Label inQueueLabel;
    private Label spectatorLabel;
    private Label userNameLabel;
    private Label userRoleLabel;
    private Label serverNameLabel;
    private Label serverPortLabel;

    private InputListener connectListener;
    private InputListener endGameListener;


    //creating a Musicplayer
    public static MusicPlayer musicPlayer = new MusicPlayer();

    /**
     * Constructor for the login screen. Necessary attributes are initialized.
     *
     * @param parent            The basic game is linked to the screen.
     * @param connectionHandler The connectionHandler is needed to send or evaluate connection.
     * @param messageReceiver   The messageReceiver is needed to get the messages from the server
     * @param gameHandler       The gameHandler is needed because its the framework of the actual game.
     * @param messageEmitter    The messageEmitter is needed to send messages.
     *
     * @author Sedat Qaja
     */
    public LoginScreen(final NoTimeToSpy parent, final ConnectionHandler connectionHandler, final MessageReceiver messageReceiver, final GameHandler gameHandler, final MessageEmitter messageEmitter) {

        this.parent = parent;
        this.connectionHandler = connectionHandler;
        this.messageReceiver = messageReceiver;
        this.gameHandler = gameHandler;
        this.messageEmitter = messageEmitter;

        // Camera, Viewport und Stage erzeugen
        camera = new OrthographicCamera(1280, 720);
        viewport = new StretchViewport(1280, 720, camera);
        stage = new Stage(viewport);

        // initiates the listeners for all interactive elements
        connectListener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);


                musicPlayer.playSound(musicPlayer.button_sound);


                boolean successfullyConnected = false;


                connectClient:
                try {

                    SimpleClient simpleClient = new SimpleClient(new URI("ws://" + serverNameTextField.getText() + ":" + serverPortTextField.getText()), connectionHandler, messageReceiver);

                    successfullyConnected = simpleClient.connectBlocking(10, TimeUnit.SECONDS);

                    if (!successfullyConnected) {
                        break connectClient;
                    }

                    connectionHandler.setSimpleClient(simpleClient);

                    messageEmitter.sendHelloMessage(playerNameTextField.getText(), roleEnumSelection.getSelected());

                    // show the label
                    inQueueLabel.setVisible(true);

                    if (roleEnumSelection.getSelected().equals(RoleEnum.SPECTATOR)) {
                        spectatorLabel.setVisible(true);
                    }

                    // disable buttons and selectList
                    connectButton.setTouchable(Touchable.disabled);
                    playerNameTextField.setTouchable(Touchable.disabled);
                    serverNameTextField.setTouchable(Touchable.disabled);
                    serverPortTextField.setTouchable(Touchable.disabled);
                    roleEnumSelection.setTouchable(Touchable.disabled);

                    successfullyConnected = true;

                } catch (InterruptedException | URISyntaxException e) {

                    e.printStackTrace();
                } finally {

                    if (!successfullyConnected) {

                        // reset touchable
                        spectatorLabel.setVisible(false);
                        inQueueLabel.setVisible(false);
                        connectButton.setTouchable(Touchable.enabled);
                        playerNameTextField.setTouchable(Touchable.enabled);
                        serverNameTextField.setTouchable(Touchable.enabled);
                        serverPortTextField.setTouchable(Touchable.enabled);
                        roleEnumSelection.setTouchable(Touchable.enabled);
                    }
                }

            }
        };

        endGameListener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                musicPlayer.disposeAudio();
                connectionHandler.closeConnection();
                Gdx.app.exit();
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
        musicPlayer.playMusic(musicPlayer.menu_music, true);
        // Camera, Viewport und Stage erzeugen
        //camera = new OrthographicCamera(1280, 720);
        //viewport = new StretchViewport(1280, 720, camera);
        //stage = new Stage(viewport);

        playerNameTextField = new TextField(NameGenerator.generateName(), NoTimeToSpy.skin);

        userNameLabel = new Label("Enter Name:", NoTimeToSpy.skin);
        userRoleLabel = new Label("Choose Role:", NoTimeToSpy.skin);

        try {
            serverNameTextField = new TextField(Inet4Address.getLocalHost().getHostAddress(), NoTimeToSpy.skin);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        serverPortTextField = new TextField("7007", NoTimeToSpy.skin);

        serverNameLabel = new Label("Enter ServerId:", NoTimeToSpy.skin);
        serverPortLabel = new Label("Enter ServerPort:", NoTimeToSpy.skin);

        roleEnumSelection = new SelectBox<>(NoTimeToSpy.skin);
        roleEnumSelection.setItems(RoleEnum.PLAYER, RoleEnum.SPECTATOR);

        connectButton = new TextButton("Connect to Server", NoTimeToSpy.skin);
        connectButton.addListener(connectListener);

        endGameButton = new TextButton("End the Game", NoTimeToSpy.skin);
        endGameButton.addListener(endGameListener);

        versionLabel = new Label("Version " + NoTimeToSpy.versionNumber, NoTimeToSpy.skin);
        versionLabel.setPosition(1100, 680);

        inQueueLabel = new Label("You are in the Queue for a Game.", NoTimeToSpy.skin);
        inQueueLabel.setPosition(15, 15);
        inQueueLabel.setVisible(false);

        spectatorLabel = new Label("Waiting for player to choose Characters and Equipment.", NoTimeToSpy.skin);
        spectatorLabel.setPosition(15, 45);
        spectatorLabel.setVisible(false);

        layoutTable = new Table();
        layoutTable.setWidth(stage.getWidth());
        layoutTable.align(Align.top | Align.center);
        layoutTable.setPosition(0, Gdx.graphics.getHeight());
        layoutTable.padTop(300);
        layoutTable.add(userNameLabel).minSize(200, 50);
        layoutTable.add(playerNameTextField).minSize(400, 50);
        layoutTable.row();
        layoutTable.add(serverNameLabel).minSize(200, 50);
        layoutTable.add(serverNameTextField).minSize(400, 50);
        layoutTable.row();
        layoutTable.add(serverPortLabel).minSize(200, 50);
        layoutTable.add(serverPortTextField).minSize(400, 50);
        layoutTable.row();
        layoutTable.add(userRoleLabel).minSize(200, 50);
        layoutTable.add(roleEnumSelection).minSize(100, 50);
        layoutTable.row();
        layoutTable.add(connectButton).minSize(200, 50);
        layoutTable.add(endGameButton).minSize(200, 50).colspan(2);


        stage.addActor(layoutTable);
        stage.addActor(versionLabel);
        stage.addActor(inQueueLabel);

        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Draws the screen every frame.
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
}
