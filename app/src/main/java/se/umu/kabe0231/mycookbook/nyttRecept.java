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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //When finished button is pressed, add new recipe to ArrayList<Recept> and call setPreferences()
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nytt_recept);
        readPreferences();
        setName = (EditText) findViewById(R.id.editNameText);
        setPort = (EditText) findViewById(R.id.PortionText);
        Instructions = (EditText) findViewById(R.id.InstructionsText);
        GenerateRecept = (Button) findViewById(R.id.GenerateRecept);
        IngredientsView1 = (LinearLayout) findViewById(R.id.IngredientsView1);
        IngredientsView2 = (LinearLayout) findViewById(R.id.IngredientsView2);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView myToolbarText = (TextView) findViewById(R.id.toolbarTitle);
        myToolbarText.setText("Nytt Recept");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

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
                    //galleryAddPic();
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

    public void showDialogFragment(View view) {
        FragmentManager fm = getSupportFragmentManager();
        AddIngredientFragment IngredientFragment = AddIngredientFragment.newInstance("ReceptFragment");
        IngredientFragment.show(fm, "fragment_add__ingredient");
    }

    @Override
    public void onFinishEditDialog(String a, String b) {
        NyttRecept.addIngredient(a, b);
        updateView(a, b);
    }

    public void GenerateRecept(View view) {
        //Kolla så att alla fält är ifyllda!
        NyttRecept.setName(setName.getText().toString());
        NyttRecept.setDescription(Instructions.getText().toString());
        NyttRecept.setPortioner(setPort.getText().toString());
        Recipes.add(NyttRecept);
        setPreferences();
        finish();
    }

    ////////////////Camera functions////////////////

    //storageDirectory



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


    //How do we get the image back??
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            performCrop();
        } else if (requestCode == PIC_CROP) {

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), cropImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Retrieve file of new cropped bitmap

            //Kör debug till hit, hur jämför sig photoFile med imageUri och Bitmap? är det samma fil?
            NyttRecept.setImage(cropPhotoFile.toString());
            //NyttRecept.setImage(bitmap);
        }
    }

    /*

    }*/

    //PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X,  OUTPUT_Y, CODE_RESULT_REQUEST);
    private void performCrop() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        //Gallery crashes without this???
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
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 480);
            intent.putExtra("outputY", 480);
            intent.putExtra("scale", true);
            //将剪切的图片保存到目标Uri中
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);
            intent.putExtra("return-data", true);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

            startActivityForResult(intent, PIC_CROP);
        }

    }

    /*private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }*/

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
