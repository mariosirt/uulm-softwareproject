package util.logic.ingame;

import NetworkStandard.Characters.Character;
import NetworkStandard.DataTypes.GadgetEnum;
import NetworkStandard.DataTypes.Gadgets.*;
import NetworkStandard.DataTypes.Operations.*;
import NetworkStandard.DataTypes.PropertyEnum;
import NetworkStandard.DataTypes.Szenario.Field;
import NetworkStandard.DataTypes.Szenario.FieldMap;
import NetworkStandard.DataTypes.Szenario.FieldStateEnum;
import NetworkStandard.DataTypes.Szenario.State;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;
import NetworkStandard.Messages.ErrorMessage;
import NetworkStandard.Messages.GameStatusMessage;
import NetworkStandard.Messages.RequestGameOperationMessage;
import NetworkStandard.Messages.StatisticsMessage;
import util.Game;
import util.User;

import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.*;

/**
 * this class handles most of the logic that is needed in a game, especially the turn mechanics
 *
 * @author Christian Wendlinger
 */

public class GameHandler {
    private Game game;

    private int characterToMoveIndex = 0;
    private Character currentCharacter = null;

    private OperationTimer timer;
    private ReconnectTimer reconnectTimer;

    private int secondsPassed = 0;
    private Character currentMoledieHolder;
    private List<PropertyEnum> currentMoledieHolderProperties;

    private VictoryEnum victoryReason;
    private UUID victoryUUID;

    private int wireTapReferenceNumber;

    private int foggyCounter;

    /**
     * constructor
     */
    public GameHandler(Game game) {
        this.game = game;
        this.currentMoledieHolderProperties = new LinkedList<>();
        this.timer = new OperationTimer(game);
        this.foggyCounter = 0;
    }

    /**
     * getter and setter
     */
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Character getCurrentCharacter() {
        return currentCharacter;
    }

    public void setCurrentCharacter(Character currentCharacter) {
        this.currentCharacter = currentCharacter;
    }

    public OperationTimer getTimer() {
        return timer;
    }

    public void setTimer(OperationTimer timer) {
        this.timer = timer;
    }

    public ReconnectTimer getReconnectTimer() {
        return reconnectTimer;
    }

    public void setReconnectTimer(ReconnectTimer reconnectTimer) {
        this.reconnectTimer = reconnectTimer;
    }

    public int getSecondsPassed() {
        return secondsPassed;
    }

    public void setSecondsPassed(int secondsPassed) {
        this.secondsPassed = secondsPassed;
    }

    public VictoryEnum getVictoryReason() {
        return victoryReason;
    }

    public void setVictoryReason(VictoryEnum victoryReason) {
        this.victoryReason = victoryReason;
    }

    public UUID getVictoryUUID() {
        return victoryUUID;
    }

    public void setVictoryUUID(UUID victoryUUID) {
        this.victoryUUID = victoryUUID;
    }

    /**
     * equals and hashCode
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameHandler that = (GameHandler) o;
        return characterToMoveIndex == that.characterToMoveIndex &&
                getSecondsPassed() == that.getSecondsPassed() &&
                wireTapReferenceNumber == that.wireTapReferenceNumber &&
                foggyCounter == that.foggyCounter &&
                Objects.equals(getGame(), that.getGame()) &&
                Objects.equals(getCurrentCharacter(), that.getCurrentCharacter()) &&
                Objects.equals(getTimer(), that.getTimer()) &&
                Objects.equals(getReconnectTimer(), that.getReconnectTimer()) &&
                Objects.equals(currentMoledieHolder, that.currentMoledieHolder) &&
                Objects.equals(currentMoledieHolderProperties, that.currentMoledieHolderProperties) &&
                getVictoryReason() == that.getVictoryReason() &&
                Objects.equals(getVictoryUUID(), that.getVictoryUUID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGame(), characterToMoveIndex, getCurrentCharacter(), getTimer(), getReconnectTimer(), getSecondsPassed(), currentMoledieHolder, currentMoledieHolderProperties, getVictoryReason(), getVictoryUUID(), wireTapReferenceNumber, foggyCounter);
    }

    /**
     * toString
     */
    @Override
    public String toString() {
        return "GameHandler{" +
                "game=" + game +
                ", characterToMoveIndex=" + characterToMoveIndex +
                ", currentCharacter=" + currentCharacter +
                ", timer=" + timer +
                ", reconnectTimer=" + reconnectTimer +
                ", secondsPassed=" + secondsPassed +
                ", currentMoledieHolder=" + currentMoledieHolder +
                ", currentMoledieHolderProperties=" + currentMoledieHolderProperties +
                ", victoryReason=" + victoryReason +
                ", victoryUUID=" + victoryUUID +
                ", wireTapReferenceNumber=" + wireTapReferenceNumber +
                ", foggyCounter=" + foggyCounter +
                '}';
    }

    /**
     * Generate the map, place Characters, place the cat, index the safes, give secrets to npcs
     */
    public void startGame() {
        if (game.ready[0] && game.ready[1]) {
            // set gameStart
            game.setGameStart(Date.from(Instant.now()));

            // create the first state where also the map is generated
            game.setState(new State(0, generateFieldMap(), null, new HashSet<>(game.getAllCharacters()), new Point(-1, -1), new Point(-1, -1)));

            // place all characters
            game.getAllCharacters().forEach(character -> character.setCoordinates(findCoordinates()));

            // place the cat
            game.getState().setCatCoordinates(findCoordinates());

            //initialize action Handler
            game.setActionHandler(new ActionHandler(game.getState().getMap(), game));

            wireTapReferenceNumber = 0;

            for (Gadget gadget : game.getAllGadgets()) { // use findCoordinates to randomly spawn the leftover gadgets
                Point coordinates = findCoordinates();
                game.getState().getMap().getField(coordinates.x, coordinates.y).setGadget(gadget);
            }

            // start actual game
            runde();
        }
    }

    /**
     * Generate the FieldMap containing Fields from FieldStateEnum in Scenario.
     *
     * @return generated Map
     */
    private FieldMap generateFieldMap() {
        FieldMap result = new FieldMap(new Field[game.getLevel().getScenario().length][]);
        int safeIndex = 1;
        int safeCounter = 0;
        List<Point> tresoreIndex = new LinkedList<>();

        for (int i = 0; i < result.getMap().length; i++) {
            result.getMap()[i] = new Field[game.getLevel().getScenario()[i].length];
            for (int j = 0; j < result.getMap()[i].length; j++) {
                result.getMap()[i][j] = new Field(game.getLevel().getScenario()[i][j], null, false, false, 0, 0, false, false);

                // Roulette Tische mit Chips befüllen
                if (result.getMap()[i][j].getState() == FieldStateEnum.ROULETTE_TABLE) {
                    result.getMap()[i][j].setChipAmount(game.getSettings().getMaxChipsRoulette());
                }

                // Tresore zwischenspeichern
                if (result.getMap()[i][j].getState() == FieldStateEnum.SAFE) {
                    tresoreIndex.add(new Point(i, j));
                    // safelist filling
                    game.getSafeCountIntoList().add(safeCounter + 1);
                    safeCounter++;
                }
            }
        }

        // Tresore zufällig durchnummerieren
        Collections.shuffle(tresoreIndex);
        for (Point p : tresoreIndex) {
            result.getMap()[p.x][p.y].setSafeIndex(safeIndex++);

            // put Necklace in last safe
            if (safeIndex == safeCounter + 1) {
                result.getField(p.x, p.y).setGadget(new Gadget(GadgetEnum.DIAMOND_COLLAR));
            }
        }

        // amount of safes
        game.setSafeCount(safeCounter);

        return result;
    }


    /**
     * Find a random Field where a character can be placed.
     *
     * @return Point with coordinates where the character can be placed
     */
    private Point findCoordinates() {
        Random placer = new Random();

        int x = placer.nextInt(game.getState().getMap().getMap().length);
        int y = placer.nextInt(game.getState().getMap().getMap()[x].length);

        // find available field
        while (!isAvailable(x, y)) {
            x = placer.nextInt(game.getState().getMap().getMap().length);
            y = placer.nextInt(game.getState().getMap().getMap()[x].length);
        }

        return new Point(x, y);
    }

    /**
     * Check if a character can be placed on a field.
     *
     * @param x
     * @param y
     * @return true if the field is available, false else
     */
    private boolean isAvailable(int x, int y) {
        boolean available = true;

        // check if the field is walkable
        Field toPlace = game.getState().getMap().getMap()[x][y];
        if (!(toPlace.getState() == FieldStateEnum.FREE || toPlace.getState() == FieldStateEnum.BAR_SEAT)) {
            available = false;
        }

        // check if there is any other character standing on the field
        if (available) {
            for (Character character : game.getAllCharacters()) {
                if (character.getCoordinates().x == x && character.getCoordinates().y == y) {
                    available = false;
                    break;
                }
            }
        }

        return available;
    }

    /**
     * Round: generate new Cocktails, check funcionality of gadgets shuffle character sequence and handle each individual character-turn
     */
    private void runde() {
        if (game.isActive()) {
            characterToMoveIndex = 0;
            game.getState().setCurrentRound(game.getState().getCurrentRound() + 1);

            // Cocktails auffüllen - iterate through map
            for (int i = 0; i < game.getState().getMap().getMap().length; i++) {
                for (int j = 0; j < game.getState().getMap().getMap()[i].length; j++) {
                    // check for Bar table with no gadget on them - set gadget to new cocktail
                    if (game.getState().getMap().getMap()[i][j].getState() == FieldStateEnum.BAR_TABLE && game.getState().getMap().getMap()[i][j].getGadget() == null) {
                        game.getState().getMap().getMap()[i][j].setGadget(new Cocktail(GadgetEnum.COCKTAIL));

                    }
                }
            }

            // Wiretap with Earplugs prüfen
            wiretapCheck:
            for (Character earplugsCharacter : game.getAllCharacters()) {
                for (Gadget earplugs : earplugsCharacter.getGadgets()) {

                    if (earplugs != null && earplugs.getGadget() == GadgetEnum.WIRETAP_WITH_EARPLUGS && !((WiretapWithEarplugs) earplugs).isWiretap()) {

                        if (((WiretapWithEarplugs) earplugs).getWorking() && game.getActionHandler().isSuccesful((int) (game.getSettings().getWiretapWithEarplugsFailChance() * 100))) {
                            ((WiretapWithEarplugs) earplugs).setWorking(false);
                            earplugsCharacter.getGadgets().remove(earplugs);
                            earplugs.decrementUsages();
                            wireTapReferenceNumber = 0;
                            if (game.getActionHandler().getCharacterById(((WiretapWithEarplugs) earplugs).getActiveOn()) != null)
                                game.getActionHandler().getCharacterById(((WiretapWithEarplugs) earplugs).getActiveOn()).getGadgets().removeIf(gadget -> gadget.getGadget() == GadgetEnum.WIRETAP_WITH_EARPLUGS);
                            break wiretapCheck;
                        }
                    }
                }
            }

            //actualize fogCounters and check if one has to be remove
            for (int i = 0; i < game.getState().getMap().getMap().length; i++) {
                for (int j = 0; j < game.getState().getMap().getMap()[i].length; j++) {
                    if (game.getState().getMap().getField(i, j).isFoggy() && foggyCounter >= 2) {
                        game.getState().getMap().getField(i, j).setFoggy(false);
                    } else if (game.getState().getMap().getField(i, j).isFoggy()) {
                        foggyCounter++;
                    }
                }
            }

            // zufällige Zugreihenfolge festlegen - Liste zufällig würfeln
            Collections.shuffle(game.getAllCharacters());

            // Züge ausführen
            zug(characterToMoveIndex);
        }
    }


    /**
     * move the cat
     */
    private void moveCat() {
        List<Point> possibleMoves = new LinkedList<>();

        // iterate over neighbours and get possible moves
        for (int x = game.getState().getCatCoordinates().x - 1; x <= game.getState().getCatCoordinates().x + 1; x++) {
            for (int y = game.getState().getCatCoordinates().y - 1; y <= game.getState().getCatCoordinates().y + 1; y++) {

                // target field in bounds
                if (x >= 0 && y >= 0 && x < game.getState().getMap().getMap().length && y < game.getState().getMap().getMap()[x].length
                        // target field is moveable on
                        && (game.getState().getMap().getField(x, y).getState() == FieldStateEnum.FREE || game.getState().getMap().getField(x, y).getState() == FieldStateEnum.BAR_SEAT)
                        // not the same coordinates as already
                        && !(x == game.getState().getCatCoordinates().x && y == game.getState().getCatCoordinates().y)
                ) {
                    possibleMoves.add(new Point(x, y));
                }
            }
        }

        // actually move the cat
        if (!possibleMoves.isEmpty()) {
            Random random = new Random();
            Point target = possibleMoves.get(random.nextInt(possibleMoves.size()));

            Character character = characterOnField(target);

            Gadget gadget = game.getState().getMap().getField(target.x, target.y).getGadget();

            // look if there is the DIAMOND_COLLAR on the target Field
            if (gadget != null && gadget.getGadget() == GadgetEnum.DIAMOND_COLLAR) {
                setWinner(null);
                game.emitter.sendStatisticsMessage(new StatisticsMessage(null, MessageTypeEnum.STATISTICS, Date.from(Instant.now()),
                        "Cat picked up the Diamond Collar", Statistics.StatisticsEntry, game.getGameHandler().getVictoryUUID(),
                        game.getGameHandler().getVictoryReason(), false));

                endGame();
            }
            // look if there is a character on the target field
            else if (character != null) {
                character.setCoordinates(game.getState().getCatCoordinates());
            }
            game.getState().setCatCoordinates(target);
        }
    }

    /**
     * look if there is a character on a specific field
     *
     * @param field - target field
     * @return character if there one, null else
     */
    private Character characterOnField(Point field) {
        for (Character character : game.getAllCharacters()) {
            if (character.getCoordinates().equals(field)) {
                return character;
            }
        }
        return null;
    }

    /**
     * Auxiliary method for the janitor movement.
     *
     * @author Luca Uhilein & Sedat Qaja
     */
    private void RoundLimitRound() {
        // removes the npc and sets them to a point used to store removed characters
        Point point = new Point(-1, -1);
        game.getNpcs().forEach(character -> character.setCoordinates(point));
        game.getAllCharacters().removeAll(game.getNpcs());

        game.getState().setJanitorCoordinates(findCoordinates());
    }


    /**
     * Method to move the janitor.
     *
     * @author Luca Uhilein & Sedat Qaja
     */
    private void moveJanitor() {
        if (game.getSettings().getRoundLimit() == game.getState().getCurrentRound()) {
            RoundLimitRound();
        } else if (game.getSettings().getRoundLimit() < game.getState().getCurrentRound()) {
            Point point = new Point(-1, -1);
            Point targetField;

            Character targetChar = game.getActionHandler().getClosestCharacter(game.getState().getJanitorCoordinates());
            targetField = targetChar.getCoordinates();

            // check if target Character has diamond Collar
            for (Gadget gadget : targetChar.getGadgets()) {
                if (gadget.getGadget() == GadgetEnum.DIAMOND_COLLAR) {
                    game.getState().getMap().getField(targetField.x, targetField.y).setGadget(gadget);
                    break;
                }
            }

            // removes character from point end sets janitor to it
            targetChar.setCoordinates(point);
            game.getAllCharacters().remove(targetChar);
            game.getState().setJanitorCoordinates(targetField);

            game.getCurrentOperations().add(new BaseOperation(OperationEnum.JANITOR_ACTION, true, targetField));

            boolean p1Dead = true;
            boolean p2Dead = true;

            if (!game.getAllCharacters().isEmpty()) {

                for (Character characterOne : game.getPlayerOneCharacters()) {
                    if (game.getAllCharacters().contains(characterOne)) {
                        p1Dead = false;
                        break;
                    }
                }


                for (Character characterOne : game.getPlayerTwoCharacters()) {
                    if (game.getAllCharacters().contains(characterOne)) {
                        p2Dead = false;
                        break;
                    }
                }
            }


        if (p1Dead || p2Dead || game.getAllCharacters().isEmpty()) {

            game.emitter.sendGameStatusMessage(new GameStatusMessage(null, MessageTypeEnum.GAME_STATUS, Date.from(Instant.now()), "Janitor action", game.getGameHandler().getCurrentCharacter().getCharacterId(), game.getCurrentOperations(), game.getState(), true));

            game.getGameHandler().setWinner(null);
            game.emitter.sendStatisticsMessage(new StatisticsMessage(null, MessageTypeEnum.STATISTICS, Date.from(Instant.now()),
                    "Janitor Ends The Game", Statistics.StatisticsEntry, game.getGameHandler().getVictoryUUID(),
                    game.getGameHandler().getVictoryReason(), false));
            endGame();
        }
    }

}


    /**
     * Handle the requesting of a character-turn.
     *
     * @param characterToMove
     */
    private void zug(int characterToMove) {
        // reset operations
        game.getCurrentOperations().clear();

        // get active character
        currentCharacter = game.getAllCharacters().get(characterToMove);

        for (Character character : game.getAllCharacters()) {
            // Exfiltration
            if (character.getHp() <= 0) {
                List<Field> freeSeats = new LinkedList();

                for (int i = 0; i < game.getState().getMap().getMap().length; i++) {// get Seats
                    for (int j = 0; j < game.getState().getMap().getMap()[i].length; j++) {
                        // check for Bar table with no gadget on them - set gadget to new cocktail
                        if (game.getState().getMap().getMap()[i][j].getState() == FieldStateEnum.BAR_SEAT) {
                            freeSeats.add(game.getState().getMap().getMap()[i][j]);
                        }
                    }
                }
                Collections.shuffle(freeSeats);
                Random randSeat = new Random();
                Field charactersNewField = freeSeats.get(randSeat.nextInt(freeSeats.size()));

                if (game.getActionHandler().getCharacterOnField(charactersNewField) != null) {// if other character on seat
                    List<Field> neighbours = game.getActionHandler().getNeighbours(charactersNewField);
                    Field newFieldOfSeatCharacter = neighbours.get(randSeat.nextInt(neighbours.size()));

                    if (newFieldOfSeatCharacter.getState() != FieldStateEnum.FREE || newFieldOfSeatCharacter.getState() != FieldStateEnum.BAR_SEAT) {
                        newFieldOfSeatCharacter = neighbours.get(randSeat.nextInt(neighbours.size()));
                    }

                    int i = game.getActionHandler().getIndex(newFieldOfSeatCharacter)[0];// move him to a random neighbour
                    int j = game.getActionHandler().getIndex(newFieldOfSeatCharacter)[1];
                    game.getActionHandler().getCharacterOnField(charactersNewField).setCoordinates(new Point(i, j));
                }

                int i = game.getActionHandler().getIndex(charactersNewField)[0];// move character with 1 Hp to new position
                int j = game.getActionHandler().getIndex(charactersNewField)[1];
                character.setCoordinates(new Point(i, j));
                character.setHp(1);
            }
        }


        // dry clothes - if next to fireplace
        if (currentCharacter.getProperties().contains(PropertyEnum.CLAMMY_CLOTHES)) {
            for (int x = 0; x < game.getState().getMap().getMap().length; x++) {
                for (int y = 0; y < game.getState().getMap().getMap()[x].length; y++) {
                    if (game.getState().getMap().getMap()[x][y].getState() == FieldStateEnum.FIREPLACE
                            && game.getActionHandler().isInRange(1, new Point(x, y), currentCharacter.getCoordinates())) {
                        currentCharacter.getProperties().remove(PropertyEnum.CLAMMY_CLOTHES);
                    }
                }
            }
        }

        // refill hp - if on bar seat
        if (game.getState().getMap().getField(currentCharacter.getCoordinates().x, currentCharacter.getCoordinates().y).getState() == FieldStateEnum.BAR_SEAT) {
            currentCharacter.setHp(100);
        }

        // MASK Gadget - HP +10
        for (Gadget g : currentCharacter.getGadgets()) {
            if (g.getGadget() == GadgetEnum.MASK) {
                currentCharacter.setHp(Math.min(100, currentCharacter.getHp() + 10));
                break;
            }
        }

        // fill mp and ap
        currentCharacter.setMp(calculateMp(currentCharacter));
        currentCharacter.setAp(calculateAp(currentCharacter));

        // modify mp and ap according to properties
        // remove mp or ap point
        if (currentCharacter.getProperties().contains(PropertyEnum.SLUGGISHNESS)) {
            if (new Random().nextInt(2) == 0) {
                currentCharacter.setMp(Math.max(0, currentCharacter.getMp() - 1));
            } else {
                currentCharacter.setAp(Math.max(0, currentCharacter.getAp() - 1));
            }
        }
        // gain additional mp or ap point
        if (currentCharacter.getProperties().contains(PropertyEnum.AGILITY)) {
            if (new Random().nextInt(2) == 0) {
                currentCharacter.setMp(currentCharacter.getMp() + 1);
            } else {
                currentCharacter.setAp(currentCharacter.getAp() + 1);
            }
        }

        // Moledie transactions
        Moledie moledie = null;
        if (game != null && currentCharacter.getGadgets() != null) {
            for (Gadget gadget : currentCharacter.getGadgets()) {// get moledie gadget
                if (gadget.getGadget() == GadgetEnum.MOLEDIE)
                    moledie = (Moledie) gadget;
            }
        }

        if (moledie != null && currentCharacter != null && currentCharacter.getGadgets() != null && currentCharacter.getGadgets().contains(moledie)) {// if someone has the moledie
            if (currentCharacter.getProperties().contains(PropertyEnum.TRADECRAFT)) {// remove the given properties if character has them and save them in a list for later
                currentCharacter.getProperties().remove(PropertyEnum.TRADECRAFT);
                currentMoledieHolderProperties.add(PropertyEnum.TRADECRAFT);
            }
            if (currentCharacter.getProperties().contains(PropertyEnum.FLAPS_AND_SEALS)) {
                currentCharacter.getProperties().remove(PropertyEnum.FLAPS_AND_SEALS);
                currentMoledieHolderProperties.add(PropertyEnum.FLAPS_AND_SEALS);
            }
            if (currentCharacter.getProperties().contains(PropertyEnum.OBSERVATION)) {
                currentCharacter.getProperties().remove(PropertyEnum.OBSERVATION);
                currentMoledieHolderProperties.add(PropertyEnum.OBSERVATION);
            }
            currentMoledieHolder = currentCharacter;// save the current Moledie holder for later

        }

        if (currentMoledieHolder != null && currentMoledieHolder.getProperties() != null && !currentMoledieHolder.getGadgets().contains(moledie)) {// if last character who held moledie does not have it anymore
            for (PropertyEnum property : currentMoledieHolderProperties) {// go through his properties and give him back what was deactivated
                currentMoledieHolder.getProperties().add(property);
                currentMoledieHolderProperties.remove(property);// flush the list for the next moledieholder
            }

        }


        // update IP due to wiretapActions
        if (game.getAllCharacters() != null) {

            for (Character earplugsCharacter : game.getAllCharacters()) {
                for (Gadget earplugs : earplugsCharacter.getGadgets()) {

                    if (earplugs != null && earplugs.getGadget() == GadgetEnum.WIRETAP_WITH_EARPLUGS && !((WiretapWithEarplugs) earplugs).isWiretap()) {

                        if (game.getActionHandler().getCharacterById(((WiretapWithEarplugs) earplugs).getActiveOn()) != null && wireTapReferenceNumber == 0) {
                            wireTapReferenceNumber = game.getActionHandler().getCharacterById(((WiretapWithEarplugs) earplugs).getActiveOn()).getIp();
                        }

                        if (((WiretapWithEarplugs) earplugs).getWorking() && game.getActionHandler().getCharacterById(((WiretapWithEarplugs) earplugs).getActiveOn()) != null && game.getActionHandler().getCharacterById(((WiretapWithEarplugs) earplugs).getActiveOn()).getIp() > wireTapReferenceNumber) {
                            int difference = (game.getActionHandler().getCharacterById(((WiretapWithEarplugs) earplugs).getActiveOn()).getIp() - wireTapReferenceNumber);// if ip from target higher than in last zug --> update
                            earplugsCharacter.setIp(earplugsCharacter.getIp() + difference);
                        }
                    }
                }
            }

            for (Character wiretapCharacter : game.getAllCharacters()) {
                for (Gadget wiretap : wiretapCharacter.getGadgets()) {
                    if (wiretap != null && wiretap.getGadget() == GadgetEnum.WIRETAP_WITH_EARPLUGS && ((WiretapWithEarplugs) wiretap).isWiretap()) {
                        wireTapReferenceNumber = wiretapCharacter.getIp();
                        break;
                    }
                }
            }
        }

        //send initial game status message
        game.emitter.sendGameStatusMessage(new GameStatusMessage(null, MessageTypeEnum.GAME_STATUS, Date.from(Instant.now()),
                "game status message at the beginning of a turn (calculating ap and mp)", currentCharacter.getCharacterId(),
                game.getCurrentOperations(), game.getState(), false));

        // start character operation
        requestOperation(currentCharacter);
    }

    /**
     * calculate the base MP that a character gets
     *
     * @param character
     * @return
     */
    private int calculateMp(Character character) {
        int mp = 2;

        if (character.getProperties().contains(PropertyEnum.NIMBLENESS)) mp++;
        if (character.getProperties().contains(PropertyEnum.SLUGGISHNESS)) mp--;

        return mp;
    }

    /**
     * calculate the base AP that a character gets
     *
     * @param character
     * @return
     */
    private int calculateAp(Character character) {
        int ap = 1;

        if (character.getProperties().contains(PropertyEnum.SPRYNESS)) ap++;

        return ap;
    }

    /**
     * Send message to correct Player or handle intern logic.
     *
     * @param character
     */
    public void requestOperation(Character character) {
        //handle timer at the beginning of an operation
        //stop active timer
        timer.stop();

        // reset passed seconds for message receiver
        game.receiver.setOperationSecondsPassed(0);

        // schedule new timer
        timer = new OperationTimer(game);
        timer.schedule(game.getSettings().getTurnPhaseLimit());

        // remove gadgets if they are used up
        character.getGadgets().removeIf(gadget -> gadget.getUsages() == 0);

        if (game.getNpcs().contains(character)) {
            // NPC bewegen sich bisher nur random
            moveNpc(character);
            nextCharacter();
        }
        // Player 1 Operation required
        else if (game.getPlayerOneCharacters().contains(character)) {
            game.emitter.sendRequestGameOperationMessage(game.getPlayers().get(0).getConnection(), new RequestGameOperationMessage(game.getPlayers().get(0).getPlayerId(), MessageTypeEnum.REQUEST_GAME_OPERATION, Date.from(Instant.now()), "Requesting Game Operation", character.getCharacterId()));
        }
        // Player 2 Operation required
        else if (game.getPlayerTwoCharacters().contains(character)) {
            game.emitter.sendRequestGameOperationMessage(game.getPlayers().get(1).getConnection(), new RequestGameOperationMessage(game.getPlayers().get(1).getPlayerId(), MessageTypeEnum.REQUEST_GAME_OPERATION, Date.from(Instant.now()), "Requesting Game Operation", character.getCharacterId()));
        }
        // error happened - should not happen (server side problem)
        else {
            System.err.println("ERROR, Characters not correctly set!");
        }
    }

    /**
     * switch to next character or next round in order
     */
    public void nextCharacter() {
        if (characterToMoveIndex < game.getAllCharacters().size() - 1) {

            //reset Strikes
            Character justMoved = game.getAllCharacters().get(characterToMoveIndex);
            if (game.getPlayerOneCharacters().contains(justMoved)) {
                game.getPlayers().get(0).setStrikeCount(0);
            } else if (game.getPlayerTwoCharacters().contains(justMoved)) {
                game.getPlayers().get(1).setStrikeCount(0);
            }

            zug(++characterToMoveIndex);
        } else {
            moveCat();
            moveJanitor();
            runde();
        }
    }

    /**
     * remove a player from the game and send game left message to players and viewers and a statistics message as well
     *
     * @param player
     */
    public void disqualify(int player) {
        User toDisqualify = game.getPlayers().get(player - 1);

        // client may be disconnected at this point
        game.emitter.sendErrorMessage(toDisqualify.getConnection(), new ErrorMessage(toDisqualify.getPlayerId(), MessageTypeEnum.ERROR, Date.from(Instant.now()), "disqualified", ErrorTypeEnum.TOO_MANY_STRIKES));

        // send statistics message
        game.emitter.sendStatisticsMessage(new StatisticsMessage(null, MessageTypeEnum.STATISTICS, Date.from(Instant.now()),
                "Player was disqualified", Statistics.StatisticsEntry, game.getPlayers().get(0) == toDisqualify ? game.getPlayers().get(1).getPlayerId() : toDisqualify.getPlayerId(),
                VictoryEnum.VICTORY_BY_RANDOMNESS, false));

        endGame();
    }

    public void endGame() {
        // stop all timers that could be running
        if (timer != null) {
            timer.stop(); // OperationTimer
        }
        if (reconnectTimer != null) {
            reconnectTimer.stop();
        }
        if (game.receiver.getPauseTimer() != null) {
            game.receiver.getPauseTimer().stop();
        }

        // game active to false
        game.setActive(false);
    }

    /**
     * @param Npc
     * @author Meriton Dzemaili
     */
    public void moveNpc(Character Npc) {
        List<Point> possibleMoves = new LinkedList<>();

        // iterate over neighbours and get possible moves
        for (int x = Npc.getCoordinates().x; x <= Npc.getCoordinates().x + 1; x++) {
            for (int y = Npc.getCoordinates().y - 1; y <= Npc.getCoordinates().y + 1; y++) {

                // target field in bounds
                if (x >= 0 && y >= 0 && x < game.getState().getMap().getMap().length && y < game.getState().getMap().getMap()[x].length
                        // target field is moveable on
                        && (game.getState().getMap().getField(x, y).getState() == FieldStateEnum.FREE || game.getState().getMap().getField(x, y).getState() == FieldStateEnum.BAR_SEAT)
                        // not the same coordinates as already
                        && !(x == Npc.getCoordinates().x && y == Npc.getCoordinates().y)
                ) {
                    possibleMoves.add(new Point(x, y));
                }
            }
        }

        // actually move the NPC
        if (!possibleMoves.isEmpty()) {
            Random random = new Random();
            Point target = possibleMoves.get(random.nextInt(possibleMoves.size()));

            // look if there is a character on the target field
            Character character = characterOnField(target);
            if (character != null) {
                character.setCoordinates(Npc.getCoordinates());
            }
            Npc.setCoordinates(target);
        }

        // let the NPC wait 1 to 6 seconds before actually moving them on the game screen
        Random timeToMove = new Random();
        try {
            Thread.sleep(1000 * (timeToMove.nextInt(7) + 1));
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Sets the winner UUID and enum.
     *
     * @param playerWithCollar The Id of the player which gave the collar to the cat.
     * @author Sedat Qaja & Luca Uihlein
     */
    public void setWinner(UUID playerWithCollar) {

        int playerOneIp = 0;
        int playerTwoIp = 0;
        int playerOneHp = 0;
        int playerTwoHp = 0;

        for (Character playerOneCharacter : game.getPlayerOneCharacters()) {

            playerOneIp += playerOneCharacter.getIp();
            playerOneHp += playerOneCharacter.getHp();
        }

        for (Character playerTwoCharacter : game.getPlayerTwoCharacters()) {

            playerOneIp += playerTwoCharacter.getIp();
            playerTwoHp += playerTwoCharacter.getHp();
        }

        // win by ip
        if (playerOneIp != playerTwoIp) {

            if (playerOneIp > playerTwoIp) {
                setVictoryUUID(game.getPlayers().get(0).getPlayerId());
            } else {
                setVictoryUUID(game.getPlayers().get(1).getPlayerId());
            }
            setVictoryReason(VictoryEnum.VICTORY_BY_IP);
        }
        // win by collar
        else if (playerWithCollar != null) {
            setVictoryUUID(playerWithCollar);
            setVictoryReason(VictoryEnum.VICTORY_BY_COLLAR);
        }
        // win by cocktails drunk
        else if (game.getPlayers().get(0).getDrunkCocktail() != game.getPlayers().get(1).getDrunkCocktail()) {

            if (game.getPlayers().get(0).getDrunkCocktail() > game.getPlayers().get(1).getDrunkCocktail()) {
                setVictoryUUID(game.getPlayers().get(0).getPlayerId());
            } else {
                setVictoryUUID(game.getPlayers().get(1).getPlayerId());
            }
            setVictoryReason(VictoryEnum.VICTORY_BY_DRINKING);
        }
        // win by cocktails sipped
        else if (game.getPlayers().get(0).getSpilledCocktail() != game.getPlayers().get(1).getSpilledCocktail()) {

            if (game.getPlayers().get(0).getSpilledCocktail() > game.getPlayers().get(1).getSpilledCocktail()) {
                setVictoryUUID(game.getPlayers().get(0).getPlayerId());
            } else {
                setVictoryUUID(game.getPlayers().get(1).getPlayerId());
            }
            setVictoryReason(VictoryEnum.VICTORY_BY_SPILLING);
        }
        // win by hp
        else if (playerOneHp != playerTwoHp) {

            if (playerOneHp > playerTwoHp) {
                setVictoryUUID(game.getPlayers().get(0).getPlayerId());
            } else {
                setVictoryUUID(game.getPlayers().get(1).getPlayerId());
            }
            setVictoryReason(VictoryEnum.VICTORY_BY_HP);
        } else {
            Random random = new Random();

            setVictoryUUID(game.getPlayers().get(random.nextInt(2) - 1).getPlayerId());
            setVictoryReason(VictoryEnum.VICTORY_BY_RANDOMNESS);
        }
    }
}
