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
    private static final String TAG = "Cook_Book";
    LinearLayout linear;
    TextView text;
    View emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
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
        linear.removeAllViews();
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
        intent.putExtra("Receptvy", string);
        startActivity(intent);
    }


    private void addDefault() {
        //add Tacos recipe if list is empty
        Recept Tacos = new Recept("Tacos");
        Tacos.addIngredient("Köttfärs", "800 gram");
        Tacos.addIngredient("TacoKrydda", "2 påsar");
        Tacos.addIngredient("Tacobröd", "8 stk");
        Tacos.addIngredient("Lök", "1 stk.");
        Tacos.addIngredient("Lime", "Till Servering");
        Tacos.addIngredient("Koriander", "Till Servering");
        Tacos.addIngredient("Guacamole", "Mycket");
        Tacos.addIngredient("Tillbehör", "Bestäm själv");
        Tacos.setDescription("Stek Färsen med Tacokryddan, värm bröden, servera bara med goda tillbehör. Som du ser är Guacamole och lök " +
                "ett måste. Resten bestämmer du själv. Servera med Lime. ");
        Tacos.setPortioner("4");
        Tacos.setPicture(R.drawable.tacos);
        Recipes.add(Tacos);

        //add Salsicciapasta recipe if list is empty
        Recept SalsicciaPasta = new Recept("Salsiccia Pasta");
        SalsicciaPasta.addIngredient("Färsk Pasta", "4 prt");
        SalsicciaPasta.addIngredient("Salsicciakorv", "8 stk.");
        SalsicciaPasta.addIngredient("Krossade Tomater", "2 burkar");
        SalsicciaPasta.addIngredient("Gullök", "2 stk.");
        SalsicciaPasta.addIngredient("Vitlöksklyftor", "6 stk.");
        SalsicciaPasta.addIngredient("Röd Chili", "2 stk.");
        SalsicciaPasta.addIngredient("Olja", "Till Stekning");
        SalsicciaPasta.addIngredient("Rödvin", "En skvätt");
        SalsicciaPasta.addIngredient("Salt", "Bestäm själv");
        SalsicciaPasta.addIngredient("Peppar", "Bestäm Själv");
        SalsicciaPasta.addIngredient("Tomatpuré", "Bestäm själv");
        SalsicciaPasta.addIngredient("Citron", "Till Servering");
        SalsicciaPasta.addIngredient("Basilika", "Till Servering");
        SalsicciaPasta.addIngredient("ParmesanOst", "Till Servering");
        SalsicciaPasta.setDescription("Fräs lök, vitlök och chili i oljan. Skala Salsicciakorven och mosa ner den till någonnting som " +
                "liknar en färs. Fräs salsicciafärsen, tillsätt salt och peppar enligt önskemål. Tillsätt Rödvin och Krossade tomater, låt koka. " +
                "Tillsätt Tomatpuré. Servera med Parmesan Ost, Citron och Basilika.");
        SalsicciaPasta.setPortioner("4");
        SalsicciaPasta.setPicture(R.drawable.salsiccia);
        Recipes.add(SalsicciaPasta);
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

        if (Recipes == null || Recipes.size() == 0) {
            addDefault();
            //setPreferences();
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
        //readPreferences();
        super.onRestoreInstanceState(savedInstanceState);

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
        setScrollable(Recipes);
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


