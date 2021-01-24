package NetworkStandard.DataTypes.Gadgets;

import NetworkStandard.DataTypes.GadgetEnum;
/**
 * @author Marios Sirtmatsis
 */
public class LaserCompact extends Gadget {
    private double laserCompactProbability;

    public LaserCompact(GadgetEnum gadget) {
        super(gadget);
    }

    public double getLaserCompactProbability() {
        return laserCompactProbability;
    }

    public void setLaserCompactProbability(double laserCompactProbability) {
        this.laserCompactProbability = laserCompactProbability;
    }
}
