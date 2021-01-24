from websocket import WebSocketApp
import websocket
import time
import _thread as thread
import json
from NetworkStandard.Messages.hello_reply_message import HelloReplyMessage
from NetworkStandard.Messages.game_started_message import GameStartedMessage
from NetworkStandard.Messages.request_item_choice_message import RequestItemChoiceMessage
from NetworkStandard.Messages.request_equipment_choice_message import RequestEquipmentChoiceMessage
from NetworkStandard.Messages.game_status_message import GameStatusMessage
from NetworkStandard.Messages.request_gameoperation_message import RequestGameOperationMessage
from NetworkStandard.Messages.strike import Strike
import random

"""@author Marios Sirtmatsis with support of Sedat Qaja"""

""" A python script containing the KI for Sopra2019/2020 of team002"""

request_gameoperation_message = []  # some variables that need to be accessed globally
active_character_info = []


def handle_message(message_string: str):
    """A method to handle received messages and pass them along"""
    if message_string.__contains__("Welcome to"):
        return
    message = json.loads(message_string)

    type_enum = str(message['type'])
    # print("Message Received: ")
    if type_enum.__contains__("HELLO"):
        print("HELLO REPLY")

        receive_hello_reply_message(HelloReplyMessage(player_Id=message['clientId'], type_enum=message['type'],
                                                      creation_date=message['creationDate'],
                                                      debug_message=message['debugMessage'], session_Id=None,
                                                      level=message['level'], settings=message['settings'],
                                                      character_settings=message['character_settings']))
    elif type_enum == "ERROR":
        print("ERROR", message['debugMessage'])

        pass
    elif type_enum == "GAME_STARTED":
        print("GAME STARTED")

        receive_game_started_message(
            GameStartedMessage(player_id=None, type_enum="GAME STARTED", creation_date=None,
                               player_one_id=message['playerOneId'], player_two_id=message['playerTwoId'],
                               player_one_name=message['playerOneName'], player_two_name=message['playerTwoName'],
                               session_Id=message['sessionId'], debug_message=message['debugMessage']))
    elif type_enum == "REQUEST_ITEM_CHOICE":

        print("REQUEST ITEM CHOICE")

        receive_request_item_choice_message(
            RequestItemChoiceMessage(player_Id=message['clientId'], type_enum="REQUEST_ITEM_CHOICE",
                                     creation_date=message['creationDate'], debug_message=message["debugMessage"],
                                     offered_gadgets=message['offeredGadgets'],
                                     offered_character_Ids=message['offeredCharacterIds']))

    elif type_enum == "REQUEST_EQUIPMENT_CHOICE":
        print("REQUEST EQUIPMENT CHOICE")

        receive_request_equipment_choice_message(
            RequestEquipmentChoiceMessage(chosen_character_Ids=message['chosenCharacterIds'],
                                          chosen_gadgets=message['chosenGadgets'],
                                          player_Id=message['clientId'], type_enum=message['type'],
                                          debug_message=message['debugMessage'],
                                          creation_date=message['creationDate']))
    elif type_enum == "GAME_STATUS":
        print("GAME STATUS")

        receive_game_status_message(GameStatusMessage(player_Id=message["clientId"], type_enum=message["type"],
                                                      creation_date=message["creationDate"],
                                                      debug_message=message["debugMessage"],
                                                      active_character_Id=message["activeCharacterId"],
                                                      operations=message["operations"],
                                                      state=message["state"], is_game_over=message["isGameOver"]))
    elif type_enum == "REQUEST_GAME_OPERATION":
        print("REQUEST GAME OPERATION")

        receive_request_game_operation_message(
            RequestGameOperationMessage(player_Id=message["clientId"], type_enum=message["type"],
                                        creation_date=message["creationDate"], debug_message=message["debugMessage"],
                                        character_Id=message[
                                            "characterId"]))  # error with MessageContainer occurs because of typo in init
    elif type_enum == "STATISTICS":
        print("STATISTICS")
        receive_statistics_message(message_string)
    elif type_enum == "GAME_LEFT":
        print("GAME LEFT")

        pass
    elif type_enum == "GAME_PAUSE":
        print("GAME PAUSE")

        pass
    elif type_enum == "META_INFORMATION":
        print("META INFORMATION")

        pass
    elif type_enum == "STRIKE":
        message = Strike(message['clientId'], message['type'], message['creationDate'], message['debugMessage'],
                         message['strikeNr'], message['strikeMax'], message['reason'])
        if "not clients turn" in message.debug_message:
            return
        strike_retire(request_gameoperation_message.character_Id, request_gameoperation_message)

    elif type_enum == "REPLAY":
        print("REPLAY")

        pass


settings = []


def receive_hello_reply_message(message):
    global settings
    settings = message.settings  # set the successchances and other settings globaly accessable


def receive_error_message(message):
    pass


def receive_game_started_message(message):
    pass


def receive_request_item_choice_message(message):
    """Method to handle receiving an item choice message"""
    item_choice_message = {}
    if message.offered_character_Ids:  # always select characters as long as possible (the first one)
        item_choice_message = {"chosenCharacterId": message.offered_character_Ids[0],
                               "clientId": message.client_Id, "type": "ITEM_CHOICE",
                               "creationDate": message.creation_date}
    else:  # if all possible characters have been selected always select the first gadget of possible ones
        item_choice_message = {"chosenGadget": message.offered_gadgets[0], "clientId": message.client_Id,
                               "type": "ITEM_CHOICE",
                               "creationDate": message.creation_date}
    json_message = json.dumps(item_choice_message)

    send(ws, json_message)


def receive_request_equipment_choice_message(message):
    """Method to handle receiving an equipment choice message"""
    equipment_choice_message = {
        "equipment": {str(message.chosen_character_Ids[0]): [str(gadget) for gadget in message.chosen_gadgets],
                      # first character gets all gadgets
                      str(message.chosen_character_Ids[1]): [],
                      str(message.chosen_character_Ids[2]): [],
                      str(message.chosen_character_Ids[3]): []},
        "clientId": message.client_Id, "type": "EQUIPMENT_CHOICE",
        "creationDate": message.creation_date}

    json_message = json.dumps(equipment_choice_message)

    send(ws, json_message)


game_status_message = []  # some other globally necessary variables containing information for the game

map = []


def receive_game_status_message(message):
    """Method to handle game status messages"""
    global game_status_message
    global map
    game_status_message = message
    getmap(game_status_message)
    # set the current gamestatus message and draw the map
    pass


def receive_request_game_operation_message(message):
    """Method to handle request game op messages"""
    time.sleep(2.5)  # For being more realisit wait for a little before going on
    get_character_info(message, game_status_message)
    global request_gameoperation_message
    request_gameoperation_message = message
    if active_character_info[
        'ap'] > 0:  # go through possible actions and try them. Order is sorted by possibility of getting IPs
        if not neighbour_action(active_character_info, "SPY_ACTION", game_status_message.state['characters'],
                                message):
            gadgets = [i['gadget'] for i in active_character_info['gadgets']]
            properties = active_character_info['properties']
            # print("GADGETS", gadgets)
            # print("PROPERTIES", properties)

            if "OBSERVATION" in properties and property_action(active_character_info, "OBSERVATION",
                                                               game_status_message.state['characters'],
                                                               message):
                return
            elif "BANG_AND_BURN" in properties and property_action(active_character_info, "BANG_AND_BURN",
                                                                   game_status_message.state['characters'],
                                                                   message):
                return
            if len(gadgets) > 0:
                gadget = gadgets[int(random.uniform(0, len(gadgets) - 1))]
                if gadget in ["HAIRDRYER",
                              "TECHNICOLOUR_PRISM", "POISON_PILLS", "GAS_GLOSS", "WIRETAP_WITH_EARPLUGS",
                              "CHICKEN_FEED",
                              "NUGGET", "MIRROR_OF_WILDERNESS",
                              "COCKTAIL"] and neighbour_action(active_character_info, gadget,
                                                               game_status_message.state['characters'], message):
                    return
                elif gadget in ["MOLEDIE", "LASER_COMPACT", "ROCKET_PEN", "MOTHBALL_POUCH", "GRAPPLE", "FOG_TIN",
                                "BOWLER_BLADE"] and lof_action(active_character_info, gadget,
                                                               game_status_message.state['characters'], message):
                    return
                elif gadget == "JETPACK" and jetpack_action(active_character_info, gadget,
                                                            game_status_message.state['characters'], message):
                    return

            if not gamble(active_character_info, 8, message):
                if not move(active_character_info, game_status_message.state['characters'], message):
                    retire(active_character_info, message)
                    return
                else:
                    return
            else:
                return
        else:
            return

    if active_character_info['mp'] > 0:
        if not move(active_character_info, game_status_message.state['characters'], message):
            retire(active_character_info, message)
            return
        else:
            return
    retire(active_character_info, message)  # if nothing can be done, retire
    return


def receive_statistics_message(message):
    """Method for receiving statistics message just ends up in starting a new game after a little delay"""
    restart()


def receive_game_left_message(message):
    """Same as statistics"""
    restart()


def restart():
    """Start a new game by sending a hello message to server"""
    time.sleep(5)
    hello_message = {"name": ki_name, "role": "PLAYER", "type": "HELLO",
                     "creationDate": "May 23, 2020, 2:53:45 PM"}
    json_message = json.dumps(hello_message)

    send(ws, json_message)


def receive_game_pause_message(message):
    pass


def receive_meta_information_message(message):
    pass


def receive_strike_message(message):
    pass


def receive_replay_message(message):
    pass


def getmap(gamestatus_message: GameStatusMessage):
    """extract map from game status message in this method"""
    state = gamestatus_message.state
    global map
    map = state['map']['map']


def get_character_info(message: RequestGameOperationMessage, gamestatus):
    """Method for getting any important information about active character globally"""
    character_Id = message.character_Id
    characters = gamestatus.state['characters']
    global active_character_info
    character_info = []
    for char in characters:
        if char['characterId'] == character_Id:
            character_info = char
    active_character_info = character_info


######  possible actions      ######

def move(active_character, characters, request_gameop_message):
    """ Method for Move Action"""
    character_coordinates = get_character_coordinates(active_character)
    neighbours = get_neighbours(character_coordinates)
    print("TRY TO MOVE")

    if active_character['mp'] > 0:
        movable = get_movable(neighbours, characters, character_coordinates)

        movement_message = {
            "operation": {
                "from": {
                    "x": character_coordinates[0],
                    "y": character_coordinates[1]
                },
                "characterId": str(active_character['characterId']),
                "type": "MOVEMENT",
                "target": {
                    "x": movable[0],
                    "y": movable[1]
                }
            },
            "clientId": request_gameop_message.client_Id,
            "type": "GAME_OPERATION",
            "creationDate": request_gameop_message.creation_date
        }
        json_message = json.dumps(movement_message)

        send(ws, json_message)
        print("MOVE ACTION")
        return True
    return False


def retire(active_character, request_gameop_message):
    """Method for Retire Action"""
    print("RETIRE")
    retire_message = {
        "operation": {
            "characterId": active_character['characterId'],
            "type": "RETIRE",
            "target": {
                "x": 0,
                "y": 0
            }
        },
        "clientId": request_gameop_message.client_Id,
        "type": "GAME_OPERATION",
        "creationDate": request_gameop_message.creation_date
    }

    json_message = json.dumps(retire_message)

    send(ws, json_message)
    return True


def strike_retire(active_character_id, request_gameop_message):
    """Method for strike retire which is a little different compared to normal retire action as it needs the character id directly"""
    print("RETIRE")
    retire_message = {
        "operation": {
            "characterId": active_character_id,
            "type": "RETIRE",
            "target": {
                "x": 0,
                "y": 0
            }
        },
        "clientId": request_gameop_message.client_Id,
        "type": "GAME_OPERATION",
        "creationDate": request_gameop_message.creation_date
    }

    json_message = json.dumps(retire_message)

    send(ws, json_message)
    return True


def gamble(active_character, stake, request_gameop_message):
    """Method for gamble action"""
    neighbours = get_neighbours(get_character_coordinates(active_character))
    neighbours_coords = get_neighbours_coords(get_character_coordinates(active_character))
    print("TRY TO GAMBLE with ", stake, "CHIPS while having ", active_character['chips'])
    chips = active_character['chips']
    if active_character['ap'] > 0 and chips >= stake:
        # print("PASSED CHIPS CHECK")
        roulette_table = []
        for i, neighbour in enumerate(neighbours):
            if neighbour['state'] == "ROULETTE_TABLE":
                roulette_table = neighbours_coords[i]
                gamble_message = {
                    "operation": {
                        "stake": stake,
                        "characterId": active_character['characterId'],
                        "type": "GAMBLE_ACTION",
                        "target": {
                            "x": roulette_table[0],
                            "y": roulette_table[1]
                        }
                    },
                    "clientId": request_gameop_message.client_Id,
                    "type": "GAME_OPERATION",
                    "creationDate": request_gameop_message.creation_date
                }
                json_message = json.dumps(gamble_message)

                send(ws, json_message)
                print("GAMBLE")
                return True

    return False


def property_action(active_character, property_enum, characters, request_gameop_message):
    """Property Actions are treated in a seperated way and some checks have to be made for avoiding strikes"""
    if property_enum == "BANG_AND_BURN":
        neighbours_coords = get_neighbours_coords(get_character_coordinates(active_character))
        neighbours = get_neighbours(get_character_coordinates(active_character))
        print("TRY TO BANG AND BURN")

        for i, neighbour in enumerate(neighbours):
            if neighbour['state'] == 'ROULETTE_TABLE':
                property_message = {
                    "operation": {
                        "usedProperty": property_enum,
                        "characterId": active_character['characterId'],
                        "type": "PROPERTY_ACTION",
                        "target": {
                            "x": neighbours_coords[i][0],
                            "y": neighbours_coords[i][1]
                        }
                    },
                    "clientId": request_gameop_message.client_Id,
                    "type": "GAME_OPERATION",
                    "creationDate": request_gameop_message.creation_date
                }
                json_message = json.dumps(property_message)

                send(ws, json_message)
                print("BANG AND BURN ACTION")
                return True
    if property_enum == "OBSERVATION":
        print("TRY TO OBSERVATION")

        char_coords = get_character_coordinates(active_character)
        in_los_coords = is_in_los_coords(char_coords, False, characters)
        in_los = is_in_los(char_coords, False, characters)

        for i, target in enumerate(in_los):
            if is_character_on_field(in_los_coords[i], characters):
                property_message = {
                    "operation": {
                        "usedProperty": property_enum,
                        "characterId": active_character['characterId'],
                        "type": "PROPERTY_ACTION",
                        "target": {
                            "x": in_los_coords[i][0],
                            "y": in_los_coords[i][1]
                        }
                    },
                    "clientId": request_gameop_message.client_Id,
                    "type": "GAME_OPERATION",
                    "creationDate": request_gameop_message.creation_date
                }
                json_message = json.dumps(property_message)

                send(ws, json_message)
                print("OBSERVATION ACTION")
                return True
    return False


def neighbour_action(active_character, gadget_enum, characters, request_gameop_message):
    """Neighbour Actions are actions that can only be performed on neighbours using gadgets except cocktail action
                            which can also be performed on the character himself and handling spy action"""
    neighbours_coords = get_neighbours_coords(get_character_coordinates(active_character))
    neighbours = get_neighbours(get_character_coordinates(active_character))
    print("TRY TO ", str(gadget_enum))

    for i, neighbour in enumerate(neighbours):
        if gadget_enum == "SPY_ACTION":

            if is_character_on_field(neighbours_coords[i], characters) or neighbour['state'] == "SAFE":
                return spy_action(active_character, neighbours_coords[i], request_gameop_message)

        if gadget_enum == "TECHNICOLOUR_PRISM" and neighbour['state'] == 'ROULETTE_TABLE':
            return gadget_action(active_character, gadget_enum, neighbours_coords[i], request_gameop_message)

        elif gadget_enum == "HAIRDRYER" and is_character_on_field(neighbours_coords[i], characters):
            return gadget_action(active_character, gadget_enum, neighbours_coords[i], request_gameop_message)

        elif gadget_enum == "POISON_PILLS" and (
                (neighbour['state'] == 'BAR_TABLE' and neighbour['gadget']['gadget'] == 'COCKTAIL') or
                is_character_on_field(neighbours_coords[i], characters) and 'COCKTAIL' in
                get_character_on_field(neighbours_coords[i], characters)['gadgets']):
            return gadget_action(active_character, gadget_enum, neighbours_coords[i], request_gameop_message)

        elif gadget_enum == "GAS_GLOSS":
            return gadget_action(active_character, gadget_enum, neighbours_coords[i], request_gameop_message)
        elif (gadget_enum == "WIRETAP_WITH_EARPLUGS" or gadget_enum == "CHICKEN_FEED" or gadget_enum == "NUGGET" or
              gadget_enum == "MIRROR_OF_WILDERNESS") and is_character_on_field(neighbours_coords[i], characters):
            return gadget_action(active_character, gadget_enum, neighbours_coords[i], request_gameop_message)

        elif gadget_enum == "COCKTAIL":
            if is_character_on_field(neighbours_coords[i], characters):
                return gadget_action(active_character, gadget_enum, neighbours_coords[i], request_gameop_message)
            else:
                return gadget_action(active_character, gadget_enum, get_character_coordinates(active_character),
                                     request_gameop_message)

    return False


def lof_action(active_character, gadget_enum, characters, request_gameop_message):
    """Lof Actions are Actions that can be performed in line of sight using gadgets"""
    char_coords = get_character_coordinates(active_character)
    in_los_coords = is_in_los_coords(char_coords, False, characters)
    in_los = is_in_los(char_coords, False, characters)
    print("TRY TO ", str(gadget_enum))

    for i, target in enumerate(in_los):
        # print(in_los)
        if gadget_enum == "MOLEDIE" and in_range(char_coords, settings["moledieRange"], in_los_coords[i]) and target[
            'state'] != "WALL":
            return gadget_action(active_character, gadget_enum, in_los_coords[i], request_gameop_message)
        elif gadget_enum == "LASER_COMPACT" and target['state'] == 'BAR_TABLE' and target['gadget'][
            'gadget'] == 'COCKTAIL':
            return gadget_action(active_character, gadget_enum, in_los_coords[i], request_gameop_message)
        elif gadget_enum == "ROCKET_PEN":
            return gadget_action(active_character, gadget_enum, in_los_coords[i], request_gameop_message)
        elif gadget_enum == "MOTHBALL_POUCH" and in_range(char_coords, settings['mothballPouchRange'],
                                                          in_los_coords[i]) and target['state'] == 'FIREPLACE':
            return gadget_action(active_character, gadget_enum, in_los_coords[i], request_gameop_message)
        elif gadget_enum == "GRAPPLE" and in_range(char_coords, settings['grappleRange'],
                                                   in_los_coords[i]):  # TODO check for gadget if buggy
            return gadget_action(active_character, gadget_enum, in_los_coords[i], request_gameop_message)
        elif gadget_enum == "FOG_TIN" and in_range(char_coords, settings['fogTinRange'], in_los_coords[i]) and target[
            'state'] != 'WALL':
            return gadget_action(active_character, gadget_enum, in_los_coords[i], request_gameop_message)

    if gadget_enum == "BOWLER_BLADE":
        in_los_coords = is_in_los_coords(char_coords, True, characters)
        in_los = is_in_los(char_coords, True, characters)
        for i, target in enumerate(in_los):
            if in_range(char_coords, settings['bowlerBladeRange'], in_los_coords[i]):
                return gadget_action(active_character, gadget_enum, in_los_coords[i], request_gameop_message)

    return False


def jetpack_action(active_character, gadget_enum, characters, request_gameop_message):
    """Method for jetpack action to jump on random field on map where no character is"""
    i = 0
    while i <= 10:
        # print("MAP: ", map)
        i_rand = random.uniform(0, len(map))
        j_rand = random.uniform(0, len(map[i_rand]))
        if map[i_rand][j_rand]['state'] == 'FREE' and not is_character_on_field((i_rand, j_rand), characters):
            return gadget_action(active_character, gadget_enum, (i_rand, j_rand), request_gameop_message)
    return False


def spy_action(active_character, target_coords, message):
    """Method for Spy Action on possible character"""
    spy_message = {
        "operation": {
            "characterId": active_character['characterId'],
            "type": "SPY_ACTION",
            "target": {
                "x": target_coords[0],
                "y": target_coords[1]
            }
        },
        "clientId": message.client_Id,
        "type": "GAME_OPERATION",
        "creationDate": message.creation_date
    }
    json_message = json.dumps(spy_message)
    print("SPY ACTION")

    send(ws, json_message)
    return True


def gadget_action(active_character, gadget, target_coords, message):
    """Method for Gadget Action"""
    gadget_message = {
        "operation": {
            "gadget": gadget,
            "characterId": active_character["characterId"],
            "type": "GADGET_ACTION",
            "target": {
                "x": target_coords[0],
                "y": target_coords[1]
            }
        },
        "clientId": message.client_Id,
        "type": "GAME_OPERATION",
        "creationDate": message.creation_date
    }
    json_message = json.dumps(gadget_message)

    send(ws, json_message)
    print(str(gadget), " ACTION")

    return True


##### end of possible actions  #####


def has_gadget(active_character, gadget_enum):
    """Method for checking if active character has gadget"""
    return gadget_enum in active_character['gadgets']


def get_movable(neighbours, characters, character_coordinates):
    """Method for returning fields which are movable"""
    for i, neighbour in enumerate(neighbours):
        if neighbour['state'] == "FREE":
            for character in characters:
                if get_character_coordinates(character) not in get_neighbours_coords(character_coordinates):
                    movable = get_neighbours_coords(character_coordinates)[i]
                    return movable


def get_character_coordinates(character):
    """get coordinates of a character"""
    return [int(character['coordinates']['x']), int(character['coordinates']['y'])]


def in_range(base, action_range, check):
    """Method for checking if coordinates check are in action_range of coordinates base"""
    return abs(base[0] - check[0]) <= action_range and abs(base[1] - check[1]) <= action_range


def get_neighbours(base):
    """Method for returning all neighbour fields of a base field"""
    neighbours = []
    iBase = base[0]
    jBase = base[1]

    if jBase - 1 >= 0:
        neighbours.append(map[iBase][jBase - 1])

    if jBase + 1 < len(map[iBase]):
        neighbours.append(map[iBase][jBase + 1])

    if iBase - 1 >= 0:
        neighbours.append(map[iBase - 1][jBase])

    if iBase + 1 < len(map):
        neighbours.append(map[iBase + 1][jBase])

    if iBase - 1 >= 0 and jBase - 1 >= 0:
        neighbours.append(map[iBase - 1][jBase - 1])

    if iBase - 1 >= 0 and jBase + 1 < len(map[iBase]):
        neighbours.append(map[iBase - 1][jBase + 1])

    if iBase + 1 < len(map) and jBase - 1 >= 0:
        neighbours.append(map[iBase + 1][jBase - 1])

    if iBase + 1 < len(map) and jBase + 1 < len(map[iBase]):
        neighbours.append(map[iBase + 1][jBase + 1])

    return neighbours


def get_neighbours_coords(base):
    """Method for returning all neighbour coordinates for a certain coordinate pair"""
    neighbours = []
    iBase = base[0]
    jBase = base[1]

    if jBase - 1 >= 0:
        neighbours.append((iBase, jBase - 1))

    if jBase + 1 < len(map[iBase]):
        neighbours.append((iBase, jBase + 1))

    if iBase - 1 >= 0:
        neighbours.append((iBase - 1, jBase))

    if iBase + 1 < len(map):
        neighbours.append((iBase + 1, jBase))

    if iBase - 1 >= 0 and jBase - 1 >= 0:
        neighbours.append((iBase - 1, jBase - 1))

    if iBase - 1 >= 0 and jBase + 1 < len(map[iBase]):
        neighbours.append((iBase - 1, jBase + 1))

    if iBase + 1 < len(map) and jBase - 1 >= 0:
        neighbours.append((iBase + 1, jBase - 1))

    if iBase + 1 < len(map) and jBase + 1 < len(map[iBase]):
        neighbours.append((iBase + 1, jBase + 1))

    return neighbours


def is_in_los(base, bowlerblade, characters):
    """Method for returning all fields in line of sight"""
    iBase = base[0]
    jBase = base[1]

    iBase -= 1
    jBase -= 1
    lof = []
    while iBase >= 0 and jBase >= 0:  # left upper diagonal
        if not_in_lof(iBase, jBase, bowlerblade, characters, base):
            break
        lof.append(map[iBase][jBase])
        iBase -= 1
        jBase -= 1

    iBase = base[0]
    jBase = base[1]

    while iBase >= 0 and jBase < len(map[iBase]):  # right upper diagonal
        if not_in_lof(iBase, jBase, bowlerblade, characters, base):
            break
        lof.append(map[iBase][jBase])
        iBase -= 1
        jBase += 1

    iBase = base[0]
    jBase = base[1]

    while iBase < len(map) and jBase >= 0:  # left upper diagonal
        if not_in_lof(iBase, jBase, bowlerblade, characters, base):
            break
        lof.append(map[iBase][jBase])
        iBase += 1
        jBase -= 1

    iBase = base[0]
    jBase = base[1]

    while iBase < len(map) and jBase < len(map[iBase]):  # right bottom diagonal
        if not_in_lof(iBase, jBase, bowlerblade, characters, base):
            break
        lof.append(map[iBase][jBase])
        iBase += 1
        jBase += 1

    iBase = base[0]
    jBase = base[1]

    while iBase < len(map):  # down straight
        if not_in_lof(iBase, jBase, bowlerblade, characters, base):
            break
        lof.append(map[iBase][jBase])
        iBase += 1

    iBase = base[0]

    while iBase >= 0:  # up straight
        if not_in_lof(iBase, jBase, bowlerblade, characters, base):
            break
        lof.append(map[iBase][jBase])
        iBase -= 1

    iBase = base[0]
    jBase = base[1]

    while jBase >= 0:  # left straight
        if not_in_lof(iBase, jBase, bowlerblade, characters, base):
            break
        lof.append(map[iBase][jBase])
        jBase -= 1

    jBase = base[1]

    while jBase < len(map[iBase]):  # right straight
        if not_in_lof(iBase, jBase, bowlerblade, characters, base):
            break
        lof.append(map[iBase][jBase])
        jBase += 1

    return lof


def is_in_los_coords(base, bowlerblade, characters):
    """Method for returning all coordinates in line of sight of a coordinates pair"""
    iBase = base[0]
    jBase = base[1]

    iBase -= 1
    jBase -= 1
    lof = []
    while iBase >= 0 and jBase >= 0:  # left upper diagonal
        if not_in_lof(iBase, jBase, bowlerblade, characters, base):
            break
        lof.append((iBase, jBase))
        iBase -= 1
        jBase -= 1

    iBase = base[0]
    jBase = base[1]

    while iBase >= 0 and jBase < len(map[iBase]):  # right upper diagonal
        if not_in_lof(iBase, jBase, bowlerblade, characters, base):
            break
        lof.append((iBase, jBase))
        iBase -= 1
        jBase += 1

    iBase = base[0]
    jBase = base[1]

    while iBase < len(map) and jBase >= 0:  # left upper diagonal
        if not_in_lof(iBase, jBase, bowlerblade, characters, base):
            break
        lof.append((iBase, jBase))
        iBase += 1
        jBase -= 1

    iBase = base[0]
    jBase = base[1]

    while iBase < len(map) and jBase < len(map[iBase]):  # right bottom diagonal
        if not_in_lof(iBase, jBase, bowlerblade, characters, base):
            break
        lof.append((iBase, jBase))
        iBase += 1
        jBase += 1

    iBase = base[0]
    jBase = base[1]

    while iBase < len(map):  # down straight
        if not_in_lof(iBase, jBase, bowlerblade, characters, base):
            break
        lof.append((iBase, jBase))
        iBase += 1

    iBase = base[0]

    while iBase >= 0:  # up straight
        if not_in_lof(iBase, jBase, bowlerblade, characters, base):
            break
        lof.append((iBase, jBase))
        iBase -= 1

    iBase = base[0]
    jBase = base[1]

    while jBase >= 0:  # left straight
        if not_in_lof(iBase, jBase, bowlerblade, characters, base):
            break
        lof.append((iBase, jBase))
        jBase -= 1

    jBase = base[1]

    while jBase < len(map[iBase]):  # right straight
        if not_in_lof(iBase, jBase, bowlerblade, characters, base):
            break
        lof.append((iBase, jBase))
        jBase += 1

    return lof


def not_in_lof(iBase, jBase, bowlerblade, characters, base):
    """Method for not in line of sight"""
    return map[iBase][jBase]['state'] == "WALL" or map[iBase][jBase][
        'state'] == "FIREPLACE" or bowlerblade and is_character_on_field((iBase, jBase), characters) or \
           map[iBase][jBase]['isFoggy'] is True and (iBase, jBase) != base


def is_character_on_field(field, characters):
    """Method for checking if a character is on a field"""
    for character in characters:
        coords = get_character_coordinates(character)
        # print("COORDS: ", coords, "FIELD: ", field)
        if coords[0] == field[0] and coords[1] == field[1]:
            return True

    return False


def get_character_on_field(field, characters):
    """Method for returning a character on a certain field"""
    for character in characters:
        coords = get_character_coordinates(character)
        # print("COORDS : ", coords)
        if coords[0] == field[0] and coords[1] == field[1]:
            return character


def on_message(ws, message):
    time.sleep(1)
    handle_message(message)


def on_error(ws, error):
    print(error)


def on_close(ws):
    print("### closed ###")


def send(ws, message):
    print("Sending Message: ", message)
    ws.send(message)


def on_open(ws):
    def run(*args):
        for i in range(1):
            time.sleep(1)
            hello_message = {"name": ki_name, "role": "PLAYER", "type": "HELLO",
                             "creationDate": "May 23, 2020, 2:53:45 PM"}  # TODO: Datetime with right format has to be inserted
            json_message = json.dumps(hello_message)

            send(ws, json_message)  # send hello message when opening ki-client
        time.sleep(1)

    thread.start_new_thread(run, ())


ki_name = "KI"
ip = "127.0.0.1"
port = 7007


def shell():
    """Shell for Opening kiclient"""
    usage = "ki002 starts the ki with the following parameters:\n" \
            "--port [PORT] ||| Alias -p [PORT] ||| Default: 7007\n" \
            "-- address [IP] ||| Alias -a [IP] ||| Default: localhost\n" \
            "--x [KEY] [VALUE] \n" \
            "--help ||| Alias -h\n" \
            "--verbosity [LEVEL] ||| Alias -v [LEVEL]\n" \
            "--name [KINAME] ||| Alias -n [KINAME]\n" \
            "--difficulty [DIFFICULTY] ||| Alias -d [DIFFICULTY]"
    command = input('"use ki002 --help" or "ki002 -h" for further information\n')
    global ki_name
    global ip
    global port
    command = command.split(" ")
    if command[0] != "ki002":
        print(usage)
        shell()
    if "--address" in command:
        ip = command[command.index("--adress") + 1]
    elif "-a" in command:
        ip = command[command.index("-a") + 1]
    if "-x" in command:
        print(usage)
        shell()
    if "--help" in command:
        print(usage)
        shell()
    elif "-h" in command:
        print(usage)
        shell()
    if "--port" in command:
        port = command[command.index("--port") + 1]
    elif "-p" in command:
        port = command[command.index("-p") + 1]
    if "--verbosity" in command:
        print(usage)
        shell()
    elif "-v" in command:
        print(usage)
        shell()
    if "--name" in command:
        ki_name = command[command.index("--name") + 1]
    elif "-n" in command:
        ki_name = command[command.index("-n") + 1]
    if "--difficulty" in command:
        print(usage)
        shell()
    elif "-d" in command:
        print(usage)
        shell()


if __name__ == "__main__":
    websocket.enableTrace(True)

    shell()
    print("ws://" + ip + ":" + str(port))
    ws = websocket.WebSocketApp("ws://" + ip + ":" + str(port),
                                on_message=on_message,
                                on_error=on_error,
                                on_close=on_close)
    ws.on_open = on_open
    ws.run_forever()
