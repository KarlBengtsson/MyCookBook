package se.umu.kabe0231.mycookbook;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class Recept implements Serializable {
    private String name;
    private String portioner;
    private String description;
    private Map<String, String> Ingredients;
    private Map<Date, String> Examples;
    private int picture;

    public Recept (String name) {
        Ingredients = new TreeMap<>();
        Examples = new TreeMap<>();
        this.name = name;
        //photograph
        //this.description = description;
        //this.portioner = portioner;
    }

    public Recept() {
        Ingredients = new HashMap<>();
        Examples = new HashMap<>();
        //this.name = name;
        //this.picture = picture;
        //this.description = description;
        //this.portioner = portioner;
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

    public Map<Date, String> getExamples() {
        return Examples;
    }

    public void addExample (Date date, String string) {
        Examples.put(date, string);
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture (int thispicture){
        this.picture = thispicture;
    }

    //get Photograph

    //add Photograph


}


