
package NetworkStandard.DataTypes.Properties;

import NetworkStandard.DataTypes.PropertyEnum;

/**
 * Property that a character can posses.
 *
 * @author Meriton Dzemaili
 */
public class RobustStomach extends Property {

    private Integer CocktailHP;

    public RobustStomach(PropertyEnum property) {
        super(property);
    }

    public Integer getCocktailHP() {
        return CocktailHP;
    }

    public void setCocktailHP(Integer cocktailHP) {
        CocktailHP = cocktailHP;
    }
}