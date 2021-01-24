package NetworkStandard.Messages;

import NetworkStandard.MessageContainer.MessageContainer;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Message send by the client to tell if game has to be paused.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class RequestGamePauseMessage extends MessageContainer {
    private boolean gamePause;

    public RequestGamePauseMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage, boolean gamePause) {
        super(playerId, MessageTypeEnum.REQUEST_GAME_PAUSE, creationDate, debugMessage);
        this.gamePause = gamePause;
    }

    public boolean isGamePause() {
        return gamePause;
    }

    public void setGamePause(boolean gamePause) {
        this.gamePause = gamePause;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestGamePauseMessage that = (RequestGamePauseMessage) o;
        return isGamePause() == that.isGamePause();
    }

    @Override
    public int hashCode() {
        return Objects.hash(isGamePause());
    }

    @Override
    public String toString() {
        return "RequestGamePauseMessage{" +
            "gamePause=" + gamePause +
            ", clientId=" + clientId +
            ", type=" + type +
            ", creationDate=" + creationDate +
            ", debugMessage='" + debugMessage + '\'' +
            '}';
    }
}
