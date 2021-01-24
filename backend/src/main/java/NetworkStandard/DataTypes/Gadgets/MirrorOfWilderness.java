package NetworkStandard.DataTypes.Gadgets;

import NetworkStandard.DataTypes.GadgetEnum;

/**
 * @author Marios Sirtmatsis
 */
public class MirrorOfWilderness extends Gadget {
    private double mowProbability;

    public MirrorOfWilderness(GadgetEnum gadget) {
        super(gadget);
    }

    public double getMowProbability() {
        return mowProbability;
    }

    public void setMowProbability(double mowProbability) {
        this.mowProbability = mowProbability;
    }
}
