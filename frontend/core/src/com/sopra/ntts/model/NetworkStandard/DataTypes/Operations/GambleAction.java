package com.sopra.ntts.model.NetworkStandard.DataTypes.Operations;

import java.awt.*;
import java.util.UUID;

/**
 * A class for a GambleAction, which can be used at roulette tables. (OperationEnum GAMBLE_ACTION)
 *
 * stake: the amount of chips being set on this particular GambleAction
 */


public class GambleAction extends Operation {
    private Integer stake;


    public GambleAction(OperationEnum type, Boolean successful, Point target, UUID characterId, Integer stake) {
        super(type, successful, target, characterId);
        this.stake = stake;
    }

    public Integer getStake() {
        return stake;
    }

    public void setStake(Integer stake) {
        this.stake = stake;
    }

    @Override
    public String toString() {
        return "GambleAction{" +
          "stake=" + stake +
          '}';
    }
}
