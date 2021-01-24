package util.logic.pregame;

import NetworkStandard.Characters.Character;
import NetworkStandard.DataTypes.GadgetEnum;
import NetworkStandard.DataTypes.Gadgets.Gadget;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;
import NetworkStandard.Messages.RequestItemChoiceMessage;
import util.Game;

import java.time.Instant;
import java.util.*;

/**
 * This class handles the ItemChoice phase of a game.
 *
 * @author Christian Wendlinger
 */
public class ItemChoicePhase {
    private Game game;

    /**
     * Constructor.
     *
     * @param game - corresponding game
     */
    public ItemChoicePhase(Game game) {
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
        ItemChoicePhase that = (ItemChoicePhase) o;
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
        return "ItemChoicePhase{" +
                "game=" + game +
                '}';
    }

    /**
     * Send a offer of 6 Gadgets and / or Characters to the two players.
     */
    public void sendOffer() {
        if (game.getOfferNr() < 9 && (game.getPlayerOneCharacters().size() + game.getPlayerOneGadgetChoices().size()) == (game.getPlayerTwoCharacters().size() + game.getPlayerTwoGadgetChoices().size())) {
            Random picker = new Random();

            // containers
            game.getPlayerOneCharOffer().clear();
            game.getPlayerTwoCharOffer().clear();

            game.getPlayerOneGadgetOffer().clear();
            game.getPlayerTwoGadgetOffer().clear();

            // player 1 offer
            // already 4 characters -> only 6 Gadgets
            if (game.getPlayerOneCharacters().size() > 3) {
                while (game.getPlayerOneGadgetOffer().size() < 6) {
                    game.getPlayerOneGadgetOffer().add(game.getAllGadgets().get(picker.nextInt(game.getAllGadgets().size())));
                }
            }
            // already 6 Gadgets -> only 6 chars
            else if (game.getPlayerOneGadgetChoices().size() > 5) {
                while (game.getPlayerOneCharOffer().size() < 6) {
                    game.getPlayerOneCharOffer().add(game.getNpcs().get(picker.nextInt(game.getNpcs().size())));
                }
            }
            // normal case -> 3 chars + 3 Gadgets
            else {
                while (game.getPlayerOneGadgetOffer().size() < 3) {
                    game.getPlayerOneGadgetOffer().add(game.getAllGadgets().get(picker.nextInt(game.getAllGadgets().size())));
                }

                while (game.getPlayerOneCharOffer().size() < 3) {
                    game.getPlayerOneCharOffer().add(game.getNpcs().get(picker.nextInt(game.getNpcs().size())));
                }
            }

            // player 2 offer
            // already 4 characters -> only 6 Gadgets
            if (game.getPlayerTwoCharacters().size() > 3) {
                while (game.getPlayerTwoGadgetOffer().size() < 6) {
                    Gadget choice = game.getAllGadgets().get(picker.nextInt(game.getAllGadgets().size()));
                    if (!game.getPlayerOneGadgetOffer().contains(choice)) {
                        game.getPlayerTwoGadgetOffer().add(choice);
                    }
                }
            }
            // already 6 Gadgets -> only 6 chars
            else if (game.getPlayerTwoGadgetChoices().size() > 5) {
                while (game.getPlayerTwoCharOffer().size() < 6) {
                    Character choice = game.getNpcs().get(picker.nextInt(game.getNpcs().size()));
                    if (!game.getPlayerOneCharOffer().contains(choice)) {
                        game.getPlayerTwoCharOffer().add(choice);
                    }
                }
            }
            // normal case -> 3 chars + 3 Gadgets
            else {
                while (game.getPlayerTwoGadgetOffer().size() < 3) {
                    Gadget choice = game.getAllGadgets().get(picker.nextInt(game.getAllGadgets().size()));
                    if (!game.getPlayerOneGadgetOffer().contains(choice)) {
                        game.getPlayerTwoGadgetOffer().add(choice);
                    }
                }

                while (game.getPlayerTwoCharOffer().size() < 3) {
                    Character choice = game.getNpcs().get(picker.nextInt(game.getNpcs().size()));
                    if (!game.getPlayerOneCharOffer().contains(choice)) {
                        game.getPlayerTwoCharOffer().add(choice);
                    }
                }
            }

            // Messages
            RequestItemChoiceMessage messagePlayer1 = generateMessageFromOffers(1, game.getPlayerOneCharOffer(), game.getPlayerOneGadgetOffer());
            RequestItemChoiceMessage messagePlayer2 = generateMessageFromOffers(2, game.getPlayerTwoCharOffer(), game.getPlayerTwoGadgetOffer());

            //send messages
            game.emitter.sendRequestItemChoiceMessage(game.getPlayers().get(0).getConnection(), messagePlayer1);
            game.emitter.sendRequestItemChoiceMessage(game.getPlayers().get(1).getConnection(), messagePlayer2);


            // increment offer counter
            game.setOfferNr(game.getOfferNr() + 1);
        }
        // Last choice was made
        else if (game.getOfferNr() == 9 && (game.getPlayerOneCharacters().size() + game.getPlayerOneGadgetChoices().size()) == (game.getPlayerTwoCharacters().size() + game.getPlayerTwoGadgetChoices().size())) {
            game.getEquipmentPhase().send();
            game.setOfferNr(-1);
        }
        // offer after choice phase - maybe client side fault
        else if (game.getOfferNr() == -1) {
            System.err.println("This function should not be called anymore.");
        }
    }

    /**
     * translate character and gadget objects to IDs and GadgetType
     *
     * @param player
     * @param charOffer
     * @param gadgetOffer
     * @return RequestItemChoice Message to send
     */
    public RequestItemChoiceMessage generateMessageFromOffers(int player, Set<Character> charOffer, Set<Gadget> gadgetOffer) {
        List<UUID> characterIds = new LinkedList<>();
        charOffer.forEach(character -> characterIds.add(character.getCharacterId()));

        List<GadgetEnum> gadgets = new LinkedList<>();
        gadgetOffer.forEach(gadget -> gadgets.add(gadget.getGadget()));

        return new RequestItemChoiceMessage(game.getPlayers().get(player - 1).getPlayerId(), MessageTypeEnum.REQUEST_ITEM_CHOICE, Date.from(Instant.now()), "Item Choice Nr. " + game.getOfferNr(), characterIds, gadgets);
    }
}
