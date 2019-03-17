package se.umu.kabe0231.mycookbook;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Recept {
    private String name;
    private String description;
    private Map<String, String> Ingredients;
    private List<RecipeExample> Examples;
    //photograph

    public Recept (String name) {
        Ingredients = new HashMap<>();
        Examples = new ArrayList<>();
        this.name = name;
        this.description = description;
        //photograph
    }

    public Recept() {

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getIngredients() {
        return Ingredients;
    }

    public void setName(String setName) {
        this.name = setName;
    }

    public void addIngredient (String string, String value) {
        Ingredients.put(string, value);
    }

    public void setDescription (String Description) {
        this.description = Description;
    }

    public void addExample () {

    }


    private class RecipeExample {
        private String example;
        private String details;
        private Date date;


        private RecipeExample () {
            this.example = example;
            this.date = date;
            this.details = details;

        }

        //Methods to set name(example), description(details) and date.
    }




}
