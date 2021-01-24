package com.sopra.ntts.model.NetworkStandard.DataTypes.Operations;

/**
 * A single entry inside of Statistics.
 *
 * title: The title of an evaluation (statistic).(e.g. "roulette wins")
 * description: description of a particular evaluation (statistic)
 * valuePlayer1: value of player one
 * valuePlayer2: value of player two
 *
 * CHANGED to work -> see NetworkStandard
 */


public enum StatisticsEntry {
    title,
    description,
    valuePlayer1,
    valuePlayer2,
}
