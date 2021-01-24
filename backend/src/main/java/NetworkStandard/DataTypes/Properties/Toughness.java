
package NetworkStandard.DataTypes.Properties;

import NetworkStandard.DataTypes.PropertyEnum;

/**
 * Property that a character can posses.
 *
 * @author Meriton Dzemaili
 */
public class Toughness extends Property {

    private Integer damage;

    public Toughness(PropertyEnum property) {
        super(property);
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }
}