package NetworkStandard.Messages;

import NetworkStandard.DataTypes.Operations.Operation;
import NetworkStandard.MessageContainer.MessageContainer;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Message send by the client to notify the server about all the chosen operation.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class GameOperationMessage extends MessageContainer {
    private Operation operation;

    public GameOperationMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage, Operation operation) {
        super(playerId, MessageTypeEnum.GAME_OPERATION, creationDate, debugMessage);
        this.operation = operation;
    }


    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameOperationMessage that = (GameOperationMessage) o;
        return Objects.equals(getOperation(), that.getOperation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOperation());
    }

    @Override
    public String toString() {
        return "GameOperationMessage{" +
            "operation=" + operation +
            '}';
    }
}
