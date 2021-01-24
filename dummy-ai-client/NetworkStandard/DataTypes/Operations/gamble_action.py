from NetworkStandard.DataTypes.Operations.operation import Operation


class GambleAction(Operation):

    def __init__(self, typeEnum, successful, target, characterId, stake: int):
        super().__init__(typeEnum, successful, target, characterId)
        self.stake = stake
