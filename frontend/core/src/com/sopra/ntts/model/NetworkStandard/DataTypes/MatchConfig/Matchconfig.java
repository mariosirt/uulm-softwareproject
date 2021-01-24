package com.sopra.ntts.model.NetworkStandard.DataTypes.MatchConfig;

import java.util.Objects;

/**
 * This class represents the configurations of a match.
 *
 * @author Christian Wendlinger
 */
public class Matchconfig {
    /* Gadget: Moledie */
    private Integer moledieRange;
    /* Gadget: BowlerBlade */
    private Integer bowlerBladeRange;
    private Double bowlerBladeHitChance;
    private Integer bowlerBladeDamage;
    /* Gadget: LaserCompact */
    private Double laserCompactHitChance;
    /* Gadget: RocketPen */
    private Integer rocketPenDamage;
    /* Gadget: GasGloss */
    private Integer gasGlossDamage;
    /* Gadget: MothballPouch */
    private Integer mothballPouchRange;
    private Integer mothballPouchDamage;
    /* Gadget: FogTin */
    private Integer fogTinRange;
    /* Gadget: Grapple */
    private Integer grappleRange;
    private Double grappleHitChance;
    /* Gadget: WiretapWithEarplugs */
    private Double wiretapWithEarplugsFailChance;
    /* Gadget: Mirror */
    private Double mirrorSwapChance;
    /* Gadget: Cocktail */
    private Double cocktailDodgeChance;
    private Integer cocktailHp;
    /* Aktionen */
    private Double spySuccessChance;
    private Double babysitterSuccessChance;
    private Double honeyTrapSuccessChance;
    private Double observationSuccessChance;
    /* Spielfaktoren */
    private Integer chipsToIpFactor;
    private Integer secretToIpFactor;
    private Integer minChipsRoulette;
    private Integer maxChipsRoulette;
    private Integer roundLimit;
    private Integer turnPhaseLimit;
    private Integer catIp;
    private Integer strikeMaximum;
    private Integer pauseLimit;
    private Integer reconnectLimit;

    /**
     * Constructor.
     */
    public Matchconfig(Integer moledieRange, Integer bowlerBladeRange, Double bowlerBladeHitChance, Integer bowlerBladeDamage, Double laserCompactHitChance, Integer rocketPenDamage, Integer gasGlossDamage, Integer mothballPouchRange, Integer mothballPouchDamage, Integer fogTinRange, Integer grappleRange, Double grappleHitChance, Double wiretapWithEarplugsFailChance, Double mirrorSwapChance, Double cocktailDodgeChance, Integer cocktailHp, Double spySuccessChance, Double babysitterSuccessChance, Double honeyTrapSuccessChance, Double observationSuccessChance, Integer chipsToIpFactor, Integer secretToIpFactor, Integer minChipsRoulette, Integer maxChipsRoulette, Integer roundLimit, Integer turnPhaseLimit, Integer catIp, Integer strikeMaximum, Integer pauseLimit, Integer reconnectLimit) {
        this.moledieRange = moledieRange;
        this.bowlerBladeRange = bowlerBladeRange;
        this.bowlerBladeHitChance = bowlerBladeHitChance;
        this.bowlerBladeDamage = bowlerBladeDamage;
        this.laserCompactHitChance = laserCompactHitChance;
        this.rocketPenDamage = rocketPenDamage;
        this.gasGlossDamage = gasGlossDamage;
        this.mothballPouchRange = mothballPouchRange;
        this.mothballPouchDamage = mothballPouchDamage;
        this.fogTinRange = fogTinRange;
        this.grappleRange = grappleRange;
        this.grappleHitChance = grappleHitChance;
        this.wiretapWithEarplugsFailChance = wiretapWithEarplugsFailChance;
        this.mirrorSwapChance = mirrorSwapChance;
        this.cocktailDodgeChance = cocktailDodgeChance;
        this.cocktailHp = cocktailHp;
        this.spySuccessChance = spySuccessChance;
        this.babysitterSuccessChance = babysitterSuccessChance;
        this.honeyTrapSuccessChance = honeyTrapSuccessChance;
        this.observationSuccessChance = observationSuccessChance;
        this.chipsToIpFactor = chipsToIpFactor;
        this.secretToIpFactor = secretToIpFactor;
        this.minChipsRoulette = minChipsRoulette;
        this.maxChipsRoulette = maxChipsRoulette;
        this.roundLimit = roundLimit;
        this.turnPhaseLimit = turnPhaseLimit;
        this.catIp = catIp;
        this.strikeMaximum = strikeMaximum;
        this.pauseLimit = pauseLimit;
        this.reconnectLimit = reconnectLimit;
    }

    /**
     * Getter and Setter.
     */
    public Integer getMoledieRange() {
        return moledieRange;
    }

    public void setMoledieRange(Integer moledieRange) {
        this.moledieRange = moledieRange;
    }

    public Integer getBowlerBladeRange() {
        return bowlerBladeRange;
    }

    public void setBowlerBladeRange(Integer bowlerBladeRange) {
        this.bowlerBladeRange = bowlerBladeRange;
    }

    public Double getBowlerBladeHitChance() {
        return bowlerBladeHitChance;
    }

    public void setBowlerBladeHitChance(Double bowlerBladeHitChance) {
        this.bowlerBladeHitChance = bowlerBladeHitChance;
    }

    public Integer getBowlerBladeDamage() {
        return bowlerBladeDamage;
    }

    public void setBowlerBladeDamage(Integer bowlerBladeDamage) {
        this.bowlerBladeDamage = bowlerBladeDamage;
    }

    public Double getLaserCompactHitChance() {
        return laserCompactHitChance;
    }

    public void setLaserCompactHitChance(Double laserCompactHitChance) {
        this.laserCompactHitChance = laserCompactHitChance;
    }

    public Integer getRocketPenDamage() {
        return rocketPenDamage;
    }

    public void setRocketPenDamage(Integer rocketPenDamage) {
        this.rocketPenDamage = rocketPenDamage;
    }

    public Integer getGasGlossDamage() {
        return gasGlossDamage;
    }

    public void setGasGlossDamage(Integer gasGlossDamage) {
        this.gasGlossDamage = gasGlossDamage;
    }

    public Integer getMothballPouchRange() {
        return mothballPouchRange;
    }

    public void setMothballPouchRange(Integer mothballPouchRange) {
        this.mothballPouchRange = mothballPouchRange;
    }

    public Integer getMothballPouchDamage() {
        return mothballPouchDamage;
    }

    public void setMothballPouchDamage(Integer mothballPouchDamage) {
        this.mothballPouchDamage = mothballPouchDamage;
    }

    public Integer getFogTinRange() {
        return fogTinRange;
    }

    public void setFogTinRange(Integer fogTinRange) {
        this.fogTinRange = fogTinRange;
    }

    public Integer getGrappleRange() {
        return grappleRange;
    }

    public void setGrappleRange(Integer grappleRange) {
        this.grappleRange = grappleRange;
    }

    public Double getGrappleHitChance() {
        return grappleHitChance;
    }

    public void setGrappleHitChance(Double grappleHitChance) {
        this.grappleHitChance = grappleHitChance;
    }

    public Double getWiretapWithEarplugsFailChance() {
        return wiretapWithEarplugsFailChance;
    }

    public void setWiretapWithEarplugsFailChance(Double wiretapWithEarplugsFailChance) {
        this.wiretapWithEarplugsFailChance = wiretapWithEarplugsFailChance;
    }

    public Double getMirrorSwapChance() {
        return mirrorSwapChance;
    }

    public void setMirrorSwapChance(Double mirrorSwapChance) {
        this.mirrorSwapChance = mirrorSwapChance;
    }

    public Double getCocktailDodgeChance() {
        return cocktailDodgeChance;
    }

    public void setCocktailDodgeChance(Double cocktailDodgeChance) {
        this.cocktailDodgeChance = cocktailDodgeChance;
    }

    public Integer getCocktailHp() {
        return cocktailHp;
    }

    public void setCocktailHp(Integer cocktailHp) {
        this.cocktailHp = cocktailHp;
    }

    public Double getSpySuccessChance() {
        return spySuccessChance;
    }

    public void setSpySuccessChance(Double spySuccessChance) {
        this.spySuccessChance = spySuccessChance;
    }

    public Double getBabysitterSuccessChance() {
        return babysitterSuccessChance;
    }

    public void setBabysitterSuccessChance(Double babysitterSuccessChance) {
        this.babysitterSuccessChance = babysitterSuccessChance;
    }

    public Double getHoneyTrapSuccessChance() {
        return honeyTrapSuccessChance;
    }

    public void setHoneyTrapSuccessChance(Double honeyTrapSuccessChance) {
        this.honeyTrapSuccessChance = honeyTrapSuccessChance;
    }

    public Double getObservationSuccessChance() {
        return observationSuccessChance;
    }

    public void setObservationSuccessChance(Double observationSuccessChance) {
        this.observationSuccessChance = observationSuccessChance;
    }

    public Integer getChipsToIpFactor() {
        return chipsToIpFactor;
    }

    public void setChipsToIpFactor(Integer chipsToIpFactor) {
        this.chipsToIpFactor = chipsToIpFactor;
    }

    public Integer getSecretToIpFactor() {
        return secretToIpFactor;
    }

    public void setSecretToIpFactor(Integer secretToIpFactor) {
        this.secretToIpFactor = secretToIpFactor;
    }

    public Integer getMinChipsRoulette() {
        return minChipsRoulette;
    }

    public void setMinChipsRoulette(Integer minChipsRoulette) {
        this.minChipsRoulette = minChipsRoulette;
    }

    public Integer getMaxChipsRoulette() {
        return maxChipsRoulette;
    }

    public void setMaxChipsRoulette(Integer maxChipsRoulette) {
        this.maxChipsRoulette = maxChipsRoulette;
    }

    public Integer getRoundLimit() {
        return roundLimit;
    }

    public void setRoundLimit(Integer roundLimit) {
        this.roundLimit = roundLimit;
    }

    public Integer getTurnPhaseLimit() {
        return turnPhaseLimit;
    }

    public void setTurnPhaseLimit(Integer turnPhaseLimit) {
        this.turnPhaseLimit = turnPhaseLimit;
    }

    public Integer getCatIp() {
        return catIp;
    }

    public void setCatIp(Integer catIp) {
        this.catIp = catIp;
    }

    public Integer getStrikeMaximum() {
        return strikeMaximum;
    }

    public void setStrikeMaximum(Integer strikeMaximum) {
        this.strikeMaximum = strikeMaximum;
    }

    public Integer getPauseLimit() {
        return pauseLimit;
    }

    public void setPauseLimit(Integer pauseLimit) {
        this.pauseLimit = pauseLimit;
    }

    public Integer getReconnectLimit() {
        return reconnectLimit;
    }

    public void setReconnectLimit(Integer reconnectLimit) {
        this.reconnectLimit = reconnectLimit;
    }

    /**
     * Equals and HashCode.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matchconfig that = (Matchconfig) o;
        return Objects.equals(moledieRange, that.moledieRange) &&
          Objects.equals(bowlerBladeRange, that.bowlerBladeRange) &&
          Objects.equals(bowlerBladeHitChance, that.bowlerBladeHitChance) &&
          Objects.equals(bowlerBladeDamage, that.bowlerBladeDamage) &&
          Objects.equals(laserCompactHitChance, that.laserCompactHitChance) &&
          Objects.equals(rocketPenDamage, that.rocketPenDamage) &&
          Objects.equals(gasGlossDamage, that.gasGlossDamage) &&
          Objects.equals(mothballPouchRange, that.mothballPouchRange) &&
          Objects.equals(mothballPouchDamage, that.mothballPouchDamage) &&
          Objects.equals(fogTinRange, that.fogTinRange) &&
          Objects.equals(grappleRange, that.grappleRange) &&
          Objects.equals(grappleHitChance, that.grappleHitChance) &&
          Objects.equals(wiretapWithEarplugsFailChance, that.wiretapWithEarplugsFailChance) &&
          Objects.equals(mirrorSwapChance, that.mirrorSwapChance) &&
          Objects.equals(cocktailDodgeChance, that.cocktailDodgeChance) &&
          Objects.equals(cocktailHp, that.cocktailHp) &&
          Objects.equals(spySuccessChance, that.spySuccessChance) &&
          Objects.equals(babysitterSuccessChance, that.babysitterSuccessChance) &&
          Objects.equals(honeyTrapSuccessChance, that.honeyTrapSuccessChance) &&
          Objects.equals(observationSuccessChance, that.observationSuccessChance) &&
          Objects.equals(chipsToIpFactor, that.chipsToIpFactor) &&
          Objects.equals(secretToIpFactor, that.secretToIpFactor) &&
          Objects.equals(minChipsRoulette, that.minChipsRoulette) &&
          Objects.equals(maxChipsRoulette, that.maxChipsRoulette) &&
          Objects.equals(roundLimit, that.roundLimit) &&
          Objects.equals(turnPhaseLimit, that.turnPhaseLimit) &&
          Objects.equals(catIp, that.catIp) &&
          Objects.equals(strikeMaximum, that.strikeMaximum) &&
          Objects.equals(pauseLimit, that.pauseLimit) &&
          Objects.equals(reconnectLimit, that.reconnectLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moledieRange, bowlerBladeRange, bowlerBladeHitChance, bowlerBladeDamage, laserCompactHitChance, rocketPenDamage, gasGlossDamage, mothballPouchRange, mothballPouchDamage, fogTinRange, grappleRange, grappleHitChance, wiretapWithEarplugsFailChance, mirrorSwapChance, cocktailDodgeChance, cocktailHp, spySuccessChance, babysitterSuccessChance, honeyTrapSuccessChance, observationSuccessChance, chipsToIpFactor, secretToIpFactor, minChipsRoulette, maxChipsRoulette, roundLimit, turnPhaseLimit, catIp, strikeMaximum, pauseLimit, reconnectLimit);
    }

    @Override
    public String toString() {
        return "Matchconfig{" +
          "moledieRange=" + moledieRange +
          ", bowlerBladeRange=" + bowlerBladeRange +
          ", bowlerBladeHitChance=" + bowlerBladeHitChance +
          ", bowlerBladeDamage=" + bowlerBladeDamage +
          ", laserCompactHitChance=" + laserCompactHitChance +
          ", rocketPenDamage=" + rocketPenDamage +
          ", gasGlossDamage=" + gasGlossDamage +
          ", mothballPouchRange=" + mothballPouchRange +
          ", mothballPouchDamage=" + mothballPouchDamage +
          ", fogTinRange=" + fogTinRange +
          ", grappleRange=" + grappleRange +
          ", grappleHitChance=" + grappleHitChance +
          ", wiretapWithEarplugsFailChance=" + wiretapWithEarplugsFailChance +
          ", mirrorSwapChance=" + mirrorSwapChance +
          ", cocktailDodgeChance=" + cocktailDodgeChance +
          ", cocktailHp=" + cocktailHp +
          ", spySuccessChance=" + spySuccessChance +
          ", babysitterSuccessChance=" + babysitterSuccessChance +
          ", honeyTrapSuccessChance=" + honeyTrapSuccessChance +
          ", observationSuccessChance=" + observationSuccessChance +
          ", chipsToIpFactor=" + chipsToIpFactor +
          ", secretToIpFactor=" + secretToIpFactor +
          ", minChipsRoulette=" + minChipsRoulette +
          ", maxChipsRoulette=" + maxChipsRoulette +
          ", roundLimit=" + roundLimit +
          ", turnPhaseLimit=" + turnPhaseLimit +
          ", catIp=" + catIp +
          ", strikeMaximum=" + strikeMaximum +
          ", pauseLimit=" + pauseLimit +
          ", reconnectLimit=" + reconnectLimit +
          '}';
    }
}
