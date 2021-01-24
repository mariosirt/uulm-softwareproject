from NetworkStandard.DataTypes.Operations.operation import Operation
from NetworkStandard.point import Point

class Movement(Operation):

    def __init__(self, typeEnum, successful, target, characterId, fromPoint: Point):
        super().__init__(typeEnum, successful, target, characterId)
        self.fromPoint = fromPoint