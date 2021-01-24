package com.sopra.ntts.model.NetworkStandard.DataTypes.Gadgets;


import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;

public class BowlerBlade extends Gadget {
    private int hatThrowRange;
    private double hatHitPropability;
    private int hatDamage;

    public BowlerBlade(GadgetEnum gadget, Integer usages) {
        super(gadget);
    }

    public int getHatThrowRange() {
        return hatThrowRange;
    }

    public double getHatHitPropability() {
        return hatHitPropability;
    }

    public int getHatDamage() {
        return hatDamage;
    }

    public void setHatThrowRange(int hatThrowRange) {
        this.hatThrowRange = hatThrowRange;
    }

    public void setHatHitPropability(double hatHitPropability) {
        this.hatHitPropability = hatHitPropability;
    }

    public void setHatDamage(int hatDamage) {
        this.hatDamage = hatDamage;
    }
}
