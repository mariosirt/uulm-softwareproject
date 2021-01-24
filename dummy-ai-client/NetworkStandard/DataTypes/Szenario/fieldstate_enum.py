from enum import Enum
from enum import auto


class FieldStateEnum(Enum):
    BAR_TABLE = auto()
    ROULETTE_TABLE = auto()
    WALL = auto()
    FREE = auto()
    BAR_SEAT = auto()
    SAFE = auto()
    FIREPLACE = auto()
