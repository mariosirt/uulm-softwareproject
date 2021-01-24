package NetworkStandard.DataTypes.Operations;

import java.awt.*;
import java.util.UUID;

/**
 * A class for an Exfiltration Operation. This one is special, because if a character dies through an operation
 * the server gives this character a exfiltration Operation. The target field is implied by the target field from Operation.
 * <p>
 * from: The starting field for an exfiltration operation. (redundancy)
 */

public class Exfiltration extends Operation {
    private Point from;

    public Exfiltration(OperationEnum type, Boolean successful, Point target, UUID characterId, Point from) {
        super(type, successful, target, characterId);
        this.from = from;
    }

    public Point getFrom() {
        return from;
    }

    public void setFrom(Point from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "Exfiltration{" +
            "from=" + from +
            '}';
    }
}
