package se.umu.kabe0231.mycookbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class viewRecept extends AppCompatActivity {
    ArrayList<Recept> Recipes = new ArrayList<>();
    Recept ThisRecept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recept);
        String name = getIntent().getStringExtra("Recept");
        readPreferences();
        getRecipe(name);
        TextView ToolbarText = (TextView) findViewById(R.id.toolbarText);
        ToolbarText.setText("  " + name);
        ToolbarText.setTextSize(36);
        ToolbarText.setGravity(Gravity.CENTER_HORIZONTAL);

        //Skickas med namn på receptet (titel på textview) från scrollview i ScrollingActivity
        //Pannkakor skickas med oavsett vilken textview man klickar på
        //Använd namn för att hämta receptet från ArrayList<Recept> och visa receptet i vyn.
        //implement camera function
        //Antal portioner, standardinmatning är 4 portioner.
    }

    private void getRecipe(String name) {
        // Hämta Recept från ArrayList med name
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

}
