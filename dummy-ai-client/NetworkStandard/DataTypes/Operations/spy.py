from NetworkStandard.DataTypes.Operations.operation import Operation


class Spy(Operation):

    def __init__(self, typeEnum, successful, target, characterId):
        super().__init__(typeEnum, successful, target, characterId)
