import json
from NetworkStandard.MessageContainer.message_container import MessageContainer
from NetworkStandard.MessageTypeEnum.messagetype_enum import MessageTypeEnum
from NetworkStandard.Messages.hello_reply_message import HelloReplyMessage
from NetworkStandard.Messages.error_message import ErrorMessage
from NetworkStandard.Messages.game_started_message import GameStartedMessage
from NetworkStandard.Messages.request_item_choice_message import RequestItemChoiceMessage
from NetworkStandard.Messages.request_equipment_choice_message import RequestEquipmentChoiceMessage
from NetworkStandard.Messages.game_status_message import GameStatusMessage
from NetworkStandard.Messages.request_gameoperation_message import RequestGameOperationMessage
from NetworkStandard.Messages.statistics_message import StatisticsMessage
from NetworkStandard.Messages.game_left_message import GameLeftMessage
from NetworkStandard.Messages.game_pause_message import GamePauseMessage
from NetworkStandard.Messages.metainformation_message import MetaInformationMessage
from NetworkStandard.Messages.strike import Strike
from NetworkStandard.Messages.replay_message import ReplayMessage


class MessageReceiver:

    def __init__(self):
        pass

    def handle_message(self, message_string: str):
        try:
            message_container = MessageContainer(json.loads(message_string))
            if message_container.type_enum == MessageTypeEnum.HELLO_REPLY:
                print("HELLO REPLY: ", message_string)

                self.receive_hello_reply_message(HelloReplyMessage(json.loads(message_string)))
            elif message_container.type_enum == MessageTypeEnum.ERROR:
                print("ERROR: ", message_string)

                self.receive_error_message(ErrorMessage(json.loads(message_string)))
            elif message_container.type_enum == MessageTypeEnum.GAME_STARTED:
                print("GAME STARTED: ", message_string)

                self.receive_game_started_message(GameStartedMessage(json.loads(message_string)))
            elif message_container.type_enum == MessageTypeEnum.REQUEST_ITEM_CHOICE:
                print("REQUEST ITEM CHOICE: ", message_string)

                self.receive_request_item_choice_message(RequestItemChoiceMessage(json.loads(message_string)))
            elif message_container.type_enum == MessageTypeEnum.REQUEST_EQUIPMENT_CHOICE:
                print("REQUEST EQUIPMENT CHOICE: ", message_string)

                self.receive_request_equipment_choice_message(RequestEquipmentChoiceMessage(json.loads(message_string)))
            elif message_container.type_enum == MessageTypeEnum.GAME_STATUS:
                print("GAME STATUS: ", message_string)

                self.receive_game_status_message(GameStatusMessage(json.loads(message_string)))
            elif message_container.type_enum == MessageTypeEnum.REQUEST_GAME_OPERATION:
                print("REQUEST GAME OPERATION: ", message_string)

                self.receive_request_game_operation_message(RequestGameOperationMessage(json.loads(message_string)))
            elif message_container.type_enum == MessageTypeEnum.STATISTICS:
                print("STATISTICS: ", message_string)

                self.receive_statistics_message(StatisticsMessage(json.loads(message_string)))
            elif message_container.type_enum == MessageTypeEnum.GAME_LEFT:
                print("GAME LEFT: ", message_string)

                self.receive_game_left_message(GameLeftMessage(json.loads(message_string)))
            elif message_container.type_enum == MessageTypeEnum.GAME_PAUSE:
                print("GAME PAUSE: ", message_string)

                self.receive_game_pause_message(GamePauseMessage(json.loads(message_string)))
            elif message_container.type_enum == MessageTypeEnum.META_INFORMATION:
                print("META INFORMATION: ", message_string)

                self.receive_meta_information_message(MetaInformationMessage(json.loads(message_string)))
            elif message_container.type_enum == MessageTypeEnum.STRIKE:
                print("STRIKE: ", message_string)

                self.receive_strike_message(Strike(json.loads(message_string)))
            elif message_container.type_enum == MessageTypeEnum.REPLAY:
                print("REPLAY: ", message_string)

                self.receive_replay_message(ReplayMessage(json.loads(message_string)))

        except:
            print("Could not read message")

    def receive_hello_reply_message(self, message):
        pass

    def receive_error_message(self, message):
        pass

    def receive_game_started_message(self, message):
        pass

    def receive_request_item_choice_message(self, message):
        pass

    def receive_request_equipment_choice_message(self, message):
        pass

    def receive_game_status_message(self, message):
        pass

    def receive_request_game_operation_message(self, message):
        pass

    def receive_statistics_message(self, message):
        pass

    def receive_game_left_message(self, message):
        pass

    def receive_game_pause_message(self, message):
        pass

    def receive_meta_information_message(self, message):
        pass

    def receive_strike_message(self, message):
        pass

    def receive_replay_message(self, message):
        pass
