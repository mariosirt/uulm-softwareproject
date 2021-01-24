from NetworkStandard.MessageContainer.message_container import MessageContainer


class RequestGameOperationMessage(MessageContainer):

    def __init__(self, player_Id, type_enum, creation_date, debug_message,
                character_Id):
        super().__init__(player_Id, type_enum, creation_date, debug_message)
        self.character_Id = character_Id
