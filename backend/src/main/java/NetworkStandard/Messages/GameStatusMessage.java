package NetworkStandard.Messages;

import NetworkStandard.DataTypes.Operations.BaseOperation;
import NetworkStandard.DataTypes.Szenario.State;
import NetworkStandard.MessageContainer.MessageContainer;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Message send by the server to inform the client about the game status and in certain circumstances send a strike.
 * Strike is send then the server has to end the round by force (time limit is surpassed).
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class GameStatusMessage extends MessageContainer {
    private UUID activeCharacterId;
    private List<BaseOperation> operations;
    private State state;
    private Boolean isGameOver;

    public GameStatusMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage, UUID activeCharacterId, List<BaseOperation> operations, State state, Boolean isGameOver) {
        super(playerId, MessageTypeEnum.GAME_STATUS, creationDate, debugMessage);
        this.activeCharacterId = activeCharacterId;
        this.operations = operations;
        this.state = state;
        this.isGameOver = isGameOver;
    }

    public UUID getActiveCharacterId() {
        return activeCharacterId;
    }

    public void setActiveCharacterId(UUID activeCharacterId) {
        this.activeCharacterId = activeCharacterId;
    }

    public List<BaseOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<BaseOperation> operations) {
        this.operations = operations;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Boolean getGameOver() {
        return isGameOver;
    }

    public void setGameOver(Boolean gameOver) {
        isGameOver = gameOver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameStatusMessage that = (GameStatusMessage) o;
        return Objects.equals(getActiveCharacterId(), that.getActiveCharacterId()) &&
            Objects.equals(getOperations(), that.getOperations()) &&
            Objects.equals(getState(), that.getState()) &&
            Objects.equals(isGameOver, that.isGameOver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getActiveCharacterId(), getOperations(), getState(), isGameOver);
    }

    @Override
    public String toString() {
        return "GameStatusMessage{" +
            "activeCharacterId=" + activeCharacterId +
            ", operations=" + operations +
            ", state=" + state +
            ", isGameOver=" + isGameOver +
            '}';
    }
}
