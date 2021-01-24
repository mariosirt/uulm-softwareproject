from NetworkStandard.MessageContainer.message_container import MessageContainer
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum
from NetworkStandard.DataTypes.role_enum import RoleEnum

import uuid


class HelloMessage(MessageContainer):

    def __init__(self, player_Id, type_enum, creation_date, debug_message, name,
                 role):
        super().__init__(player_Id, type_enum, creation_date, debug_message)
        self.name = name
        self.role = role
