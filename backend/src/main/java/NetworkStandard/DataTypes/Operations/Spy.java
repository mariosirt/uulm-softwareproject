package NetworkStandard.DataTypes.Operations;

import java.awt.*;
import java.util.UUID;

/**
 * represents the operation when a player wants to use spy
 */

public class Spy extends Operation {
    public Spy(OperationEnum type, Boolean successful, Point target, UUID characterId) {
        super(type, successful, target, characterId);
    }
}
