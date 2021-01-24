package NetworkStandard.DataTypes.Gadgets;

import NetworkStandard.DataTypes.GadgetEnum;
/**
 * @author Marios Sirtmatsis
 */
public class Cocktail extends Gadget {
    private Boolean isPoisoned;     //true, if the cocktail is poisoned and false if not
    private double cocktailDodgeProbability;
    private int cocktailHp;

    public Cocktail(GadgetEnum gadget) {
        super(gadget);
        this.isPoisoned = false;
    }


    public Boolean getPoisoned() {
        return isPoisoned;
    }

    public void setPoisoned(Boolean poisoned) {
        isPoisoned = poisoned;
    }

    public double getCocktailDodgeProbability() {
        return cocktailDodgeProbability;
    }

    public void setCocktailDodgeProbability(double cocktailDodgeProbability) {
        this.cocktailDodgeProbability = cocktailDodgeProbability;
    }

    public int getCocktailHp() {
        return cocktailHp;
    }

    public void setCocktailHp(int cocktailHp) {
        this.cocktailHp = cocktailHp;
    }
}
