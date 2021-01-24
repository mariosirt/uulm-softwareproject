package com.sopra.ntts.controller;

import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Handles the drafting phase and drafting screen.
 *
 * @author Sedat Qaja
 */
public class DraftingHandler {

  final Logger logger = LoggerFactory.getLogger(DraftingHandler.class);

  public MessageEmitter messageEmitter;

  public Integer offerNr;

  public List<UUID> offeredCharacterIds;

  public List<GadgetEnum> offeredGadgets;

  public UUID chosenCharacterId;

  public GadgetEnum chosenGadget;

  /**
   * Implemented for checking (NetworkStandard 5.2.5). Redundant.
   */
  public List<UUID> chosenCharacters;

  /**
   * Implemented for checking (NetworkStandard 5.2.5). Redundant.
   */
  public List<GadgetEnum> chosenGadgets;

  /**
   * True if lists are received from the server.
   */
  public boolean draftingInitialized;

  /**
   * Setter for the messageEmitter.
   *
   * @param messageEmitter The messageEmitter to the send the messages.
   */
  public void setMessageEmitter(MessageEmitter messageEmitter) {
    this.messageEmitter = messageEmitter;
  }

  /**
   * Default Constructor.
   */
  public DraftingHandler() {
    offeredCharacterIds = new LinkedList<>();
    offeredGadgets = new LinkedList<>();
  }

  /**
   * Sends the chosen item and adds other to the
   *
   * @param chosenCharacterId Character who is chosen.
   * @param chosenGadget      Gadget which is chosen.
   */
  public void sendItemChoice(UUID chosenCharacterId, GadgetEnum chosenGadget) {
    messageEmitter.sendItemChoiceMessage(chosenCharacterId, chosenGadget);
    clear();
  }

  /**
   * Method to fill the lists and offerNr.
   *
   * @param offeredCharacterIds List of offered Characters.
   * @param offeredGadgets      List of offered Gadgets.
   */
  public void setItemChoiceLists( List<UUID> offeredCharacterIds, List<GadgetEnum> offeredGadgets) {
    this.offeredCharacterIds.addAll(offeredCharacterIds);
    this.offeredGadgets.addAll(offeredGadgets);
  }

  /**
   * Setter for the Character.
   *
   * @param chosenCharacterId Chosen Character.
   */
  public void setChosenCharacterId(UUID chosenCharacterId) {
    this.chosenCharacterId = chosenCharacterId;
  }

  /**
   * Setter for the Gadget.
   *
   * @param chosenGadget Chosen Gadget.
   */
  public void setChosenGadget(GadgetEnum chosenGadget) {
    this.chosenGadget = chosenGadget;
  }

  /**
   * Resets the choice.
   */
  public void resetChosenCharacterId() {
    this.chosenCharacterId = null;
  }

  /**
   * Resets the choice.
   */
  public void resetChosenGadget() {
    this.chosenGadget = null;
  }


  /**
   * Auxiliary method to clean all decisions.
   */
  private void clear() {
    offeredCharacterIds.clear();
    offeredGadgets.clear();
    chosenCharacterId = null;
    chosenGadget = null;
  }

}
