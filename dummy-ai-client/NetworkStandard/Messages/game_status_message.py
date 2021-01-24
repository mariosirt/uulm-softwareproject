from NetworkStandard.MessageContainer.message_container import MessageContainer
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum
from NetworkStandard.DataTypes.Szenario.state import State

import uuid


class GameStatusMessage(MessageContainer):

    def __init__(self, player_Id, type_enum, creation_date, debug_message,
                 active_character_Id, operations, state, is_game_over):
        super().__init__(player_Id, type_enum, creation_date, debug_message)
        self.active_character_Id = active_character_Id
        self.operations = operations
        self.state = state
        self.is_game_over = is_game_over
