package se.umu.kabe0231.mycookbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;


public class ScrollingActivity extends AppCompatActivity {
    ArrayList<Recept> Recipes = new ArrayList<>();
    private static final String TAG = "CookBook";
    LinearLayout linear;
    TextView text;
    View emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        linear = (LinearLayout) findViewById(R.id.LinearLayout);
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

        //implement adding a new recipe
        //set Listener to all TextViews
        //implement searchable function
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
        for (final String string: display) {
            text = new TextView(this);
            emptyView = new View(this);
            int dividerHeight = (int) (getResources().getDisplayMetrics().density * 1);
            emptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dividerHeight));
            emptyView.setBackgroundColor(Color.parseColor("#000000"));
            text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            text.setText(string);
            text.setTextSize(40);
            text.setGravity(Gravity.CENTER_HORIZONTAL);
            text.setPadding(2, 2, 2, 2);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GenerateRecipeView(string);
                }
            });
            linear.addView(text);
            linear.addView(emptyView);
        }
    }

    private void GenerateRecipeView(String string) {
        Intent intent = new Intent(this, viewRecept.class);
        intent.putExtra("Recept", string);
        startActivity(intent);
    }


    private void addDefault() {
        //add Soppa recipe if list is empty
        Recept Pannkakor = new Recept("Pannkakor");
        Pannkakor.addIngredient("Mjöl", "2,5 dl");
        Pannkakor.addIngredient("Mjölk", "6 dl");
        Pannkakor.addIngredient("Ägg", "3 stk");
        Pannkakor.setDescription("Blanda ihop och stek, servera med något gott!");
        Recipes.add(Pannkakor);

        //add Carbonara recipe if list is empty
        Recept Carbonara = new Recept("Carbonara");
        Carbonara.addIngredient("Pasta", "4 prt");
        Carbonara.addIngredient("Bacon", "150 gram");
        Carbonara.addIngredient("vispgrädde", "0,5 dl");
        Carbonara.addIngredient("ost", "Mycket");
        Carbonara.setDescription("Stek Bacon, koka pasta, i med grädde, på med ost. ");
        Recipes.add(Carbonara);
    }

    private void readPreferences() {
        SharedPreferences preferences;
        preferences = getSharedPreferences("CookBook" , Context.MODE_PRIVATE);

        try {
            Recipes = (ArrayList<Recept>) ObjectSerializer.deserialize(preferences.getString("CookBook",
                    ObjectSerializer.serialize(new ArrayList<Recept>())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (Recipes == null || Recipes.size() == 0) {
            addDefault();
        }

    }

    private void setPreferences () {
        SharedPreferences preferences = this.getSharedPreferences("CookBook", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        try {
            editor.putString("CookBook", ObjectSerializer.serialize(Recipes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();
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


