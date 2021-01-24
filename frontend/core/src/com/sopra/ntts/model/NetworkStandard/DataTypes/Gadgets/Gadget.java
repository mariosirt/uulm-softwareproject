package com.sopra.ntts.model.NetworkStandard.DataTypes.Gadgets;

import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;

import java.util.Objects;

public class Gadget {
    private GadgetEnum gadget;      //type of the gadget and their action
    private Integer usages;         //describe how often the player can use his gadget.

    /**
     * Constructor.
     *
     * @param gadget - type of gadget
     */

    public Gadget(GadgetEnum gadget) {
        this.gadget = gadget;
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
     * toString.
     */
    @Override
    public String toString() {
        return "Gadget{" +
                "gadget=" + gadget +
                ", usages=" + usages +
                '}';
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
                usages = 1;
                break;
            case MOTHBALL_POUCH:
            case POISON_PILLS:
                usages = 5;
                break;
        }
    }
}
