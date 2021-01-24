
package NetworkStandard.DataTypes.Properties;

import NetworkStandard.DataTypes.PropertyEnum;

/**
 * Property that a character can posses.
 *
 * @author Meriton Dzemaili
 */
public class ClammyClothes extends Property {

    private double successProperbility;

    public ClammyClothes(PropertyEnum property) {
        super(property);
    }

    public double getSuccessProperbility() {
        return successProperbility;
    }
}