from NetworkStandard.DataTypes.Operations.operation import Operation
from NetworkStandard.DataTypes.gadget_enum import GadgetEnum


class GadgetAction(Operation):

    def __init__(self, typeEnum, successful, target, characterId, gadget: GadgetEnum):
        super().__init__(typeEnum, successful, target, characterId)
        self.gadget = gadget
