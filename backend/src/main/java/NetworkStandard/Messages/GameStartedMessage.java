package NetworkStandard.Messages;

import NetworkStandard.MessageContainer.MessageContainer;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Message send by the server to inform the client that the game is established.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class GameStartedMessage extends MessageContainer {
    private UUID playerOneId;
    private UUID playerTwoId;
    private String playerOneName;
    private String playerTwoName;
    private UUID sessionId;

    public GameStartedMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage, UUID playerOneId, UUID playerTwoId, String playerOneName, String playerTwoName, UUID sessionId) {
        super(playerId, MessageTypeEnum.GAME_STARTED, creationDate, debugMessage);
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
        this.sessionId = sessionId;
    }

    public UUID getPlayerOneId() {
        return playerOneId;
    }

    public void setPlayerOneId(UUID playerOneId) {
        this.playerOneId = playerOneId;
    }

    public UUID getPlayerTwoId() {
        return playerTwoId;
    }

    public void setPlayerTwoId(UUID playerTwoId) {
        this.playerTwoId = playerTwoId;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameStartedMessage that = (GameStartedMessage) o;
        return Objects.equals(getPlayerOneId(), that.getPlayerOneId()) &&
            Objects.equals(getPlayerTwoId(), that.getPlayerTwoId()) &&
            Objects.equals(getPlayerOneName(), that.getPlayerOneName()) &&
            Objects.equals(getPlayerTwoName(), that.getPlayerTwoName()) &&
            Objects.equals(getSessionId(), that.getSessionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayerOneId(), getPlayerTwoId(), getPlayerOneName(), getPlayerTwoName(), getSessionId());
    }

    @Override
    public String toString() {
        return "GameStartedMessage{" +
            "playerOneId=" + playerOneId +
            ", playerTwoId=" + playerTwoId +
            ", playerOneName='" + playerOneName + '\'' +
            ", playerTwoName='" + playerTwoName + '\'' +
            ", sessionId=" + sessionId +
            '}';
    }
}
