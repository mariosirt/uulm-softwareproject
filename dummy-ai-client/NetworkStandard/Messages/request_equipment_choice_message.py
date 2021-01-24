from NetworkStandard.MessageContainer.message_container import MessageContainer
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum

import uuid


class RequestEquipmentChoiceMessage(MessageContainer):
    def __init__(self, player_Id, type_enum, creation_date, debug_message,
                 chosen_character_Ids, chosen_gadgets):
        super().__init__(player_Id, type_enum, creation_date, debug_message)
        self.chosen_character_Ids = chosen_character_Ids
        self.chosen_gadgets = chosen_gadgets
