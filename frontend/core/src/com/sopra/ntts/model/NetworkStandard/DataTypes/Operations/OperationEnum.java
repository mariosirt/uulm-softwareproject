package com.sopra.ntts.model.NetworkStandard.DataTypes.Operations;

/**
 * @author standardisation comitee
 * An Operation is a possible action, the player sends to the server. This is being done for any character through
 * the so called RequestGameOperationMessage (server side) and answered by the playing client through a GameOperationMessage.
 * <p>
 * Further information on standardisation document p. 22.
 */


public enum OperationEnum {
    GADGET_ACTION,
    SPY_ACTION,
    GAMBLE_ACTION,
    PROPERTY_ACTION,
    MOVEMENT,
    CAT_ACTION,
    JANITOR_ACTION,
    EXFILTRATION,
    RETIRE
}