package com.sopra.ntts.model.NetworkStandard.Characters;

import java.util.Objects;
import java.util.UUID;

public class CharacterInformation extends CharacterDescription {
    /**
     * Server assigned character-UUID
     */
    private UUID characterId;

    public CharacterInformation(UUID characterId) {
        this.characterId = characterId;
    }

    public UUID getCharacterId() {
        return characterId;
    }

    public void setCharacterId(UUID characterId) {
        this.characterId = characterId;
    }

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

    @Override
    public String toString() {
        return "CharacterInformation{" +
                "characterId=" + characterId +
                '}';
    }
}
