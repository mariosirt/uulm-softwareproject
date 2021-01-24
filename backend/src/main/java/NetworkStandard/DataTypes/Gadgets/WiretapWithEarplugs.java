package NetworkStandard.DataTypes.Gadgets;

import NetworkStandard.DataTypes.GadgetEnum;

import java.util.UUID;
/**
 * @author Marios Sirtmatsis
 */
public class WiretapWithEarplugs extends Gadget {
    private Boolean working;    //describes whether the gadget still works
    private UUID activeOn;      //desribes on which target the gadget ist active. If the gadget isn't activated, set the field to null.
    private double wireTapProbability;
    private boolean isWiretap;

    /**
     * Constructor.
     *
     * @param gadget - type of gadget
     */
    public WiretapWithEarplugs(GadgetEnum gadget) {
        super(gadget);
    }

    public double getWireTapProbability() {
        return wireTapProbability;
    }

    public void setWireTapProbability(double wireTapProbability) {
        this.wireTapProbability = wireTapProbability;
    }

    public Boolean getWorking() {
        return working;
    }

    public UUID getActiveOn() {
        return activeOn;
    }

    public void setWorking(Boolean working) {
        this.working = working;
    }

    public void setActiveOn(UUID activeOn) {
        this.activeOn = activeOn;
    }

    public boolean isWiretap() {
        return isWiretap;
    }

    public void setWiretap(boolean wiretap) {
        isWiretap = wiretap;
    }
}
