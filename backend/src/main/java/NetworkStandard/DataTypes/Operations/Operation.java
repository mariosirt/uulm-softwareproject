package NetworkStandard.DataTypes.Operations;

import java.awt.*;
import java.util.UUID;

/**
 * Operation provides basic information about an operation and therefore is the base of specialised classes for any operations
 * which have to do witch the player (character).
 * <p>
 * characterId: just an attribute for the character which takes action with an operation.
 */

public class Operation extends BaseOperation {
    private UUID characterId;

    public Operation(OperationEnum type, Boolean successful, Point target, UUID characterId) {
        super(type, successful, target);
        this.characterId = characterId;
    }

    public UUID getCharacterId() {
        return characterId;
    }

    public void setCharacterId(UUID characterId) {
        this.characterId = characterId;
    }

    @Override
    public String toString() {
        return "Operation{" +
            "characterId=" + characterId +
            '}';
    }
}
