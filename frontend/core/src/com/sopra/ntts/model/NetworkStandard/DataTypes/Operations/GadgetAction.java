package com.sopra.ntts.model.NetworkStandard.DataTypes.Operations;

import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;

import java.awt.*;
import java.util.UUID;

/**
 * A class for an action which is being done using a gadget. The OperationEnum for that would be GADGET_ACTION
 * <p>
 * gadget: The gadget which is used in the operation. (e.g. GadgetEnum COCKTAIL)
 */

public class GadgetAction extends Operation {
    private GadgetEnum gadget;

    public GadgetAction(OperationEnum type, Boolean successful, Point target, UUID characterId, GadgetEnum gadget) {
        super(type, successful, target, characterId);
        this.gadget = gadget;
    }

    public GadgetEnum getGadget() {
        return gadget;
    }

    public void setGadget(GadgetEnum gadget) {
        this.gadget = gadget;
    }

    @Override
    public String toString() {
        return "GadgetAction{" +
          "gadget=" + gadget +
          '}';
    }
}
