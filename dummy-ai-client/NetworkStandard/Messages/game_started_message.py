from NetworkStandard.MessageContainer.message_container import MessageContainer
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum

import uuid


class GameStartedMessage(MessageContainer):

    def __init__(self, player_id, type_enum, creation_date, debug_message,
                 player_one_id,
                 player_two_id, player_one_name, player_two_name, session_Id):
        super().__init__(player_id, type_enum, creation_date, debug_message)
        self.player_one_id = player_id
        self.player_two_id = player_two_id
        self.player_one_name = player_one_name
        self.player_two_name = player_two_name
        self.session_Id = session_Id
