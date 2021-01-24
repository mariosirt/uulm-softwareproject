package com.sopra.ntts.model.NetworkStandard.Characters;

import com.sopra.ntts.model.NetworkStandard.DataTypes.Gadgets.Gadget;
import com.sopra.ntts.model.NetworkStandard.DataTypes.PropertyEnum;

import java.awt.*;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Character {
    /**
     * implements the specific features of a character
     * describes character-status in a running game
     */

    private UUID characterId;
    private String name;
    private Point coordinates;

    /**
     * mp: movementPoints
     * ap: actionPoints
     * hp: healthPoints
     * ip: intelligencePoints
     * chips: amount of chips
     */
    private Integer mp, ap, hp, ip, chips;
    private Set<PropertyEnum> properties;
    private Set<Gadget> gadgets;

    public Character(UUID characterId, String name, Point coordinates, Integer mp, Integer ap, Integer hp, Integer ip, Integer chips, Set<PropertyEnum> properties, Set<Gadget> gadgets) {
        this.characterId = characterId;
        this.name = name;
        this.coordinates = coordinates;
        this.mp = mp;
        this.ap = ap;
        this.hp = hp;
        this.ip = ip;
        this.chips = chips;
        this.properties = properties;
        this.gadgets = gadgets;
    }

    public UUID getCharacterId() {
        return characterId;
    }

    public void setCharacterId(UUID characterId) {
        this.characterId = characterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public Integer getMp() {
        return mp;
    }

    public void setMp(Integer mp) {
        this.mp = mp;
    }

    public Integer getAp() {
        return ap;
    }

    public void setAp(Integer ap) {
        this.ap = ap;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getIp() {
        return ip;
    }

    public void setIp(Integer ip) {
        this.ip = ip;
    }

    public Integer getChips() {
        return chips;
    }

    public void setChips(Integer chips) {
        this.chips = chips;
    }

    public Set<PropertyEnum> getProperties() {
        return properties;
    }

    public void setProperties(Set<PropertyEnum> properties) {
        this.properties = properties;
    }

    public Set<Gadget> getGadgets() {
        return gadgets;
    }

    public void setGadgets(Set<Gadget> gadgets) {
        this.gadgets = gadgets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return Objects.equals(getCharacterId(), character.getCharacterId()) &&
                Objects.equals(getName(), character.getName()) &&
                Objects.equals(getCoordinates(), character.getCoordinates()) &&
                Objects.equals(getMp(), character.getMp()) &&
                Objects.equals(getAp(), character.getAp()) &&
                Objects.equals(getHp(), character.getHp()) &&
                Objects.equals(getIp(), character.getIp()) &&
                Objects.equals(getChips(), character.getChips()) &&
                Objects.equals(getProperties(), character.getProperties()) &&
                Objects.equals(getGadgets(), character.getGadgets());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCharacterId(), getName(), getCoordinates(), getMp(), getAp(), getHp(), getIp(), getChips(), getProperties(), getGadgets());
    }

    @Override
    public String toString() {
        return "Character{" +
                "characterId=" + characterId +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", mp=" + mp +
                ", ap=" + ap +
                ", hp=" + hp +
                ", ip=" + ip +
                ", chips=" + chips +
                ", properties=" + properties +
                ", gadgets=" + gadgets +
                '}';
    }
}
