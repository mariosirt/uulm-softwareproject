package NetworkStandard.DataTypes.Gadgets;

import NetworkStandard.Characters.Character;
import NetworkStandard.DataTypes.GadgetEnum;
import NetworkStandard.DataTypes.Operations.*;
import NetworkStandard.DataTypes.Properties.Observation;
import NetworkStandard.DataTypes.PropertyEnum;
import NetworkStandard.DataTypes.Szenario.Field;
import NetworkStandard.DataTypes.Szenario.FieldMap;
import NetworkStandard.DataTypes.Szenario.FieldStateEnum;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;
import NetworkStandard.Messages.GameOperationMessage;
import NetworkStandard.Messages.GameStatusMessage;
import NetworkStandard.Messages.StatisticsMessage;
import NetworkStandard.Messages.Strike;
import util.Game;
import util.User;

import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Marios Sirtmatsis
 * <p>
 * Class for handling Operations
 */


public class ActionHandler {
    private FieldMap map;
    private Game game;


    private Field baseField;
    private Field targetField;
    private Gadget gadget;


    private Boolean successful;
    private Boolean isDead;
    private int currentRound;
    private Character activeCharacter;

    private boolean actionPerformed;

    private boolean observationOnEnemyPlayer;


    public ActionHandler(FieldMap map, Game game) {
        this.map = map;
        this.game = game;
        this.successful = false;
        this.isDead = false;
    }

    public FieldMap getMap() {
        return map;
    }

    public void setMap(FieldMap map) {
        this.map = map;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public Game getGame() {
        return game;
    }

    public Field getBaseField() {
        return baseField;
    }

    public Field getTargetField() {
        return targetField;
    }

    public Gadget getGadget() {
        return gadget;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public Boolean getDead() {
        return isDead;
    }

    public Character getActiveCharacter() {
        return activeCharacter;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setBaseField(Field baseField) {
        this.baseField = baseField;
    }

    public void setTargetField(Field targetField) {
        this.targetField = targetField;
    }

    public void setGadget(Gadget gadget) {
        this.gadget = gadget;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public void setDead(Boolean dead) {
        isDead = dead;
    }

    public void setActiveCharacter(Character activeCharacter) {
        this.activeCharacter = activeCharacter;
    }

    public boolean isObservationOnEnemyPlayer() {
        return observationOnEnemyPlayer;
    }

    public void setObservationOnEnemyPlayer(boolean observationOnEnemyPlayer) {
        this.observationOnEnemyPlayer = observationOnEnemyPlayer;
    }

    /**
     * A method to handle incoming messages and map their attributes to the ones needed in this class.
     *
     * @param message: GameOperationmessage contains the operation and therefore is extracted for further actions
     * @author Marios Sirtmatsis & Sedat Qaja
     */
    public void handleIncomingMessage(GameOperationMessage message) {
        // character is on a foggy field
        if (message.getOperation() instanceof GadgetAction) {

            // no GadgetAction on foggy field allowed
            if (getFieldOfCharacter(game.getGameHandler().getCurrentCharacter()).isFoggy()) {
                this.successful = false;
                finishHandling(message, false, "game status after illegal action (field is foggy)", "player tried to perform an action on a foggy field", "you tried to perform an action but the field is foggy", false, OperationEnum.GADGET_ACTION);
                return;
            }

            GadgetAction gadgetAction = (GadgetAction) message.getOperation();
            Character gadgetCharacter = getCharacterById(gadgetAction.getCharacterId());


            this.activeCharacter = gadgetCharacter;
            baseField = map.getField(gadgetCharacter.getCoordinates().x, gadgetCharacter.getCoordinates().y);
            targetField = map.getField(gadgetAction.getTarget().x, gadgetAction.getTarget().y);
            this.successful = true;

            boolean characterHasGadget = false;

            for (Gadget gadget : activeCharacter.getGadgets()) {
                if (gadget.getGadget() == gadgetAction.getGadget()) {
                    this.gadget = gadget;// get chosen gadget
                    characterHasGadget = true;
                }
            }

            if (this.gadget == null && targetField.getGadget() != null && targetField.getGadget().getGadget() == GadgetEnum.COCKTAIL) {//Cocktail
                this.gadget = targetField.getGadget();
            }

            if ((characterHasGadget || ((targetField.getGadget() != null && targetField.getGadget().getGadget() == gadget.getGadget()))) && (gadgetCharacter.getAp() >= 0)) {// check if character who wants to make an action has the necessary gadget
                this.actionPerformed = handleGadgetAction(message);
            }

            this.isDead = gadgetCharacter.getHp() == 0;
            if (this.actionPerformed || this.successful)
                activeCharacter.setAp(activeCharacter.getAp() - 1);

            finishHandling(message, true, "game status update after successfull gadgetAction", "Illegal GadgetAction", "You tried to perform an illegal gadgetAction", false, OperationEnum.GADGET_ACTION);

        } else if (message.getOperation() instanceof GambleAction) {
            handleGambleAction(message);
        } else if (message.getOperation() instanceof PropertyAction) {
            PropertyAction propertyAction = (PropertyAction) message.getOperation();
            Character propertyCharacter = getCharacterById(propertyAction.getCharacterId());

            activeCharacter = propertyCharacter;
            baseField = map.getField(propertyCharacter.getCoordinates().x, propertyCharacter.getCoordinates().y);
            targetField = map.getField(propertyAction.getTarget().x, propertyAction.getTarget().y);
            this.successful = true;
            PropertyEnum property = propertyAction.getUsedProperty();// property for action

            if (propertyCharacter.getProperties() != null && propertyCharacter.getProperties().contains(property) && propertyCharacter.getAp() >= 0) {// check if character really has this property
                this.actionPerformed = handlePropertyAction(property, message, 0);
            }
            this.isDead = propertyCharacter.getHp() == 0;

            if (this.actionPerformed || this.successful)
                activeCharacter.setAp(activeCharacter.getAp() - 1);

            propertyAction.setEnemy(isObservationOnEnemyPlayer());

            finishHandling(message, this.observationOnEnemyPlayer, "game status update after successfull propertyAction", "Illegal PropertyAction", "You tried to perform an illegal propertyAction", false, OperationEnum.PROPERTY_ACTION);
        } else if (message.getOperation() instanceof Movement) {
            handleMovement(message);// move a character
        } else if (message.getOperation() instanceof Retire) {
            this.successful = true;

            // set ap and mp 0 zero of character
            Character current = game.getGameHandler().getCurrentCharacter();
            current.setMp(0);
            current.setAp(0);

            // move onto next player
            finishHandling(message, true, "game status update after successful retire", "Illegal Retire", "You tried to perform an illegal retire action", false, OperationEnum.RETIRE);
        } else if (message.getOperation() instanceof Spy) {
            handleSpyAction(message);
        }

    }


    /**
     * go to next character if no actions left, else request same character
     *
     * @author Christian Wendlinger
     */
    private void evaluateNextRequest() {
        // moves left
        if (game.getGameHandler().getCurrentCharacter().getMp() == 0 && game.getGameHandler().getCurrentCharacter().getAp() == 0) {
            game.getGameHandler().nextCharacter();
        }
        // no moves left
        else {
            game.getGameHandler().requestOperation(game.getGameHandler().getCurrentCharacter());
        }
    }

    /**
     * check if something is a certain in range
     *
     * @param range  - range
     * @param from   - from
     * @param target - target
     * @return true if in range, false else
     * @author Christian Wendlinger
     */
    public boolean isInRange(int range, Point from, Point target) {
        return Math.abs(from.x - target.x) <= range && Math.abs(from.y - target.y) <= range;
    }

    /**
     * handle new request or move to next character
     *
     * @param message         - original message
     * @param successful      - if the action was successful (by chance)
     * @param debugGameStatus - debug message for game status
     * @param debugStrike     - debug message for strike
     * @param reasonStrike    - reason in strike to show client
     * @param gameOver        - if the game is over
     * @param operationType   - which type the operation is, needed because message got deconstructed by RuntimeTypeAdapterFactory
     * @author Christian Wendlinger
     */
    private void finishHandling(GameOperationMessage message, boolean successful, String debugGameStatus, String debugStrike, String reasonStrike, boolean gameOver, OperationEnum operationType) {
        // resets the values
        setTargetField(null);
        setBaseField(null);
        this.gadget = null;

        // the operation was legal
        if (this.successful) {
            // modify operation
            message.getOperation().setSuccessful(successful);
            message.getOperation().setType(operationType);

            // set exfiltration data
            if (isDead) {
                Exfiltration exfiltration = (Exfiltration) message.getOperation();
                exfiltration.setFrom(new Point(getIndex(baseField)[0], getIndex(baseField)[1]));
            }

            // add operation
            game.getCurrentOperations().add(message.getOperation());

            // send new game status, checks if game active because of game end
            if (game.isActive()) {
                game.emitter.sendGameStatusMessage(new GameStatusMessage(null, MessageTypeEnum.GAME_STATUS, Date.from(Instant.now()), debugGameStatus, game.getGameHandler().getCurrentCharacter().getCharacterId(), game.getCurrentOperations(), game.getState(), gameOver));
                evaluateNextRequest();
            }
        }
        // illegal operation - send strike
        else {
            sendStrike(debugStrike, message, reasonStrike);
        }
    }

    /**
     * @param debugMessage
     * @param message
     * @param reason
     * @author Christian Wendlinger
     */
    public void sendStrike(String debugMessage, GameOperationMessage message, String reason) {
        // send Strike
        User user = game.getPlayers().get(0).getPlayerId().equals(message.clientId) ? game.getPlayers().get(0) : game.getPlayers().get(1);
        user.setStrikeCount(user.getStrikeCount() + 1);

        Strike strike = new Strike(message.getClientId(), MessageTypeEnum.STRIKE, Date.from(Instant.now()), debugMessage, user.getStrikeCount(), game.getSettings().getStrikeMaximum(), reason);

        // disqualify Player because of Maximum Strike count
        if (user.getStrikeCount() == game.getSettings().getStrikeMaximum()) {
            game.emitter.sendStrikeMessage(user.getConnection(), strike);
            game.getGameHandler().disqualify(game.getPlayers().get(0) == user ? 1 : 2);
        }
        // send strike message and request new Operation
        else {
            game.emitter.sendStrikeMessage(user.getConnection(), strike);
            game.getGameHandler().requestOperation(game.getGameHandler().getCurrentCharacter());
        }
    }

    /**
     * probability check
     *
     * @param chance - between 0, 100 = percentage
     * @return true if successful, false else
     * @author Christian Wendlinger
     */
    public boolean isSuccesful(int chance) {
        return new Random().nextInt(100) < chance;
    }


    /**
     * A method to handle an incoming gadgetAction and call the right message for the right gadget.
     */
    public boolean handleGadgetAction(GameOperationMessage message) {
        switch (gadget.getGadget()) {// iterate through different gadgettypes
            case HAIRDRYER:
                return hairdryerAction(message);
            case MOLEDIE:
                return moledieThrow(message);
            case TECHNICOLOUR_PRISM:
                return technicolourPrismAction(message);
            case BOWLER_BLADE:
                return bowlerBladeThrow(message, 0);
            case POISON_PILLS:
                return poisonPillsAction(message);
            case LASER_COMPACT:
                return laserCompactAction(message, 0);
            case ROCKET_PEN:
                return rocketPenAction(message);
            case GAS_GLOSS:
                return gasGlossAction(message);
            case MOTHBALL_POUCH:
                return mothballPouchAction(message);
            case FOG_TIN:
                return fogTinAction(message);
            case GRAPPLE:
                return grappleAction(message, 0);
            case JETPACK:
                return jetpackAction(message);
            case WIRETAP_WITH_EARPLUGS:
                return wiretapWithEarplugsAction(message);
            case CHICKEN_FEED:
                return chickenFeedAction(message);
            case NUGGET:
                return nuggetAction(message);
            case MIRROR_OF_WILDERNESS:
                return mirrorOfWildernessAction(message, 0);
            case COCKTAIL:
                return cocktailAction(message, 0);
            case DIAMOND_COLLAR:
                return diamondCollarAction(message);
        }

        return false;
    }


    /**
     * This Method handles an incoming gamble action and plays roulette on a neighbour target field
     *
     * @param message: message containing necessary information
     * @author Christian Wendlinger
     */
    public void handleGambleAction(GameOperationMessage message) {
        GambleAction gambleAction = (GambleAction) message.getOperation();
        Character gambleCharacter = game.getGameHandler().getCurrentCharacter();

        int x = gambleAction.getTarget().x;
        int y = gambleAction.getTarget().y;

        // enough Ap
        if (gambleCharacter.getAp() > 0

                // Table in range
                && isInRange(1, gambleCharacter.getCoordinates(), gambleAction.getTarget())

                // enough chips
                && gambleCharacter.getChips() >= gambleAction.getStake()

                // target is a table
                && game.getState().getMap().getField(x, y).getState() == FieldStateEnum.ROULETTE_TABLE

                // roulette table is not destroyed
                && !game.getState().getMap().getField(x, y).isDestroyed()

                // roulette table has minimal chip to play
                && game.getSettings().getMinChipsRoulette() <= game.getState().getMap().getField(x, y).getChipAmount()
        ) {
            // action is possible
            this.successful = true;

            // reduce AP
            gambleCharacter.setAp(gambleCharacter.getAp() - 1);

            // get the winning chance and adjust it according to properties
            float winningChance;

            if (gambleCharacter.getProperties().contains(PropertyEnum.LUCKY_DEVIL) && gambleCharacter.getProperties().contains(PropertyEnum.JINX)) {
                winningChance = 18f / 37f;
            } else if (gambleCharacter.getProperties().contains(PropertyEnum.LUCKY_DEVIL)) {
                winningChance = 23f / 37f;
            } else if (gambleCharacter.getProperties().contains(PropertyEnum.JINX)) {
                winningChance = 13f / 37f;
            } else if (game.getState().getMap().getField(x, y).isInverted()) {
                winningChance = 37f / 18f;
            } else {
                winningChance = 18f / 37f;
            }

            // successful gamble
            if (isSuccesful(Math.round(winningChance * 100))) {

                // stake or max chip amount of table
                int chipsWon = Math.min(gambleAction.getStake(), game.getState().getMap().getField(x, y).getChipAmount());

                // add winning money to character and remove money from table
                gambleCharacter.setChips(gambleCharacter.getChips() + chipsWon);
                game.getState().getMap().getField(x, y).setChipAmount(game.getState().getMap().getField(x, y).getChipAmount() - chipsWon);

                finishHandling(message, true, "game status update after successful gamble", "Illegal Gamble Action", "You tried to perform an illegal gamble", false, OperationEnum.GAMBLE_ACTION);
                return;
            }

            // money lost
            else {
                gambleCharacter.setChips(gambleCharacter.getChips() - gambleAction.getStake());
                game.getState().getMap().getField(x, y).setChipAmount(game.getState().getMap().getField(x, y).getChipAmount() + gambleAction.getStake());
            }
        } else {
            this.successful = false;
        }
        finishHandling(message, false, "game status update after successful gamble", "Illegal Gamble Action", "You tried to perform an illegal gamble", false, OperationEnum.GAMBLE_ACTION);
    }

    /**
     * This method handles properties that include an action (bang and burn or observation)
     *
     * @param property: bang and burn or observation
     */
    public boolean handlePropertyAction(PropertyEnum property, GameOperationMessage message, int tradecraftCounter) {
        if (property == PropertyEnum.BANG_AND_BURN) {
            if (isInNeighbours(baseField, targetField) && targetField.getState() == FieldStateEnum.ROULETTE_TABLE) {// neighbour roulette table can be burned
                targetField.setDestroyed(true);// roulette table is then destroyed
                return true;
            } else {
                return false;
            }
        }

        if (property == PropertyEnum.OBSERVATION) {
            Character base = getCharacterOnField(baseField);
            Character target = getCharacterOnField(targetField);

            if (base == null || target == null)
                return false;

            if (isInLineOfSight1(baseField, targetField, false)) {
                Observation observation = new Observation(property);

                boolean clammyClothes = false;
                if (base.getProperties().contains(PropertyEnum.CLAMMY_CLOTHES) || base.getProperties().contains(PropertyEnum.CONSTANT_CLAMMY_CLOTHES)) {// clammy clothes check
                    game.getSettings().setObservationSuccessChance(game.getSettings().getObservationSuccessChance() / 2);// change if clammy clothes to half probability
                    clammyClothes = true;
                }
                this.successful = true;
                this.observationOnEnemyPlayer = false;
                if (isSuccesful((int) (game.getSettings().getObservationSuccessChance() * 100))) {

                    if (babySitterCheck(target, targetField)) {// babysitter check right after first probability check
                        return false;
                    }

                    for (Gadget gadget : target.getGadgets()) {// Pocket Litter aborts observation
                        if (gadget.getGadget() == GadgetEnum.POCKET_LITTER) {
                            return false;
                        }
                    }

                    if (honeyTrap(target)) {// Honeytrap
                        for (Character character : game.getAllCharacters()) {


                            if (getFieldOfCharacter(character) != null && isInLineOfSight1(baseField, getFieldOfCharacter(character), false) && !character.equals(base) && !character.equals(target)) {// selects new target for action because of honeytrap
                                target = character;
                                targetField = getFieldOfCharacter(character);
                                return handlePropertyAction(property, message, 0);
                            }
                        }
                    }

                    boolean successfull = false;
                    if (game.getPlayerOneCharacters().contains(base) && game.getPlayerTwoCharacters().contains(target)) {// observe a character but only if in different team

                        if (game.getPlayerOneObservedCharacters() != null && !game.getPlayerOneObservedCharacters().contains(target)) {
                            game.getPlayerOneObservedCharacters().add(target);
                            this.observationOnEnemyPlayer = true;
                            successfull = true;
                        }

                    } else if (game.getPlayerTwoCharacters().contains(base) && game.getPlayerOneCharacters().contains(target)) {// same as above
                        if (game.getPlayerTwoObservedCharacters() != null && !game.getPlayerTwoObservedCharacters().contains(target)) {
                            game.getPlayerTwoObservedCharacters().add(target);
                            this.observationOnEnemyPlayer = true;
                            successfull = true;
                        }
                    }
                    if (clammyClothes)
                        game.getSettings().setObservationSuccessChance(game.getSettings().getObservationSuccessChance() * 2);// set clammy clothes off again
                    return successfull;
                } else {
                    if (tradecraft(base) && tradecraftCounter == 0) {// anoher chance for tradecraft
                        return handlePropertyAction(property, message, 1);// if someone failed by probabilities and is a tradecraft guy, repeating action is allowed
                    }
                }

            } else {
                this.successful = false;
                return false;
            }
        }
        return false;
    }

    /**
     * Method for moving a character. Therefore solely a characters coordinates are being changed and movepoints are
     * decremented by one. If there was another character on the field before, this character switches coordinates with
     * our character
     *
     * @param message: GameOperationMessage
     * @author Christian Wendlinger
     */
    public void handleMovement(GameOperationMessage message) {
        Movement movement = (Movement) message.getOperation();
        Character movementCharacter = game.getGameHandler().getCurrentCharacter();

        int x = movement.getTarget().x;
        int y = movement.getTarget().y;

        // correct move from coordinates
        if (movement.getFrom().equals(movementCharacter.getCoordinates())
                // enough mp
                && movementCharacter.getMp() > 0

                // target is on the map
                && x < game.getState().getMap().getMap().length
                && y < game.getState().getMap().getMap()[x].length

                // target field is walkable
                && (game.getState().getMap().getField(x, y).getState() == FieldStateEnum.FREE
                || game.getState().getMap().getField(x, y).getState() == FieldStateEnum.BAR_SEAT)

                // target field is a neighbour field
                && isInRange(1, movement.getFrom(), movement.getTarget())

                // target is not the same as start
                && !movement.getTarget().equals(movementCharacter.getCoordinates())

                //not Cat or Janitor
                && !movement.getTarget().equals(game.getState().getCatCoordinates())
                && !movement.getTarget().equals(game.getState().getJanitorCoordinates())
        ) {
            // switch places if there is already another character
            for (Character character : game.getAllCharacters()) {
                if (character.getCoordinates().equals(movement.getTarget())) {
                    character.setCoordinates(movementCharacter.getCoordinates());
                }
            }

            // place Character
            movementCharacter.setCoordinates(movement.getTarget());

            // reduce MP
            movementCharacter.setMp(movementCharacter.getMp() - 1);

            // pick up gadget
            Field movedOn = game.getState().getMap().getField(movementCharacter.getCoordinates().x, movementCharacter.getCoordinates().y);
            if (movedOn.getGadget() != null) {
                movementCharacter.addGadget(movedOn.getGadget());
                movedOn.setGadget(null);
            }

            this.successful = true;
        } else {
            this.successful = false;
        }
        finishHandling(message, true, "game status update after successful movement", "Illegal Movement", "You tried to perform an illegal movement", false, OperationEnum.MOVEMENT);
    }

    /**
     * A method to check if neighbours are having a babysitter property or not
     *
     * @param target:      target you need for finding surrounding neighbours
     * @param targetField: targetfield you need to find surrounding neighbour fields
     * @return true if babysitter will be activated and false if not
     */
    public boolean babySitterCheck(Character target, Field targetField) {
        if (target == null)
            return false;

        for (Field neighbour : getNeighbours(targetField)) {
            Character neighbourC = getCharacterOnField(neighbour);// check any neighbour for being a babysitter
            if (getCharacterOnField(neighbour) != null) {
                if (getCharacterOnField(neighbour).getProperties().contains(PropertyEnum.BABYSITTER) && ((game.getPlayerOneCharacters().contains(neighbourC)
                        && game.getPlayerOneCharacters().contains(target)) || (game.getPlayerTwoCharacters().contains(neighbourC)
                        && game.getPlayerTwoCharacters().contains(target)))) { // but only interesting if character is in ones faction

                    if (isSuccesful((int) (game.getSettings().getBabysitterSuccessChance() * 100))) {// if babysitterprobability also hits in, return true for babysittercheck
                        return true;
                    }
                }
            }
        }

        return false;
    }


    /**
     * A method to just shuffle the list  of Characters for random selection when searching for a new target later
     *
     * @param target: character that should have been attacked
     */
    public boolean honeyTrap(Character target) {
        if (target != null && target.getProperties().contains(PropertyEnum.HONEY_TRAP)) {// just shuffle indices for randomness of new target if someone has honeytrap property
            Collections.shuffle(game.getAllCharacters());
            return true;
        }
        return false;
    }

    /**
     * method for tradecraft check
     *
     * @param base: base field is being checked for being tradecraft character
     * @return true if yes and false if no
     */
    public boolean tradecraft(Character base) {
        return base.getProperties().contains(PropertyEnum.TRADECRAFT);// just check if someone has tradecraft property
    }


    /**
     * Method to return the character standing on a field by using its coordinates
     *
     * @param field: Field on which a character might stand
     * @return Character which is standing on Field field
     */
    public Character getCharacterOnField(Field field) {
        for (Character character : game.getAllCharacters()) {
            if (character.getCoordinates().equals(new Point(getIndex(field)[0], getIndex(field)[1]))) {// if character has same coordinates as field, he is on this field
                return character;// coordinates and field indices are unique, thats why this is possible
            }
        }
        return null;
    }

    /**
     * Method to get Field of a certain character
     *
     * @param character: to get field from
     * @return Field on which character stands
     */
    public Field getFieldOfCharacter(Character character) {
        return map.getMap()[character.getCoordinates().x][character.getCoordinates().y]; // now get a Field on which a Character is
    }


    /**
     * A method to return a character on the scenario by using his id.
     *
     * @param characterId: Id of a searched character
     * @return character with id characterid
     */
    public Character getCharacterById(UUID characterId) {
        for (Character character : game.getAllCharacters()) {
            if (character.getCharacterId().equals(characterId))// get a character by his id --> possible because of uniqueness
                return character;
        }
        return null;
    }

    /**
     * Hairdryeraction is being called on a specific target character and removes the clammy clothes property if given
     *
     * @param message: message for strike
     */
    private boolean hairdryerAction(GameOperationMessage message) {
        Character target = getCharacterOnField(targetField);
        if (target != null) {
            if (baseField != targetField) { // if target is not the base Character himself
                if (isInNeighbours(baseField, targetField)) {// check if target is in neighbours
                    target.getProperties().remove(PropertyEnum.CLAMMY_CLOTHES);// remove clammy clothes if so and get out of method
                    return true;
                } else {
                    this.successful = false;
                    return false;
                }

            }
            target.getProperties().remove(PropertyEnum.CLAMMY_CLOTHES);//remove ones clammy clothes property
            return true;
        }
        return false;
    }

    /**
     * A moledieThrow is being performed for getting rid of a moledie. Moledie can only be thrown onto Fields, which aren't
     * WALL-Fields.
     *
     * @param message: message for strike
     */
    public boolean moledieThrow(GameOperationMessage message) {
        Moledie moledie = (Moledie) gadget;
        Character base = getCharacterOnField(baseField);
        if (isInRange(baseField, targetField, moledie.getMoledieThrowRange()) && isInLineOfSight1(baseField, targetField, false)
                && targetField.getState() != FieldStateEnum.WALL) {// if someone is in range and in line of sight and not a wall, the moledie throw can be performed
            if (base != null && base.getGadgets() != null && base.getGadgets().contains(gadget))
                base.getGadgets().remove(gadget);// the base field loses a moledie
            getClosestCharacterPosition(targetField).addGadget(moledie);// the target character of moledie is closest to target field of throw
            return true;
        }
        this.successful = false;
        return false;
    }


    /**
     * A technicolourPrismAction inverts the winning chances at a roulette table. After usage, the technicoulor prism
     * cannot be used anymore
     *
     * @param message: message for strike
     */
    private boolean technicolourPrismAction(GameOperationMessage message) {
        if (isInNeighbours(baseField, targetField) && targetField.getState() == FieldStateEnum.ROULETTE_TABLE) {
            targetField.setInverted(true);// just set inverted if a neighbour roulette table gets this prism
            gadget.decrementUsages();// prisms usage decremented
            return true;

        }
        this.successful = false;
        return false;
    }


    /**
     * A bowlerbladethrow can hit a target in range and with a hathitprobability. If it does, it moves to a neighbour of
     * the target afer having hit the target. (Here I just used the first neighbour from mapNeighbours)
     *
     * @param message: message for strike
     */
    private boolean bowlerBladeThrow(GameOperationMessage message, int tradecraftCounter) {
        Character base = getCharacterOnField(baseField);
        Character target = getCharacterOnField(targetField);
        BowlerBlade bowlerblade = (BowlerBlade) gadget;

        Random rand = new Random();

        Field neighbour = getNeighbours(targetField).get(rand.nextInt(getNeighbours(targetField).size()));// random neighbour field selected

        while (neighbour.getState() != FieldStateEnum.FREE) {// neighbour has to be free field
            neighbour = getNeighbours(targetField).get(rand.nextInt(getNeighbours(targetField).size()));
        }

        if (target != null && isInRange(baseField, targetField, bowlerblade.getHatThrowRange()) && isInLineOfSight1(baseField, targetField, true)) {// if lucky and hit a target
            this.successful = true;
            if (isSuccesful((int) (game.getSettings().getBowlerBladeHitChance() * 100))) {
                for (Gadget gadgetCheck : target.getGadgets()) {
                    if (gadgetCheck.getGadget() == GadgetEnum.MAGNETIC_WATCH) {// and target does not have magnetic watch
                        base.getGadgets().remove(gadget);
                        neighbour.setGadget(bowlerblade);
                        return false;
                    }
                }
                if (babySitterCheck(target, targetField)) {// or a babysitter friend
                    base.getGadgets().remove(bowlerblade);
                    neighbour.setGadget(bowlerblade);// just lose gadget on random neighbour
                    return false;
                }

                if (honeyTrap(target)) {// or is a honeytrap

                    for (Character character : game.getAllCharacters()) {

                        if (getFieldOfCharacter(character) != null && isInRange(baseField, getFieldOfCharacter(character), bowlerblade.getHatThrowRange()) && isSuccesful((int) (game.getSettings().getBowlerBladeHitChance() * 100))
                                && isInLineOfSight1(baseField, getFieldOfCharacter(character), true) && !character.equals(base) && !character.equals(target)) {
                            target = character;
                            targetField = getFieldOfCharacter(character);
                            return bowlerBladeThrow(message, 0);// perform new action on new target

                        }
                    }
                }

                boolean toughness = false;

                if (target.getProperties().contains(PropertyEnum.TOUGHNESS)) {
                    bowlerblade.setHatDamage(bowlerblade.getHatDamage() / 2);// check for toughness and change damage
                    toughness = true;
                }
                boolean clammyClothes = false;
                if (base.getProperties().contains(PropertyEnum.CLAMMY_CLOTHES) || base.getProperties().contains(PropertyEnum.CONSTANT_CLAMMY_CLOTHES)) {
                    game.getSettings().setBowlerBladeHitChance(game.getSettings().getBowlerBladeHitChance() / 2);// classic clammy clothes check
                    clammyClothes = true;
                }

                base.getGadgets().remove(bowlerblade);
                target.setHp(target.getHp() - bowlerblade.getHatDamage());// the target gets hit
                neighbour.setGadget(bowlerblade);

                if (toughness) {
                    bowlerblade.setHatDamage(bowlerblade.getHatDamage() * 2);// reset toughness
                }

                if (clammyClothes) {
                    game.getSettings().setBowlerBladeHitChance(game.getSettings().getBowlerBladeHitChance() * 2);// reset clammy clothes
                }
                return true;

            }
        } else if (isInRange(baseField, targetField, bowlerblade.getHatThrowRange()) && isInLineOfSight1(baseField, targetField, false)) {// if not successful
            if (tradecraft(base) && tradecraftCounter == 0) {// another chance for tradecraft
                return bowlerBladeThrow(message, 1);

            }
            base.getGadgets().remove(bowlerblade);
            neighbour.setGadget(bowlerblade);// just lose gadget on random neighbour
        } else {
            this.successful = false;
            return false;
        }


        return false;

    }


    /**
     * A PoisonPills Action can poison a beneighboured cocktail on a bar table or a cocktail inside of ones Characters
     * inventory.
     *
     * @param message: message for strike
     */
    private boolean poisonPillsAction(GameOperationMessage message) {
        Character target = getCharacterOnField(targetField);
        Character base = getCharacterOnField(baseField);

        if (isInNeighbours(baseField, targetField) && targetField.getGadget() != null && targetField.getGadget().getGadget() == GadgetEnum.COCKTAIL) {// if neighbour is field with cocktail

            Cocktail cocktail = (Cocktail) targetField.getGadget();

            cocktail.setPoisoned(true);// cocktail is poisoned and character loses a pill
            gadget.decrementUsages();
            return true;

        } else if (isInNeighbours(baseField, targetField)) {// if not a field but target is a character

            if (babySitterCheck(target, targetField)) {// check all the stuff
                return false;
            }

            if (honeyTrap(target)) {// Honeytrap
                for (Character character : game.getAllCharacters()) {

                    if (getFieldOfCharacter(character) != null && (isInNeighbours(baseField, getFieldOfCharacter(character)) && getFieldOfCharacter(character).getGadget().getGadget() == GadgetEnum.COCKTAIL) && !character.equals(base) && !character.equals(target)) {

                        target = character;
                        targetField = getFieldOfCharacter(character);
                        return poisonPillsAction(message);

                    }
                }
            }


            for (Gadget gadgetCheck : target.getGadgets()) {

                if (gadgetCheck.getGadget() == GadgetEnum.COCKTAIL) {// poison the characters cocktail
                    Cocktail cocktail = (Cocktail) gadgetCheck;
                    cocktail.setPoisoned(true);
                    gadget.decrementUsages();
                    return true;
                }
            }
        } else {
            this.successful = false;
            return false;
        }
        return false;
    }


    /**
     * With a laserCompactAction a cocktail on a target field or in the inventory of a target Character can be steamed
     * (usage = 0) depending on a laserCompactProbability
     *
     * @param message: message for strike
     */
    private boolean laserCompactAction(GameOperationMessage message, int tradecraftCounter) {
        Character target = getCharacterOnField(targetField);
        Character base = getCharacterOnField(baseField);
        LaserCompact laserCompact = (LaserCompact) gadget;

        boolean successfull = false;

        boolean clammyClothes = false;


        if (base != null && base.getProperties() != null && (base.getProperties().contains(PropertyEnum.CLAMMY_CLOTHES) || base.getProperties().contains(PropertyEnum.CONSTANT_CLAMMY_CLOTHES))) {// clammy clothes check
            game.getSettings().setLaserCompactHitChance(game.getSettings().getLaserCompactHitChance() / 2);
            clammyClothes = true;
        }

        if (targetField.getGadget() != null && targetField.getGadget().getGadget() == GadgetEnum.COCKTAIL
                && isInLineOfSight1(baseField, targetField, false)) {// if successful and just a field without a character

            this.successful = true;
            if (isSuccesful((int) (game.getSettings().getLaserCompactHitChance() * 100))) {
                Cocktail cocktail = (Cocktail) targetField.getGadget();
                cocktail.decrementUsages();//Cocktail is being steamed
                cocktail = null;
                successfull = true;

            }
        } else if (isSuccesful((int) (game.getSettings().getLaserCompactHitChance() * 100)) && isInLineOfSight1(baseField, targetField, false)) {// if character

            if (babySitterCheck(target, targetField)) {
                if (clammyClothes)
                    game.getSettings().setLaserCompactHitChance(game.getSettings().getLaserCompactHitChance() * 2);
                return false;
            }

            if (honeyTrap(target)) {// Honeytrap
                for (Character character : game.getAllCharacters()) {
                    if (getFieldOfCharacter(character) != null && isInLineOfSight1(baseField, getFieldOfCharacter(character), false) && !character.equals(base) && !character.equals(target)) {
                        this.successful = true;
                        if (isSuccesful((int) (game.getSettings().getLaserCompactHitChance() * 100))) {
                            target = character;
                            targetField = getFieldOfCharacter(character);
                            return laserCompactAction(message, 0);// new target

                        }
                    }
                }
            }
            for (Gadget gadgetCheck : target.getGadgets()) {

                if (gadgetCheck.getGadget() == GadgetEnum.COCKTAIL) {

                    Cocktail cocktail = (Cocktail) gadgetCheck;
                    cocktail.decrementUsages(); // steamed in a characters hands
                    cocktail = null;
                    successfull = true;

                }
            }
        } else if (!isSuccesful((int) (game.getSettings().getLaserCompactHitChance() * 100))) {
            if (tradecraft(base) && tradecraftCounter == 0) {// anoher chance for tradecraft
                return laserCompactAction(message, 1);

            }
        } else if (!isInLineOfSight1(baseField, targetField, false)) {
            this.successful = false;

        }
        if (clammyClothes)
            game.getSettings().setLaserCompactHitChance(game.getSettings().getLaserCompactHitChance() * 2);

        return successfull;
    }


    /**
     * A method for using a rocket pen. If a rocket pen hits a wall field in line of sight with its rocket, this field gets
     * state FREE. The neighboured fields of the targetfield are being set to FREE too if they are WALL fields and
     * characters on neighboured fields are suffering a RocketDamage.
     *
     * @param message: message for strike
     */
    private boolean rocketPenAction(GameOperationMessage message) {
        Character target = getCharacterOnField(targetField);
        RocketPen rocketPen = (RocketPen) gadget;
        Character base = getCharacterOnField(baseField);

        boolean successfull = false;

        if (isInLineOfSight1(baseField, targetField, false)) { // other isinlineofsight because of hitting walls

            if (targetField.getState() == FieldStateEnum.WALL) {
                targetField.setState(FieldStateEnum.FREE);// wall is eliminated
                successfull = true;
            }

            for (Field neighbour : getNeighbours(targetField)) {
                if (neighbour.getState() == FieldStateEnum.WALL) {
                    neighbour.setState(FieldStateEnum.FREE);// neighbour walls too
                }

                Character neighbourC = getCharacterOnField(neighbour);
                if (neighbourC != null) {

                    boolean toughness = false;

                    if (babySitterCheck(neighbourC, neighbour)) {
                        continue;
                    }

                    if (neighbourC.getProperties() != null && neighbourC.getProperties().contains(PropertyEnum.TOUGHNESS)) {
                        rocketPen.setRocketDamage(rocketPen.getRocketDamage() / 2);
                        toughness = true;
                    }

                    neighbourC.setHp(neighbourC.getHp() - rocketPen.getRocketDamage());// neighbour characters get damage


                    if (toughness)
                        rocketPen.setRocketDamage(rocketPen.getRocketDamage() * 2);

                }

            }
            rocketPen.decrementUsages();// rocket pen is lost
            base.getGadgets().remove(rocketPen);
            successfull = true;

        } else {
            this.successful = false;

        }

        return successfull;
    }


    /**
     * A method to perform a gasGlossAction. Irritant gas can be fired on a neighboured field, which is getting foggy
     * and a character standing on this field suffers a irritant gas damage
     *
     * @param message: message for strike
     */
    private boolean gasGlossAction(GameOperationMessage message) {
        Character target = getCharacterOnField(targetField);
        Character base = getCharacterOnField(baseField);

        Gasgloss gasgloss = (Gasgloss) gadget;
        if (isInNeighbours(baseField, targetField)) {// if field is neighbour

            gasgloss.decrementUsages();

            if (target == null)
                return true;

            boolean toughness = false;

            if (target.getProperties().contains(PropertyEnum.TOUGHNESS)) {
                gasgloss.setIrritantGasDamage(gasgloss.getIrritantGasDamage() / 2);
                toughness = true;
            }

            if (babySitterCheck(target, targetField)) {
                return false;
            }

            if (honeyTrap(target)) {// Honeytrap
                for (Character character : game.getAllCharacters()) {
                    if (getFieldOfCharacter(character) != null && isInNeighbours(baseField, getFieldOfCharacter(character)) && !character.equals(base) && !character.equals(target)) {
                        target = character;
                        targetField = getFieldOfCharacter(character);
                        return gasGlossAction(message);

                    }
                }
            }


            target.setHp(target.getHp() - gasgloss.getIrritantGasDamage());// damage character on field

            if (toughness) {
                gasgloss.setIrritantGasDamage(gasgloss.getIrritantGasDamage() * 2);
            }

            return true;
        } else {
            this.successful = false;

            return false;

        }
    }


    /**
     * A method to throw a mothball to a FIREPLACE field in line of sight and in range. Every neighboured character suffers
     * a mothballpouchdamage. When hitting the fireplace there is a big explosion.
     *
     * @param message: message for strike
     */
    private boolean mothballPouchAction(GameOperationMessage message) {
        MothballPouch mothballPouch = (MothballPouch) gadget;

        if (isInRange(baseField, targetField, mothballPouch.getMothballPouchRange()) && isInLineOfSight1(baseField, targetField, false) // other is in line of sight because of test
                && targetField.getState() == FieldStateEnum.FIREPLACE) {// target has to be in range, line of sight and a fireplace
            for (Field neighbour : getNeighbours(targetField)) {


                Character neighbourC = getCharacterOnField(neighbour);

                if (neighbourC == null)
                    continue;

                if (babySitterCheck(neighbourC, neighbour)) {
                    continue;
                }


                boolean toughness = false;
                if (neighbourC.getProperties().contains(PropertyEnum.TOUGHNESS)) {
                    mothballPouch.setMothballPouchDamage(mothballPouch.getMothballPouchDamage() / 2);
                    toughness = true;
                }


                neighbourC.setHp(neighbourC.getHp() - mothballPouch.getMothballPouchDamage());// every neighbour is damaged

                if (toughness) {
                    mothballPouch.setMothballPouchDamage(mothballPouch.getMothballPouchDamage() * 2);
                }


            }
            mothballPouch.decrementUsages();
            return true;
        } else {
            this.successful = false;
            return false;
        }
    }


    /**
     * a method to get an object from a target field to ones own field using a grapple
     *
     * @param message: message for strike
     */
    private boolean grappleAction(GameOperationMessage message, int tradecraftCounter) {
        Grapple grapple = (Grapple) gadget;
        Character target = getCharacterOnField(targetField);
        Character base = getCharacterOnField(baseField);

        boolean successfull = false;

        boolean clammyClothes = false;
        if (base.getProperties().contains(PropertyEnum.CLAMMY_CLOTHES) || base.getProperties().contains(PropertyEnum.CONSTANT_CLAMMY_CLOTHES)) {
            game.getSettings().setGrappleHitChance(game.getSettings().getGrappleHitChance() / 2);
            clammyClothes = true;
        }

        if (targetField.getGadget() != null && isInLineOfSight1(baseField, targetField, false)
                && isInRange(baseField, targetField, grapple.getGrappleRange())) {// targetfield contains a grappleable gadget,
            // in line of sight, in Range and probability high enough

            this.successful = true;
            if (isSuccesful((int) (game.getSettings().getGrappleHitChance() * 100))) {
                if (base != null)
                    base.addGadget(targetField.getGadget());
                else
                    baseField.setGadget(targetField.getGadget());

                targetField.setGadget(null);
                successfull = true;
            }
        } else if (!isSuccesful((int) (game.getSettings().getGrappleHitChance() * 100))) {

            if (tradecraft(base) && tradecraftCounter == 0) {// another try for tradecrafts
                return grappleAction(message, 1);
            }
        } else {

            this.successful = false;
        }

        if (clammyClothes)
            game.getSettings().setGrappleHitChance(game.getSettings().getGrappleHitChance() * 2);
        return successfull;
    }

    /**
     * A fogtin action can be performed on a targetfield in line of sight and fogtinrange which is not a wall.
     * It creates a dense fog, where characters cannot move and which blocks the line of sight. It stays for the rest of
     * the first tound(where it was thrown) and following two rounds.
     *
     * @param message: message for strike
     */
    private boolean fogTinAction(GameOperationMessage message) {

        FogTin fogTin = (FogTin) gadget;
        if (targetField.getState() != FieldStateEnum.WALL && isInRange(baseField, targetField, fogTin.getFogTinRange())
                && isInLineOfSight1(baseField, targetField, false)) {//targetfield not a wall, in range and in line of sight

            if (targetField.getState() == FieldStateEnum.FREE) {
                targetField.setFoggy(true);
            }
            for (Field neighbour : getNeighbours(targetField)) {
                if (neighbour.getState() == FieldStateEnum.FREE) {
                    neighbour.setFoggy(true);
                }
            }
            fogTin.decrementUsages();
            return true;

        } else {
            this.successful = false;
        }
        return false;
    }


    /**
     * A method to fly with a jetpack to any free field in the game
     *
     * @param message: message for strike
     */
    private boolean jetpackAction(GameOperationMessage message) {
        Character base = getCharacterOnField(baseField);
        Character target = getCharacterOnField(targetField);


        if (targetField.getState() == FieldStateEnum.FREE && target == null) {// like movement action just can go anywhere without decrementing mp
            base.setCoordinates(new Point(getIndex(targetField)[0], getIndex(targetField)[1]));// target is ones own character one wants to fly with
            gadget.decrementUsages();

            // pick up gadget on target field
            if (targetField.getGadget() != null) {
                base.addGadget(targetField.getGadget());
                targetField.setGadget(null);
            }
            return true;
        } else {
            this.successful = false;
            return false;
        }

    }

    /**
     * A method for wiretap with Earplugs Action. As long as wiretap Probability is not enough wiretap and earplugs are working
     * As soon as they aren't anymore, they get removed from both, base and target character.
     * While working, any ip the target gets are being added to the base's ips to.
     *
     * @param message: message for strike
     */
    private boolean wiretapWithEarplugsAction(GameOperationMessage message) {
        boolean successful = false;
        Character base = getCharacterOnField(baseField);
        Character target = getCharacterOnField(targetField);

        if (isInNeighbours(baseField, targetField) && target != null) {

            WiretapWithEarplugs earplugs = (WiretapWithEarplugs) gadget;

            if (earplugs.isWiretap())// no actions for people with wiretap allowed
                return false;

            if (babySitterCheck(target, targetField)) {
                return false;
            }

            if (honeyTrap(target)) {// Honeytrap
                for (Character character : game.getAllCharacters()) {
                    if (getFieldOfCharacter(character) != null && isInNeighbours(baseField, getFieldOfCharacter(character)) && !character.equals(base) && !character.equals(target)) {
                        target = character;
                        targetField = getFieldOfCharacter(character);
                        return wiretapWithEarplugsAction(message);

                    }
                }
            }

            WiretapWithEarplugs wiretap = new WiretapWithEarplugs(GadgetEnum.WIRETAP_WITH_EARPLUGS);
            wiretap.setWiretap(true);
            wiretap.setUsages(0); // 0 for making wiretap invisible
            target.addGadget(wiretap);// target got wiretap

            earplugs.setActiveOn(target.getCharacterId());// character on which wiretap is active on
            earplugs.setWorking(true);
            successful = true;
        } else {
            this.successful = false;
        }
        return successful;
    }


    /**
     * A method for the chickenfeedAction. If it is being performed on a target from the same fraction, nothing happens
     * and it just disappears. If it is being used on a character from the other fraction, the absdiff is added or substracted
     * dependant on which's Ips are more.
     *
     * @param message: message for strike
     */
    private boolean chickenFeedAction(GameOperationMessage message) {
        if (isInNeighbours(baseField, targetField)) {

            Character base = getCharacterOnField(baseField);
            Character target = getCharacterOnField(targetField);

            if (base == null || target == null)
                return false;

            if ((game.getPlayerOneCharacters().contains(base) && !game.getPlayerTwoCharacters().contains(target))
                    || (game.getPlayerTwoCharacters().contains(base) && !game.getPlayerOneCharacters().contains(target))) {// same faction --> nothing happens

                gadget.decrementUsages();
                return false;

            } else if ((game.getPlayerOneCharacters().contains(base) && game.getPlayerTwoCharacters().contains(target))
                    || (game.getPlayerTwoCharacters().contains(base) && game.getPlayerOneCharacters().contains(target))) {// different faction

                if (babySitterCheck(target, targetField)) {
                    return false;
                }

                int absdiff = Math.abs(base.getIp() - target.getIp());// math formula from lastenheft

                if (base.getIp() < target.getIp()) {
                    base.setIp(Math.max(base.getIp() - absdiff, 0));
                } else if (base.getIp() > target.getIp())
                    base.setIp(base.getIp() + absdiff);

                gadget.decrementUsages();
                return true;
            }
        } else {
            this.successful = false;

        }
        return false;

    }


    /**
     * A method for a nugget action. If a character gives the nugget to a npc from the other fraction, this npc changes
     * sides and is controllable by player of base character. If nugget is given to playable character from different
     * fraction, the nugget goes to the opponents inventory and he knows what happened.
     *
     * @param message: message for strike
     */
    private boolean nuggetAction(GameOperationMessage message) {

        if (isInNeighbours(baseField, targetField)) {// if neighbours

            Character base = getCharacterOnField(baseField);
            Character target = getCharacterOnField(targetField);

            if (base == null || target == null)
                return false;

            if (game.getNpcs().contains(target)) {// if target is npc, else nothing happens
                if (game.getPlayerOneCharacters().contains(base) && !game.getPlayerOneCharacters().contains(target)) {// change faction of npc
                    game.getNpcs().remove(target);
                    game.getPlayerOneCharacters().add(target);
                    gadget.decrementUsages();
                    return true;
                } else if (game.getPlayerTwoCharacters().contains(base) && !game.getPlayerTwoCharacters().contains(target)) {// change faction for other player constellation
                    game.getNpcs().remove(target);
                    game.getPlayerTwoCharacters().add(target);
                    gadget.decrementUsages();
                    return true;
                }
            }

            if (game.getPlayerOneCharacters().contains(base) && game.getPlayerTwoCharacters().contains(target)) {// target gets gadget if in same faction
                base.getGadgets().remove(gadget);
                target.addGadget(gadget);
                return false;
            } else if (game.getPlayerTwoCharacters().contains(base) && game.getPlayerOneCharacters().contains(target)) {
                base.getGadgets().remove(gadget);
                target.addGadget(gadget);
                return false;
            }


        } else {
            this.successful = false;

            return false;
        }
        return false;
    }


    /**
     * A method to perform a mirrorofwilderness action. If a character uses this action on its fraction's character it always
     * is successful and the performing character swaps Ip. If the target character is from a different fraction
     * the action only is successful with a certain mowProbability and the mow vanishes after being used.
     *
     * @param message: message for strike
     */
    private boolean mirrorOfWildernessAction(GameOperationMessage message, int tradecraftCounter) {
        boolean successfull = false;

        if (isInNeighbours(baseField, targetField)) {
            MirrorOfWilderness mow = (MirrorOfWilderness) gadget;

            Character base = getCharacterOnField(baseField);
            Character target = getCharacterOnField(targetField);

            if (base == null || target == null)
                return false;

            boolean clammyClothes = false;
            if (base.getProperties().contains(PropertyEnum.CLAMMY_CLOTHES) || base.getProperties().contains(PropertyEnum.CONSTANT_CLAMMY_CLOTHES)) {
                game.getSettings().setMirrorSwapChance(game.getSettings().getMirrorSwapChance() / 2);
                clammyClothes = true;
            }

            if (game.getPlayerOneCharacters().contains(base) && game.getPlayerOneCharacters().contains(target) ||
                    game.getPlayerTwoCharacters().contains(base) && game.getPlayerTwoCharacters().contains(target)) {// switch ips if from same faction
                int tmp = base.getIp();
                base.setIp(target.getIp());
                target.setIp(tmp);
                successfull = true;

            } else if (game.getPlayerOneCharacters().contains(base) && game.getPlayerTwoCharacters().contains(target) ||
                    game.getPlayerTwoCharacters().contains(base) && game.getPlayerOneCharacters().contains(target)) {// if from different factions
                this.successful = true;
                if (isSuccesful((int) (game.getSettings().getMirrorSwapChance() * 100))) {

                    if (babySitterCheck(target, targetField)) {
                        if (clammyClothes)
                            game.getSettings().setMirrorSwapChance(game.getSettings().getMirrorSwapChance() * 2);

                        return false;
                    }

                    if (honeyTrap(target)) {// Honeytrap
                        for (Character character : game.getAllCharacters()) {
                            if (getFieldOfCharacter(character) != null && isInNeighbours(baseField, getFieldOfCharacter(character)) && !character.equals(base) && !character.equals(target)) {
                                target = character;
                                targetField = getFieldOfCharacter(character);
                                return mirrorOfWildernessAction(message, 0);

                            }
                        }
                    }


                    int tmp = base.getIp();// change ip after probability check and other checks
                    base.setIp(target.getIp());
                    target.setIp(tmp);

                    base.getGadgets().remove(mow);
                    mow.decrementUsages();
                    successfull = true;
                } else {
                    if (tradecraft(base) && tradecraftCounter == 0) {
                        mirrorOfWildernessAction(message, 1);// tradecraft can try again
                    }
                }

            }

            if (clammyClothes)
                game.getSettings().setMirrorSwapChance(game.getSettings().getMirrorSwapChance() * 2);

            return successfull;
        } else {
            this.successful = false;

        }
        return false;

    }


    /**
     * A cocktailAction can be three different things. It either is using the cocktail to throw it over someone else,
     * drink the cocktail or get the cocktail from a bar table next to ones self.
     *
     * @param message: message for strike
     * @author Marios Sirtmatsis & Sedat Qaja
     */
    public boolean cocktailAction(GameOperationMessage message, int tradecraftCounter) {
        Character base = getCharacterOnField(baseField);
        Character target = getCharacterOnField(targetField);
        Cocktail cocktail = (Cocktail) gadget;

        boolean successfull = false;

        boolean clammyClothes = false;
        if (base.getProperties().contains(PropertyEnum.CLAMMY_CLOTHES) || base.getProperties().contains(PropertyEnum.CONSTANT_CLAMMY_CLOTHES)) {
            game.getSettings().setCocktailDodgeChance(game.getSettings().getCocktailDodgeChance() / 2);
            clammyClothes = true;
        }

        if (isInNeighbours(baseField, targetField)) {// if neighbours

            if (target != null && base.getGadgets().contains(cocktail) && !base.equals(target)) {// spill cocktail if you allready have one
                this.successful = true;
                if (isSuccesful((int) (game.getSettings().getCocktailDodgeChance() * 100))) {
                    if (babySitterCheck(target, targetField)) {
                        if (clammyClothes)
                            game.getSettings().setCocktailDodgeChance(game.getSettings().getCocktailDodgeChance() * 2);

                        return false;
                    }

                    if (honeyTrap(target)) {// Honeytrap
                        for (Character character : game.getAllCharacters()) {
                            if (getFieldOfCharacter(character) != null && isInNeighbours(baseField, getFieldOfCharacter(character)) && getFieldOfCharacter(character).getGadget().equals(gadget) && !character.equals(base) && !character.equals(target)) {
                                target = character;
                                targetField = getFieldOfCharacter(character);
                                return cocktailAction(message, 0);

                            }
                        }
                    }

                    target.addProperty(PropertyEnum.CLAMMY_CLOTHES);// spill action
                    successfull = true;

                } else {
                    if (tradecraft(base) && tradecraftCounter == 0) {
                        cocktailAction(message, 1);
                    }
                }

                // increment spilled cocktails
                if (game.getPlayerOneCharacters().contains(base)) {

                    game.getPlayers().get(0).iterateSpilledCocktail();
                } else {
                    if (game != null && game.getPlayers().size() > 1)
                        game.getPlayers().get(1).iterateSpilledCocktail();
                }
                cocktail.decrementUsages();

            } else if (!base.getGadgets().contains(cocktail) && targetField.getGadget().getGadget() == GadgetEnum.COCKTAIL) {// get cocktail if you dont have it
                base.addGadget(targetField.getGadget());
                targetField.setGadget(null);
                successfull = true;
            }
        } else if (base.getGadgets().contains(cocktail) && base.equals(target)) {// drink cocktail

            boolean robustStomach = false;

            if (base.getProperties().contains(PropertyEnum.ROBUST_STOMACH) && cocktail.getPoisoned()) {// robust stomach changes way of cocktails influence on character
                game.getSettings().setCocktailHp(game.getSettings().getCocktailHp() / 2);
                robustStomach = true;
            } else if (base.getProperties().contains(PropertyEnum.ROBUST_STOMACH) && !cocktail.getPoisoned()) {
                game.getSettings().setCocktailHp(game.getSettings().getCocktailHp() * 2);
                robustStomach = true;
            }

            if (cocktail.getPoisoned() != null && cocktail.getPoisoned()) {// if poisoned get damage and if not get hp
                base.setHp(base.getHp() - game.getSettings().getCocktailHp());

            } else {
                base.setHp(base.getHp() + game.getSettings().getCocktailHp());

            }
            successfull = true;

            if (robustStomach && cocktail.getPoisoned()) {// reset robust stomach
                game.getSettings().setCocktailHp(game.getSettings().getCocktailHp() * 2);

            } else if (robustStomach && !cocktail.getPoisoned()) {
                game.getSettings().setCocktailHp(game.getSettings().getCocktailHp() / 2);

            }

            // increment drunk cocktails
            if (game.getPlayerOneCharacters().contains(base)) {

                game.getPlayers().get(0).iterateDrunkCocktail();
            } else {
                if (game != null && game.getPlayers().size() > 1)
                    game.getPlayers().get(1).iterateDrunkCocktail();
            }
            cocktail.decrementUsages();
        } else {
            this.successful = false;

        }
        if (clammyClothes)
            game.getSettings().setCocktailDodgeChance(game.getSettings().getCocktailDodgeChance() * 2);

        return successfull;
    }

    /**
     * handle spying (safe or character)
     *
     * @param message The message to work with from the client
     * @author Christian Wendlinger & Sedat Qaja
     */
    private void handleSpyAction(GameOperationMessage message) {
        Spy spy = (Spy) message.getOperation();
        Character spyCharacter = game.getGameHandler().getCurrentCharacter();
        Character targetCharacter = characterOnField(spy.getTarget());

        int x = spy.getTarget().x;
        int y = spy.getTarget().y;

        // enough Ap
        if (spyCharacter.getAp() > 0

                // Table in range
                && isInRange(1, spyCharacter.getCoordinates(), spy.getTarget())

                // target is a safe or character
                && (game.getState().getMap().getField(x, y).getState() == FieldStateEnum.SAFE || targetCharacter != null)
        ) {
            spyCharacter.setAp(spyCharacter.getAp() - 1);

            // safe
            if (targetCharacter == null) {
                this.successful = true;

                // message is from player one
                if (game.getPlayerOneCharacters().contains(spyCharacter)) {

                    // Player 1 has Combination
                    if (game.getPlayerOneSafeCombinations().contains(game.getState().getMap().getField(x, y).getSafeIndex())) {
                        // already opened
                        if (game.getPlayerOneOpenedSafes().contains(game.getState().getMap().getField(x, y).getSafeIndex())) {
                            finishHandling(message, false, "game status after trying to open a safe which was already opened by that player", "Player tried to open a safe that was already opened by him", "You tried to open a safe that you you already had opened", false, OperationEnum.SPY_ACTION);
                        }
                        // new safe
                        else {
                            // adjust IP of character
                            spyCharacter.setIp(spyCharacter.getIp() + game.getState().getMap().getField(x, y).getSafeIndex() * game.getSettings().getSecretToIpFactor());

                            // add safes to already opened
                            game.getPlayerOneOpenedSafes().add(game.getState().getMap().getField(x, y).getSafeIndex());

                            // give next Combination or Necklace
                            if (game.getState().getMap().getField(x, y).getGadget() == null) {
                                game.getPlayerOneSafeCombinations().add(game.getState().getMap().getField(x, y).getSafeIndex() + 1);
                            } else {
                                spyCharacter.addGadget(game.getState().getMap().getField(x, y).getGadget());
                            }

                            finishHandling(message, true, "game status after successfully opening a safe", "Player opened a safe", "You tried to open a safe", false, OperationEnum.SPY_ACTION);
                        }
                    }
                    // no combination for safe
                    else {
                        finishHandling(message, false, "game status after trying to open a safe where the player does not have the combination", "Player tried to open a safe that he doesn't have access to", "You tried to open a safe that you dont have access to", false, OperationEnum.SPY_ACTION);
                    }
                }

                // message from Player two
                else {
                    // Player 2 has Combination
                    if (game.getPlayerTwoSafeCombinations().contains(game.getState().getMap().getField(x, y).getSafeIndex())) {
                        // already opened
                        if (game.getPlayerTwoOpenedSafes().contains(game.getState().getMap().getField(x, y).getSafeIndex())) {
                            finishHandling(message, false, "game status after trying to open a safe which was already opened by that player", "Player tried to open a safe that was already opened by him", "You tried to open a safe that you you already had opened", false, OperationEnum.SPY_ACTION);
                        }
                        // new safe
                        else {
                            // adjust IP of character
                            spyCharacter.setIp(spyCharacter.getIp() + game.getState().getMap().getField(x, y).getSafeIndex() * game.getSettings().getSecretToIpFactor());

                            // add safes to already opened
                            game.getPlayerTwoOpenedSafes().add(game.getState().getMap().getField(x, y).getSafeIndex());

                            // give next Combination or Necklace
                            if (game.getState().getMap().getField(x, y).getGadget() == null) {
                                game.getPlayerTwoSafeCombinations().add(game.getState().getMap().getField(x, y).getSafeIndex() + 1);
                            } else {
                                spyCharacter.addGadget(game.getState().getMap().getField(x, y).getGadget());
                            }

                            finishHandling(message, true, "game status after successfully opening a safe", "Player opened a safe", "You tried to open a safe", false, OperationEnum.SPY_ACTION);
                        }
                    }
                    // no combination for safe
                    else {
                        finishHandling(message, false, "game status after trying to open a safe where the player does not have the combination", "Player tried to open a safe that he doesn't have access to", "You tried to open a safe that you dont have access to", false, OperationEnum.SPY_ACTION);
                    }
                }
            }
            // target is a character
            else {
                // spying NPC
                if (game.getNpcs().contains(targetCharacter)) {
                    this.successful = true;

                    if (isSuccesful((int) (game.getSettings().getSpySuccessChance() * 100))) {

                        Random random = new Random();
                        List<Integer> copyList = new LinkedList<>(game.getSafeCountIntoList());

                        if (game.getPlayerOneCharacters().contains(spyCharacter) && !game.getPlayerOneSpiedNPCs().contains(targetCharacter)) {
                            copyList.removeAll(game.getPlayerOneSafeCombinations());

                            if (game.getPlayerOneSafeCombinations().size() != game.getSafeCount()) {
                                Integer randomInt;
                                // gets the random value in linear time
                                do {
                                    randomInt = copyList.get(random.nextInt(copyList.size()));
                                    copyList.remove(randomInt);
                                } while (game.getPlayerOneSafeCombinations().contains(randomInt));

                                // ads found key to safe combinations and give player ip
                                spyCharacter.setIp(spyCharacter.getIp() + (randomInt * game.getSettings().getSecretToIpFactor()));
                                game.getPlayerOneSafeCombinations().add(randomInt);
                                game.getPlayerOneSpiedNPCs().add(targetCharacter);
                            } else {
                                // if all safe unlocked 3 ip are given as reward for spying a npc
                                spyCharacter.setIp(spyCharacter.getIp() + 3);
                            }
                        } else if (game.getPlayerTwoCharacters().contains(spyCharacter) && !game.getPlayerTwoSpiedNPCs().contains(targetCharacter)) {
                            copyList.removeAll(game.getPlayerTwoSafeCombinations());

                            if (game.getPlayerTwoSafeCombinations().size() != game.getSafeCount()) {
                                Integer randomInt;

                                // gets the random value in linear time
                                do {
                                    randomInt = copyList.get(random.nextInt(copyList.size()));
                                    copyList.remove(randomInt);
                                } while (game.getPlayerTwoSafeCombinations().contains(randomInt));

                                // ads found key to safe combinations
                                game.getPlayerTwoSafeCombinations().add(randomInt);
                                game.getPlayerTwoSpiedNPCs().add(targetCharacter);
                            } else {
                                // if all safe unlocked 3 ip are given as reward
                                spyCharacter.setIp(spyCharacter.getIp() + 3);
                            }
                        }
                        finishHandling(message, true, "spy action was on npc successful", "Player spied on NPC.", "You´re spy action was succesfull", false, OperationEnum.SPY_ACTION);
                    } else {
                        finishHandling(message, false, "spy action failed by chance", "Player has failed chance on spying.", "You´re spy action failed", false, OperationEnum.SPY_ACTION);
                    }
                }
                // kein NPC
                else {
                    if (game.getPlayerOneCharacters().contains(spyCharacter)) {
                        if (game.getPlayerOneCharacters().contains(targetCharacter)) {
                            finishHandling(message, false, "chosen team character to spy on", "Player has chosen own character to spy on.", "You tried to spy on your own character.", false, OperationEnum.SPY_ACTION);
                        } else {
                            finishHandling(message, false, "spied on other team character", "Player has spied on diffrent team", "You´re spy action failed", false, OperationEnum.SPY_ACTION);
                        }
                    } else if (game.getPlayerTwoCharacters().contains(spyCharacter)) {
                        if (game.getPlayerTwoCharacters().contains(targetCharacter)) {
                            finishHandling(message, false, "chosen team character to spy on", "Player has chosen own character to spy on.", "You tried to spy on your own character.", false, OperationEnum.SPY_ACTION);
                        } else {
                            finishHandling(message, false, "spied on other team character", "Player has spied on diffrent team", "You´re spy action failed", false, OperationEnum.SPY_ACTION);
                        }
                    }
                }
            }
        }
        // invalid target to spy
        else {
            this.successful = false;
            finishHandling(message, false, "game status update after illegal spy action", "Illegal Spy Action", "You tried to perform an illegal spy action", false, OperationEnum.SPY_ACTION);
        }
    }

    /**
     * look if there is a character on a specific field
     *
     * @param field - target field
     * @return character if there one, null else
     * @author Christian Wendlinger
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
     * Just a method returning whether a check Field is inside of base Field's neighbours.
     *
     * @param base:  base field of which check can be a neighbour
     * @param check: is this field a neighbour of base ?
     * @return true if check is base's neighbour and false if not
     */
    public boolean isInNeighbours(Field base, Field check) {
        List<Field> neighbours = getNeighbours(base);
        for (Field neighbour : neighbours) {
            if (getIndex(neighbour)[0] == getIndex(check)[0] && getIndex(neighbour)[1] == getIndex(check)[1]) {
                return true;
            }
        }
        return false;

    }


    /**
     * Method to return a list of a fields neighbours
     *
     * @param base: field of which the neighbours shall be returned.
     * @return List of neighbours of a field (base)
     */
    public List<Field> getNeighbours(Field base) {
        List<Field> neighbours = new LinkedList<>();
        int[] baseIndex = getIndex(base);
        int iBase = baseIndex[0];
        int jBase = baseIndex[1];

        if (jBase - 1 >= 0)
            neighbours.add(map.getMap()[iBase][jBase - 1]);

        if (jBase + 1 < map.getMap()[iBase].length)
            neighbours.add(map.getMap()[iBase][jBase + 1]);


        if (iBase - 1 >= 0)
            neighbours.add(map.getMap()[iBase - 1][jBase]);

        if (iBase + 1 < map.getMap().length)
            neighbours.add(map.getMap()[iBase + 1][jBase]);

        if (iBase - 1 >= 0 && jBase - 1 >= 0)
            neighbours.add(map.getMap()[iBase - 1][jBase - 1]);

        if (iBase - 1 >= 0 && jBase + 1 < map.getMap()[iBase].length)
            neighbours.add(map.getMap()[iBase - 1][jBase + 1]);

        if (iBase + 1 < map.getMap().length && jBase - 1 >= 0)
            neighbours.add(map.getMap()[iBase + 1][jBase - 1]);

        if (iBase + 1 < map.getMap().length && jBase + 1 < map.getMap()[iBase].length)
            neighbours.add(map.getMap()[iBase + 1][jBase + 1]);

        return neighbours;
    }


    /**
     * This method checks whether a check Field is in range of a base Field. It therefore checks every direction (if possible
     * a rangeXrange square. If the check Field is inside of this square it is inside of base's range.
     *
     * @param base:  field from which range calc starts
     * @param check: field of which it is being asked if it is in base's range or not
     * @param range: the range
     * @return true if check in base's range and false if not
     */
    public boolean isInRange(Field base, Field check, int range) {
        int[] baseIndex = getIndex(base);
        int[] checkIndex = getIndex(check);

        return Math.abs(baseIndex[0] - checkIndex[0]) <= range && Math.abs(baseIndex[1] - checkIndex[1]) <= range;
    }


    /**
     * An approach for line of sight. Here the line of sigt is defined as the straight up, down, left, right and the
     * diagonal lines (left up, left down, right up, right down) of a base Field. Thererfore if a field which is being
     * checked for one of the lines is not free or has a character on it, the current check of line breaks and tries
     * the next line check.  If a loop is not abborted and is able to find a field equalling the check Field instanc,
     * (Field instance which has to be checked whether in line of sight or not) then it can be returned true, as
     * there was not obstacle in front of the check instance.
     *
     * @param base:  base Field from whicht the line of sight calc starts
     * @param check: check Field to which the line of sight is being asked
     * @return true if line of sight is free and false if not
     */
    public boolean isInLineOfSight(Field base, Field check, Boolean bowlerBlade) {
        int[] baseIndex = getIndex(base);

        int iBase = baseIndex[0];
        int jBase = baseIndex[1];


        iBase--;
        jBase--;
        while (iBase >= 0 && jBase >= 0) {// left upper diagonal
            //System.out.println("LUD" + iBase + ":" + jBase + " " + map.getMap()[iBase][jBase].getState());
            if (((map.getMap()[iBase][jBase].getState() == FieldStateEnum.WALL) || (map.getMap()[iBase][jBase].getState() == FieldStateEnum.FIREPLACE)
                    || bowlerBlade && (getCharacterOnField(map.getMap()[iBase][jBase]) != null) || map.getMap()[iBase][jBase].isFoggy()) && map.getMap()[iBase][jBase] != check && map.getMap()[iBase][jBase] != base
            )
                break;
            if (map.getMap()[iBase][jBase] == check)
                return true;
            iBase--;
            jBase--;
        }

        iBase = baseIndex[0];
        jBase = baseIndex[1];
        while (iBase >= 0 && jBase < map.getMap()[iBase].length) {// right upper diagonal
            //System.out.println("RUD" + iBase + ":" + jBase + " " + map.getMap()[iBase][jBase].getState());
            if (((map.getMap()[iBase][jBase].getState() == FieldStateEnum.WALL) || (map.getMap()[iBase][jBase].getState() == FieldStateEnum.FIREPLACE)
                    || bowlerBlade && (getCharacterOnField(map.getMap()[iBase][jBase]) != null) || map.getMap()[iBase][jBase].isFoggy()) && map.getMap()[iBase][jBase] != check && map.getMap()[iBase][jBase] != base
            )
                break;
            if (map.getMap()[iBase][jBase] == check) {
                System.out.println(true);
                return true;
            }
            iBase--;
            jBase++;
        }

        iBase = baseIndex[0];
        jBase = baseIndex[1];
        while (iBase < map.getMap().length && jBase >= 0) {// left bottom diagonal
            //System.out.println("LBD" + iBase + ":" + jBase + " " + map.getMap()[iBase][jBase].getState());
            if (((map.getMap()[iBase][jBase].getState() == FieldStateEnum.WALL) || (map.getMap()[iBase][jBase].getState() == FieldStateEnum.FIREPLACE)
                    || bowlerBlade && (getCharacterOnField(map.getMap()[iBase][jBase]) != null) || map.getMap()[iBase][jBase].isFoggy()) && map.getMap()[iBase][jBase] != check && map.getMap()[iBase][jBase] != base
            )
                break;
            if (map.getMap()[iBase][jBase] == check)
                return true;
            iBase++;
            jBase--;
        }

        iBase = baseIndex[0];
        jBase = baseIndex[1];
        while (iBase < map.getMap().length && jBase < map.getMap()[iBase].length) {// right bottom diagonal
            //System.out.println("RBD" + iBase + ":" + jBase + " " + map.getMap()[iBase][jBase].getState());

            if (((map.getMap()[iBase][jBase].getState() == FieldStateEnum.WALL) || (map.getMap()[iBase][jBase].getState() == FieldStateEnum.FIREPLACE)
                    || bowlerBlade && (getCharacterOnField(map.getMap()[iBase][jBase]) != null) || map.getMap()[iBase][jBase].isFoggy()) && map.getMap()[iBase][jBase] != check && map.getMap()[iBase][jBase] != base
            )
                break;
            if (map.getMap()[iBase][jBase] == check)
                return true;
            iBase++;
            jBase++;
        }

        iBase = baseIndex[0];
        jBase = baseIndex[1];
        while (iBase < map.getMap().length) {// down straight
            //System.out.println("DS " + iBase + ":" + jBase + " " + map.getMap()[iBase][jBase].getState());

            if (((map.getMap()[iBase][jBase].getState() == FieldStateEnum.WALL) || (map.getMap()[iBase][jBase].getState() == FieldStateEnum.FIREPLACE)
                    || bowlerBlade && (getCharacterOnField(map.getMap()[iBase][jBase]) != null) || map.getMap()[iBase][jBase].isFoggy()) && map.getMap()[iBase][jBase] != check && map.getMap()[iBase][jBase] != base
            )
                break;
            if (map.getMap()[iBase][jBase] == check)
                return true;
            iBase++;
        }

        iBase = baseIndex[0];
        while (iBase >= 0) {// up straight
            //System.out.println("US" + iBase + ":" + jBase + " " + map.getMap()[iBase][jBase].getState());

            if (((map.getMap()[iBase][jBase].getState() == FieldStateEnum.WALL) || (map.getMap()[iBase][jBase].getState() == FieldStateEnum.FIREPLACE)
                    || bowlerBlade && (getCharacterOnField(map.getMap()[iBase][jBase]) != null) || map.getMap()[iBase][jBase].isFoggy()) && map.getMap()[iBase][jBase] != check && map.getMap()[iBase][jBase] != base
            )
                break;
            if (map.getMap()[iBase][jBase] == check)
                return true;
            iBase--;
        }

        iBase = baseIndex[0];
        jBase = baseIndex[1];
        while (jBase >= 0) {// left straiht
            //System.out.println("LS" + iBase + ":" + jBase + " " + map.getMap()[iBase][jBase].getState());

            if (((map.getMap()[iBase][jBase].getState() == FieldStateEnum.WALL) || (map.getMap()[iBase][jBase].getState() == FieldStateEnum.FIREPLACE)
                    || bowlerBlade && (getCharacterOnField(map.getMap()[iBase][jBase]) != null) || map.getMap()[iBase][jBase].isFoggy()) && map.getMap()[iBase][jBase] != check && map.getMap()[iBase][jBase] != base
            )
                break;
            if (map.getMap()[iBase][jBase] == check)
                return true;
            jBase--;
        }

        jBase = baseIndex[1];
        while (jBase < map.getMap()[iBase].length) {// right straight
            //System.out.println("RS" + iBase + ":" + jBase + " " + map.getMap()[iBase][jBase].getState());

            if (((map.getMap()[iBase][jBase].getState() == FieldStateEnum.WALL) || (map.getMap()[iBase][jBase].getState() == FieldStateEnum.FIREPLACE)
                    || bowlerBlade && (getCharacterOnField(map.getMap()[iBase][jBase]) != null) || map.getMap()[iBase][jBase].isFoggy()) && map.getMap()[iBase][jBase] != check && map.getMap()[iBase][jBase] != base
            )
                break;
            if (map.getMap()[iBase][jBase] == check)
                return true;
            jBase++;
        }
        return false;


    }

    /**
     * Checker if line of sight is appropriate, returns false if wrong.
     *
     * @param base:  base Field from whicht the line of sight calc starts
     * @param check: check Field to which the line of sight is being asked
     * @return true if line of sight is free and false if not
     * @author Sedat Qaja
     */
    public boolean isInLineOfSight1(Field base, Field check, Boolean bowlerBlade) {

        List<Field> fieldGetsCrossed = new LinkedList<Field>();

        int[] baseCoordinates = getIndex(base);
        int[] checkCoordinates = getIndex(check);

        // calculates the gradient to check, if the points are diagonal to each other
        float m = ((float) Math.abs(checkCoordinates[1] - baseCoordinates[1])) / ((float) Math.abs(checkCoordinates[0] - baseCoordinates[0]));

        for (int i = 0; i < map.getMap().length; i++) {
            for (int j = 0; j < map.getMap()[i].length; j++) {
                if (checkFieldCollision(new Point(baseCoordinates[0], baseCoordinates[1]), new Point(checkCoordinates[0], checkCoordinates[1]), i, j)) {
                    // if for the diagonal
                    if (m == 1) {
                        // for loop to construct the whole diagonal
                        for (int k = 0; k < Math.max(map.getMap().length, map.getMap()[i].length); k++) {
                            if (((i == baseCoordinates[0] + k) && (j == baseCoordinates[1] + k)) || ((i == baseCoordinates[0] + k) && (j == baseCoordinates[1] - k)) || ((i == baseCoordinates[0] - k) && (j == baseCoordinates[1] - k)) || ((i == baseCoordinates[0] - k) && (j == baseCoordinates[1] + k))) {
                                fieldGetsCrossed.add(map.getField(i, j));
                            }
                        }
                    } else {
                        fieldGetsCrossed.add(map.getField(i, j));
                    }
                }

            }
        }

        for (Field fieldToCheck : fieldGetsCrossed) {

            if ((fieldToCheck.getState() == FieldStateEnum.WALL) || (fieldToCheck.getState() == FieldStateEnum.FIREPLACE) || fieldToCheck.isFoggy()) {
                if (fieldToCheck != check) {
                    return false;
                }
            }

            // if bowlerBlade is thrown
            if (bowlerBlade) {

                for (Character character : game.getAllCharacters()) {

                    if ((getIndex(fieldToCheck)[0] == character.getCoordinates().getX()) && (getIndex(fieldToCheck)[1] == character.getCoordinates().getY())
                            && fieldToCheck != base && fieldToCheck != check) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * A method to get the index of a certain field
     *
     * @param base: the field one wants to get the index from
     * @return index array with index[0] being row and index[1] being column of feeldmap
     */
    public int[] getIndex(Field base) {
        int[] index = {-1, -1};
        for (int i = 0; i < map.getMap().length; i++) {
            for (int j = 0; j < map.getMap()[i].length; j++) {
                if (map.getMap()[i][j] == (base)) {
                    index[0] = i;
                    index[1] = j;
                    return index;
                }
            }
        }
        return null;
    }


    /**
     * A method to get the closest character based on a base Field with a certain position
     *
     * @param baseField: Field base from which the search of a closest character starts
     * @return Character closest to basefield (necessary for molediethrow)
     */
    public Character getClosestCharacterPosition(Field baseField) {
        int[] baseIndex = getIndex(baseField);
        int iBase = baseIndex[0];
        int jBase = baseIndex[1];

        Point[] allCoordinates = new Point[game.getAllCharacters().size()];

        for (int i = 0; i < game.getAllCharacters().size(); i++) {
            allCoordinates[i] = game.getAllCharacters().get(i).getCoordinates();
        }

        Point closest = allCoordinates[0];
        Point base = new Point(iBase, jBase);
        for (int j = 1; j < allCoordinates.length; j++) {
            if (getDistance(allCoordinates[j].x, base.x, allCoordinates[j].y, base.y) < getDistance(closest.x, base.x, closest.y, base.y)) {
                closest = allCoordinates[j];
            }
        }

        Character closestC = null;
        for (Character character : game.getAllCharacters()) {
            if (character.getCoordinates().equals(closest)) {
                closestC = character;
            }
        }
        return closestC;
    }

    /**
     * get the closest Character / for Janitor
     *
     * @param base
     * @return
     */
    public Character getClosestCharacter(Point base) {
        Character closest = null;
        int distance = 0;

        for (Character character : game.getAllCharacters()) {
            int currentDistance = Math.abs(character.getCoordinates().x - base.x) + Math.abs(character.getCoordinates().y - base.y);

            if (distance == 0 || currentDistance < distance) {
                distance = currentDistance;
                closest = character;
            }
        }

        return closest;
    }

    /**
     * gets an unsigned and unbiased distance between two Points/Fieldpositions
     *
     * @param x1: x of first Point
     * @param x2: x of second Point to compare
     * @param y1: y of first Point
     * @param y2: y of second Point to compare
     * @return Distance between two points
     */
    public double getDistance(int x1, int x2, int y1, int y2) {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow(y1 - y2, 2));

    }

    /**
     * Handles the diamond collar action, if true game ends.
     *
     * @param message The message from the client
     * @return If successful.
     * @author Luca Uhilein && Sedat Qaja
     */
    private boolean diamondCollarAction(GameOperationMessage message) {
        GadgetAction gadgetAction = (GadgetAction) message.getOperation();
        Character gadgetCharacter = game.getGameHandler().getCurrentCharacter();
        Field baseField = game.getState().getMap().getField(gadgetCharacter.getCoordinates().x, gadgetCharacter.getCoordinates().y);
        Field targetField = game.getState().getMap().getField(gadgetAction.getTarget().x, gadgetAction.getTarget().y);

        if (gadgetAction.getTarget().equals(game.getState().getCatCoordinates()) && isInNeighbours(baseField, targetField)) {

            gadgetCharacter.setIp(gadgetCharacter.getIp() + game.getSettings().getCatIp());

            game.getGameHandler().setWinner(gadgetAction.getCharacterId());

            game.emitter.sendStatisticsMessage(new StatisticsMessage(null, MessageTypeEnum.STATISTICS, Date.from(Instant.now()),
                    "Player gave the Diamond Collar to the Cat", Statistics.StatisticsEntry, game.getGameHandler().getVictoryUUID(),
                    game.getGameHandler().getVictoryReason(), false));

            game.setActive(false);

            return true;
        }
        return false;
    }

    /**
     * Method to determine if field is crossed by something.
     * Modified.
     *
     * @param basePos   Origin point
     * @param targetPos Target point
     * @param fieldx    Field x position
     * @param fieldy    Field y position
     * @return Returns if field is hit.
     * @author http://www.jeffreythompson.org/collision-detection/line-rect.php
     */
    boolean checkFieldCollision(Point basePos, Point targetPos, int fieldx, int fieldy) {

        float rx = (float) fieldx - 0.5f;
        float ry = (float) fieldy - 0.5f;
        float rw = (float) 1;
        float rh = (float) 1;

        // check if the line has hit any of the rectangle's sides
        // uses the Line/Line function below
        boolean left = checkLineCollision((float) basePos.x, (float) basePos.y, (float) targetPos.x, (float) targetPos.y, rx, ry, rx, ry + rh);
        boolean right = checkLineCollision((float) basePos.x, (float) basePos.y, (float) targetPos.x, (float) targetPos.y, rx + rw, ry, rx + rw, ry + rh);
        boolean top = checkLineCollision((float) basePos.x, (float) basePos.y, (float) targetPos.x, (float) targetPos.y, rx, ry, rx + rw, ry);
        boolean bottom = checkLineCollision((float) basePos.x, (float) basePos.y, (float) targetPos.x, (float) targetPos.y, rx, ry + rh, rx + rw, ry + rh);

        // if ANY of the above are true, the line
        // has hit the rectangle
        if (left || right || top || bottom) {
            return true;
        }
        return false;
    }


    /**
     * Auxiliary method to check if line hits line.
     *
     * @param x1 Position of base x
     * @param y1 Position of base y
     * @param x2 Position of target x
     * @param y2 Position of target y
     * @param x3 Position of left corner rectangle x
     * @param y3 Position of left corner rectangle y
     * @param x4 Width of rectangle
     * @param y4 Height of rectangle
     * @return Boolean if lines colide.
     * @author http://www.jeffreythompson.org/collision-detection/line-rect.php
     */
    boolean checkLineCollision(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {

        // calculate the direction of the lines
        float uA = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
        float uB = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));

        // if uA and uB are between 0-1, lines are colliding
        if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
            return true;
        }
        return false;
    }
}
