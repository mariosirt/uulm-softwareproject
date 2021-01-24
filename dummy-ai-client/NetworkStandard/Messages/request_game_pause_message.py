from NetworkStandard.MessageContainer.message_container import MessageContainer
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum

import uuid


class RequestGamePausMessage(MessageContainer):

    def __int__(self, player_Id, type_enum, creation_date, debug_message,
                game_pause):
        super().__init__(player_Id, type_enum, creation_date, debug_message)
        self.game_pause = game_pause
