package com.sopra.ntts.model.NetworkStandard.DataTypes.Gadgets;


import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;

public class Moledie extends Gadget {
    private int moledieThrowRange;

    public Moledie(GadgetEnum gadget, Integer usages) {
        super(gadget);
    }

    public int getMoledieThrowRange() {
        return moledieThrowRange;
    }

    public void setMoledieThrowRange(int moledieThrowRange) {
        this.moledieThrowRange = moledieThrowRange;
    }
}
