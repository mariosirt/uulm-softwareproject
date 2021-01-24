package NetworkStandard.DataTypes.Szenario;

import NetworkStandard.DataTypes.Gadgets.Gadget;

import java.util.Objects;

/**
 * This class represents a Field on the board.
 *
 * @author Christian Wendlinger
 */
public class Field {
    private FieldStateEnum state;
    private Gadget gadget;
    private boolean isDestroyed;
    private boolean isInverted;
    private int chipAmount;
    private int safeIndex;
    private boolean isFoggy;
    private boolean isUpdated;

    /**
     * Constructor.
     *
     * @param state       - state of the field
     * @param gadget      - Gadget that may be on the field
     * @param isDestroyed - is the field destroyed?
     * @param isInverted  - is the this field inverted (roulette table)?
     * @param chipAmount  - how many chips are there (roulette table)?
     * @param safeIndex   - what number does the safe have (safe)?
     * @param isFoggy     - is there fog on the field?
     * @param isUpdated   - was this field updated?
     */
    public Field(FieldStateEnum state, Gadget gadget, boolean isDestroyed, boolean isInverted, int chipAmount, int safeIndex, boolean isFoggy, boolean isUpdated) {
        this.state = state;
        this.gadget = gadget;
        this.isDestroyed = isDestroyed;
        this.isInverted = isInverted;
        this.chipAmount = chipAmount;
        this.safeIndex = safeIndex;
        this.isFoggy = isFoggy;
        this.isUpdated = isUpdated;
    }

    /**
     * Getters and Setters.
     */

    public FieldStateEnum getState() {
        return state;
    }

    public void setState(FieldStateEnum state) {
        this.state = state;
    }

    public Gadget getGadget() {
        return gadget;
    }

    public void setGadget(Gadget gadget) {
        this.gadget = gadget;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }

    public boolean isInverted() {
        return isInverted;
    }

    public void setInverted(boolean inverted) {
        isInverted = inverted;
    }

    public int getChipAmount() {
        return chipAmount;
    }

    public void setChipAmount(int chipAmount) {
        this.chipAmount = chipAmount;
    }

    public int getSafeIndex() {
        return safeIndex;
    }

    public void setSafeIndex(int safeIndex) {
        this.safeIndex = safeIndex;
    }

    public boolean isFoggy() {
        return isFoggy;
    }

    public void setFoggy(boolean foggy) {
        isFoggy = foggy;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    /**
     * Equals and Hashcode.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return isDestroyed() == field.isDestroyed() &&
            isInverted() == field.isInverted() &&
            getChipAmount() == field.getChipAmount() &&
            getSafeIndex() == field.getSafeIndex() &&
            isFoggy() == field.isFoggy() &&
            isUpdated() == field.isUpdated() &&
            getState() == field.getState() &&
            Objects.equals(getGadget(), field.getGadget());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getState(), getGadget(), isDestroyed(), isInverted(), getChipAmount(), getSafeIndex(), isFoggy(), isUpdated());
    }

    /**
     * toString.
     */
    @Override
    public String toString() {
        return "Field{" +
            "state=" + state +
            ", gadget=" + gadget +
            ", isDestroyed=" + isDestroyed +
            ", isInverted=" + isInverted +
            ", chipAmount=" + chipAmount +
            ", safeIndex=" + safeIndex +
            ", isFoggy=" + isFoggy +
            ", isUpdated=" + isUpdated +
            '}';
    }
}
