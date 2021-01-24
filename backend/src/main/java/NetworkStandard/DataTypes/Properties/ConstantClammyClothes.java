package NetworkStandard.DataTypes.Properties;

import NetworkStandard.DataTypes.PropertyEnum;

/**
 * Property that a character can posses.
 *
 * @author Meriton Dzemaili
 */
public class ConstantClammyClothes extends Property {

    private double successProperbility;

    public ConstantClammyClothes(PropertyEnum property) {
        super(property);
    }

    public double getSuccessProperbility() {
        return successProperbility;
    }

}