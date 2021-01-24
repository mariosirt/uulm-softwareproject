package com.sopra.ntts.model.NetworkStandard.DataTypes.Gadgets;

import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;

public class MothballPouch extends Gadget {
    private int mothballPouchRange;
    private int mothballPouchDamage;

    public MothballPouch(GadgetEnum gadget, Integer usages) {
        super(gadget);
    }

    public int getMothballPouchRange() {
        return mothballPouchRange;
    }

    public int getMothballPouchDamage() {
        return mothballPouchDamage;
    }

    public void setMothballPouchRange(int mothballPouchRange) {
        this.mothballPouchRange = mothballPouchRange;
    }

    public void setMothballPouchDamage(int mothballPouchDamage) {
        this.mothballPouchDamage = mothballPouchDamage;
    }
}
