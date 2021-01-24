import uuid
from ..point import Point


class Character:

    def __init__(self, characterId: uuid.UUID, name: str, coordinates: Point, mp: int, ap: int, hp: int, ip: int,
                 chips: int, properties: set, gadgets: set):
        self.characterId = characterId
        self.name = name
        self.coordinates = coordinates
        self.mp = mp
        self.ap = ap
        self.hp = hp
        self.ip = ip
        self.chips = chips
        self.properties = properties
        self.gadgets = gadgets
