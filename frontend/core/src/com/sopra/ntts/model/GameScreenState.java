package com.sopra.ntts.model;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.sopra.ntts.model.NetworkStandard.Characters.Character;
import com.sopra.ntts.model.NetworkStandard.DataTypes.Gadgets.Gadget;

/**
 * Auxiliary class. Is needed to initialize the state in the game screen.
 *
 * @author Sedat Qaja
 */
public class GameScreenState {
  Image image;
  GameScreenFieldStateEnum fieldState;
  Character character;
  Gadget gadget;

  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    this.image = image;
  }

  public GameScreenFieldStateEnum getFieldState() {
    return fieldState;
  }

  public void setFieldState(GameScreenFieldStateEnum fieldState) {
    this.fieldState = fieldState;
  }

  public Character getCharacter() {
    return character;
  }

  public void setCharacter(Character character) {
    this.character = character;
  }

  public Gadget getGadget() {
    return gadget;
  }

  public void setGadget(Gadget gadget) {
    this.gadget = gadget;
  }

  public GameScreenState() {
  }

  public GameScreenState(Image image) {
    this.image = image;
  }

  public GameScreenState(Image image, GameScreenFieldStateEnum fieldState) {
    this.image = image;
    this.fieldState = fieldState;
  }
}
