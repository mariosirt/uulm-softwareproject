package NetworkStandard.Messages;

import NetworkStandard.DataTypes.GadgetEnum;
import NetworkStandard.MessageContainer.MessageContainer;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Message send by the server to inform the client what items are available in the choice phase.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class RequestItemChoiceMessage extends MessageContainer {
    private List<UUID> offeredCharacterIds;
    private List<GadgetEnum> offeredGadgets;

    public RequestItemChoiceMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage, List<UUID> offeredCharacterIds, List<GadgetEnum> offeredGadgets) {
        super(playerId, MessageTypeEnum.REQUEST_ITEM_CHOICE, creationDate, debugMessage);
        this.offeredCharacterIds = offeredCharacterIds;
        this.offeredGadgets = offeredGadgets;
    }

    public List<UUID> getOfferedCharacterIds() {
        return offeredCharacterIds;
    }

    public void setOfferedCharacterIds(List<UUID> offeredCharacterIds) {
        this.offeredCharacterIds = offeredCharacterIds;
    }

    public List<GadgetEnum> getOfferedGadgets() {
        return offeredGadgets;
    }

    public void setOfferedGadgets(List<GadgetEnum> offeredGadgets) {
        this.offeredGadgets = offeredGadgets;
    }
}
