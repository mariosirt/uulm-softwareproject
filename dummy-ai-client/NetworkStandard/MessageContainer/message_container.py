import uuid
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum


class MessageContainer:

    def __init__(self, client_Id, type_enum, creation_date, debug_message):
        self.client_Id = client_Id
        self.type_enum = type_enum
        self.creation_date = creation_date
        self.debug_message = debug_message
