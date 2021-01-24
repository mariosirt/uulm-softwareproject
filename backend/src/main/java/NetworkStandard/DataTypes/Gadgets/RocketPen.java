package NetworkStandard.DataTypes.Gadgets;

import NetworkStandard.DataTypes.GadgetEnum;
/**
 * @author Marios Sirtmatsis
 */
public class RocketPen extends Gadget {
    private int rocketDamage;

    public RocketPen(GadgetEnum gadget) {
        super(gadget);
    }

    public int getRocketDamage() {
        return rocketDamage;
    }

    public void setRocketDamage(int rocketDamage) {
        this.rocketDamage = rocketDamage;
    }
}
