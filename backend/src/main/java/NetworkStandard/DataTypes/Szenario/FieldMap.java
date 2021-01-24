package NetworkStandard.DataTypes.Szenario;

import java.util.Arrays;

/**
 * This class represents the board.
 *
 * @author Christian Wendlinger
 */
public class FieldMap {
    private Field[][] map;

    /**
     * Constructor.
     *
     * @param map - 2D Array of all the Fields on the board.
     */
    public FieldMap(Field[][] map) {
        this.map = map;
    }

    /**
     * Getter and Setter.
     */
    public Field[][] getMap() {
        return map;
    }

    public void setMap(Field[][] map) {
        this.map = map;
    }

    /**
     * Equals and Hashcode.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldMap fieldMap = (FieldMap) o;
        return Arrays.equals(getMap(), fieldMap.getMap());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getMap());
    }

    /**
     * toString.
     */
    @Override
    public String toString() {
        return "FieldMap{" +
            "map=" + Arrays.toString(map) +
            '}';
    }

    /**
     * Getter and Setter for an individual Field.
     */
    public Field getField(int x, int y) {
        return map[x][y];
    }

    public void setField(int x, int y, Field updatedField) {
        map[x][y] = updatedField;
    }

}
