package com.sopra.ntts.model.NetworkStandard.Messages;

import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;
import com.sopra.ntts.model.NetworkStandard.MessageContainer.MessageContainer;
import com.sopra.ntts.model.NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Message send by the client which item is selected by the client.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class ItemChoiceMessage extends MessageContainer {
    private UUID chosenCharacterId;
    private GadgetEnum chosenGadget;

    public ItemChoiceMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage, UUID chosenCharacterId, GadgetEnum chosenGadget) {
        super(playerId, MessageTypeEnum.ITEM_CHOICE, creationDate, debugMessage);
        this.chosenCharacterId = chosenCharacterId;
        this.chosenGadget = chosenGadget;
    }

    public UUID getChosenCharacterId() {
        return chosenCharacterId;
    }

    public void setChosenCharacterId(UUID chosenCharacterId) {
        this.chosenCharacterId = chosenCharacterId;
    }

    public GadgetEnum getChosenGadget() {
        return chosenGadget;
    }

    public void setChosenGadget(GadgetEnum chosenGadget) {
        this.chosenGadget = chosenGadget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemChoiceMessage that = (ItemChoiceMessage) o;
        return
                Objects.equals(getChosenCharacterId(), that.getChosenCharacterId()) &&
                        getChosenGadget() == that.getChosenGadget();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChosenCharacterId(), getChosenGadget());
    }

    @Override
    public String toString() {
        return "ItemChoiceMessage{" +
                ", chosenCharacterId=" + chosenCharacterId +
                ", chosenGadget=" + chosenGadget +
                '}';
    }
}
