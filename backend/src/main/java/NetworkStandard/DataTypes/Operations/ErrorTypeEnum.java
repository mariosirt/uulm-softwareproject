package NetworkStandard.DataTypes.Operations;

/**
 * All possible reasons for a ConnectionErrorMessage which is necessary for connection establishment.
 * <p>
 * NAME_NOT_AVAILABLE: if the player tries to use a name which was used allready by someone else, this error occurs
 * ALREADY_SERVING: if server is allready used and not usable for any other player, this error occurs
 * GENERAL: just a placeholder and is not defined in detail
 */
public enum ErrorTypeEnum {
    NAME_NOT_AVAILABLE,
    ALREADY_SERVING,
    SESSION_DOES_NOT_EXIST,
    ILLEGAL_MESSAGE,
    TOO_MANY_STRIKES,
    GENERAL
}
