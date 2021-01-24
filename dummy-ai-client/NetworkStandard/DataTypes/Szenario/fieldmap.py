from NetworkStandard.DataTypes.Szenario.field import Field


class FieldMap:

    def __init__(self, state_map):
        self.state_map = state_map

    def get_field(self, x: int, y: int) -> Field:
        return self.state_map[x][y]

    def set_field(self, x: int, y: int, updated_field: Field):
        self.state_map[x][y] = updated_field
