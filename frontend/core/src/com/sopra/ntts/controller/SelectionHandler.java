package com.sopra.ntts.controller;

import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Handles the selection phase and selection screen.
 *
 * @author Sedat Qaja
 */
public class SelectionHandler {

  final Logger logger = LoggerFactory.getLogger(SelectionHandler.class);

  public MessageEmitter messageEmitter;

  public List<UUID> chosenCharacterIds;

  public List<GadgetEnum> chosenGadgets;

  public Map<UUID, Set<GadgetEnum>> equipment;

  public Map<String, UUID> nameUuidMap;

  /**
   * Setter for the private attribute messageEmitter.
   *
   * @param messageEmitter The set messageEmitter to the game.
   */
  public void setMessageEmitter(MessageEmitter messageEmitter) {
    this.messageEmitter = messageEmitter;
  }

  /**
   * Default Constructor.
   */
  public SelectionHandler() {

    chosenCharacterIds = new LinkedList<>();
    chosenGadgets = new LinkedList<>();
    equipment = new HashMap<>();
    nameUuidMap = new HashMap<>();
  }


  public void setList(List<UUID> chosenCharacterIds, List<GadgetEnum> chosenGadgets) {

    this.chosenCharacterIds.addAll(chosenCharacterIds);
    this.chosenGadgets.addAll(chosenGadgets);
  }

  /**
   * Declaring the HashMap for the first time and creating Lists for every Character.
   */
  public void fillHashMap() {
    for (UUID character : chosenCharacterIds) {

      equipment.put(character, new HashSet<GadgetEnum>());
    }
  }

  /**
   * Setter for chosen Gadget.
   *
   * @param chosenCharacter Chosen character for the gadget.
   * @param chosenGadget    Chosen gadget for the character.
   */
  public void setGadgetToCharacter(UUID chosenCharacter, GadgetEnum chosenGadget) {

    equipment.get(chosenCharacter).add(chosenGadget);

    chosenGadgets.remove(chosenGadget);
  }

  /**
   * Remover for a specific gadget.
   *
   * @param chosenCharacter Character which the gadget needs to be removed from.
   * @param chosenGadget    Gadget which needs to be removed.
   */
  public void removeGadgetFromCharacter(UUID chosenCharacter, GadgetEnum chosenGadget) {

    equipment.get(chosenCharacter).remove(chosenGadget);

    chosenGadgets.add(chosenGadget);
  }

  /**
   * Auxiliary method to clean all decisions.
   */
  public void clear() {
    chosenCharacterIds.clear();
    chosenGadgets.clear();
    equipment.clear();
    nameUuidMap.clear();
  }
}
