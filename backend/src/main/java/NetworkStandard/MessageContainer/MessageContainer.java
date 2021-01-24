package NetworkStandard.MessageContainer;

import NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Basic MessageContainer for the communication between the client and server.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class MessageContainer {
    public UUID clientId;
    public MessageTypeEnum type;
    public Date creationDate;
    public String debugMessage;

    public MessageContainer(UUID clientId, MessageTypeEnum type, Date creationDate, String debugMessage) {
        this.clientId = clientId;
        this.type = type;
        this.creationDate = creationDate;
        this.debugMessage = debugMessage;
    }

    @Override
    public String toString() {
        return "MessageContainer{" +
                "playerId=" + clientId +
                ", type=" + type +
                ", creationDate=" + creationDate +
                ", debugMessage='" + debugMessage + '\'' +
                '}';
    }

    /**
     * getter and setter
     */
    public UUID getClientId() {
        return clientId;
    }

    public MessageTypeEnum getType() {
        return type;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public void setType(MessageTypeEnum type) {
        this.type = type;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    /**
     * equals and hashCode
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageContainer that = (MessageContainer) o;
        return Objects.equals(getClientId(), that.getClientId()) &&
                getType() == that.getType() &&
                Objects.equals(getCreationDate(), that.getCreationDate()) &&
                Objects.equals(getDebugMessage(), that.getDebugMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClientId(), getType(), getCreationDate(), getDebugMessage());
    }
}
