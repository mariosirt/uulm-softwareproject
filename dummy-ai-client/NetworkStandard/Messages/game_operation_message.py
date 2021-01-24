from NetworkStandard.MessageContainer.message_container import MessageContainer
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum
from NetworkStandard.DataTypes.Operations.operation import Operation

import uuid


class GameOperationMessage(MessageContainer):

    def __init__(self, player_Id, type_enum, creation_date, debug_message,
                 operation):
        super().__init__(player_Id, type_enum, creation_date, debug_message)
        self.operation = operation
