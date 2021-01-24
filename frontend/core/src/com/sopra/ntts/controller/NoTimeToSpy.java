package com.sopra.ntts.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.sopra.ntts.view.DraftingPhaseScreen;
import com.sopra.ntts.view.GameScreen;
import com.sopra.ntts.view.LoginScreen;
import com.sopra.ntts.view.SelectionPhaseScreen;

/**
 * Basic game structure. Handles the screens and is a parent to the other classes.
 *
 * @author Sedat Qaja
 */
public class NoTimeToSpy extends Game {

    /**
     * Version number for the Client.
     */
    public final static String versionNumber = "0.1.6";

    /**
     * Handler for the Screens and Handler for the Messages.
     */
    private MessageReceiver messageReceiver;
    private ConnectionHandler connectionHandler;
    private GameHandler gameHandler;
    private DraftingHandler draftingHandler;
    private SelectionHandler selectionHandler;
    public MessageEmitter messageEmitter;

    /**
     * Screens for the game.
     */
    private LoginScreen loginScreen;
    private GameScreen gameScreen;
    private DraftingPhaseScreen draftingPhaseScreen;
    private SelectionPhaseScreen selectionPhaseScreen;

    /**
     * Ressources for the Screens.
     */
    public static Texture nttsPicture;
    public static SpriteBatch spriteBatch;
    public static Skin skin;

    public static Texture barSeat, barSeatSelected, barSeatSelectedFriend, barTable, barTableCocktail, barTableCocktailSelected, barTableSelected, fireplace, fireplaceSelected, free;
    public static Texture freeSelected, freeSelectedFriend, roulette, rouletteSelected, safe, safeSelected, safeOpened, safeOpenedSelected, wall, wallSelected, foggy, foggySelected;
    public static Texture barSeatSelectedFoe, freeSelectedFoe;

    public static Texture bowlerBlade, chickenfeed, diamondCollar, fogTin, gasGloss, grapple, hairdryer, jetPack, laserCompact, magneticWatch, mirrorOfWilderness;
    public static Texture moleDie, mothPouch, nugget, pocketLitter, poisonPills, rocketPen, technicolorPrism, wireTapsWithEarPlug, mask;

    public static Texture male, female, diverse, cat, keys;

    /**
     * Method to set the Screen.
     */
    public void showLoginScreen() {
        this.setScreen(loginScreen);
    }

    /**
     * Method to set the Screen.
     */
    public void showGameScreen() {
        this.setScreen(gameScreen);
    }

    /**
     * Method to set the Screen.
     */
    public void showDraftingPhaseScreen() {
        this.setScreen(draftingPhaseScreen);
    }

    /**
     * Method to set the Screen.
     */
    public void showSelectionPhaseScreen() {
        this.setScreen(selectionPhaseScreen);
    }

    @Override
    public void create() {
        //Window title
        Gdx.graphics.setTitle("No Time To Spy Simple Client");

        spriteBatch = new SpriteBatch();

        initiateTextures();

        connectionHandler = new ConnectionHandler();
        messageReceiver = new MessageReceiver();
        messageEmitter = new MessageEmitter();
        gameHandler = new GameHandler();
        draftingHandler = new DraftingHandler();
        selectionHandler = new SelectionHandler();

        gameHandler.setMessageEmitter(messageEmitter);
        draftingHandler.setMessageEmitter(messageEmitter);
        selectionHandler.setMessageEmitter(messageEmitter);

        messageEmitter.setConnectionHandler(connectionHandler);

        loginScreen = new LoginScreen(this, connectionHandler, messageReceiver, gameHandler, messageEmitter);
        draftingPhaseScreen = new DraftingPhaseScreen(this, connectionHandler, messageReceiver, draftingHandler, gameHandler, messageEmitter);
        selectionPhaseScreen = new SelectionPhaseScreen(this, selectionHandler, gameHandler);
        gameScreen = new GameScreen(this, gameHandler);

        connectionHandler.setParent(this);

        messageReceiver.setParent(this);
        messageReceiver.setConnectionHandler(connectionHandler);
        messageReceiver.setGameHandler(gameHandler);
        messageReceiver.setDraftingHandler(draftingHandler);
        messageReceiver.setSelectionHandler(selectionHandler);

        messageReceiver.setGameScreen(gameScreen);
        messageReceiver.setDraftingPhaseScreen(draftingPhaseScreen);
        messageReceiver.setSelectionPhaseScreen(selectionPhaseScreen);

        this.showLoginScreen();
    }

    /**
     * Method from Libgdx.
     */
    @Override
    public void render() {
        super.render();
    }

    /**
     * Method from Libgdx.
     */
    @Override
    public void dispose() {
        spriteBatch.dispose();
        nttsPicture.dispose();
        skin.dispose();

        //gadgets
        nttsPicture.dispose();
        cat.dispose();
        keys.dispose();
        bowlerBlade.dispose();
        chickenfeed.dispose();
        diamondCollar.dispose();
        fogTin.dispose();
        gasGloss.dispose();
        grapple.dispose();
        hairdryer.dispose();
        jetPack.dispose();
        laserCompact.dispose();
        magneticWatch.dispose();
        mirrorOfWilderness.dispose();
        moleDie.dispose();
        mothPouch.dispose();
        nugget.dispose();
        pocketLitter.dispose();
        poisonPills.dispose();
        rocketPen.dispose();
        technicolorPrism.dispose();
        wireTapsWithEarPlug.dispose();
        mask.dispose();

        //tiles
        barSeat.dispose();
        barSeatSelected.dispose();
        barSeatSelectedFriend.dispose();
        barTable.dispose();
        barTableSelected.dispose();
        barTableCocktail.dispose();
        barTableCocktailSelected.dispose();
        fireplace.dispose();
        fireplaceSelected.dispose();
        free.dispose();
        freeSelected.dispose();
        freeSelectedFriend.dispose();
        roulette.dispose();
        rouletteSelected.dispose();
        safe.dispose();
        safeSelected.dispose();
        safeOpened.dispose();
        safeOpenedSelected.dispose();
        wall.dispose();
        wallSelected.dispose();
        foggy.dispose();
        foggySelected.dispose();
        barSeatSelectedFoe.dispose();
        freeSelectedFoe.dispose();

        male.dispose();
        female.dispose();
        diverse.dispose();
    }

    /**
     * Auxiliary method. Could be in create method.
     */
    private void initiateTextures() {

        skin = new Skin(Gdx.files.internal("plain-james/skin/plain-james-ui.json"));

        //gadgets
        nttsPicture = new Texture("notimetospy.png");
        cat = new Texture(Gdx.files.internal("character/png/cat.png"));
        keys = new Texture(Gdx.files.internal("Keys.png"));
        bowlerBlade = new Texture(Gdx.files.internal("gadgets/bowlerblade.png"));
        chickenfeed = new Texture(Gdx.files.internal("gadgets/chickenfeed.png"));
        diamondCollar = new Texture(Gdx.files.internal("gadgets/diamondcollar.png"));
        fogTin = new Texture(Gdx.files.internal("gadgets/fogtin.png"));
        gasGloss = new Texture(Gdx.files.internal("gadgets/gasgloss.png"));
        grapple = new Texture(Gdx.files.internal("gadgets/grapple.png"));
        hairdryer = new Texture(Gdx.files.internal("gadgets/hairdryer.png"));
        jetPack = new Texture(Gdx.files.internal("gadgets/jetpack.png"));
        laserCompact = new Texture(Gdx.files.internal("gadgets/lasercompact.png"));
        magneticWatch = new Texture(Gdx.files.internal("gadgets/magneticwatch.png"));
        mirrorOfWilderness = new Texture(Gdx.files.internal("gadgets/mirrorofwilderness.png"));
        moleDie = new Texture(Gdx.files.internal("gadgets/moledie.png"));
        mothPouch = new Texture(Gdx.files.internal("gadgets/mothpouch.png"));
        nugget = new Texture(Gdx.files.internal("gadgets/nugget.png"));
        pocketLitter = new Texture(Gdx.files.internal("gadgets/pocketlitter.png"));
        poisonPills = new Texture(Gdx.files.internal("gadgets/poisonpills.png"));
        rocketPen = new Texture(Gdx.files.internal("gadgets/rocketpen.png"));
        technicolorPrism = new Texture(Gdx.files.internal("gadgets/technicolorprism.png"));
        wireTapsWithEarPlug = new Texture(Gdx.files.internal("gadgets/wiretapswithearplug.png"));
        mask = new Texture(Gdx.files.internal("gadgets/mask.png"));

        //tiles
        barSeat = new Texture("tiles/png/barseat.png");
        barSeatSelected = new Texture("tiles/png/barseat-selected.png");
        barSeatSelectedFriend = new Texture("tiles/png/barseat-selected - friend.png");
        barTable = new Texture("tiles/png/bartable.png");
        barTableSelected = new Texture("tiles/png/bartable-selected.png");
        barTableCocktail = new Texture("tiles/png/bartable-cocktail.png");
        barTableCocktailSelected = new Texture("tiles/png/bartable-cocktail-selected.png");
        fireplace = new Texture("tiles/png/fireplace.png");
        fireplaceSelected = new Texture("tiles/png/fireplace-selected.png");
        free = new Texture("tiles/png/free.png");
        freeSelected = new Texture("tiles/png/free-selected.png");
        freeSelectedFriend = new Texture("tiles/png/free-selected - friend.png");
        roulette = new Texture("tiles/png/roulette.png");
        rouletteSelected = new Texture("tiles/png/roulette-selected.png");
        safe = new Texture("tiles/png/safe.png");
        safeSelected = new Texture("tiles/png/safe-selected.png");
        safeOpened = new Texture("tiles/png/safe-opened.png");
        safeOpenedSelected = new Texture("tiles/png/safe-opened-selected.png");
        wall = new Texture("tiles/png/wall.png");
        wallSelected = new Texture("tiles/png/wall-selected.png");
        foggy = new Texture("tiles/png/foggy.png");
        foggySelected = new Texture("tiles/png/foggy-selected.png");
        barSeatSelectedFoe = new Texture("tiles/png/barseat-selected - foe.png");
        freeSelectedFoe = new Texture("tiles/png/free-selected - foe.png");

        male = new Texture(Gdx.files.internal("character/png/agent.png"));
        female = new Texture(Gdx.files.internal("character/png/girl_idle.png"));
        diverse = new Texture(Gdx.files.internal("character/png/corona-bond.png"));
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public DraftingHandler getDraftingHandler() {
        return draftingHandler;
    }

    public SelectionHandler getSelectionHandler() {
        return selectionHandler;
    }
}
