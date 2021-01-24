package com.sopra.ntts.model.NetworkStandard.Messages;

import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;
import com.sopra.ntts.model.NetworkStandard.MessageContainer.MessageContainer;
import com.sopra.ntts.model.NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.*;

/**
 * Message send by the client to notify the server about all the chosen gadgets.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class EquipmentChoiceMessage extends MessageContainer {
    private Map<UUID, Set<GadgetEnum>> equipment;

    public EquipmentChoiceMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage, Map<UUID, Set<GadgetEnum>> equipment) {
        super(playerId, MessageTypeEnum.EQUIPMENT_CHOICE, creationDate, debugMessage);
        this.equipment = equipment;
    }

    public Map<UUID, Set<GadgetEnum>> getEquipment() {
        return equipment;
    }

    public void setEquipment(Map<UUID, Set<GadgetEnum>> equipment) {
        this.equipment = equipment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquipmentChoiceMessage that = (EquipmentChoiceMessage) o;
        return Objects.equals(getEquipment(), that.getEquipment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEquipment());
    }

    @Override
    public String toString() {
        return "EquipmentChoiceMessage{" +
                "equipment=" + equipment +
                '}';
    }
}
