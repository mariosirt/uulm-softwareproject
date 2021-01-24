package NetworkStandard.Messages;

import NetworkStandard.DataTypes.RoleEnum;
import NetworkStandard.MessageContainer.MessageContainer;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.UUID;

/**
 * Initial message when connection is established, send by the server.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class HelloMessage extends MessageContainer {
    public String name;
    public RoleEnum role;

    public HelloMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage, String name, RoleEnum role) {
        super(playerId, MessageTypeEnum.HELLO, creationDate, debugMessage);
        this.name = name;
        this.role = role;
    }


}
