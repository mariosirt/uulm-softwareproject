package NetworkStandard.DataTypes.Gadgets;

import NetworkStandard.DataTypes.GadgetEnum;
/**
 * @author Marios Sirtmatsis
 */
public class FogTin extends Gadget {
    private int fogTinRange;

    public FogTin(GadgetEnum gadget) {
        super(gadget);
    }

    public int getFogTinRange() {
        return fogTinRange;
    }

    public void setFogTinRange(int fogTinRange) {
        this.fogTinRange = fogTinRange;
    }
}
