package NetworkStandard.Characters;

import NetworkStandard.DataTypes.PropertyEnum;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class CharacterInformation extends CharacterDescription {
    /**
     * Server assigned character-UUID
     */
    private UUID characterId;

    /**
     * constructor
     *
     * @param name
     * @param description
     * @param gender
     * @param features
     * @param characterId
     */
    public CharacterInformation(String name, String description, GenderEnum gender, Set<PropertyEnum> features, UUID characterId) {
        super(name, description, gender, features);
        this.characterId = characterId;
    }

    /**
     * getter and setter
     */
    public UUID getCharacterId() {
        return characterId;
    }

    public void setCharacterId(UUID characterId) {
        this.characterId = characterId;
    }

    /**
     * euqals and hashCode
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CharacterInformation that = (CharacterInformation) o;
        return Objects.equals(getCharacterId(), that.getCharacterId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCharacterId());
    }

    /**
     * toString
     */
    @Override
    public String toString() {
        return "CharacterInformation{" +
                "characterId=" + characterId +
                '}';
    }
}
