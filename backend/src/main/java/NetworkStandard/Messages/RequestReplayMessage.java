package NetworkStandard.Messages;

import NetworkStandard.MessageContainer.MessageContainer;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.UUID;

/**
 * OPTIONAL
 * Message send by the client to ask the server for a reply.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class RequestReplayMessage extends MessageContainer {
    public RequestReplayMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage) {
        super(playerId, MessageTypeEnum.REQUEST_REPLAY, creationDate, debugMessage);
    }
}
