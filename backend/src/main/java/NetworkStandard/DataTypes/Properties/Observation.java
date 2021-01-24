package NetworkStandard.DataTypes.Properties;

import NetworkStandard.DataTypes.PropertyEnum;

/**
 * Property that a character can posses.
 *
 * @author Meriton Dzemaili
 */
public class Observation extends Property {
    private double observationProbability;

    public Observation(PropertyEnum property) {
        super(property);
    }

    public double getObservationProbability() {
        return observationProbability;
    }

    public void setObservationProbability(double observationProbability) {
        this.observationProbability = observationProbability;
    }
}
