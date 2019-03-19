package se.umu.kabe0231.mycookbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class viewRecept extends AppCompatActivity implements addEventFragment.addEventDialogListener{
    ArrayList<Recept> Recipes = new ArrayList<>();
    Map<String, String> ingredients = new TreeMap<>();
    Map<String, String> events = new TreeMap<>();
    Recept ThisRecept;
    String Instructions;
    String Portioner;
    TextView InstructionsText;
    TextView PortionText;
    LinearLayout left;
    LinearLayout right;
    TextView text;
    TextView text1;
    ImageView bild;
    private static final String TAG = "view_Recipe";
    private int picture;

    //Lägg till spinner som anger hur många portioner receptet skall visas för.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.wiew_recept);
        String name = getIntent().getStringExtra("Receptvy");
        InstructionsText = (TextView) findViewById(R.id.InstructionsText);
        PortionText = (TextView) findViewById(R.id.PortionText);
        left = (LinearLayout) findViewById(R.id.LeftLayout);
        right = (LinearLayout) findViewById(R.id.RightLayout);
        bild = (ImageView) findViewById(R.id.MatBild);
        readPreferences();
        /////////////////Börja här!! Funkar det att hämta receptet så som du gör???///////////////
        /////////////////Fixa wiew_layout så att det funkar
        /////////////////Uppdatera vyn med updateView() /////////////////////////////
        TextView ToolbarText = (TextView) findViewById(R.id.toolbarText);
        ToolbarText.setText(name);
        ToolbarText.setTextSize(30);
        ToolbarText.setGravity(Gravity.CENTER_HORIZONTAL);
        ThisRecept = getRecipe(name);
        updateView(ThisRecept);

        //Skickas med namn på receptet (titel på textview) från scrollview i ScrollingActivity
        //Pannkakor skickas med oavsett vilken textview man klickar på
        //Använd namn för att hämta receptet från ArrayList<Recept> och visa receptet i vyn.
        //implement camera function
        //Antal portioner, standardinmatning är 4 portioner.
    }

    private Recept getRecipe(String name) {
        // Hämta Recept från ArrayList(lagras i sharedPreferences) med name
        for (Recept r: Recipes) {
            if (r.getName().equals(name)){
                return r;
            }
        }
        return null;
    }

    //Update view based on descriptions, picture and ingredients of ThisRecept
    private void updateView(Recept thisRecept) {
        //Update Instructions TextView
        Instructions = thisRecept.getDescription();
        InstructionsText.setText(Instructions);

        //Update ImageView
        picture = thisRecept.getPicture();
        bild.setBackgroundResource(picture);

        //Update portioner TextView
        Portioner = thisRecept.getPortioner();
        PortionText.setText(Portioner);

        //Update Ingredients View
        ingredients = thisRecept.getIngredients();
        if (ingredients != null) {
            for (Map.Entry<String, String> entry : ingredients.entrySet()) {
                String a = entry.getKey();
                String b = entry.getValue();
                text = new TextView(this);
                text1 = new TextView(this);
                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                text1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                text.setText(a + ":  ");
                text.setAutoSizeTextTypeUniformWithConfiguration(1, 20, 1, TypedValue.COMPLEX_UNIT_DIP);
                text.setGravity(Gravity.RIGHT);
                text1.setText(b);
                text1.setAutoSizeTextTypeUniformWithConfiguration(1, 20, 1, TypedValue.COMPLEX_UNIT_DIP);
                left.addView(text);
                right.addView(text1);
            }
        }
    }


    public void addEvent (View view) {
        FragmentManager fm = getSupportFragmentManager();
        addEventFragment EventFragment = addEventFragment.newInstance("EventFragment");
        EventFragment.show(fm, "fragment_add__event");
    }

    @Override
    public void onFinishEditDialog(String a, String b) {
        ThisRecept.addEvent(a, b);
        for (Recept r: Recipes) {
            if (r.getName().equals(ThisRecept.getName())) {
                Recipes.remove(r);
                Recipes.add(ThisRecept);
            }
        }
        setPreferences();
    }

    public void viewEvent (View view) {
        events = ThisRecept.getEvents();
        ArrayList<String> eventList = new ArrayList<>();
        if (events == null || events.size() == 0) {
            Toast toast = Toast.makeText( getApplicationContext(),
                    "Det finns inga tidigare kommentarer för det här Receptet", Toast.LENGTH_LONG );
            toast.setGravity( Gravity.CENTER, 0, 0 );
            toast.show();
        } else {
            for (Map.Entry<String, String> entry : events.entrySet()) {
                String a = entry.getKey();
                String b = entry.getValue();
                eventList.add("(" + a + ") --> " + b);
            }
        }
    }

    public void newPicture (View view) {
        //Ta ny bild på maträtten
    }

    public void deleteRecept (View view) {
        //Säkerhetsfråga, är du säker på att du vill ta bort receptet??
        Recipes.remove(ThisRecept);
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
        SharedPreferences preferences = this.getSharedPreferences("CookBook", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        try {
            editor.putString("Recept", ObjectSerializer.serialize(Recipes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();
    }


}
