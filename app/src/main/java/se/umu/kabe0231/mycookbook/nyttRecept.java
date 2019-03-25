package se.umu.kabe0231.mycookbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
* Class to create a new Recipe, when the recipe is complete,
 * it is added to ArrayList<Recept> Recipes which is saved in
 * sharedPreferences.
*/

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
    private String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PIC_CROP = 2;
    private File photoFile;
    private File cropPhotoFile;
    private Bitmap bitmap;
    private Uri imageUri;
    private Uri cropImageUri;
    private Toolbar myToolbar;
    private int counter = 0;
    private boolean picTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nytt_recept);
        readPreferences();
        //Edit text that sets the name of the recipe
        setName = (EditText) findViewById(R.id.editNameText);
        setName.requestFocus();
        //Edit text that sets the number of portions
        setPort = (EditText) findViewById(R.id.PortionText);
        //Edit text that sets the instructions
        Instructions = (EditText) findViewById(R.id.InstructionsText);
        //Button to finish the recipe
        GenerateRecept = (Button) findViewById(R.id.GenerateRecept);
        //Two linearlayouts to display the added ingredients.
        IngredientsView1 = (LinearLayout) findViewById(R.id.IngredientsView1);
        IngredientsView2 = (LinearLayout) findViewById(R.id.IngredientsView2);
        //enable backbutton on toolbar and update toolbar title to "nytt recept"
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView myToolbarText = (TextView) findViewById(R.id.toolbarTitle);
        myToolbarText.setText("Nytt Recept");

        //StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        //StrictMode.setVmPolicy(builder.build());

        //Button to add photograph to recipe
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setName.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText( getApplicationContext(),
                            "Skriv in ett namn på receptet innan ni tar en bild", Toast.LENGTH_LONG );
                    toast.setGravity( Gravity.CENTER, 0, 0 );
                    toast.show();
                } else {
                    dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE);
                }
            }
        });
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

    //Opens dialog Fragment to add ingredient to recipe
    public void showDialogFragment(View view) {
        FragmentManager fm = getSupportFragmentManager();
        AddIngredientFragment IngredientFragment = AddIngredientFragment.newInstance("ReceptFragment");
        IngredientFragment.show(fm, "fragment_add__ingredient");
    }

    //Returns the ingredient from dialogFragment
    @Override
    public void onFinishEditDialog(String a, String b) {
        //add ingredient to recipe
        NyttRecept.addIngredient(a, b);
        //add new ingredient to view
        updateView(a, b);
    }

    //Save the recipe to ArrayList<Recept> Recipes
    public void GenerateRecept(View view) {
        //Creates an ArrayList of all recipe names to amke sure the name doesnt already exist
        String newName = setName.getText().toString().toLowerCase().replaceAll("\\s+","");
        ArrayList<String> nameList = new ArrayList<>();
        for (Recept recept: Recipes) {
            nameList.add(recept.getName().toLowerCase().replaceAll("\\s+",""));
        }

        //Change recipe name if recipe name already exists
        if (nameList.contains(newName)) {
            Toast toast = Toast.makeText( getApplicationContext(),
                    "Receptet finns redan, vänligen välj ett annat namn.", Toast.LENGTH_LONG );
            toast.setGravity( Gravity.CENTER, 0, 0 );
            toast.show();

        //Double check to see that the user has given name, portions and description to recipe
        } else if (setName.getText().toString().isEmpty() || setPort.getText().toString().isEmpty()
                    || Instructions.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText( getApplicationContext(),
                    "Du måste ange ett namn, antal portioner samt instruktioner " +
                            "för att spara receptet", Toast.LENGTH_LONG );
            toast.setGravity( Gravity.CENTER, 0, 0 );
            toast.show();

        //Double check if a picture is added
        } else if (!picTaken) {
            Toast toast = Toast.makeText( getApplicationContext(),
                    "Du har inte tagit någon bild för detta recept, " +
                            "klicka på kamera knappen för att ta en bild. Klicka på " +
                            "Spara recept för att spara utan bild.", Toast.LENGTH_LONG );
            toast.setGravity( Gravity.CENTER, 0, 0 );
            toast.show();
            picTaken = true;
        }

        //add recipe to saved REcipes
        else {
                NyttRecept.setName(setName.getText().toString());
                NyttRecept.setDescription(Instructions.getText().toString());
                NyttRecept.setPortioner(setPort.getText().toString());
                Recipes.add(NyttRecept);
                setPreferences();
                finish();
            }

    }

    ////////////////Camera functions////////////////
    //Initiate Camera
    private void dispatchTakePictureIntent(int actionCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile(".original_");
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                imageUri = Uri.fromFile(photoFile);
                imageUri = FileProvider.getUriForFile(nyttRecept.this,
                        "com.kb.fileprovider",
                        photoFile);
                takePictureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, actionCode);

            }
        }
    }

    private File createImageFile(String string) throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String name = setName.getText().toString();
        String imageFileName = name + string + ".jpg" ;
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
        //File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        File image = new File(storageDir + imageFileName);
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //crop photo to match imageview size in recept view layout.
            performCrop();
        } else if (requestCode == PIC_CROP) {
            //returns cropped image.
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), cropImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Retrieve file of new cropped bitmap and save path to Recipe.
            NyttRecept.setImage(cropPhotoFile.toString());
            picTaken = true;
        }
    }

    //Crop taken photo
    private void performCrop() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        // Create the File where the photo should go
        cropPhotoFile = null;
        try {
            cropPhotoFile = createImageFile(".crop");
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (cropPhotoFile != null) {
            cropImageUri = Uri.fromFile(cropPhotoFile);
            intent.setDataAndType(imageUri, "image/*");
            intent.putExtra("crop", "true");
            //set X and Y constraints to crop image
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 480);
            intent.putExtra("outputY", 480);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);
            intent.putExtra("return-data", true);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, PIC_CROP);
        }

    }

    //Retrieve Recipes saved in SharedPreferences
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

    //Update recipes saved in SharedPreferences.
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
    public boolean onSupportNavigateUp() {
        if (counter == 0) {
            Toast toast = Toast.makeText( getApplicationContext(),
                    "Du har inte sparat receptet. Vill du gå tillbaka " +
                            "utan att spara, klicka på bakåt pilen igen.", Toast.LENGTH_LONG );
            toast.setGravity( Gravity.CENTER, 0, 0 );
            toast.show();
            counter++;
        } else {
            onBackPressed();
        }
        return true;
    }


}
