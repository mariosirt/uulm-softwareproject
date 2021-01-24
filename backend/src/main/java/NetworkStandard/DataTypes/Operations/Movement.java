package NetworkStandard.DataTypes.Operations;

import java.awt.*;
import java.util.UUID;

/**
 * A class for a Movement Operation (OperationEnum MOVEMENT)
 * <p>
 * from: gives us the starting field of a Movement Operation (partly because of redundancy)
 */
public class Movement extends Operation {
    private Point from;

    public Movement(OperationEnum type, Boolean successful, Point target, UUID characterId, Point from) {
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
        return "Movement{" +
            "from=" + from +
            '}';
    }
}
