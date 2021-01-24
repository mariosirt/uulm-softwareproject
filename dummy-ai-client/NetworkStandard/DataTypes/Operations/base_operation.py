from .operation_enum import OperationEnum
from NetworkStandard.point import Point


class BaseOperation:

    def __init__(self, typeEnum: OperationEnum, successful: bool, target: Point):
        self.typeEnum = typeEnum
        self.successful = successful
        self.target = target
