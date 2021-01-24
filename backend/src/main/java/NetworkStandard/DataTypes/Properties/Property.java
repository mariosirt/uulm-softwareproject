package NetworkStandard.DataTypes.Properties;

import NetworkStandard.DataTypes.PropertyEnum;

import java.util.Objects;

/**
 * Property that a character can posses.
 *
 * @author Meriton Dzemaili
 */
public class Property {

    private PropertyEnum property;

    public Property(PropertyEnum property) {
        this.property = property;

    }

    @Override
    public String toString() {
        return "Property.java{" +
            "propertie=" + property +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property1 = (Property) o;
        return property == property1.property;
    }

    @Override
    public int hashCode() {
        return Objects.hash(property);
    }

    public PropertyEnum getProperty() {
        return property;
    }

    public void setProperty(PropertyEnum property) {
        this.property = property;
    }
}