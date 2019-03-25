package se.umu.kabe0231.mycookbook;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
* Scrolling Activity is the main Activity for the application
*/

public class ScrollingActivity extends AppCompatActivity implements searchFragment.searchDialogListener {
    ArrayList<Recept> Recipes = new ArrayList<>();
    ArrayList<Recept> searchRecipes = new ArrayList<>();
    ArrayList<String> searchResultList = new ArrayList<>();
    private static final String TAG = "Cook_Book";
    LinearLayout linear;
    TextView text;
    View emptyView;
    LinearLayout linearToolBar;
    Toolbar myToolbar;
    ImageButton searchButton;
    FloatingActionButton fab;
    private boolean searchPressed;
    private boolean ButtonSearch;
    private String searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.activity_scrolling);
        //update toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        linear = (LinearLayout) findViewById(R.id.LinearLayout);
        linearToolBar = (LinearLayout) findViewById(R.id.toolbar_item_container);
        readPreferences();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenerateNewRecipe();
            }
        });
        onCheckPerm();
    }

    //add search button to toolbar
    private void addSearchButton() {
        if (!ButtonSearch) {
            searchButton = new ImageButton(this);
            searchButton.setBackgroundResource(R.drawable.search);
            searchButton.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
            linearToolBar.setVerticalGravity(Gravity.CENTER_VERTICAL);
            linearToolBar.addView(searchButton);
            ButtonSearch = true;
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getSupportFragmentManager();
                    searchFragment searchingFragment = searchFragment.newInstance("searchFragment");
                    searchingFragment.show(fm, "fragment_search");
                }
            });
        }
    }

    //Returns the searched string from searchFragment
    @Override
    public void onFinishEditDialog(String a) {
        searchResultList.clear();
        searchRecipes.clear();
        searchResult = a;
        searchResultList = searchForResult(searchResult);

        //search gave no results
        if (searchResultList.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Ditt sök gav inga resultat", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            searchPressed = true;
            for (Recept r : Recipes) {
                for (int i = 0; i < searchResultList.size(); i++) {
                    if (r.getName().equalsIgnoreCase(searchResultList.get(i))) {
                        searchRecipes.add(r);
                    }
                }
            }

            //Update Scrollview to display searchresults
            linearToolBar.removeView(searchButton);
            TextView myToolbarText = (TextView) findViewById(R.id.toolbarTitle);
            myToolbarText.setText("SökResultat: " + searchResult);
            setScrollable(searchRecipes);
            fab.hide();
        }
    }

    //Search function, checks if any recipe name or ingredient equals the
    //searched term and if so returns the recipe.
    private ArrayList<String> searchForResult(String searchResult) {
        //Check if Recipe name equals the searched item
        for (Recept recept : Recipes) {
            if (recept.getName().replaceAll("\\s+","")
                    .equalsIgnoreCase(searchResult.replaceAll("\\s+",""))) {
                searchResultList.add(recept.getName());
            } else {
                Map<String, String> ingredientsMap = recept.getIngredients();
                for (Map.Entry<String, String> entry : ingredientsMap.entrySet()) {
                    if (entry.getKey().replaceAll("\\s+","").
                            equalsIgnoreCase(searchResult.replaceAll("\\s+",""))) {
                        searchResultList.add(recept.getName());
                    }
                }
            }
        }
        java.util.Collections.sort(searchResultList);
        return searchResultList;
    }

    //Launches the new recipe acticity
    private void GenerateNewRecipe() {
        Intent intent = new Intent(this, nyttRecept.class);
        startActivity(intent);
    }

    //update scrollview to represent either search results or all recipes
    public void setScrollable(ArrayList<Recept> RecipesList) {
        //Lägg till receptnamn och Sortera i alfabetisk ordning i ny lista.
        ArrayList<String> display = new ArrayList<>();
        for (Recept a : RecipesList) {
            String string = a.getName();
            display.add(string);
        }
        java.util.Collections.sort(display);

        //Lägg till en textView i scrollview för varje recept som finns.
        linear.removeAllViews();
        for (final String string : display) {
            text = new TextView(this);
            emptyView = new View(this);
            int dividerHeight = (int) (getResources().getDisplayMetrics().density * 1);
            emptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dividerHeight));
            emptyView.setBackgroundColor(Color.parseColor("#000000"));
            text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 160));
            text.setText(string);
            text.setTextSize(40);
            text.setGravity(Gravity.CENTER_HORIZONTAL);
            text.setPadding(20, 2, 20, 2);
            text.setAutoSizeTextTypeUniformWithConfiguration(15, 40, 1, TypedValue.COMPLEX_UNIT_DIP);
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

    //launches activity to view a recipe
    private void GenerateRecipeView(String string) {
        Intent intent = new Intent(this, viewRecept.class);
        intent.putExtra("Receptvy", string);
        intent.putExtra("search", searchPressed);
        startActivity(intent);
    }

    //If the app contains no recipes, two default recipes are added.
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
        Tacos.addEvent("2019-03-01", "Gott med Grillad Annanas");
        Tacos.addEvent("2019-02-15", "Gör Guacamolen på Avokado, Vitlök, lök, tomater, Lime och Koriander. Den " +
                "blir godast så");
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
                "liknar en färs. Fräs salsicciafärsen, tillsätt tomatpure, salt och peppar enligt önskemål. Tillsätt Rödvin och Krossade tomater, låt koka. " +
                "Servera med Parmesan Ost, Citron och Basilika.");
        SalsicciaPasta.setPortioner("4");
        SalsicciaPasta.setPicture(R.drawable.salsiccia);
        Recipes.add(SalsicciaPasta);
    }

    //Checks that the user has given permisson to use the camera, write and read files.
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    private void onCheckPerm() {
        int PERMISSION_ALL = 3;
        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
        };
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    //Retrieve Recipes saved in SharedPreferences
    private void readPreferences() {
        SharedPreferences preferences;
        preferences = getSharedPreferences("CookBook", Context.MODE_PRIVATE);
        try {
            Recipes = (ArrayList<Recept>) ObjectSerializer.deserialize(preferences.getString("Recept",
                    ObjectSerializer.serialize(new ArrayList<Recept>())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            searchRecipes = (ArrayList<Recept>) ObjectSerializer.deserialize(preferences.getString("searchRecept",
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

    //Update recipes saved in SharedPreferences.
    private void setPreferences() {
        SharedPreferences preferences = getSharedPreferences("CookBook", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        try {
            editor.putString("Recept", ObjectSerializer.serialize(Recipes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            editor.putString("searchRecept", ObjectSerializer.serialize(searchRecipes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onSaveInstanceState() called");
        savedInstanceState.putBoolean("searchPressed", searchPressed);
        savedInstanceState.putString("searchResult", searchResult);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "RestoreInstanceState() called");
        searchPressed = savedInstanceState.getBoolean("searchPressed");
        searchResult = savedInstanceState.getString("searchResult");
        super.onRestoreInstanceState(savedInstanceState);
    }

    //Handles the backbutton
    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "onSupportNavigateUp() called");
        if (searchPressed) {
            ButtonSearch = false;
            addSearchButton();
            TextView myToolbarText = (TextView) findViewById(R.id.toolbarTitle);
            myToolbarText.setText("Min Kokbok   ");
            setScrollable(Recipes);
            fab.show();
            searchPressed = false;
        } else {
            onBackPressed();
        }
        return true;
    }

    //sets the contents of scrollview depending on if it is a searchresult or not.
    @Override
    protected void onResume() {
        super.onResume();
        readPreferences();
        if (!searchPressed) {
            setScrollable(Recipes);
            fab.show();
            addSearchButton();
            TextView myToolbarText = (TextView) findViewById(R.id.toolbarTitle);
            myToolbarText.setText("Min Kokbok   ");
        } else if (searchPressed && searchRecipes.size() == 0) {
            setScrollable(Recipes);
            fab.show();
            ButtonSearch = false;
            addSearchButton();
            TextView myToolbarText = (TextView) findViewById(R.id.toolbarTitle);
            myToolbarText.setText("Min Kokbok   ");
            searchPressed = false;
        } else {
            linearToolBar.removeView(searchButton);
            TextView myToolbarText = (TextView) findViewById(R.id.toolbarTitle);
            myToolbarText.setText("SökResultat: " + searchResult);
            myToolbarText.setAutoSizeTextTypeUniformWithConfiguration(10, 30, 1, TypedValue.COMPLEX_UNIT_DIP);
            setScrollable(searchRecipes);
            fab.hide();
            ButtonSearch = false;
        }
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        setPreferences();
        Log.d(TAG, "onPause() called");
    }

}
