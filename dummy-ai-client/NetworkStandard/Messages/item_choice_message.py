from NetworkStandard.MessageContainer.message_container import MessageContainer
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum
from NetworkStandard.DataTypes.gadget_enum import GadgetEnum

import uuid


class ItemChoiceMessage(MessageContainer):

    def __int__(self, player_Id, type_enum, creation_date, debug_message,
                chosen_character_Id, chosen_gadget):
        super().__init__(player_Id, type_enum, creation_date, debug_message)
        self.chosen_character_Id = chosen_character_Id
        self.chosen_gadget = chosen_gadget
