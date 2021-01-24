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
import com.sopra.ntts.controller.GameHandler;
import com.sopra.ntts.controller.MusicPlayer;
import com.sopra.ntts.controller.NoTimeToSpy;
import com.sopra.ntts.controller.SelectionHandler;
import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.List;

/**
 * Selection phase screen is used to draw the selection elements.
 *
 * @author Sedat Qaja
 */
public class SelectionPhaseScreen implements Screen {

    private final Logger logger = LoggerFactory.getLogger(LoginScreen.class);

    private NoTimeToSpy parent;
    private SelectionHandler selectionHandler;
    private GameHandler gameHandler;

    private OrthographicCamera camera;
    private StretchViewport viewport;
    private Stage stage;

    private Table layoutTable;

    private Label versionLabel;
    private Label inEquipmentPhase;

    private List<Label> chosenGadgetLabel;
    private List<SelectBox<String>> forGadgetSelectBox;

    private InputListener submitListener;
    private TextButton submit;

    private TextButton gameLeaveButton;
    private InputListener gameLeaveListener;

    private Boolean dialogOpen = false;
    private MusicPlayer musicPlayer;

    /**
     * Constructor for the draft screen. Necessary attributes are initialized.
     *
     * @param parent           The basic game is linked to the screen.
     * @param selectionHandler The draftingHandler is needed because its the framework of the selection phase.
     * @param gameHandler      The gameHandler is needed because its the framework of the actual game.
     * @author Sedat Qaja
     */
    public SelectionPhaseScreen(NoTimeToSpy parent, final SelectionHandler selectionHandler, final GameHandler gameHandler) {
        this.parent = parent;
        this.selectionHandler = selectionHandler;
        this.gameHandler = gameHandler;
        this.musicPlayer = new MusicPlayer();
        setDefaultValues();

        // initiates the listeners for all interactive elements
        submitListener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                musicPlayer.playSound(musicPlayer.button_sound);
                setEquipmentMap();
                sendEquipmentChoice();
                submit.setTouchable(Touchable.disabled);
                selectionHandler.clear();
            }
        };

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

        versionLabel = new Label("Version " + NoTimeToSpy.versionNumber, NoTimeToSpy.skin);
        versionLabel.setPosition(1100, 680);

        inEquipmentPhase = new Label("You are in the EquipmentPhase. Assign gadgets to your Characters.", NoTimeToSpy.skin);
        inEquipmentPhase.setPosition(15, 15);

        submit = new TextButton("SUBMIT", NoTimeToSpy.skin);
        submit.addListener(submitListener);

        chosenGadgetLabel = new LinkedList<>();
        forGadgetSelectBox = new LinkedList<>();

        for (int i = 0; i < selectionHandler.chosenGadgets.size(); i++) {
            chosenGadgetLabel.add(new Label("", NoTimeToSpy.skin));
            forGadgetSelectBox.add(new SelectBox<String>(NoTimeToSpy.skin));
        }

        layoutTable = new Table();
        layoutTable.setWidth(stage.getWidth());
        layoutTable.align(Align.top | Align.center);
        layoutTable.setPosition(0, Gdx.graphics.getHeight());
        layoutTable.padTop(300);

        for (int i = 0; i < selectionHandler.chosenGadgets.size(); i++) {
            layoutTable.add(chosenGadgetLabel.get(i)).minSize(400, 50);
            layoutTable.add(forGadgetSelectBox.get(i)).minSize(200, 50);
            if (i < selectionHandler.chosenGadgets.size() - 1) {
                layoutTable.row();
            }
        }
        layoutTable.add(submit);

        setLabelAndSelectionBox();

        gameLeaveButton = new TextButton("Leave Game", NoTimeToSpy.skin);
        gameLeaveButton.addListener(gameLeaveListener);
        gameLeaveButton.setPosition(850, 670);

        stage.addActor(gameLeaveButton);
        stage.addActor(layoutTable);
        stage.addActor(versionLabel);
        stage.addActor(inEquipmentPhase);

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
     * Setter for the labels and selectBoxes.
     *
     * @author Sedat Qaja
     */
    public void setLabelAndSelectionBox() {
        LinkedList<String> charNames = new LinkedList<>();

        //sets array for selectBoxes
        for (UUID uuid : selectionHandler.chosenCharacterIds) {
            selectionHandler.nameUuidMap.put(gameHandler.characterMap.get(uuid).getName(), uuid);
            charNames.add(gameHandler.characterMap.get(uuid).getName());
        }

        for (int i = 0; i < selectionHandler.chosenGadgets.size(); i++) {
            chosenGadgetLabel.get(i).setText(selectionHandler.chosenGadgets.get(i).toString());
            String[] items = new String[charNames.size()];
            for (int j = 0; j < charNames.size(); j++) {
                items[j] = charNames.get(j);
            }
            forGadgetSelectBox.get(i).setItems(items);
        }
    }

    /**
     * Auxiliary method to set the equipment map in the selectionHandler.
     *
     * @author Sedat Qaja
     */
    private void setEquipmentMap() {
        for (int i = 0; i < selectionHandler.chosenGadgets.size(); i++) {
            selectionHandler.equipment.get(selectionHandler.nameUuidMap.get(forGadgetSelectBox.get(i).getSelected())).add(selectionHandler.chosenGadgets.get(i));
        }
    }

    /**
     * Sends the chosen equipment.
     *
     * @author Sedat Qaja
     */
    private void sendEquipmentChoice() {
        selectionHandler.messageEmitter.sendEquipmentChoiceMessage(selectionHandler.equipment);
    }

    /**
     * Auxiliary printer for the send map.
     *
     * @author Sedat Qaja
     */
    private void printMap() {

        for (Map.Entry<UUID, Set<GadgetEnum>> entry : selectionHandler.equipment.entrySet())
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.toString());
    }

    /**
     * Default Constructor.
     *
     * @author Sedat Qaja
     */
    private void setDefaultValues() {

        chosenGadgetLabel = new LinkedList<>();
        forGadgetSelectBox = new LinkedList<>();
    }
}
