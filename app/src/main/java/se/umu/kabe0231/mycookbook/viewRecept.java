package se.umu.kabe0231.mycookbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
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

//Implement method to edit recipe, new class but same view as newRecipe!

public class viewRecept extends AppCompatActivity implements addEventFragment.addEventDialogListener {
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
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private int picture;
    private String string;

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
        TextView ToolbarText = (TextView) findViewById(R.id.toolbarText);
        ToolbarText.setText(name);
        ToolbarText.setTextSize(30);
        ToolbarText.setGravity(Gravity.CENTER_HORIZONTAL);
        ThisRecept = getRecipe(name);
        updateView(ThisRecept);

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
        int counter =1;
        events = ThisRecept.getEvents();
        a = checkEvents(a, events, counter);
        ThisRecept.addEvent(a, b);
        updateRecipes();

    }

    private void updateRecipes() {
        for (int i = 0; i < Recipes.size(); i++) {
            if (Recipes.get(i).getName().equals(ThisRecept.getName())) {
                Recipes.remove(i);
                Recipes.add(i, ThisRecept);
            }
        }
        setPreferences();
    }

    //if two or more comments are added on the same date, the date string is updated.
    private String checkEvents(String a, Map<String, String> events, int counter) {
        counter ++;
        if (counter == 2) {
            string = a;
        }
        if (!events.containsKey(a)) {
            return a;
        }
        a = string + "(" + String.valueOf(counter) + ")";
        a = checkEvents (a, events, counter);
        return a;
    }

    public void viewEvent (View view) {
        //////////////////////////////////////Skickar bara med senaste kommentaren. varför???????
        events = ThisRecept.getEvents();
        ArrayList<String> eventList = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        if (events == null || events.size() == 0) {
            Toast toast = Toast.makeText( getApplicationContext(),
                    "Det finns inga tidigare kommentarer för det här Receptet", Toast.LENGTH_LONG );
            toast.setGravity( Gravity.CENTER, 0, 0 );
            toast.show();
        } else {
            for (Map.Entry<String, String> entry : events.entrySet()) {
                String a = entry.getKey();
                String b = entry.getValue();
                dateList.add(a);
                eventList.add(b);
            }
            FragmentManager fm = getSupportFragmentManager();
            viewEventsFragment EventFragment = viewEventsFragment.newInstance("viewEventFragment");
            Bundle args = new Bundle();
            args.putStringArrayList("events", eventList);
            args.putStringArrayList("dates", dateList);
            EventFragment.setArguments(args);
            EventFragment.show(fm, "fragment_view__events");
        }
    }

    public void onFinishEditDialog(String a) {

    }

    public void newPicture (View view) {
        dispatchTakePictureIntent();
        updateRecipes();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            bild.setImageBitmap(imageBitmap);
        }
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
