package NetworkStandard.DataTypes.Properties;

import NetworkStandard.DataTypes.PropertyEnum;

/**
 * Property that a character can posses.
 *
 * @author Meriton Dzemaili
 */
public class JINX extends Property {

    private double rouletteWinChanche = 13 / 37;

    public JINX(PropertyEnum property) {
        super(property);
    }

    public double getRouletteWinChanche() {
        return rouletteWinChanche;
    }

}