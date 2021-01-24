package NetworkStandard.DataTypes.Gadgets;

import NetworkStandard.DataTypes.GadgetEnum;
import NetworkStandard.DataTypes.MatchConfig.Matchconfig;
import com.google.gson.Gson;

import java.io.*;
import java.util.LinkedList;
import java.util.Objects;

/**
 * @author Marios Sirtmatsis and Christian Wendlinger
 */


public class Gadget {
    private GadgetEnum gadget;      //type of the gadget and their action
    private Integer usages;         //describe how often the player can use his gadget.
    private static Matchconfig settings;


    /**
     * Constructor.
     *
     * @param gadget - type of gadget
     */
    public Gadget(GadgetEnum gadget) {
        this.gadget = gadget;
        settings = this.readSettings("src/main/java/config/matchconfig.match");
        initUsages();


    }


    /**
     * Getter and Setter.
     */

    public GadgetEnum getGadget() {
        return gadget;
    }

    public void setGadget(GadgetEnum gadget) {
        this.gadget = gadget;
    }

    public Integer getUsages() {
        return usages;
    }

    public void setUsages(Integer usages) {
        this.usages = usages;
    }

    /**
     * Equals and Hashcode.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gadget gadget1 = (Gadget) o;
        return getGadget() == gadget1.getGadget() &&
                Objects.equals(getUsages(), gadget1.getUsages());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGadget(), getUsages());
    }

    /**
     * Reduce usages of Gadget but 1 but not below 0;
     */
    public void decrementUsages() {
        this.usages = Math.max(0, usages - 1);
    }

    /**
     * Generate all available Gadgets excluding Necklace.
     *
     * @return all Gadgets as List.
     */
    public static LinkedList<Gadget> generateAllGadgets() {
        LinkedList<Gadget> gadgets = new LinkedList<>();


        // -1 usages = infty
        gadgets.add(new Gadget(GadgetEnum.HAIRDRYER));
        gadgets.add(new Moledie(GadgetEnum.MOLEDIE));
        gadgets.add(new Gadget(GadgetEnum.TECHNICOLOUR_PRISM));
        gadgets.add(new BowlerBlade(GadgetEnum.BOWLER_BLADE));
        gadgets.add(new Gadget(GadgetEnum.MAGNETIC_WATCH));
        gadgets.add(new Gadget(GadgetEnum.POISON_PILLS));
        gadgets.add(new LaserCompact(GadgetEnum.LASER_COMPACT));
        gadgets.add(new RocketPen(GadgetEnum.ROCKET_PEN));
        gadgets.add(new Gasgloss(GadgetEnum.GAS_GLOSS));
        gadgets.add(new MothballPouch(GadgetEnum.MOTHBALL_POUCH));
        gadgets.add(new FogTin(GadgetEnum.FOG_TIN));
        gadgets.add(new Grapple(GadgetEnum.GRAPPLE));
        gadgets.add(new Gadget(GadgetEnum.JETPACK));
        gadgets.add(new WiretapWithEarplugs(GadgetEnum.WIRETAP_WITH_EARPLUGS));
        gadgets.add(new Gadget(GadgetEnum.CHICKEN_FEED));
        gadgets.add(new Gadget(GadgetEnum.NUGGET));
        gadgets.add(new MirrorOfWilderness(GadgetEnum.MIRROR_OF_WILDERNESS));
        gadgets.add(new Gadget(GadgetEnum.POCKET_LITTER));
        gadgets.add(new Gadget(GadgetEnum.MASK));

        for (Gadget gadget : gadgets) {
            switch (gadget.getGadget()) {
                case BOWLER_BLADE:
                    ((BowlerBlade) gadget).setHatDamage(settings.getBowlerBladeDamage());
                    ((BowlerBlade) gadget).setHatHitPropability(settings.getBowlerBladeHitChance());
                    ((BowlerBlade) gadget).setHatThrowRange(settings.getBowlerBladeRange());
                    break;
                case COCKTAIL:
                    ((Cocktail) gadget).setCocktailHp(settings.getCocktailHp());
                    ((Cocktail) gadget).setCocktailDodgeProbability(settings.getCocktailDodgeChance());
                    break;
                case FOG_TIN:
                    ((FogTin) gadget).setFogTinRange(settings.getFogTinRange());
                    break;
                case GAS_GLOSS:
                    ((Gasgloss) gadget).setIrritantGasDamage(settings.getGasGlossDamage());
                    break;
                case GRAPPLE:
                    ((Grapple) gadget).setGrappleProbability(settings.getGrappleHitChance());
                    ((Grapple) gadget).setGrappleRange(settings.getGrappleRange());
                    break;
                case LASER_COMPACT:
                    ((LaserCompact) gadget).setLaserCompactProbability(settings.getLaserCompactHitChance());
                    break;
                case MIRROR_OF_WILDERNESS:
                    ((MirrorOfWilderness) gadget).setMowProbability(settings.getMirrorSwapChance());
                    break;
                case MOLEDIE:
                    ((Moledie) gadget).setMoledieThrowRange(settings.getMoledieRange());
                    break;
                case MOTHBALL_POUCH:
                    ((MothballPouch) gadget).setMothballPouchDamage(settings.getMothballPouchDamage());
                    ((MothballPouch) gadget).setMothballPouchRange(settings.getMothballPouchRange());
                    break;
                case ROCKET_PEN:
                    ((RocketPen) gadget).setRocketDamage(settings.getRocketPenDamage());
                    break;
                case WIRETAP_WITH_EARPLUGS:
                    ((WiretapWithEarplugs) gadget).setWireTapProbability(settings.getWiretapWithEarplugsFailChance());
                    ((WiretapWithEarplugs) gadget).setWiretap(false);
                    ((WiretapWithEarplugs) gadget).setWorking(false);
                    break;
            }
        }
        return gadgets;
    }


    public void initUsages() {
        switch (getGadget()) {
            case HAIRDRYER:
            case MOLEDIE:
            case BOWLER_BLADE:
            case MAGNETIC_WATCH:
            case LASER_COMPACT:
            case GRAPPLE:
            case POCKET_LITTER:
            case DIAMOND_COLLAR:
            case MASK:
                usages = -1;
                break;
            case TECHNICOLOUR_PRISM:
            case ROCKET_PEN:
            case GAS_GLOSS:
            case FOG_TIN:
            case JETPACK:
            case WIRETAP_WITH_EARPLUGS:
            case CHICKEN_FEED:
            case NUGGET:
            case MIRROR_OF_WILDERNESS:
            case COCKTAIL:
                usages = 1;
                break;
            case MOTHBALL_POUCH:
            case POISON_PILLS:
                usages = 5;
                break;
        }
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
     * read match config data
     *
     * @param path - path to .match file
     * @return Matchconfig object created from file
     */
    private Matchconfig readSettings(String path) {
        Gson gson = new Gson();
        try {
            String settings = readFile(path);
            return gson.fromJson(settings, Matchconfig.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * toString.
     */
    @Override
    public String toString() {
        return "Gadget{" +
                "gadget=" + gadget +
                ", usages=" + usages +
                '}';
    }
}
