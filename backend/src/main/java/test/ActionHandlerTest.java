package test;

import NetworkStandard.Characters.Character;
import NetworkStandard.Characters.CharacterInformation;
import NetworkStandard.DataTypes.GadgetEnum;
import NetworkStandard.DataTypes.Gadgets.ActionHandler;
import NetworkStandard.DataTypes.Gadgets.Cocktail;
import NetworkStandard.DataTypes.Gadgets.Gadget;
import NetworkStandard.DataTypes.Gadgets.WiretapWithEarplugs;
import NetworkStandard.DataTypes.MatchConfig.Matchconfig;
import NetworkStandard.DataTypes.Operations.GadgetAction;
import NetworkStandard.DataTypes.Operations.OperationEnum;
import NetworkStandard.DataTypes.Operations.PropertyAction;
import NetworkStandard.DataTypes.PropertyEnum;
import NetworkStandard.DataTypes.Szenario.*;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;
import NetworkStandard.Messages.GameOperationMessage;
import com.google.gson.Gson;
import org.junit.Test;
import org.testng.Assert;
import util.Game;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.*;

/**
 * @author Marios Sirtmatsis
 */

public class ActionHandlerTest {

    private Game game;
    private Scenario level;
    private Matchconfig settings;
    private CharacterInformation[] character_settings;
    private Gson gson;
    private GameOperationMessage message;


    @Test
    public void hairDryerActionTest() {
        initializeMap();

        Gadget hairDryer = null;
        for (Gadget gadget : game.getAllGadgets()) {
            if (gadget.getGadget() == GadgetEnum.HAIRDRYER)
                hairDryer = gadget;
        }
        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new GadgetAction(OperationEnum.GADGET_ACTION, true, new Point(2, 2), game.getAllCharacters().get(0).getCharacterId(), GadgetEnum.HAIRDRYER));

        game.getAllCharacters().get(0).addGadget(hairDryer);//initialize Characters
        game.getAllCharacters().get(1).addProperty(PropertyEnum.CLAMMY_CLOTHES);

        game.getActionHandler().setGadget(hairDryer);//initialize ActionHandler
        game.getActionHandler().setBaseField(game.getState().getMap().getField(2, 1));
        game.getActionHandler().setTargetField(game.getState().getMap().getField(2, 2));
        game.getAllCharacters().get(0).setCoordinates(new Point(2,1));
        game.getAllCharacters().get(1).setCoordinates(new Point(2,2));

        boolean successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);//Assert a successful action on a neighbour

        boolean checkIfClammyClothesLost = game.getAllCharacters().get(1).getProperties().contains(PropertyEnum.CLAMMY_CLOTHES);

        Assert.assertFalse(checkIfClammyClothesLost);

        game.getActionHandler().setTargetField(game.getState().getMap().getField(2, 1));
        game.getAllCharacters().get(0).addProperty(PropertyEnum.CLAMMY_CLOTHES);

        successful = game.getActionHandler().handleGadgetAction(message);
        Assert.assertTrue(successful);// Assert a successful action from a character on himself

        checkIfClammyClothesLost = game.getAllCharacters().get(0).getProperties().contains(PropertyEnum.CLAMMY_CLOTHES);

        Assert.assertFalse(checkIfClammyClothesLost);


    }

    @Test
    public void testMoledieThrow() {
        initializeMap();

        Gadget moledie = null;
        for (Gadget gadget : game.getAllGadgets()) {
            if (gadget.getGadget() == GadgetEnum.MOLEDIE) {
                moledie = gadget;
            }
        }

        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new GadgetAction(OperationEnum.GADGET_ACTION, true, new Point(2, 4), game.getAllCharacters().get(0).getCharacterId(), GadgetEnum.MOLEDIE));

        game.getAllCharacters().get(0).addGadget(moledie);//prepare Character

        game.getActionHandler().setGadget(moledie);//initialize actionhandler
        game.getActionHandler().setBaseField(game.getState().getMap().getField(2, 1));
        game.getActionHandler().setTargetField(game.getState().getMap().getField(2, 4));

        boolean successfull = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successfull);// hit a wall

        game.getActionHandler().setBaseField(game.getState().getMap().getField(2, 1));
        game.getActionHandler().setTargetField(game.getState().getMap().getField(3, 1));
        game.getAllCharacters().get(0).setCoordinates(new Point(2,1));
        game.getAllCharacters().get(1).setCoordinates(new Point(3,1));

        successfull = game.getActionHandler().handleGadgetAction(message);
        Assert.assertTrue(successfull);// hit a valid target

        boolean hasMoledie = game.getAllCharacters().get(1).getGadgets().contains(moledie);
        boolean lostMoledie = game.getAllCharacters().get(0).getGadgets().contains(moledie);


        Assert.assertTrue(hasMoledie);// check if closest character has moledie
        Assert.assertFalse(lostMoledie);//check if base character lost moledie


    }

    @Test
    public void testTechniColourPrismAction() {
        initializeMap();

        Gadget techniColourPrism = null;
        for (Gadget gadget : game.getAllGadgets()) {
            if (gadget.getGadget() == GadgetEnum.TECHNICOLOUR_PRISM) {
                techniColourPrism = gadget;
            }
        }

        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new GadgetAction(OperationEnum.GADGET_ACTION, true, new Point(3, 3), game.getAllCharacters().get(0).getCharacterId(), GadgetEnum.TECHNICOLOUR_PRISM));
        game.getActionHandler().setGadget(techniColourPrism);
        game.getAllCharacters().get(0).setCoordinates(new Point(3, 2));
        game.getAllCharacters().get(0).addGadget(techniColourPrism);
        Field baseField = game.getActionHandler().getFieldOfCharacter(game.getAllCharacters().get(0));

        game.getActionHandler().setBaseField(baseField);

        Field targetField = game.getState().getMap().getField(3, 3);
        game.getActionHandler().setTargetField(targetField);

        int usages = techniColourPrism.getUsages();

        boolean successfull = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successfull);// hit a valid roulette table

        int decrementedUsages = usages - 1;
        Assert.assertEquals(Optional.of(decrementedUsages), Optional.of(techniColourPrism.getUsages()));
        // check if decrementing usages worked
        Assert.assertTrue(targetField.isInverted());// check if is Inverted

        Assert.assertFalse(game.getAllCharacters().get(0).getGadgets().contains(techniColourPrism));//check if character lost techprism
    }

    @Test
    public void testBowlerBladeThrow() {
        initializeMap();

        game.getSettings().setBowlerBladeHitChance(1.0);
        Gadget bowlerBlade = null;
        for (Gadget gadget : game.getAllGadgets()) {
            if (gadget.getGadget() == GadgetEnum.BOWLER_BLADE) {
                bowlerBlade = gadget;
            }
        }

        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new GadgetAction(OperationEnum.GADGET_ACTION, true, new Point(2, 2), game.getAllCharacters().get(0).getCharacterId(), GadgetEnum.BOWLER_BLADE));

        game.getAllCharacters().get(0).setCoordinates(new Point(2, 2));
        game.getAllCharacters().get(0).addGadget(bowlerBlade);
        game.getAllCharacters().get(1).setCoordinates(new Point(3, 2));
        game.getAllCharacters().get(2).setCoordinates(new Point(4, 2));

        game.getActionHandler().setGadget(bowlerBlade);
        game.getActionHandler().setBaseField(game.getActionHandler().getFieldOfCharacter(game.getAllCharacters().get(0)));
        game.getActionHandler().setTargetField(game.getActionHandler().getFieldOfCharacter(game.getAllCharacters().get(1)));

        int expectedHP = game.getAllCharacters().get(1).getHp() - game.getSettings().getBowlerBladeDamage();

        boolean successful = game.getActionHandler().handleGadgetAction(message);// Case 1 without babysitter, honeytrap or magnetic watch or toughness

        Assert.assertTrue(successful);
        Assert.assertFalse(game.getAllCharacters().get(0).getGadgets().contains(bowlerBlade));

        Assert.assertEquals(Optional.of(expectedHP), Optional.ofNullable(game.getAllCharacters().get(1).getHp()));
        for (Field neighbour : game.getActionHandler().getNeighbours(game.getState().getMap().getField(3, 2))) {
            if (neighbour.getGadget() != null)
                Assert.assertTrue(neighbour.getGadget() == bowlerBlade);
        }
        Assert.assertFalse(game.getAllCharacters().get(0).getGadgets().contains(bowlerBlade));

        game.getAllCharacters().get(2).addProperty(PropertyEnum.BABYSITTER);// babysitter test
        game.getSettings().setBabysitterSuccessChance(1.0);
        game.getPlayerOneCharacters().add(game.getAllCharacters().get(1));
        game.getPlayerOneCharacters().add(game.getAllCharacters().get(2));
        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successful);

        Gadget magnetic = new Gadget(GadgetEnum.MAGNETIC_WATCH);

        game.getAllCharacters().get(2).getProperties().remove(PropertyEnum.BABYSITTER);//magnetic watch test
        game.getAllCharacters().get(1).getGadgets().add(magnetic);
        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successful);

        for (Field neighbour : game.getActionHandler().getNeighbours(game.getState().getMap().getField(3, 2))) {
            if (neighbour.getGadget() != null)
                Assert.assertTrue(neighbour.getGadget() == bowlerBlade);
        }

        game.getAllCharacters().get(1).getGadgets().remove(magnetic);

        game.getAllCharacters().get(1).addProperty(PropertyEnum.HONEY_TRAP);// test honey trap
        game.getAllCharacters().get(2).setCoordinates(new Point(2, 3));


        int expectedHPFirstTarget = game.getActionHandler().getCharacterOnField(game.getState().getMap().getField(3, 2)).getHp();
        int expectedHPSecondTarget = game.getActionHandler().getCharacterOnField(game.getState().getMap().getField(2, 3)).getHp() - game.getSettings().getBowlerBladeDamage();

        game.getActionHandler().handleGadgetAction(message);

        Assert.assertEquals(Optional.of(expectedHPFirstTarget), Optional.ofNullable(game.getActionHandler().getCharacterOnField(game.getState().getMap().getField(3, 2)).getHp()));
        Assert.assertEquals(Optional.of(expectedHPSecondTarget), Optional.ofNullable(game.getActionHandler().getCharacterOnField(game.getState().getMap().getField(2, 3)).getHp()));

        Character m = game.getActionHandler().getCharacterOnField(game.getState().getMap().getField(3, 2));
        m.getProperties().remove(PropertyEnum.HONEY_TRAP);

        m.addProperty(PropertyEnum.TOUGHNESS);// check toughness
        m.setHp(100);
        game.getActionHandler().setTargetField(game.getState().getMap().getField(3, 2));
        int expectedHpToughness = m.getHp() - 2;
        int bowlerBladeDamage = game.getSettings().getBowlerBladeDamage();
        game.getActionHandler().handleGadgetAction(message);
        Assert.assertEquals(Optional.of(expectedHpToughness), Optional.ofNullable(m.getHp()));
        Assert.assertEquals(Optional.of(bowlerBladeDamage), Optional.ofNullable(game.getSettings().getBowlerBladeDamage()));

        double bowlerBladeHitChance = game.getSettings().getBowlerBladeHitChance();
        m.getProperties().remove(PropertyEnum.TOUGHNESS);
        m.addProperty(PropertyEnum.CLAMMY_CLOTHES);//test clammy clothes
        game.getActionHandler().handleGadgetAction(message);
        Assert.assertEquals(bowlerBladeHitChance, game.getSettings().getBowlerBladeHitChance());

        game.getSettings().setBowlerBladeHitChance(0.0);// case false and in range/line of sight
        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successful);
        for (Field neighbour : game.getActionHandler().getNeighbours(game.getState().getMap().getField(3, 2))) {
            if (neighbour.getGadget() != null)
                Assert.assertTrue(neighbour.getGadget() == bowlerBlade);
        }
    }

    @Test
    public void testPoisonPills() {
        initializeMap();

        Gadget poisonPills = null;
        for (Gadget gadget : game.getAllGadgets()) {
            if (gadget.getGadget() == GadgetEnum.POISON_PILLS) {
                poisonPills = gadget;
            }
        }

        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new GadgetAction(OperationEnum.GADGET_ACTION, true, new Point(2, 2), game.getAllCharacters().get(0).getCharacterId(), GadgetEnum.POISON_PILLS));
        game.getAllCharacters().get(0).setCoordinates(new Point(3, 2));
        game.getAllCharacters().get(1).setCoordinates(new Point(2, 2));
        game.getActionHandler().setGadget(poisonPills);
        game.getActionHandler().setBaseField(game.getState().getMap().getField(3, 2));


        Cocktail cocktail = new Cocktail(GadgetEnum.COCKTAIL);// case successful and cocktail on neighbour feild
        game.getActionHandler().setTargetField(game.getState().getMap().getField(4, 2));
        game.getState().getMap().getField(4, 2).setGadget(cocktail);
        int expectedUsages = poisonPills.getUsages() - 1;
        boolean successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertTrue(cocktail.getPoisoned());
        Assert.assertEquals(Optional.of(expectedUsages), Optional.ofNullable(poisonPills.getUsages()));

        Cocktail characterCocktail = new Cocktail(GadgetEnum.COCKTAIL);
        game.getActionHandler().setTargetField(game.getState().getMap().getField(2, 2));
        game.getAllCharacters().get(1).getGadgets().add(characterCocktail);
        expectedUsages = poisonPills.getUsages() - 1;
        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertTrue(characterCocktail.getPoisoned());
        Assert.assertEquals(Optional.of(expectedUsages), Optional.ofNullable(poisonPills.getUsages()));
    }

    @Test
    public void testLaserCompactAction() {
        initializeMap();
        game.getSettings().setLaserCompactHitChance(1.0);


        Gadget laserCompact = null;
        for (Gadget gadget : game.getAllGadgets()) {
            if (gadget.getGadget() == GadgetEnum.LASER_COMPACT) {
                laserCompact = gadget;
            }
        }


        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new GadgetAction(OperationEnum.GADGET_ACTION, true, new Point(2, 2), game.getAllCharacters().get(0).getCharacterId(), GadgetEnum.LASER_COMPACT));

        game.getAllCharacters().get(0).setCoordinates(new Point(3, 2));
        game.getAllCharacters().get(1).setCoordinates(new Point(2, 2));
        game.getActionHandler().setBaseField(game.getState().getMap().getField(3, 2));
        game.getActionHandler().setGadget(laserCompact);

        game.getSettings().setLaserCompactHitChance(1.0);
        game.getActionHandler().setTargetField(game.getState().getMap().getField(4, 2));// case successful and field without character but with cocktail
        Cocktail cocktailOnField = new Cocktail(GadgetEnum.COCKTAIL);
        game.getState().getMap().getField(4, 2).setGadget(cocktailOnField);

        int decrementedUsages = cocktailOnField.getUsages() - 1;
        boolean successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertEquals(Optional.of(decrementedUsages), Optional.ofNullable(cocktailOnField.getUsages()));

        game.getActionHandler().setTargetField(game.getState().getMap().getField(2, 2));// case hitting character on a field which has cocktail
        Cocktail cocktailCharacter = new Cocktail(GadgetEnum.COCKTAIL);
        game.getAllCharacters().get(1).addGadget(cocktailCharacter);
        game.getAllCharacters().get(0).addGadget(laserCompact);



        int decrementedUsagesCharacter = cocktailCharacter.getUsages() - 1;
        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertEquals(Optional.of(decrementedUsagesCharacter), Optional.ofNullable(cocktailCharacter.getUsages()));

        game.getSettings().setLaserCompactHitChance(0.0);//tradecraft case
        game.getAllCharacters().get(0).addProperty(PropertyEnum.TRADECRAFT);
        cocktailCharacter.setUsages(10);
        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successful);


    }

    @Test
    public void testRocketPenAction() {
        initializeMap();
        Gadget rocketPen = null;
        for (Gadget gadget : game.getAllGadgets()) {
            if (gadget.getGadget() == GadgetEnum.ROCKET_PEN) {
                rocketPen = gadget;
            }
        }


        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new GadgetAction(OperationEnum.GADGET_ACTION, true, new Point(-1, -1), game.getAllCharacters().get(0).getCharacterId(), GadgetEnum.ROCKET_PEN));

        game.getAllCharacters().get(0).setCoordinates(new Point(3, 2));//case successful and hitting a wall
        game.getAllCharacters().get(1).setCoordinates(new Point(2, 3));
        game.getActionHandler().setBaseField(game.getState().getMap().getField(3, 2));
        game.getActionHandler().setTargetField(game.getState().getMap().getField(3, 4));
        game.getActionHandler().setGadget(rocketPen);

        boolean successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertNotEquals(game.getAllCharacters().get(1).getHp(), 100);
        Assert.assertEquals(Optional.ofNullable(rocketPen.getUsages()), Optional.of(0));



    }

    @Test
    public void testGasGlossAction() {
        initializeMap();

        Gadget gasGloss = null;
        for (Gadget gadget : game.getAllGadgets()) {
            if (gadget.getGadget() == GadgetEnum.GAS_GLOSS) {
                gasGloss = gadget;
            }
        }


        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new GadgetAction(OperationEnum.GADGET_ACTION, true, new Point(-1, -1), game.getAllCharacters().get(0).getCharacterId(), GadgetEnum.GAS_GLOSS));

        game.getAllCharacters().get(0).setCoordinates(new Point(2, 2));
        game.getAllCharacters().get(1).setCoordinates(new Point(3, 2));
        game.getAllCharacters().get(2).setCoordinates(new Point(4, 2));

        game.getActionHandler().setGadget(gasGloss);
        game.getActionHandler().setBaseField(game.getState().getMap().getField(2, 2));
        game.getActionHandler().setTargetField(game.getState().getMap().getField(2, 1));// case empty field

        int usages = gasGloss.getUsages();
        boolean successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertTrue(usages > gasGloss.getUsages());

        game.getActionHandler().setTargetField(game.getState().getMap().getField(3, 2));// case with character on reachable neighbour feild
        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertNotEquals(game.getActionHandler().getCharacterOnField(game.getState().getMap().getField(3, 2)).getHp(), 100);


        game.getActionHandler().setTargetField(game.getState().getMap().getField(4, 2));// case not successful because target is out of reach

        Assert.assertFalse(game.getActionHandler().isInNeighbours(game.getState().getMap().getField(2, 2), game.getState().getMap().getField(4, 2)));
        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successful);

    }

    @Test
    public void testMothballPouchAction() {
        initializeMap();
        Gadget mothballpouch = null;
        for (Gadget gadget : game.getAllGadgets()) {
            if (gadget.getGadget() == GadgetEnum.MOTHBALL_POUCH) {
                mothballpouch = gadget;
            }
        }


        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new GadgetAction(OperationEnum.GADGET_ACTION, true, new Point(-1, -1), game.getAllCharacters().get(0).getCharacterId(), GadgetEnum.MOTHBALL_POUCH));

        game.getAllCharacters().get(0).setCoordinates(new Point(2, 2));
        game.getAllCharacters().get(1).setCoordinates(new Point(2, 1));
        game.getLevel().setFieldState(1, 2, FieldStateEnum.FREE);
        game.getAllCharacters().get(2).setCoordinates(new Point(1, 2));

        game.getActionHandler().setGadget(mothballpouch);// case everything works out fine
        game.getActionHandler().setBaseField(game.getState().getMap().getField(2, 2));
        game.getActionHandler().setTargetField(game.getState().getMap().getField(1, 1));

        boolean successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertEquals(Optional.ofNullable(game.getAllCharacters().get(1).getHp()), Optional.of(100 - game.getSettings().getMothballPouchDamage()));
        Assert.assertEquals(Optional.ofNullable(game.getAllCharacters().get(2).getHp()), Optional.of(100 - game.getSettings().getMothballPouchDamage()));
        Assert.assertEquals(Optional.ofNullable(mothballpouch.getUsages()), Optional.of(4));

        game.getActionHandler().setTargetField(game.getState().getMap().getField(3, 1));// case target is not a fireplace
        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successful);
        Assert.assertEquals(Optional.ofNullable(mothballpouch.getUsages()), Optional.of(4));

    }


    @Test
    public void testGrappleAction() {
        initializeMap();

        Gadget grapple = null;
        for (Gadget gadget : game.getAllGadgets()) {
            if (gadget.getGadget() == GadgetEnum.GRAPPLE) {
                grapple = gadget;
            }
        }


        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new GadgetAction(OperationEnum.GADGET_ACTION, true, new Point(-1, -1), game.getAllCharacters().get(0).getCharacterId(), GadgetEnum.GRAPPLE));

        game.getAllCharacters().get(0).setCoordinates(new Point(2, 2));

        game.getSettings().setGrappleHitChance(1.0);// case successful throw
        game.getActionHandler().setGadget(grapple);
        Cocktail grappleCocktail = new Cocktail(GadgetEnum.COCKTAIL);
        game.getActionHandler().setBaseField(game.getState().getMap().getField(2, 2));
        game.getActionHandler().setTargetField(game.getState().getMap().getField(4, 2));
        game.getState().getMap().getField(4, 2).setGadget(grappleCocktail);

        boolean successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertNull(game.getState().getMap().getField(4, 2).getGadget());
        Assert.assertTrue(game.getAllCharacters().get(0).getGadgets().contains(grappleCocktail));

        game.getSettings().setGrappleHitChance(0.0);// case unsuccessful

        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successful);

        game.getSettings().setGrappleHitChance(1.0);
        game.getActionHandler().setTargetField(game.getState().getMap().getField(6, 2));// case out of range

        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successful);


    }

    @Test
    public void testFogTinAction() {
        initializeMap();

        Gadget fogTin = null;
        for (Gadget gadget : game.getAllGadgets()) {
            if (gadget.getGadget() == GadgetEnum.FOG_TIN) {
                fogTin = gadget;
            }
        }


        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new GadgetAction(OperationEnum.GADGET_ACTION, true, new Point(-1, -1), game.getAllCharacters().get(0).getCharacterId(), GadgetEnum.FOG_TIN));

        game.getActionHandler().setGadget(fogTin);
        game.getActionHandler().setBaseField(game.getState().getMap().getField(2, 2));// case successfull in range
        game.getActionHandler().setTargetField(game.getState().getMap().getField(4, 2));

        int usages = fogTin.getUsages();
        boolean successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertTrue(game.getState().getMap().getField(4, 2).isFoggy());
        Assert.assertTrue(fogTin.getUsages() < usages);

        game.getActionHandler().setTargetField(game.getState().getMap().getField(2, 4));// hit a wall shall not be possible

        successful = game.getActionHandler().handleGadgetAction(message);
        Assert.assertFalse(successful);

        game.getActionHandler().setTargetField(game.getState().getMap().getField(7, 2));// out of range

        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successful);

    }

    @Test
    public void testJetpackAction() {
        initializeMap();

        Gadget jetpack = null;
        for (Gadget gadget : game.getAllGadgets()) {
            if (gadget.getGadget() == GadgetEnum.JETPACK) {
                jetpack = gadget;
            }
        }


        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new GadgetAction(OperationEnum.GADGET_ACTION, true, new Point(-1, -1), game.getAllCharacters().get(0).getCharacterId(), GadgetEnum.JETPACK));

        game.getActionHandler().setGadget(jetpack);// successfull jetpackAction
        game.getActionHandler().setBaseField(game.getState().getMap().getField(2, 2));
        game.getAllCharacters().get(1).setCoordinates(new Point(10, 2));// move m away
        game.getActionHandler().setTargetField(game.getState().getMap().getField(10, 1));

        game.getAllCharacters().get(0).setCoordinates(new Point(2, 2));

        int usages = jetpack.getUsages();
        boolean successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertEquals(game.getAllCharacters().get(0).getCoordinates(), new Point(10, 1));
        Assert.assertTrue(usages > jetpack.getUsages());
        Assert.assertNull(game.getActionHandler().getCharacterOnField(game.getActionHandler().getBaseField()));

        game.getActionHandler().setTargetField(game.getState().getMap().getField(10, 2));// m should block and jetpackAction should not be possible
        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successful);

        game.getActionHandler().setTargetField(game.getState().getMap().getField(10, 0));// no free field
        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successful);
    }

    @Test
    public void testWiretapWithEarplugsAction() {
        initializeMap();
        WiretapWithEarplugs wiretapWithEarplugs = null;
        for (Gadget gadget : game.getAllGadgets()) {
            if (gadget.getGadget() == GadgetEnum.WIRETAP_WITH_EARPLUGS) {
                wiretapWithEarplugs = (WiretapWithEarplugs) gadget;
            }
        }


        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new GadgetAction(OperationEnum.GADGET_ACTION, true, new Point(-1, -1), game.getAllCharacters().get(0).getCharacterId(), GadgetEnum.WIRETAP_WITH_EARPLUGS));

        game.getActionHandler().setGadget(wiretapWithEarplugs);

        game.getAllCharacters().get(0).setCoordinates(new Point(4, 2));
        game.getAllCharacters().get(1).setCoordinates(new Point(3, 2));
        game.getAllCharacters().get(0).addGadget(wiretapWithEarplugs);

        game.getActionHandler().setBaseField(game.getState().getMap().getField(4, 2));// case neighbours and everything works out
        game.getActionHandler().setTargetField((game.getState().getMap().getField(3, 2)));

        boolean successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertTrue(game.getAllCharacters().get(0).getGadgets().contains(wiretapWithEarplugs));
        boolean receivedWiretap= false;

        for (Gadget gadget : game.getAllCharacters().get(1).getGadgets()){
            if (gadget.getGadget() == GadgetEnum.WIRETAP_WITH_EARPLUGS)
                receivedWiretap = true;
        }

        Assert.assertTrue(receivedWiretap);
        Assert.assertTrue(wiretapWithEarplugs.getWorking());
        Assert.assertEquals(game.getAllCharacters().get(1).getCharacterId(), wiretapWithEarplugs.getActiveOn());

        game.getActionHandler().setTargetField(game.getState().getMap().getField(2, 2));

        successful = game.getActionHandler().handleGadgetAction(message);// case no character on target field

        Assert.assertFalse(successful);

        game.getAllCharacters().get(2).setCoordinates(new Point(2, 2));// case out of reach

        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successful);

    }

    @Test
    public void testChickenFeedAction() {
        initializeMap();

        Gadget chickenFeed = null;
        for (Gadget gadget : game.getAllGadgets()) {
            if (gadget.getGadget() == GadgetEnum.CHICKEN_FEED) {
                chickenFeed = gadget;
            }
        }


        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new GadgetAction(OperationEnum.GADGET_ACTION, true, new Point(-1, -1), game.getAllCharacters().get(0).getCharacterId(), GadgetEnum.CHICKEN_FEED));

        game.getAllCharacters().get(0).setCoordinates(new Point(2, 2));
        game.getAllCharacters().get(1).setCoordinates(new Point(3, 2));
        game.getPlayerOneCharacters().add(game.getAllCharacters().get(0));// case same fraction for player one and player two
        game.getPlayerOneCharacters().add(game.getAllCharacters().get(1));

        game.getActionHandler().setGadget(chickenFeed);
        game.getActionHandler().setBaseField(game.getState().getMap().getField(2, 2));
        game.getActionHandler().setTargetField(game.getState().getMap().getField(3, 2));
        int usages = chickenFeed.getUsages();
        boolean successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successful);
        Assert.assertTrue(usages > chickenFeed.getUsages());

        game.getPlayerOneCharacters().remove(game.getAllCharacters().get(0));
        game.getPlayerOneCharacters().remove(game.getAllCharacters().get(1));
        game.getPlayerTwoCharacters().add(game.getAllCharacters().get(0));
        game.getPlayerTwoCharacters().add(game.getAllCharacters().get(1));

        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successful);
        Assert.assertTrue(usages > chickenFeed.getUsages());

        game.getPlayerTwoCharacters().remove(game.getAllCharacters().get(0));
        game.getPlayerTwoCharacters().remove(game.getAllCharacters().get(1));

        game.getPlayerOneCharacters().add(game.getAllCharacters().get(0));// case different fractions
        game.getPlayerTwoCharacters().add(game.getAllCharacters().get(1));

        game.getAllCharacters().get(0).setIp(30);// case base has less ip than target
        game.getAllCharacters().get(1).setIp(100);

        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertEquals(Optional.ofNullable(game.getAllCharacters().get(0).getIp()), Optional.of(0));
        Assert.assertTrue(usages > chickenFeed.getUsages());

        game.getAllCharacters().get(0).setIp(100);// case base more less ip than target
        game.getAllCharacters().get(1).setIp(30);

        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertEquals(Optional.ofNullable(game.getAllCharacters().get(0).getIp()), Optional.of(170));
        Assert.assertTrue(usages > chickenFeed.getUsages());


        game.getAllCharacters().get(2).setCoordinates(new Point(4, 2));
        game.getActionHandler().setTargetField(game.getState().getMap().getField(4, 2));// case out of reach
        int usagesUntouched = chickenFeed.getUsages();
        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successful);
        Assert.assertTrue(usagesUntouched == chickenFeed.getUsages());


    }

    @Test
    public void testNuggetAction() {
        initializeMap();

        Gadget nugget = null;
        for (Gadget gadget : game.getAllGadgets()) {
            if (gadget.getGadget() == GadgetEnum.NUGGET) {
                nugget = gadget;
            }
        }


        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new GadgetAction(OperationEnum.GADGET_ACTION, true, new Point(-1, -1), game.getAllCharacters().get(0).getCharacterId(), GadgetEnum.NUGGET));

        game.getAllCharacters().get(0).setCoordinates(new Point(2, 2));
        game.getAllCharacters().get(1).setCoordinates(new Point(3, 2));

        game.getActionHandler().setGadget(nugget);
        game.getActionHandler().setBaseField(game.getState().getMap().getField(2, 2));
        game.getActionHandler().setTargetField(game.getState().getMap().getField(3, 2));

        game.getPlayerOneCharacters().add(game.getAllCharacters().get(0));
        game.setNpcs(new LinkedList<Character>());
        game.getNpcs().add(game.getAllCharacters().get(1));

        int usages = nugget.getUsages();
        boolean successful = game.getActionHandler().handleGadgetAction(message);// case same fractions and successful

        Assert.assertTrue(successful);
        Assert.assertTrue(usages > nugget.getUsages());
        Assert.assertFalse(game.getNpcs().contains(game.getAllCharacters().get(1)));
        Assert.assertTrue(game.getPlayerOneCharacters().contains(game.getAllCharacters().get(1)));

        game.getPlayerTwoCharacters().add(game.getAllCharacters().get(1));// case unsuccessful because target of other fraction

        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successful);
        Assert.assertFalse(game.getAllCharacters().get(0).getGadgets().contains(nugget));
        Assert.assertTrue(game.getAllCharacters().get(1).getGadgets().contains(nugget));


    }

    @Test
    public void testMirrorOfWildernessAction() {
        initializeMap();

        Gadget mow = null;
        for (Gadget gadget : game.getAllGadgets()) {
            if (gadget.getGadget() == GadgetEnum.MIRROR_OF_WILDERNESS) {
                mow = gadget;
            }
        }


        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new GadgetAction(OperationEnum.GADGET_ACTION, true, new Point(-1, -1), game.getAllCharacters().get(0).getCharacterId(), GadgetEnum.MIRROR_OF_WILDERNESS));

        game.getActionHandler().setGadget(mow);
        game.getActionHandler().setBaseField(game.getState().getMap().getField(3, 2));
        game.getActionHandler().setTargetField(game.getState().getMap().getField(4, 2));

        game.getAllCharacters().get(0).setCoordinates(new Point(3, 2));
        game.getAllCharacters().get(1).setCoordinates(new Point(4, 2));

        game.getPlayerOneCharacters().add(game.getAllCharacters().get(0));// case same fractions
        game.getPlayerOneCharacters().add(game.getAllCharacters().get(1));
        game.getAllCharacters().get(0).setIp(10);

        int usages = mow.getUsages();
        boolean successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertEquals(Optional.ofNullable(game.getAllCharacters().get(0).getIp()), Optional.of(5));
        Assert.assertEquals(Optional.ofNullable(game.getAllCharacters().get(1).getIp()), Optional.of(10));
        Assert.assertTrue(usages == mow.getUsages());

        game.getPlayerOneCharacters().remove(game.getAllCharacters().get(1));
        game.getPlayerTwoCharacters().add(game.getAllCharacters().get(1));// case different fractions
        game.getSettings().setMirrorSwapChance(1.0);//for having it return true

        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertEquals(Optional.ofNullable(game.getAllCharacters().get(0).getIp()), Optional.of(10));
        Assert.assertEquals(Optional.ofNullable(game.getAllCharacters().get(1).getIp()), Optional.of(5));
        Assert.assertTrue(usages > mow.getUsages());


    }

    @Test
    public void testCocktailAction() {
        initializeMap();

        Cocktail cocktail = new Cocktail(GadgetEnum.COCKTAIL);


        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new GadgetAction(OperationEnum.GADGET_ACTION, true, new Point(-1, -1), game.getAllCharacters().get(0).getCharacterId(), GadgetEnum.COCKTAIL));

        game.getActionHandler().setGadget(cocktail);
        game.getActionHandler().setBaseField(game.getState().getMap().getField(2, 2));
        game.getActionHandler().setTargetField(game.getState().getMap().getField(3, 2));

        game.getAllCharacters().get(0).setCoordinates(new Point(2, 2));// case spill cocktail on neighbour
        game.getAllCharacters().get(0).addGadget(cocktail);

        game.getAllCharacters().get(1).setCoordinates(new Point(3, 2));
        game.getSettings().setCocktailDodgeChance(1.0);

        int usages = cocktail.getUsages();
        boolean successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertTrue(usages > cocktail.getUsages());
        Assert.assertTrue(game.getAllCharacters().get(1).getProperties().contains(PropertyEnum.CLAMMY_CLOTHES));

        game.getAllCharacters().get(0).getGadgets().remove(cocktail);// case cocktail is on neighbour field and character grabs it
        game.getState().getMap().getField(2, 1).setGadget(cocktail);
        game.getActionHandler().setTargetField(game.getState().getMap().getField(2, 1));

        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertTrue(game.getAllCharacters().get(0).getGadgets().contains(cocktail));
        Assert.assertTrue(game.getState().getMap().getField(2, 1).getGadget() == null);

        game.getActionHandler().setTargetField(game.getState().getMap().getField(2, 2));// drink cocktail without any properties
        game.getAllCharacters().get(0).setHp(10);

        int hpBefore = game.getAllCharacters().get(0).getHp();
        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertTrue(hpBefore < game.getAllCharacters().get(0).getHp());

        cocktail.setPoisoned(true);// if poisoned

        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertTrue(successful);
        Assert.assertTrue(hpBefore == game.getAllCharacters().get(0).getHp());

        game.getActionHandler().setTargetField(game.getState().getMap().getField(5, 2));// case unsuccessful /out of range

        successful = game.getActionHandler().handleGadgetAction(message);

        Assert.assertFalse(successful);

    }

    @Test
    public void testBangAndBurn() {
        initializeMap();

        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new PropertyAction(OperationEnum.GADGET_ACTION, true, new Point(-1, -1), game.getAllCharacters().get(0).getCharacterId(), PropertyEnum.BANG_AND_BURN, null));

        game.getAllCharacters().get(0).setCoordinates(new Point(3, 2));
        game.getActionHandler().setTargetField(game.getState().getMap().getField(3, 3));
        game.getActionHandler().setBaseField(game.getState().getMap().getField(3, 2));

        boolean successful = game.getActionHandler().handlePropertyAction(PropertyEnum.BANG_AND_BURN, message, 0);// successful case

        Assert.assertTrue(successful);
        Assert.assertTrue(game.getState().getMap().getField(3, 3).isDestroyed());


    }

    @Test
    public void testObservation() {
        initializeMap();

        message = new GameOperationMessage(UUID.randomUUID(), MessageTypeEnum.GAME_OPERATION, new Date(), "nothing",
            new PropertyAction(OperationEnum.GADGET_ACTION, true, new Point(-1, -1), game.getAllCharacters().get(0).getCharacterId(), PropertyEnum.OBSERVATION, null));

        game.getAllCharacters().get(0).setCoordinates(new Point(2, 2));
        game.getAllCharacters().get(1).setCoordinates(new Point(4, 2));
        game.getActionHandler().setBaseField(game.getState().getMap().getField(2, 2));
        game.getActionHandler().setTargetField(game.getState().getMap().getField(4, 2));

        game.getSettings().setObservationSuccessChance(1.0);// case successful different fractions
        game.getPlayerOneCharacters().add(game.getAllCharacters().get(0));
        game.getPlayerTwoCharacters().add(game.getAllCharacters().get(1));

        boolean successful = game.getActionHandler().handlePropertyAction(PropertyEnum.OBSERVATION, message, 0);

        Assert.assertTrue(successful);
        Assert.assertTrue(game.getPlayerOneObservedCharacters().contains(game.getAllCharacters().get(1)));

        game.getPlayerOneObservedCharacters().remove(game.getAllCharacters().get(1));
        game.getPlayerTwoCharacters().remove(game.getAllCharacters().get(1));
        game.getPlayerOneCharacters().add(game.getAllCharacters().get(1));

        successful = game.getActionHandler().handlePropertyAction(PropertyEnum.OBSERVATION, message, 0);

        Assert.assertFalse(successful);
        Assert.assertFalse(game.getPlayerOneObservedCharacters().contains(game.getAllCharacters().get(1)));


    }


    @Test
    public void testBabySitterCheck() {
        initializeMap();

        game.getSettings().setBabysitterSuccessChance(1.0);
        game.getAllCharacters().get(0).setCoordinates(new Point(2, 2));
        game.getAllCharacters().get(0).addProperty(PropertyEnum.BABYSITTER);
        game.getAllCharacters().get(1).setCoordinates(new Point(3, 2));
        game.getPlayerOneCharacters().add(game.getAllCharacters().get(0));
        game.getPlayerOneCharacters().add(game.getAllCharacters().get(1));

        boolean successful = game.getActionHandler().babySitterCheck(game.getAllCharacters().get(1), game.getState().getMap().getField(3, 2));

        Assert.assertTrue(successful);// working player one

        game.getPlayerOneCharacters().remove(0);
        game.getPlayerOneCharacters().remove(0);

        game.getPlayerTwoCharacters().add(game.getAllCharacters().get(0));
        game.getPlayerTwoCharacters().add(game.getAllCharacters().get(1));

        successful = game.getActionHandler().babySitterCheck(game.getAllCharacters().get(1), game.getState().getMap().getField(3, 2));

        Assert.assertTrue(successful);// working player two


        game.getPlayerTwoCharacters().remove(0);
        game.getPlayerTwoCharacters().remove(0);

        game.getPlayerOneCharacters().add(game.getAllCharacters().get(0));
        game.getPlayerTwoCharacters().add(game.getAllCharacters().get(1));

        successful = game.getActionHandler().babySitterCheck(game.getAllCharacters().get(1), game.getState().getMap().getField(3, 2));

        Assert.assertFalse(successful);// not working different fractions

    }


    @Test
    public void testCharacterOnField() {
        initializeMap();

        Character jamesBondTest;
        Field jamesBondField = game.getState().getMap().getField(2, 1);

        Character jamesBond = game.getAllCharacters().get(0);
        jamesBond.setCoordinates(new Point(2,1));
        jamesBondTest = game.getActionHandler().getCharacterOnField(jamesBondField);

        Assert.assertEquals(jamesBond, jamesBondTest);

        Character mTest;
        Field mField = game.getState().getMap().getField(2, 2);
        Character m = game.getAllCharacters().get(1);
        m.setCoordinates(new Point(2,2));
        mTest = game.getActionHandler().getCharacterOnField(mField);

        Assert.assertEquals(m, mTest);
    }

    @Test
    public void testIsInNeighbours() {
        initializeMap();

        Field baseField = game.getState().getMap().getField(2, 1);
        Field neighbour = game.getState().getMap().getField(2, 2);

        Assert.assertTrue(game.getActionHandler().isInNeighbours(baseField, neighbour));
    }


    @Test
    public void testGetIndex() {
        initializeMap();

        for (int i = 0; i < game.getState().getMap().getMap().length; i++) {
            for (int j = 0; j < game.getState().getMap().getMap()[i].length; ) {
                Assert.assertEquals(i, game.getActionHandler().getIndex(game.getState().getMap().getMap()[i][j])[0]);
                Assert.assertEquals(j, game.getActionHandler().getIndex(game.getState().getMap().getMap()[i][j])[1]);
                j++;

            }
        }
    }

    public void initializeMap() {
        gson = new Gson();

        level = readLevel("src/main/java/config/levelTest.scenario");
        settings = readSettings("src/main/java/config/matchconfig.match");
        character_settings = readCharacter_settings("src/main/java/config/charactersTest.json");

        game = new Game(UUID.randomUUID(), new LinkedList<>(), new LinkedList<>(), level, settings, character_settings);

        // create the first state where also the map is generated
        game.setState(new State(0, generateFieldMap(), null, new HashSet<>(game.getAllCharacters()), new Point(-1, -1), new Point(-1, -1)));

        UUID jamesbondID = UUID.randomUUID();

        UUID mId = UUID.randomUUID();

        UUID qId = UUID.randomUUID();
        game.getAllCharacters().get(0).setCharacterId(jamesbondID);
        game.getAllCharacters().get(1).setCharacterId(mId);
        game.getAllCharacters().get(2).setCharacterId(qId);

        game.getAllCharacters().get(0).setCoordinates(new Point(2, 2));
        game.getAllCharacters().get(1).setCoordinates(new Point(3, 2));
        game.getAllCharacters().get(2).setCoordinates(new Point(4, 2));


        //initialize action Handler
        game.setActionHandler(new ActionHandler(game.getState().getMap(), game));


    }

    @Test
    public void testIsInRange() {
        initializeMap();

        Field jamesField = game.getActionHandler().getFieldOfCharacter(game.getAllCharacters().get(0));
        Field mField = game.getActionHandler().getFieldOfCharacter(game.getAllCharacters().get(1));

        Assert.assertTrue(game.getActionHandler().isInRange(jamesField, mField, 1));

        int range = 2;
        for (int i = 0; i < game.getState().getMap().getMap().length; i++) {
            for (int j = 0; j < game.getState().getMap().getMap()[i].length; j++) {
                if (j <= 4 && i <= 4) {
                    Assert.assertTrue(game.getActionHandler().isInRange(game.getState().getMap().getField(2, 2), game.getState().getMap().getField(i, j), range));
                }
                if (i > 4 || j > 4) {
                    Assert.assertFalse(game.getActionHandler().isInRange(game.getState().getMap().getField(2, 2), game.getState().getMap().getField(i, j), range));
                }
            }
        }
    }

    @Test
    public void testIsInLineOfSight() {
        initializeMap();
        Field jamesField = game.getActionHandler().getFieldOfCharacter(game.getAllCharacters().get(0));
        Field mField = game.getActionHandler().getFieldOfCharacter(game.getAllCharacters().get(2));

        Assert.assertTrue(game.getActionHandler().isInLineOfSight1(jamesField, mField, false));

        Field jamesField1 = game.getActionHandler().getFieldOfCharacter(game.getAllCharacters().get(0));
        Field mField1 = game.getActionHandler().getFieldOfCharacter(game.getAllCharacters().get(2));

        Assert.assertFalse(game.getActionHandler().isInLineOfSight1(jamesField1, mField1, true));
    }


    @Test
    public void testGetDistance() {
        initializeMap();
        Point point1 = new Point(3, 1);
        Point point2 = new Point(4, 2);

        System.out.println(game.getActionHandler().getDistance(point1.x, point2.x, point1.y, point2.y));
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
     * Generate the FieldMap containing Fields from FieldStateEnum in Scenario.
     *
     * @return generated Map
     */
    private FieldMap generateFieldMap() {
        FieldMap result = new FieldMap(new Field[game.getLevel().getScenario().length][]);
        int safeIndex = 1;
        List<Point> tresoreIndex = new LinkedList<>();

        for (int i = 0; i < result.getMap().length; i++) {
            result.getMap()[i] = new Field[game.getLevel().getScenario()[i].length];
            for (int j = 0; j < result.getMap()[i].length; j++) {
                result.getMap()[i][j] = new Field(game.getLevel().getScenario()[i][j], null, false, false, 0, 0, false, false);

                // Roulette Tische mit Chips befüllen
                if (result.getMap()[i][j].getState() == FieldStateEnum.ROULETTE_TABLE) {
                    result.getMap()[i][j].setChipAmount(150);
                }

                // Tresore zwischenspeichern
                if (result.getMap()[i][j].getState() == FieldStateEnum.SAFE) {
                    tresoreIndex.add(new Point(i, j));
                }
            }
        }

        // Tresore zufällig durchnummerieren
        Collections.shuffle(tresoreIndex);
        for (Point p : tresoreIndex) {
            result.getMap()[p.x][p.y].setSafeIndex(safeIndex++);
        }

        return result;
    }

    /**
     * read the complete content of a file;
     *
     * @param path -path to the file
     * @return - String with content of the file
     * @throws FileNotFoundException
     */
    private String readFile(String path) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        try {
            StringBuilder str = new StringBuilder();
            String content;

            while ((content = reader.readLine()) != null) {
                str.append(content);
            }
            return str.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * read level data
     *
     * @param path - path to .scenario file
     * @return Scenario object created from file
     */
    private Scenario readLevel(String path) {
        try {
            String settings = readFile(path);
            return gson.fromJson(settings, Scenario.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * read match config data
     *
     * @param path - path to .match file
     * @return Matchconfig object created from file
     */
    private Matchconfig readSettings(String path) {
        try {
            String settings = readFile(path);
            return gson.fromJson(settings, Matchconfig.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * read character settings data
     *
     * @param path - path to .json file
     * @return CharacterInformation Array created from file
     */
    private CharacterInformation[] readCharacter_settings(String path) {
        try {
            String chars = readFile(path);
            return gson.fromJson(chars, CharacterInformation[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}