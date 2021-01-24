from NetworkStandard.DataTypes.Operations.operation import Operation
from NetworkStandard.DataTypes.property_enum import  PropertyEnum

class PropertyAction(Operation):

    def __init__(self, typeEnum, successful, target, characterId, usedProperty: PropertyEnum, isEnuemy: bool):
        super().__init__(typeEnum, successful, target, characterId)
        self.usedProperty = usedProperty
        self.isEnemy = isEnuemy