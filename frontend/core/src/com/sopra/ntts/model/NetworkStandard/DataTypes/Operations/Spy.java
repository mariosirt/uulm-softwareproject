package com.sopra.ntts.model.NetworkStandard.DataTypes.Operations;

import java.awt.*;
import java.util.UUID;

public class Spy extends Operation {
    public Spy(OperationEnum type, Boolean successful, Point target, UUID characterId) {
        super(type, successful, target, characterId);
    }
}
