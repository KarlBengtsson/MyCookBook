package se.umu.kabe0231.mycookbook;


import java.util.HashMap;
import java.util.Map;


public class Recept {
    private String name;
    private Map<String, String> Ingredients;


    public Recept () {
        Ingredients = new HashMap<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getIngredients() {
        return Ingredients;
    }

    private void addIngredient (String name, String amount) {
        Ingredients.put(name, amount);
    }

    private void setName (String name) {
        this.name = name;
    }


}
