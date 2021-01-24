package util.logic.pregame;

/**
 * This class handles the Equipment phase of a game.
 *
 * @author Christian Wendlinger
 */

import NetworkStandard.Characters.Character;
import NetworkStandard.DataTypes.GadgetEnum;
import NetworkStandard.DataTypes.Gadgets.Gadget;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;
import NetworkStandard.Messages.RequestEquipmentChoiceMessage;
import util.Game;

import java.time.Instant;
import java.util.*;

public class EquipmentPhase {
    private Game game;

    /**
     * Constructor.
     *
     * @param game - corresponding game
     */
    public EquipmentPhase(Game game) {
        this.game = game;
    }

    /**
     * Getter and Setter.
     */
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Equals and Hashcode.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquipmentPhase that = (EquipmentPhase) o;
        return Objects.equals(getGame(), that.getGame());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGame());
    }

    /**
     * toString.
     */
    @Override
    public String toString() {
        return "EquipmentPhase{" +
                "game=" + game +
                '}';
    }

    /**
     * start the equipment phase
     */
    public void send() {
        //check for duplicates
        game.getPlayerOneCharacters().forEach(character -> {
            if (game.getPlayerTwoCharacters().contains(character)) {
                System.err.println("Duplicate found: " + character);
            }
        });
        game.getPlayerOneGadgetChoices().forEach(gadget -> {
            if (game.getPlayerTwoGadgetChoices().contains(gadget)) {
                System.err.println("Duplicate found: " + gadget);
            }
        });

        // generate Messages
        RequestEquipmentChoiceMessage messagePlayer1 = generateEquipmentMessage(1, game.getPlayerOneCharacters(), game.getPlayerOneGadgetChoices());
        RequestEquipmentChoiceMessage messagePlayer2 = generateEquipmentMessage(2, game.getPlayerTwoCharacters(), game.getPlayerTwoGadgetChoices());

        // send Messages
        game.emitter.sendRequestEquipmentChoiceMessage(game.getPlayers().get(0).getConnection(), messagePlayer1);
        game.emitter.sendRequestEquipmentChoiceMessage(game.getPlayers().get(1).getConnection(), messagePlayer2);
    }

    /**
     * generate message for equipment phase
     *
     * @param player
     * @param chars
     * @param gadgets
     * @return a RequestEquipmentChoice message to send
     */
    public RequestEquipmentChoiceMessage generateEquipmentMessage(int player, List<Character> chars, List<Gadget> gadgets) {
        List<UUID> charsToSend = new LinkedList<>();
        chars.forEach(character -> charsToSend.add(character.getCharacterId()));

        List<GadgetEnum> gadgetsToSend = new LinkedList<>();
        gadgets.forEach(gadget -> gadgetsToSend.add(gadget.getGadget()));

        return new RequestEquipmentChoiceMessage(game.getPlayers().get(player - 1).getPlayerId(), MessageTypeEnum.REQUEST_EQUIPMENT_CHOICE, Date.from(Instant.now()), "Request Equipment Choice", charsToSend, gadgetsToSend);
    }
}
