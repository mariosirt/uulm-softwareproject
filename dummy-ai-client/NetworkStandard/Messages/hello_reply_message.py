from NetworkStandard.MessageContainer.message_container import MessageContainer
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum
from NetworkStandard.DataTypes.Matchconfig.matchconfig import Matchconfig
from NetworkStandard.DataTypes.Szenario.scenario import Scenario

import uuid


class HelloReplyMessage(MessageContainer):

    def __init__(self, player_Id, type_enum, creation_date, debug_message,
                 session_Id,
                 level, settings, character_settings):
        super().__init__(player_Id, type_enum, creation_date, debug_message)
        self.level = level
        self.session_id = session_Id
        self.settings = settings
        self.character_settings = character_settings
