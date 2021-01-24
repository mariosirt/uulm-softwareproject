class Point:

    def __init__(self, x: int, y: int):
        self.x = x
        self.y = y

    def get_distance(self, other_point):
        return self.x - other_point.x, self.y - other_point.y
