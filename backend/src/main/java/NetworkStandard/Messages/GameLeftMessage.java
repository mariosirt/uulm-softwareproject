package NetworkStandard.Messages;

import NetworkStandard.MessageContainer.MessageContainer;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Message send by the server to inform the client about leaved client.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class GameLeftMessage extends MessageContainer {
    private UUID leftUserId;

    public GameLeftMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage, UUID leftUserId) {
        super(playerId, MessageTypeEnum.GAME_LEFT, creationDate, debugMessage);
        this.leftUserId = leftUserId;
    }

    public UUID getLeftUserId() {
        return leftUserId;
    }

    public void setLeftUserId(UUID leftUserId) {
        this.leftUserId = leftUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameLeftMessage that = (GameLeftMessage) o;
        return Objects.equals(getLeftUserId(), that.getLeftUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLeftUserId());
    }

    @Override
    public String toString() {
        return "GameLeftMessage{" +
            "leftUserId=" + leftUserId +
            ", clientId=" + clientId +
            ", type=" + type +
            ", creationDate=" + creationDate +
            ", debugMessage='" + debugMessage + '\'' +
            '}';
    }
}
