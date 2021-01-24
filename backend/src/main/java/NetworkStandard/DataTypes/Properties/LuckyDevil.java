
package NetworkStandard.DataTypes.Properties;

import NetworkStandard.DataTypes.PropertyEnum;

/**
 * Property that a character can posses.
 *
 * @author Meriton Dzemaili
 */
public class LuckyDevil extends Property {

    private double rouletteWinChanche = 23 / 37;

    public LuckyDevil(PropertyEnum property) {
        super(property);
    }

    public double getRouletteWinChanche() {
        return rouletteWinChanche;
    }

}