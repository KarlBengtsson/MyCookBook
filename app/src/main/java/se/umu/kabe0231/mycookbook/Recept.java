package se.umu.kabe0231.mycookbook;


import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Recept {
    private String name;
    private String description;
    private Set<Ingredient> Ingredients;
    private List<RecipeExample> Examples;

    public Recept () {

    }


    public Recept (String name) {
        Ingredients = new HashSet<>();
        Examples = new ArrayList<>();
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<Ingredient> getIngredients() {
        return Ingredients;
    }

    private void addIngredient (Ingredient ing) {
        Ingredients.add(ing);
    }

    private void setName (String name) {
        this.name = name;
    }

    private void setDescription (String Description) {
        this.description = Description;
    }

    private class Ingredient {
        private String ingredientName;
        private String value;

        private Ingredient (String ingredientName, String value) {
            this.ingredientName = ingredientName;
            this.value = value;
        }

    }

    private class RecipeExample {
        private String example;
        private Date date;
        private ContactsContract.CommonDataKinds.Photo photo;

        private RecipeExample () {
            this.example = example;
            this.date = date;
            this.photo = photo;
        }
    }




}
