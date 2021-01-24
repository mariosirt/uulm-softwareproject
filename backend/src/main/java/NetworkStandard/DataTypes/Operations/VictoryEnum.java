package NetworkStandard.DataTypes.Operations;

/**
 * An enum for the reasion of a win, which is being sent within a StatisticsMessage at the end of a game.
 * <p>
 * The provided entries include the possible reasons of winning.
 */

public enum VictoryEnum {
    VICTORY_BY_IP,
    VICTORY_BY_COLLAR,
    VICTORY_BY_DRINKING,
    VICTORY_BY_SPILLING,
    VICTORY_BY_HP,
    VICTORY_BY_RANDOMNESS
}
