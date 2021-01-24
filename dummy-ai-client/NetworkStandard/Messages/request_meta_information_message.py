from NetworkStandard.MessageContainer.message_container import MessageContainer
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum

import uuid


class RequestMetaInformationMessage(MessageContainer):

    def __int__(self, player_Id, type_enum, creation_date, debug_message, keys):
        super().__init__(player_Id, type_enum, creation_date, debug_message)
        self.keys = keys
