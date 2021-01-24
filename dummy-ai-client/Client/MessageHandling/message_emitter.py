from ki_client import SimpleClient

import json


class MessageEmitter:

    def __init__(self, connection: SimpleClient):
        self.connection = connection

    def send_message(self, json_message: str):
        self.connection.send(json_message)

    def send_hello_message(self, name: str, role=None):
        hello_message = {"name": "Dick Wade", "role": "PLAYER", "type": "HELLO",
                         "creationDate": "May 23, 2020, 2:53:45 PM"}
        json_message = json.dumps(hello_message)
        print("HELLO SENT: ", json_message)
        #self.send_message(json_message)



class Encoder(json.JSONEncoder):
    def default(self, o):
        return o.__dict__
