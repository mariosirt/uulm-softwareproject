package com.sopra.ntts.model.NetworkStandard.Messages;


import com.sopra.ntts.model.NetworkStandard.MessageContainer.MessageContainer;
import com.sopra.ntts.model.NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Message send then the client wants to reestablish the connection with the server.
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class ReconnectMessage extends MessageContainer {
  private UUID sessionId;

  public ReconnectMessage(UUID clientId, MessageTypeEnum type, Date creationDate, String debugMessage, UUID sessionId) {
    super(clientId, type, creationDate, debugMessage);
    this.sessionId = sessionId;
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
    ReconnectMessage that = (ReconnectMessage) o;
    return Objects.equals(getSessionId(), that.getSessionId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getSessionId());
  }

  @Override
  public String toString() {
    return "ReconnectMessage{" +
            "sessionId=" + sessionId +
            ", clientId=" + clientId +
            ", type=" + type +
            ", creationDate=" + creationDate +
            ", debugMessage='" + debugMessage + '\'' +
            '}';
  }
}
