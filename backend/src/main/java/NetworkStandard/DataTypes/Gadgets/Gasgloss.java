package NetworkStandard.DataTypes.Gadgets;

import NetworkStandard.DataTypes.GadgetEnum;
/**
 * @author Marios Sirtmatsis
 */
public class Gasgloss extends Gadget {
    private int irritantGasDamage;

    public Gasgloss(GadgetEnum gadget) {
        super(gadget);
    }

    public int getIrritantGasDamage() {
        return irritantGasDamage;
    }

    public void setIrritantGasDamage(int irritantGasDamage) {
        this.irritantGasDamage = irritantGasDamage;
    }
}
