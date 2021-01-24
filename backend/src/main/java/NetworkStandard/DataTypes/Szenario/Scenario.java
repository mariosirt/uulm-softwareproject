package NetworkStandard.DataTypes.Szenario;

import java.util.Arrays;

/**
 * This class represents a scenario that can be configured pregame.
 *
 * @author Christian Wendlinger
 */

public class Scenario {
    private FieldStateEnum[][] scenario;

    /**
     * Constructor.
     *
     * @param scenario - map containing all the states of the fields.
     */
    public Scenario(FieldStateEnum[][] scenario) {
        this.scenario = scenario;
    }

    /**
     * Getter and Setter.
     */
    public FieldStateEnum[][] getScenario() {
        return scenario;
    }

    public void setScenario(FieldStateEnum[][] scenario) {
        this.scenario = scenario;
    }

    /**
     * Equals and Hashcode.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scenario scenario1 = (Scenario) o;
        return Arrays.equals(getScenario(), scenario1.getScenario());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getScenario());
    }

    /**
     * toString.
     */
    @Override
    public String toString() {
        String scenarioString = "";

        for (int i = 0; i < scenario.length; i++) {
            for (int j = 0; j < scenario[i].length; j++) {
                scenarioString += scenario[i][j];
                if (j < scenario[i].length) {
                    scenarioString += ", ";
                }
            }
            scenarioString += "\n";
        }

        return "Scenario{" + scenarioString +
            '}';
    }


    /**
     * Getter and Setter for individual FieldState.
     */
    public FieldStateEnum getFieldState(int x, int y) {
        return scenario[x][y];
    }

    public void setFieldState(int x, int y, FieldStateEnum newState) {
        scenario[x][y] = newState;
    }
}
