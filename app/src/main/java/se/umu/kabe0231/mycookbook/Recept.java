package se.umu.kabe0231.mycookbook;


import android.provider.ContactsContract;

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

    public Recept (String name) {
        Ingredients = new HashMap<>();
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

    public Map<String, String> getIngredients() {
        return Ingredients;
    }

    public void addIngredient (String string, String value) {
        Ingredients.put(string, value);
    }

    public void setDescription (String Description) {
        this.description = Description;
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
