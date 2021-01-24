package NetworkStandard.DataTypes.Operations;

import java.awt.*;
import java.util.UUID;

/**
 * Represents the operation when a player wants to end their turn early.
 */

public class Retire extends Operation {
    public Retire(OperationEnum type, Boolean successful, Point target, UUID characterId) {
        super(type, successful, target, characterId);
    }
}
