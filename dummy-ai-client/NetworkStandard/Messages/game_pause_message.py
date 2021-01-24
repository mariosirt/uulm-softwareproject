from NetworkStandard.MessageContainer.message_container import MessageContainer
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum

import uuid


class GamePauseMessage(MessageContainer):

    def __init__(self, client_Id, type_enum, creation_date, debug_message,
                 game_paused, server_enforced):
        super().__init__(client_Id, type_enum, creation_date, debug_message)
        self.game_paused = game_paused
        self.server_enforced = server_enforced
