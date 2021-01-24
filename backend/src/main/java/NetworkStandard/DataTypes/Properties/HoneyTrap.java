package NetworkStandard.DataTypes.Properties;

import NetworkStandard.DataTypes.PropertyEnum;

/**
 * Property that a character can posses.
 *
 * @author Meriton Dzemaili
 */
public class HoneyTrap extends Property {
    private double honeyTrapProbability;

    public HoneyTrap(PropertyEnum property) {
        super(property);
    }

    public double getHoneyTrapProbability() {
        return honeyTrapProbability;
    }

    public void setHoneyTrapProbability(double honeyTrapProbability) {
        this.honeyTrapProbability = honeyTrapProbability;
    }
}
