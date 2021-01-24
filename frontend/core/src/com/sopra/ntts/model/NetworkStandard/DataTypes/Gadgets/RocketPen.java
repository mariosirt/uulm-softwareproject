package com.sopra.ntts.model.NetworkStandard.DataTypes.Gadgets;

import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;

public class RocketPen extends Gadget {
    private int rocketDamage;

    public RocketPen(GadgetEnum gadget, Integer usages) {
        super(gadget);
    }

    public int getRocketDamage() {
        return rocketDamage;
    }

    public void setRocketDamage(int rocketDamage) {
        this.rocketDamage = rocketDamage;
    }
}
