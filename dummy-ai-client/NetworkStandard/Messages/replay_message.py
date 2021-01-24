from NetworkStandard.MessageContainer.message_container import MessageContainer
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum
from NetworkStandard.DataTypes.Matchconfig.matchconfig import Matchconfig
from NetworkStandard.DataTypes.Szenario.scenario import Scenario

import uuid


class ReplayMessage(MessageContainer):
    def __int__(self, player_Id, type_enum, creation_date, debug_message,
                session_Id,
                game_start, game_end, player_one_Id, player_two_Id, player_one_name,
                player_two_name, rounds, level, settings, character_settings,
                messages):
        super().__init__(player_Id, type_enum, creation_date, debug_message)
        self.session_Id = session_Id
        self.game_start = game_start
        self.game_end = game_end
        self.player_one_Id = player_one_Id
        self.player_two_Id = player_two_Id
        self.player_one_name = player_one_name
        self.player_two_name = player_two_name
        self.rounds = rounds
        self.level = level
        self.settings = settings
        self.character_settings = character_settings
        self.messages = messages
