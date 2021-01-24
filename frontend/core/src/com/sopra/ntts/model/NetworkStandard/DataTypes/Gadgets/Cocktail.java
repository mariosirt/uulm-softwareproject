package com.sopra.ntts.model.NetworkStandard.DataTypes.Gadgets;


import com.sopra.ntts.model.NetworkStandard.DataTypes.GadgetEnum;

public class Cocktail extends Gadget {
    private Boolean isPoisoned;     //true, if the cocktail is poisoned and false if not
    private double cocktailDodgeProbability;
    private int cocktailHp;

    public Cocktail(GadgetEnum gadget, Integer usages) {
        super(gadget);
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
