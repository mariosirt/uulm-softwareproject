package NetworkStandard.DataTypes.Properties;

import NetworkStandard.DataTypes.PropertyEnum;

/**
 * Property that a character can posses.
 *
 * @author Meriton Dzemaili
 */
public class Babysitter extends Property {
    private double babysitterProbability;

    public Babysitter(PropertyEnum property) {
        super(property);
    }


    public double getBabysitterProbability() {
        return babysitterProbability;
    }

    public void setBabysitterProbability(double babysitterProbability) {
        this.babysitterProbability = babysitterProbability;
    }
}