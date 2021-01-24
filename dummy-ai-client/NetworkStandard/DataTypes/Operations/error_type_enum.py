from enum import  Enum
from enum import auto

class ErrorTypeEnum(Enum):
    NAME_NOT_AVAILABLE = auto()
    ALREADY_SERVING = auto()
    SESSION_DOES_NOT_EXIST = auto()
    ILLEGAL_MESSAGE = auto()
    TOO_MANY_STRIKES = auto()
    GENERAL = auto()