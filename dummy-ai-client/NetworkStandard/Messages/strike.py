from NetworkStandard.MessageContainer.message_container import MessageContainer
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum

import uuid


class Strike(MessageContainer):

    def __init__(self, player_Id, type_enum, creation_date, debug_message,
                strike_nr, strike_max, reason):
        super().__init__(player_Id, type_enum, creation_date, debug_message)
        self.strike_nr = strike_nr
        self.strike_max = strike_max
        self.reason = reason
