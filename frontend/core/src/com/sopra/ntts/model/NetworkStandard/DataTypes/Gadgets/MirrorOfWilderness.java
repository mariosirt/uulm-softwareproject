package com.sopra.ntts.model.NetworkStandard.DataTypes.Gadgets;


import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;

public class MirrorOfWilderness extends Gadget {
    private double mowProbability;

    public MirrorOfWilderness(GadgetEnum gadget, Integer usages) {
        super(gadget);
    }

    public double getMowProbability() {
        return mowProbability;
    }

    public void setMowProbability(double mowProbability) {
        this.mowProbability = mowProbability;
    }
}
