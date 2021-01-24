package NetworkStandard.Characters;

import NetworkStandard.DataTypes.PropertyEnum;

import java.util.Objects;
import java.util.Set;

public class CharacterDescription {

    /**
     * This class describes a character.
     * name and description are not allowed to be null
     */
    private String name;
    private String description;
    private GenderEnum gender;
    private Set<PropertyEnum> features;

    /**
     * constructor
     */
    public CharacterDescription(String name, String description, GenderEnum gender, Set<PropertyEnum> features) {
        this.name = name;
        this.description = description;
        this.gender = gender;
        this.features = features;
    }

    /**
     * getter and setter
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public Set<PropertyEnum> getFeatures() {
        return features;
    }

    public void setFeatures(Set<PropertyEnum> features) {
        this.features = features;
    }

    /**
     * equals and hashCode
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacterDescription that = (CharacterDescription) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                getGender() == that.getGender() &&
                Objects.equals(getFeatures(), that.getFeatures());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getGender(), getFeatures());
    }

    /**
     * toString
     */
    @Override
    public String toString() {
        return "CharacterDescription{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", gender=" + gender +
                ", features=" + features +
                '}';
    }
}
