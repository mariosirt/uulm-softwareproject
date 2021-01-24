from NetworkStandard.MessageContainer.message_container import MessageContainer


class RequestItemChoiceMessage(MessageContainer):

    def __init__(self, player_Id, type_enum, creation_date, debug_message, offered_character_Ids, offered_gadgets):
        super().__init__(player_Id, type_enum, creation_date, debug_message)
        self.offered_character_Ids = offered_character_Ids
        self.offered_gadgets = offered_gadgets
