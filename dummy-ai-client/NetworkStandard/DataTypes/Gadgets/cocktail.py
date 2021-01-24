from NetworkStandard.DataTypes.Gadgets.gadget import Gadget
from NetworkStandard.DataTypes.gadget_enum import GadgetEnum


class Cocktail(Gadget):

    def __init__(self, gadget: GadgetEnum):
        super.__init__(gadget)
        self.is_poisoned = False

    def set_poisoned(self, is_poisoned):
        self.is_poisoned = is_poisoned
