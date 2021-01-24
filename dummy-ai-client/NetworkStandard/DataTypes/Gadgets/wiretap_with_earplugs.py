from NetworkStandard.DataTypes.Gadgets.gadget import Gadget
from NetworkStandard.DataTypes.gadget_enum import GadgetEnum

import uuid


class WiretapWithEarplugs(Gadget):

    def __init__(self, gadget: GadgetEnum):
        super.__init__(gadget)
        self.working = False;
        self.active_on = None

    def set_working(self, working):
        self.working = working

    def set_active_on(self, active_on: uuid.UUID):
        self.active_on = active_on
