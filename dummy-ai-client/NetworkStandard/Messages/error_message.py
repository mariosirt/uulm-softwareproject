from NetworkStandard.MessageContainer.message_container import MessageContainer
import uuid
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum


class ErrorMessage(MessageContainer):

    def __init__(self, client_id, type_enum, creation_date, debug_message,
                 reason: str):
        super().__init__(client_id, type_enum, creation_date, debug_message)
        self.reason = reason
