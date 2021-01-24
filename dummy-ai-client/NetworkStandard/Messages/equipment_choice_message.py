from NetworkStandard.MessageContainer.message_container import MessageContainer
import uuid
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum


class EquipmentChoiceMessage(MessageContainer):

    def __init__(self, player_id, type_enum, creation_date, debug_message, equipment):
        super().__init__(player_id, MessageTypeEnum.EQUIPMENT_CHOICE, creation_date, debug_message)
        self.equipment = equipment
