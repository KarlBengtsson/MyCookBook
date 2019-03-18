package se.umu.kabe0231.mycookbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.IOException;
import java.util.ArrayList;

public class nyttRecept extends AppCompatActivity {
    ArrayList<Recept> Recipes = new ArrayList<>();
    Recept NyttRecept = new Recept();
    Button GenerateRecept;
    EditText setName;
    EditText setPort;
    EditText Instructions;
    EditText Ingredient;
    EditText Amount;
    EditText Unit;
    LinearLayout IngredientsView1;
    LinearLayout IngredientsView2;
    LinearLayout IngredientsView3;
    private ScrollView ScrollView1;
    int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //When finished button is pressed, add new recipe to ArrayList<Recept> and call setPreferences()
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nytt_recept);
        readPreferences();
        setName = (EditText) findViewById(R.id.editNameText);
        setPort = (EditText) findViewById(R.id.PortionText);
        Instructions = (EditText) findViewById(R.id.InstructionsText);
        GenerateRecept = (Button) findViewById(R.id.GenerateRecept);
        IngredientsView1 = (LinearLayout) findViewById(R.id.IngredientsView1);
        IngredientsView2 = (LinearLayout) findViewById(R.id.IngredientsView2);
        IngredientsView3 = (LinearLayout) findViewById(R.id.IngredientsView3);
        ScrollView1 = (ScrollView) findViewById(R.id.scrollView3);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewPhoto();
            }
        });
    }

    private void AddNewPhoto() {
        //Take picture of recept, implement this method
    }

    public void AddNewIngredient(View view) {
        //Button to edit ingredient??
        //Button to delete ingredient
        //Add Ingredients to Recept
        counter++;
        if (counter > 1) {
            addIngredientToRecepie();
        }
        //////////////////////////////////////////////////////////////////////////////////////////
        //Add ingredients using Dialog fragment.
        //////////////////////////////////////////////////////////////////////////////////////////
        Ingredient = new EditText(this);
        Ingredient.setHint("Ingrediens");
        Ingredient.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        IngredientsView1.addView(Ingredient);
        Amount = new EditText(this);
        Amount.setHint("Mängd");
        Amount.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        IngredientsView2.addView(Amount);
        Unit = new EditText(this);
        Unit.setHint("Måttenhet");
        Unit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        IngredientsView3.addView(Unit);
    }


    private void addIngredientToRecepie() {
        String a = Ingredient.getText().toString();
        String b = Amount.getText().toString();
        String c = Unit.getText().toString();
        String d = b + " " + c;
        NyttRecept.addIngredient(a, d);
    }

    public void GenerateRecept(View view) {
        //Säkerhetsfråga, är du säker på att du är färdig med receptet??
        addIngredientToRecepie();
        NyttRecept.setName(setName.getText().toString());
        NyttRecept.setDescription(Instructions.getText().toString());
        NyttRecept.setPortioner(setPort.getText().toString());
        Recipes.add(NyttRecept);
        setPreferences();
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
