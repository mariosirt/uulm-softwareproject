package com.sopra.ntts.model.NetworkStandard.MessageContainer;

import com.sopra.ntts.model.NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
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
}
