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
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.activity_scrolling);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        linear = (LinearLayout) findViewById(R.id.LinearLayout);
        linearToolBar = (LinearLayout) findViewById(R.id.toolbar_item_container);
        readPreferences();
        if (!searchPressed) {
            addSearchButton();
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenerateNewRecipe();
            }
        });
        setScrollable(Recipes);
        onCheckPerm();
    }

    private void addSearchButton() {
        searchButton = new ImageButton(this);
        searchButton.setBackgroundResource(R.drawable.search);
        searchButton.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        linearToolBar.setVerticalGravity(Gravity.CENTER_VERTICAL);
        linearToolBar.addView(searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                searchFragment searchingFragment = searchFragment.newInstance("searchFragment");
                searchingFragment.show(fm, "fragment_search");
            }
        });
    }

    @Override
    public void onFinishEditDialog(String a) {
        searchPressed = true;
        searchResultList.clear();
        searchRecipes.clear();
        String searchResult = a;
        searchResultList = searchForResult(searchResult);
        if (searchResultList.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Ditt sök gav inga resultat", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            for (Recept r : Recipes) {
                for (int i = 0; i < searchResultList.size(); i++) {
                    if (r.getName().equalsIgnoreCase(searchResultList.get(i))) {
                        searchRecipes.add(r);
                    }
                }
            }
            linearToolBar.removeView(searchButton);
            TextView myToolbarText = (TextView) findViewById(R.id.toolbarTitle);
            myToolbarText.setText("SökResultat");
            setScrollable(searchRecipes);
            fab.hide();
        }
    }

    private ArrayList<String> searchForResult(String searchResult) {
        //Check if Recipe name equals the searched item
        for (Recept recept : Recipes) {
            if (recept.getName().equalsIgnoreCase(searchResult)) {
                searchResultList.add(recept.getName());
            } else {
                Map<String, String> ingredientsMap = recept.getIngredients();
                for (Map.Entry<String, String> entry : ingredientsMap.entrySet()) {
                    if (entry.getKey().equalsIgnoreCase(searchResult)) {
                        searchResultList.add(recept.getName());
                    }
                }
            }
        }
        java.util.Collections.sort(searchResultList);
        return searchResultList;
    }

    private void GenerateNewRecipe() {
        Intent intent = new Intent(this, nyttRecept.class);
        startActivity(intent);
    }

    public void setScrollable(ArrayList<Recept> Recipes) {
        ArrayList<String> display = new ArrayList<>();
        for (Recept a : Recipes) {
            String string = a.getName();
            display.add(string);
        }
        //Sortera i alfabetisk ordning i ny lista.
        java.util.Collections.sort(display);

        //Lägg till en textView i scrollview för varje recept som finns.
        linear.removeAllViews();
        for (final String string : display) {
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
                "liknar en färs. Fräs salsicciafärsen, tillsätt salt och peppar enligt önskemål. Tillsätt Rödvin och Krossade tomater, låt koka. " +
                "Tillsätt Tomatpuré. Servera med Parmesan Ost, Citron och Basilika.");
        SalsicciaPasta.setPortioner("4");
        SalsicciaPasta.setPicture(R.drawable.salsiccia);
        Recipes.add(SalsicciaPasta);
    }

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
        // The request code used in ActivityCompat.requestPermissions()
        // and returned in the Activity's onRequestPermissionsResult()
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
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "RestoreInstanceState() called");
        searchPressed = savedInstanceState.getBoolean("searchPressed");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (searchPressed) {
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

    @Override
    protected void onResume() {
        super.onResume();
        readPreferences();
        if (!searchPressed) {
            setScrollable(Recipes);
            fab.show();
        } else {
            linearToolBar.removeView(searchButton);
            TextView myToolbarText = (TextView) findViewById(R.id.toolbarTitle);
            myToolbarText.setText("SökResultat");
            setScrollable(searchRecipes);
        }
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        setPreferences();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() called");
    }

}
