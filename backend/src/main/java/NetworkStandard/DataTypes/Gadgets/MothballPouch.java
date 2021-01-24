package NetworkStandard.DataTypes.Gadgets;

import NetworkStandard.DataTypes.GadgetEnum;
/**
 * @author Marios Sirtmatsis
 */
public class MothballPouch extends Gadget {
    private int mothballPouchRange;
    private int mothballPouchDamage;

    public MothballPouch(GadgetEnum gadget) {
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
