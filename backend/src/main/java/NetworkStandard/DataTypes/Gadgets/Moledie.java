package NetworkStandard.DataTypes.Gadgets;

import NetworkStandard.DataTypes.GadgetEnum;
/**
 * @author Marios Sirtmatsis
 */
public class Moledie extends Gadget {
    private int moledieThrowRange;

    public Moledie(GadgetEnum gadget) {
        super(gadget);
    }

    public int getMoledieThrowRange() {
        return moledieThrowRange;
    }

    public void setMoledieThrowRange(int moledieThrowRange) {
        this.moledieThrowRange = moledieThrowRange;
    }
}
