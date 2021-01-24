from NetworkStandard.MessageContainer.message_container import MessageContainer
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum

import uuid


class MetaInformationMessage(MessageContainer):

    def __int__(self, player_Id, type_enum, creation_date, debug_message):
        super().__init__(player_Id, type_enum, creation_date, debug_message)
        self.information = None

    def set_information(self, information):
        self.information = information
