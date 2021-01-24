from NetworkStandard.MessageContainer.message_container import MessageContainer
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum
from NetworkStandard.DataTypes.Operations.statistics_enum import Statistics
from NetworkStandard.DataTypes.Operations.victory_enum import VictoryEnum

import uuid


class StatisticsMessage(MessageContainer):

    def __int__(self, player_Id, type_enum, creation_date, debug_message,
                statistics, winner, reason, has_replay):
        super().__init__(player_Id, type_enum, creation_date, debug_message)
        self.statistics = statistics
        self.winner = winner
        self.reason = reason
        self.has_replay = has_replay
