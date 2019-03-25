package se.umu.kabe0231.mycookbook;


import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class that describes a Recipe
 */

public class Recept implements Serializable {
    private String name;
    private String portioner;
    private String description;
    private Map<String, String> Ingredients;
    private Map<String, String> Examples;
    private int picture;
    private String image;

    public Recept (String name) {
        Ingredients = new TreeMap<>();
        Examples = new TreeMap<>();
        this.name = name;
    }

    public Recept() {
        Ingredients = new TreeMap<>();
        Examples = new TreeMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String setName) {
        this.name = setName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription (String Description) {
        this.description = Description;
    }

    public String getPortioner() {
        return portioner;
    }

    public void setPortioner(String port) {
        this.portioner = port;
    }

    public Map<String, String> getIngredients() {
        return Ingredients;
    }

    public void addIngredient (String string, String value) {
        Ingredients.put(string, value);
    }

    public Map<String, String> getEvents() {
        return Examples;
    }

    public void addEvent (String date, String string) {
        Examples.put(date, string);
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture (int thispicture){
        this.picture = thispicture;
    }

    public void setImage(String string) {
        this.image = string;
    }

    public String getImage() {
        return image;
    }

}


