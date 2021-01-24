package NetworkStandard.DataTypes.Szenario;

import NetworkStandard.Characters.Character;

import java.awt.*;
import java.util.Objects;
import java.util.Set;

/**
 * This class represents the current state of the game.
 *
 * @author Christian Wendlinger
 */
public class State {
    private int currentRound;
    private FieldMap map;
    private Set<Integer> mySafeCombinations;
    private Set<Character> characters;
    private Point catCoordinates;
    private Point janitorCoordinates;

    /**
     * Constructor.
     *
     * @param currentRound       - current round
     * @param map                - current map
     * @param mySafeCombinations - combinations that a player acquired.
     * @param characters         - all characters on the map
     * @param catCoordinates     - coordinates of the white cat
     * @param janitorCoordinates - coordinates of the janitor
     */
    public State(int currentRound, FieldMap map, Set<Integer> mySafeCombinations, Set<Character> characters, Point catCoordinates, Point janitorCoordinates) {
        this.currentRound = currentRound;
        this.map = map;
        this.mySafeCombinations = mySafeCombinations;
        this.characters = characters;
        this.catCoordinates = catCoordinates;
        this.janitorCoordinates = janitorCoordinates;
    }

    /**
     * Getters and Setters.
     */
    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public FieldMap getMap() {
        return map;
    }

    public void setMap(FieldMap map) {
        this.map = map;
    }

    public Set<Integer> getMySafeCombinations() {
        return mySafeCombinations;
    }

    public void setMySafeCombinations(Set<Integer> mySafeCombinations) {
        this.mySafeCombinations = mySafeCombinations;
    }

    public Set<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(Set<Character> characters) {
        this.characters = characters;
    }

    public Point getCatCoordinates() {
        return catCoordinates;
    }

    public void setCatCoordinates(Point catCoordinates) {
        this.catCoordinates = catCoordinates;
    }

    public Point getJanitorCoordinates() {
        return janitorCoordinates;
    }

    public void setJanitorCoordinates(Point janitorCoordinates) {
        this.janitorCoordinates = janitorCoordinates;
    }

    /**
     * Equals and Hashcode.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return getCurrentRound() == state.getCurrentRound() &&
            Objects.equals(getMap(), state.getMap()) &&
            Objects.equals(getMySafeCombinations(), state.getMySafeCombinations()) &&
            Objects.equals(getCharacters(), state.getCharacters()) &&
            Objects.equals(getCatCoordinates(), state.getCatCoordinates()) &&
            Objects.equals(getJanitorCoordinates(), state.getJanitorCoordinates());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCurrentRound(), getMap(), getMySafeCombinations(), getCharacters(), getCatCoordinates(), getJanitorCoordinates());
    }

    /**
     * toString.
     */
    @Override
    public String toString() {
        return "State{" +
            "currentRound=" + currentRound +
            ", map=" + map +
            ", mySafeCombinations=" + mySafeCombinations +
            ", characters=" + characters +
            ", catCoordinates=" + catCoordinates +
            ", janitorCoordinates=" + janitorCoordinates +
            '}';
    }
}
