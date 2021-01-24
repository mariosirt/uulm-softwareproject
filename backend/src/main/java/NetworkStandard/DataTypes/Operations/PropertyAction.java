package NetworkStandard.DataTypes.Operations;


import NetworkStandard.DataTypes.PropertyEnum;

import java.awt.*;
import java.util.UUID;

/**
 * A class for a PropertyOperation (OperationEnum PROPERTY_ACTION)
 * <p>
 * usedProperty: identifies the use of a property- (e.g. BANG_AND_BURN or OBSERVATION)
 */

public class PropertyAction extends Operation {
    private PropertyEnum usedProperty;
    Boolean isEnemy;

    public PropertyAction(OperationEnum type, Boolean successful, Point target, UUID characterId, PropertyEnum usedProperty, Boolean isEnemy) {
        super(type, successful, target, characterId);
        this.usedProperty = usedProperty;
        this.isEnemy = isEnemy;
    }


    public PropertyEnum getUsedProperty() {
        return usedProperty;
    }

    public void setUsedProperty(PropertyEnum usedProperty) {
        this.usedProperty = usedProperty;
    }

    public Boolean getEnemy() {
        return isEnemy;
    }

    public void setEnemy(Boolean enemy) {
        isEnemy = enemy;
    }

    @Override
    public String toString() {
        return "PropertyAction{" +
            "usedProperty=" + usedProperty +
            ", isEnemy=" + isEnemy +
            '}';
    }
}
