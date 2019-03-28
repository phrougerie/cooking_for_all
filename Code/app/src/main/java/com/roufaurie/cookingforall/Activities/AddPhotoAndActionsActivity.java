package com.roufaurie.cookingforall.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.roufaurie.cookingforall.Adapter.ImageAdapter;
import com.roufaurie.cookingforall.Adapter.IngredientAdapter;
import com.roufaurie.cookingforall.Database.DatabaseHelper;
import com.roufaurie.cookingforall.Modele.Ingredient;
import com.roufaurie.cookingforall.Modele.Recipe;
import com.roufaurie.cookingforall.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This activity allows the user to add photos and a description. At the end the user save his recipe.
 */

public class AddPhotoAndActionsActivity extends ListActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private List<Ingredient> listIngredients;
    private ListView lIngredients;
    private IngredientAdapter adapterIngredient;
    private List<Bitmap> listPhotos = new ArrayList<>();
    private ImageAdapter adapterImage;
    private StorageReference mStorageRef;
    public static final String KEY_LIST ="2222";
    public final static int PHOTO_CODE = 1046;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String finUri = "";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    String [] appPermission ={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    private String name;
    private DatabaseHelper db;
    private long idRecipe;
    private static final int PERMISSIONS_REQUEST_CODE = 1240;
    private Bundle savedInstance;
    private int total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo_and_actions);
        getWindow().getDecorView().setBackgroundColor(Color.rgb(214, 201, 62));

        //Permissions for camera use and access to the storage
        if(checkAndRequestPermissions()){
            // All permissions are granted already. Procead ahead
            initAct(savedInstanceState);
        }


    }

    /**
     * Initialize the adapters and lists. Called by onCreate and permissionResult.
     * @param savedInstanceState
     */
    private void initAct(Bundle savedInstanceState){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        savedInstance = savedInstanceState;
        FirebaseApp.initializeApp(this);
        Intent intent = getIntent();
        name = intent.getStringExtra(AddIngredientsActivity.EXTRA_MESSAGE_NAME);
        listIngredients = intent.getParcelableArrayListExtra(AddIngredientsActivity.EXTRA_MESSAGE_LIST);
        TextView test = findViewById(R.id.name);
        lIngredients = findViewById(R.id.listIngredients);
        adapterIngredient = new IngredientAdapter(this, android.R.layout.simple_expandable_list_item_1, listIngredients);
        lIngredients.setAdapter(adapterIngredient);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(savedInstance != null && prefs.getString(KEY_LIST, null)!=null) {
            Gson gson = new Gson();
            String json = prefs.getString(KEY_LIST, null);
            Type type = new TypeToken<ArrayList<Bitmap>>() {
            }.getType();
            listPhotos = gson.fromJson(json, type);
        }
        adapterImage = new ImageAdapter(this, R.layout.list_image,listPhotos);
        setListAdapter(adapterImage);
        test.setText(name);
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    /**
     * Calls the camera
     * @param view
     */
    public void cameraOn(View view){
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePic, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Add a photo and save it in the shared preference with the list.
     * @param imageBitmap
     */
    private void mainAddPhoto(final Bitmap imageBitmap){
        listPhotos.add(imageBitmap);
        adapterImage.notifyDataSetChanged();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AddPhotoAndActionsActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listPhotos);
        editor.putString(KEY_LIST,json);
        editor.apply();
    }

    /**
     * Check if the permissions are accepted by the user
     * @return true if the permission are granted, else false
     */
    public boolean checkAndRequestPermissions(){
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : appPermission){
            if(ContextCompat.checkSelfPermission(this,perm) != PackageManager.PERMISSION_GRANTED){
                listPermissionsNeeded.add(perm);
            }
        }
        if(!listPermissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSIONS_REQUEST_CODE);
            return false;
        }

        return true;
    }

    /**
     * Ask the permissions to the user.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE){
            Map<String, Integer> permissionResults = new HashMap<>();
            int deniedCount = 0;
            for (int i=0; i<grantResults.length; i++){

                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    permissionResults.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }

            if(deniedCount == 0){
                initAct(savedInstance);
            }

            else {
                for (Map.Entry<String, Integer> entry : permissionResults.entrySet()){
                    String permName = entry.getKey();
                    int permResult = entry.getValue();

                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,permName)){
                        showDialog("", getString(R.string.advertPermission),
                                getString(R.string.grantPermission),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        checkAndRequestPermissions();
                                    }
                                },
                                getString(R.string.noPermission), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                },false);
                    }
                    else {
                        showDialog("", getString(R.string.deniedSome), getString(R.string.goSettings), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(),null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        },getString(R.string.noPermission), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        },false);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Displays the dialog in fonction of the permissions granted.
     * @param title
     * @param msg
     * @param positiveLabel
     * @param positiveOnClick
     * @param negativeLabel
     * @param negativeOnClick
     * @param isCancelAble
     * @return
     */
    public AlertDialog showDialog(String title, String msg, String positiveLabel,
                                  DialogInterface.OnClickListener positiveOnClick, String negativeLabel,
                                  DialogInterface.OnClickListener negativeOnClick, boolean isCancelAble)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(isCancelAble);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLabel,positiveOnClick);
        builder.setNegativeButton(negativeLabel,negativeOnClick);

        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    /**
     * Compresse the bitmap to stock it in the storage.
     * @param image
     */
    private void compressBitmap(Bitmap image){
        double ratio = image.getWidth()/image.getHeight();
        Log.d("test10", String.valueOf(image.getWidth())+" "+image.getHeight());
        if(ratio>=1.0){
            Bitmap newBitmap = Bitmap.createScaledBitmap(image, 810,
                    540, true);
            mainAddPhoto(newBitmap);
        }
        else {
            Bitmap newBitmap = Bitmap.createScaledBitmap(image, 540,
                    810, true);
            mainAddPhoto(newBitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            compressBitmap(image);
        }
        else if(data != null && requestCode == PHOTO_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                compressBitmap(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Go searching photos in folders.
     * @param view
     */
    public void searchPhoto(View view){
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, PHOTO_CODE);
        }

    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    }

    /**
     * Stores the new recipe in the local base and on the fireStore.
     * @param view
     */
    public void saveRecipe(View view) {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mData = (NetworkInfo) connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if( mWifi.isConnected() == true || mData.isConnected() == true){
            total =0;
            db = new DatabaseHelper(this);

            findViewById(R.id.takePhoto).setClickable(false);
            findViewById(R.id.searchPhoto).setClickable(false);
            findViewById(R.id.buttonSave).setClickable(false);
            TextView steps = findViewById(R.id.desc);
            idRecipe = db.addRecipe(name, steps.getText().toString());
            findViewById(R.id.desc).setEnabled(false);
            findViewById(R.id.listIngredients).setEnabled(false);

            Toast.makeText(getApplicationContext(), getString(R.string.load), Toast.LENGTH_SHORT).show();

            for (Ingredient ingredient : listIngredients ) {

                long idIngredient = db.addIngredient(ingredient);
                db.addIngredientRecipe(idIngredient,idRecipe);
            }
            for (int i=0; i<listPhotos.size(); i++ ) {
                int nb = db.getNbPhotos()+1;

                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                listPhotos.get(i).compress(Bitmap.CompressFormat.PNG,100,bao);
                byte[] byteArray = bao.toByteArray();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://projectcooking-631ba.appspot.com");
                String mail = currentUser.getEmail().split("@")[0];
                Log.d("test11",mail);
                final StorageReference imageRef = storageRef.child(mail+"/"+"image"+nb+".jpg");
                db.updateNumberPhoto(nb);

                UploadTask uploadTask =  imageRef.putBytes(byteArray);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("test355", "onSuccess: uri= "+ uri.toString());
                                finUri= uri.toString();
                                db.addPhoto(finUri);
                                db.addPhotoRecipe(finUri,idRecipe);
                                if (total == listPhotos.size()-1){
                                    uploadNewRecipe();
                                    Intent intent = new Intent(AddPhotoAndActionsActivity.this, MainActivity.class);

                                    startActivity(intent);
                                }
                                ++total;

                            }
                        });
                    }
                });
            }
            if(listPhotos.size()==0){
                uploadNewRecipe();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.notConnectect), Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * Stores the recipe in the firestore
     */
    private void uploadNewRecipe(){
        long count = db.getCount("recipe");
        Recipe recipe = db.getRecipe(count);
        FirebaseFirestore dbFire = FirebaseFirestore.getInstance();
        db.close();
        CollectionReference users = dbFire.collection(mAuth.getCurrentUser().getEmail().split("@")[0]);


        List<String> names = new ArrayList<>();
        List<String> units = new ArrayList<>();
        List<String> quantities = new ArrayList<>();
        for (Ingredient ingredient : listIngredients ){
            names.add(ingredient.getName());
            units.add(ingredient.getUnit());
            quantities.add(String.valueOf(ingredient.getQuantity()));
        }
        Map<String,Object> data = new HashMap<>();
        data.put("name", recipe.getRecipeName());
        data.put("ingredientNames", names);
        data.put("units",units);
        data.put("quantities", quantities);
        data.put("steps",recipe.getSteps());
        data.put("photos",recipe.getPhotos());
        users.document(String.valueOf(count)).set(data);
    }
}
