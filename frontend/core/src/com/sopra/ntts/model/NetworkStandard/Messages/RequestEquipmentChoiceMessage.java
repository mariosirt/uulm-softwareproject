package com.sopra.ntts.model.NetworkStandard.Messages;

import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;
import com.sopra.ntts.model.NetworkStandard.MessageContainer.MessageContainer;
import com.sopra.ntts.model.NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Message send by the server. It is a redundant message, is valid for checking errors in the communication.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class RequestEquipmentChoiceMessage extends MessageContainer {
    private List<UUID> chosenCharacterIds;
    private List<GadgetEnum> chosenGadgets;

    public RequestEquipmentChoiceMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage, List<UUID> chosenCharacterIds, List<GadgetEnum> chosenGadgets) {
        super(playerId, MessageTypeEnum.REQUEST_EQUIPMENT_CHOICE, creationDate, debugMessage);
        this.chosenCharacterIds = chosenCharacterIds;
        this.chosenGadgets = chosenGadgets;
    }

    public List<UUID> getChosenCharacterIds() {
        return chosenCharacterIds;
    }

    public void setChosenCharacterIds(List<UUID> chosenCharacterIds) {
        this.chosenCharacterIds = chosenCharacterIds;
    }

    public List<GadgetEnum> getChosenGadgets() {
        return chosenGadgets;
    }

    public void setChosenGadgets(List<GadgetEnum> chosenGadgets) {
        this.chosenGadgets = chosenGadgets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestEquipmentChoiceMessage that = (RequestEquipmentChoiceMessage) o;
        return Objects.equals(getChosenCharacterIds(), that.getChosenCharacterIds()) &&
                Objects.equals(getChosenGadgets(), that.getChosenGadgets());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChosenCharacterIds(), getChosenGadgets());
    }

    @Override
    public String toString() {
        return "RequestEquipmentChoiceMessage{" +
                "chosenCharacterIds=" + chosenCharacterIds +
                ", chosenGadgets=" + chosenGadgets +
                '}';
    }
}
