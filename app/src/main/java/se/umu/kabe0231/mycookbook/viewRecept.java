package se.umu.kabe0231.mycookbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

//Implement method to edit recipe, when creating intent, send int to determine if it is new recipe or
//edit recipe. This will determine what happens in onCreate Method and what the layout will be.
//This way no new class is needed.

public class viewRecept extends AppCompatActivity implements addEventFragment.addEventDialogListener {
    ArrayList<Recept> Recipes = new ArrayList<>();
    Map<String, String> ingredients = new TreeMap<>();
    Map<String, String> events = new TreeMap<>();
    Recept ThisRecept;
    String Instructions;
    TextView InstructionsText;
    TextView PortionText;
    LinearLayout left;
    LinearLayout right;
    TextView text;
    TextView text1;
    ImageView bild;
    Toolbar myToolbar;
    private static final String TAG = "view_Recipe";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private int picture;
    private String image;
    private String string;
    private Bitmap bitmap;
    private String name;
    private String port;
    private boolean deletePressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.wiew_recept);
        name = getIntent().getStringExtra("Receptvy");
        InstructionsText = (TextView) findViewById(R.id.InstructionsText);
        PortionText = (TextView) findViewById(R.id.PortionText);
        left = (LinearLayout) findViewById(R.id.LeftLayout);
        right = (LinearLayout) findViewById(R.id.RightLayout);
        bild = (ImageView) findViewById(R.id.MatBild);
        readPreferences();
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ThisRecept = getRecipe(name);
        updateView(ThisRecept);
        updateToolBar();

    }

    private void updateToolBar() {
        TextView Recipename = (TextView) findViewById(R.id.toolbarTitle);
        Recipename.setText(name);
        Recipename.setGravity(Gravity.CENTER_HORIZONTAL);
        Recipename.setGravity(Gravity.CENTER_VERTICAL);
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

        port = ThisRecept.getPortioner();
        int portValue = Integer.parseInt(port);
        if ( portValue > 1) {
            PortionText.setText(port + " portioner");
        } else {
            PortionText.setText(port + " portion");
        }

        //Update ImageView
        if (thisRecept.getImage() == null) {
            picture = thisRecept.getPicture();
            if (picture == 0) {
                bild.setBackgroundResource(R.drawable.logo);
            } else {
                bild.setBackgroundResource(picture);
            }
        } else {
            image = thisRecept.getImage();
            Uri imageUri = Uri.fromFile(new File(image));
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bild.setImageBitmap(bitmap);
        }

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

    /*public void onFinishEditDialog(String a) {

    }*/

    public void newPicture (View view) {
        //View picture
        //If time make possible to take new photograph
    }

    public void deleteRecept (View view) {
        if (deletePressed) {
            //Säkerhetsfråga, är du säker på att du vill ta bort receptet??
            Recipes.remove(ThisRecept);
            setPreferences();
            finish();
        } else {
            Toast toast = Toast.makeText( getApplicationContext(),
                    "När du har tagit bort ett recept går det inte att återskapa, " +
                            "för att ta bort receptet, klicka på TA BORT RECEPT igen.", Toast.LENGTH_LONG );
            toast.setGravity( Gravity.CENTER, 0, 0 );
            toast.show();
            deletePressed = true;
        }
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


         }
