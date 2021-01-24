package NetworkStandard.Messages;

import NetworkStandard.DataTypes.Operations.ErrorTypeEnum;
import NetworkStandard.MessageContainer.MessageContainer;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Message send by the server to notify the client about error.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class ErrorMessage extends MessageContainer {
    private ErrorTypeEnum reason;

    public ErrorMessage(UUID clientId, MessageTypeEnum type, Date creationDate, String debugMessage, ErrorTypeEnum reason) {
        super(clientId, type, creationDate, debugMessage);
        this.reason = reason;
    }

    public ErrorTypeEnum getReason() {
        return reason;
    }

    public void setReason(ErrorTypeEnum reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorMessage that = (ErrorMessage) o;
        return getReason() == that.getReason();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReason());
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
            "reason=" + reason +
            '}';
    }
}
