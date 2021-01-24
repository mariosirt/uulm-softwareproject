package com.sopra.ntts.model.NetworkStandard.DataTypes.Gadgets;


import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;

public class LaserCompact extends Gadget {
    private double laserCompactProbability;

    public LaserCompact(GadgetEnum gadget, Integer usages) {
        super(gadget);
    }

    public double getLaserCompactProbability() {
        return laserCompactProbability;
    }

    public void setLaserCompactProbability(double laserCompactProbability) {
        this.laserCompactProbability = laserCompactProbability;
    }
}
