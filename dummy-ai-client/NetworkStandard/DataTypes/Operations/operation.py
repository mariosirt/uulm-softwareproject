from NetworkStandard.DataTypes.Operations.base_operation import BaseOperation
import uuid



class Operation(BaseOperation):

    def __init__(self, typeEnum, successful, target, characterId: uuid.UUID):
        super().__init__(typeEnum, successful, target)
        self.characterId = characterId

