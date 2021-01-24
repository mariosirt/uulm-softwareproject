package com.sopra.ntts.model.NetworkStandard.DataTypes.Gadgets;

import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;

public class Gasgloss extends Gadget {
    private int irritantGasDamage;

    public Gasgloss(GadgetEnum gadget, Integer usages) {
        super(gadget);
    }

    public int getIrritantGasDamage() {
        return irritantGasDamage;
    }

    public void setIrritantGasDamage(int irritantGasDamage) {
        this.irritantGasDamage = irritantGasDamage;
    }


}
