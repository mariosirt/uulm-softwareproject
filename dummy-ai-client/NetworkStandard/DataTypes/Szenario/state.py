from NetworkStandard.point import Point


class State:

    def __init__(self, current_round: int, field_map, my_safe_combinations: set, characters: set,
                 cat_coordinates: Point, janitor_coordinates: Point):
        self.current_round = current_round
        self.field_map = field_map
        self.my_safe_combinations = my_safe_combinations
        self.characters = characters,
        self.cat_coordinates = cat_coordinates
        self.janitor_coordinates = janitor_coordinates
