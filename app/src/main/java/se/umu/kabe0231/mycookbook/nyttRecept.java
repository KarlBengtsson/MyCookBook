package se.umu.kabe0231.mycookbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class nyttRecept extends AppCompatActivity implements AddIngredientFragment.AddIngredientDialogListener {
    ArrayList<Recept> Recipes = new ArrayList<>();
    Recept NyttRecept = new Recept();
    Button GenerateRecept;
    EditText setName;
    EditText setPort;
    EditText Instructions;
    TextView Ingredient1;
    TextView Amount1;
    LinearLayout IngredientsView1;
    LinearLayout IngredientsView2;
    private static final String TAG = "New_Recipe";

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

    //Lägg till ingredienser till vyn
    public void updateView (String a, String b) {
        //Ingrediens
        Ingredient1 = new TextView(this);
        Ingredient1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Ingredient1.setText(a + ": ");
        Ingredient1.setGravity(Gravity.RIGHT);
        Ingredient1.setAutoSizeTextTypeUniformWithConfiguration(1, 20, 1, TypedValue.COMPLEX_UNIT_DIP);
        IngredientsView1.addView(Ingredient1);

        //Mängd
        Amount1 = new TextView(this);
        Amount1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Amount1.setText(b);
        Amount1.setAutoSizeTextTypeUniformWithConfiguration(1, 20, 1, TypedValue.COMPLEX_UNIT_DIP);
        IngredientsView2.addView(Amount1);
    }

    public void showDialogFragment(View view) {
        FragmentManager fm = getSupportFragmentManager();
        AddIngredientFragment IngredientFragment = AddIngredientFragment.newInstance("ReceptFragment");
        IngredientFragment.show(fm, "fragment_add__ingredient");
    }

    @Override
    public void onFinishEditDialog(String a, String b) {
        NyttRecept.addIngredient(a, b);
        updateView(a, b);
    }

    public void GenerateRecept(View view) {
        //Säkerhetsfråga, är du säker på att du är färdig med receptet??
        //Kod

        //Uppdatera resten
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

    @Override
    protected void onStart() {
        super.onStart();
        // readPreferences();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //readPreferences();
        Log.d(TAG, "onRestart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        readPreferences();
        Log.d(TAG, "onResume() called");

    }

    @Override
    protected void onPause() {
        super.onPause();
        setPreferences();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //setPreferences();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //setPreferences();
        Log.d(TAG, "onDestroy() called");
    }

}
