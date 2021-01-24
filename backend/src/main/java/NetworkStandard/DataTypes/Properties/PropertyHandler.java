package NetworkStandard.DataTypes.Properties;

import NetworkStandard.Characters.Character;
import NetworkStandard.DataTypes.PropertyEnum;

import java.util.List;
import java.util.Random;

/**
 * Property that a character can posses.
 *
 * @author Meriton Dzemaili
 */
public class PropertyHandler {

    private Character character;

    public PropertyHandler(Character character) {
        this.character = character;

    }

    public void checkForCharachterProperties(List<PropertyEnum> properties) {
        for (PropertyEnum e : properties) {
            setPropertieEffect(e);
        }

    }

    public void setPropertieEffect(PropertyEnum property) {

        switch (property) {

            case NIMBLENESS:
                character.setMp((3)); //Charakter bekommt in jeder Runde 3 Bewegungspunkte
                break;
            case SLUGGISHNESS:
                character.setMp((1));//Charakter bekommt in jeder Runde nur 1 Bewegungspunkt.
                break;
            case SPRYNESS:
                character.setAp((2));
                break;  //Charakter bekommt in jeder Runde 2 Aktionspunkte.
            case AGILITY:
                agility();
                break;//Charakter bekommt in jeder Runde zufällig 1 Bewegungspunkt oder 1 Aktionspunkt zusätzlich.
            default:
                break;

        }

    }

    private void agility() {            //Charakter bekommt in jeder Runde zufällig 1 Bewegungspunkt oder 1 Aktionspunkt zusätzlich.
        Random zufall = new Random();
        int zufallszahl = zufall.nextInt(2);
        if (zufallszahl == 0) {
            character.setMp((1));
        } else {
            character.setAp((1));
        }
    }
}