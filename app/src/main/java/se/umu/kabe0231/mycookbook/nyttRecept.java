package se.umu.kabe0231.mycookbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;

public class nyttRecept extends AppCompatActivity {
    ArrayList<Recept> Recipes = new ArrayList<>();
    Recept NyttRecept = new Recept();
    Button GenerateRecept;
    EditText setName;
    EditText Instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //When finished button is pressed, add new recipe to ArrayList<Recept> and setPreferences()
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nytt_recept);
        readPreferences();
        setName = (EditText) findViewById(R.id.editNameText);
        Instructions = (EditText) findViewById(R.id.InstructionsText);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewPhoto();
            }
        });
    }

    private void AddNewPhoto() {

    }

    public void AddNewIngredient(View view) {
        //Add Linear layout (horizontal) in vertical linear layout that contains 2 textviews (name and amount) and one spinner (dl, msk, tsk, prt mm)
        //Button to edit ingredient??
        //Button to delete ingredient
        //Add Ingredients to Recept
    }

    public void GenerateRecept(View view) {
        //Säkerhetsfråga, är du säker på att du är färdig med receptet??
        NyttRecept.setName(setName.getText().toString());
        NyttRecept.setDescription(Instructions.getText().toString());
    finish();
    }

    private void readPreferences() {
        SharedPreferences preferences;
        preferences = getSharedPreferences("CookBook" , Context.MODE_PRIVATE);

        try {
            Recipes = (ArrayList<Recept>) ObjectSerializer.deserialize(preferences.getString("Recept",
                    ObjectSerializer.serialize(new ArrayList<Recept>())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void setPreferences () {
        SharedPreferences preferences = getSharedPreferences("CookBook", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        try {
            editor.putString("Recept", ObjectSerializer.serialize(Recipes));
        } catch (IOException e) {
            e.printStackTrace();
        }

        editor.commit();
    }

}
