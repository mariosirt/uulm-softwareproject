from NetworkStandard.MessageContainer.message_container import MessageContainer
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum

import uuid


class GameLeftMessage(MessageContainer):

    def __init__(self, player_Id, type_enum, creation_date, debug_message,
                 left_user_Id: uuid.UUID):
        super().__init__(player_Id, type_enum, creation_date, debug_message)
        self.left_user_Id = left_user_Id
