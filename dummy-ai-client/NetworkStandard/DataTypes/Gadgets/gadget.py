from NetworkStandard.DataTypes.gadget_enum import GadgetEnum


class Gadget:

    def __init__(self, gadget: GadgetEnum):
        self.gadget = gadget
        self.usages = 0

    def set_usages(self, usages: int):
        self.usages = usages
