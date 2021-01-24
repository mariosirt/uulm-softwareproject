from NetworkStandard.DataTypes.Szenario.fieldstate_enum import FieldStateEnum
from NetworkStandard.DataTypes.Gadgets.gadget import Gadget


class Field:

    def __init__(self, state: FieldStateEnum, gadget: Gadget, is_destroyed: bool, is_inverted: bool, chip_amount: int,
                 safe_index: int, is_foggy: bool, is_updated: bool):
        self.state = state
        self.gadget = gadget
        self.is_destroyed = is_destroyed
        self.is_inverted = is_inverted
        self.chip_amount = chip_amount
        self.safe_index = safe_index
        self.is_foggy = is_foggy
        self.is_updated = is_updated
