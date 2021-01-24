package NetworkStandard.Messages;

import NetworkStandard.MessageContainer.MessageContainer;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.UUID;

/**
 * Message send by the server asks the client about the action, that the client wants to take with the character who is in charge.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class RequestGameOperationMessage extends MessageContainer {
    UUID characterId;

    public RequestGameOperationMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage, UUID characterId) {
        super(playerId, MessageTypeEnum.REQUEST_GAME_OPERATION, creationDate, debugMessage);
        this.characterId = characterId;
    }
}
