package com.sopra.ntts.model.NetworkStandard.DataTypes.Operations;

import java.awt.*;
import java.util.UUID;

public class Retire extends Operation {
    public Retire(OperationEnum type, Boolean successful, Point target, UUID characterId) {
        super(type, successful, target, characterId);
    }
}
