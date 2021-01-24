from Client.MessageHandling.message_emitter import MessageEmitter
from ki_client import SimpleClient
import json


class GameHandler:

    def __init__(self, message_emitter: MessageEmitter):
        self.message_emitter = message_emitter


if __name__ == "__main__":
    hello_message = {"name": "Dick Wade", "role": "PLAYER", "type": "HELLO",
                     "creationDate": "May 23, 2020, 2:53:45 PM"}
    json_message = json.dumps(hello_message)
    print(json_message)
    simple_client = SimpleClient("ws://localhost:8080")
    message_emitter = MessageEmitter(simple_client)
    simple_client.send(hello_message)
    simple_client.sendMessage("", hello_message)
    print("message sent")
    # game = GameHandler(message_emitter)
    # game.message_emitter.send_hello_message("KI")
