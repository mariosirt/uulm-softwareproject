package com.sopra.ntts.model.NetworkStandard.DataTypes.Gadgets;


import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;

public class Grapple extends Gadget {
    private int grappleRange;
    private double grappleProbability;

    public Grapple(GadgetEnum gadget, Integer usages) {
        super(gadget);
    }

    public int getGrappleRange() {
        return grappleRange;
    }

    public double getGrappleProbability() {
        return grappleProbability;
    }

    public void setGrappleRange(int grappleRange) {
        this.grappleRange = grappleRange;
    }

    public void setGrappleProbability(double grappleProbability) {
        this.grappleProbability = grappleProbability;
    }
}
