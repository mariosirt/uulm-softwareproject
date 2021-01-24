package com.sopra.ntts.model.NetworkStandard.Messages;

import com.sopra.ntts.model.NetworkStandard.MessageContainer.MessageContainer;
import com.sopra.ntts.model.NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Message send by the server to inform the client that the game is paused.
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class GamePauseMessage extends MessageContainer {
  private Boolean gamePaused;
  private Boolean serverEnforced;

  public GamePauseMessage(UUID clientId, MessageTypeEnum type, Date creationDate, String debugMessage, Boolean gamePaused, Boolean serverEnforced) {
    super(clientId, type, creationDate, debugMessage);
    this.gamePaused = gamePaused;
    this.serverEnforced = serverEnforced;
  }

  public Boolean getGamePaused() {
    return gamePaused;
  }

  public void setGamePaused(Boolean gamePaused) {
    this.gamePaused = gamePaused;
  }

  public Boolean getServerEnforced() {
    return serverEnforced;
  }

  public void setServerEnforced(Boolean serverEnforced) {
    this.serverEnforced = serverEnforced;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GamePauseMessage that = (GamePauseMessage) o;
    return Objects.equals(getGamePaused(), that.getGamePaused()) &&
            Objects.equals(getServerEnforced(), that.getServerEnforced());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getGamePaused(), getServerEnforced());
  }

  @Override
  public String toString() {
    return "GamePauseMessage{" +
            "gamePaused=" + gamePaused +
            ", serverEnforced=" + serverEnforced +
            ", clientId=" + clientId +
            ", type=" + type +
            ", creationDate=" + creationDate +
            ", debugMessage='" + debugMessage + '\'' +
            '}';
  }
}
