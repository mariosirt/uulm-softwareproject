package com.sopra.ntts.model.NetworkStandard.DataTypes.Gadgets;


import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;

public class FogTin extends Gadget {
    private int fogTinRange;

    public FogTin(GadgetEnum gadget, Integer usages) {
        super(gadget);
    }

    public int getFogTinRange() {
        return fogTinRange;
    }

    public void setFogTinRange(int fogTinRange) {
        this.fogTinRange = fogTinRange;
    }
}
