package se.umu.kabe0231.mycookbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;


public class ScrollingActivity extends AppCompatActivity {
    ArrayList<Recept> Recipes = new ArrayList<>();
    private static final String TAG = "CookBook";
    Recept nyttRecept = new Recept();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        readPreferences();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenerateNewRecipe();
            }
        });

        if (savedInstanceState != null) {

        }
        setScrollable(Recipes);
    }

    private void GenerateNewRecipe() {
        Intent intent = new Intent(this, nyttRecept.class);
        startActivity(intent);
    }

    public void setScrollable (ArrayList<Recept> Recipes) {
        ArrayList<String> display = new ArrayList<>();
        for (Recept a: Recipes) {
            String string = a.getName();
            display.add(string);
        }
        //Sortera i alfabetisk ordning i ny lista.
        java.util.Collections.sort(display);

        //Lägg till en textView i scrollview för varje recept som finns.

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

        if (Recipes == null) {
            Recipes.add(new Recept("Carbonara"));
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onSaveInstanceState() called");

        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "RestoreInstanceState() called");
        // Restore state members from saved instance

        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        readPreferences();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        readPreferences();
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
        setPreferences();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setPreferences();
        Log.d(TAG, "onDestroy() called");
    }


}


