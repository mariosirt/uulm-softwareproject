package com.sopra.ntts.model.NetworkStandard.DataTypes.Gadgets;

import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;

import java.util.UUID;

public class WiretapWithEarplugs extends Gadget {
    Boolean working;    //describes whether the gadget still works
    UUID activeOn;      //desribes on which target the gadget ist active. If the gadget isn't activated, set the field to null.
    double wireTapProbability;

    /**
     * Constructor.
     *
     * @param gadget - type of gadget
     * @param usages - how often the gadget may be used
     */
    public WiretapWithEarplugs(GadgetEnum gadget, Integer usages) {
        super(gadget);
    }

    public double getWireTapProbability() {
        return wireTapProbability;
    }

    public void setWireTapProbability(double wireTapProbability) {
        this.wireTapProbability = wireTapProbability;
    }
}
