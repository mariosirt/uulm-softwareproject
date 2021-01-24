from NetworkStandard.MessageContainer.message_container import MessageContainer
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum

import uuid


class ReconnectMessage(MessageContainer):
    def __int__(self, player_Id, type_enum, creation_date, debug_message,
                session_Id):
        super().__init__(player_Id, type_enum, creation_date, debug_message)
        self.session_Id = session_Id
