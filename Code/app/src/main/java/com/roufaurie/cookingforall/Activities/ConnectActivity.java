package com.roufaurie.cookingforall.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.roufaurie.cookingforall.Database.DatabaseHelper;
import com.roufaurie.cookingforall.Modele.Ingredient;
import com.roufaurie.cookingforall.Modele.Recipe;
import com.roufaurie.cookingforall.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 *This activity allows the user to connect.
 * @author thlafaurie and phrougerie
 */

public class ConnectActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private final String NAME = "name" ;
    private final String QUANTIIES = "quantities" ;
    private final String UNITS = "units" ;
    private final String PHOTOS = "photos" ;
    private final String STEPS = "steps" ;
    private final String INGREDIENTNAME = "ingredientNames" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setBackgroundColor(Color.rgb(214, 201, 62));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    /**
     * Connects the user to the firebase
     * @param view The view.
     * */
    public void connect(View view) {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        String mail = email.getText().toString();
        String pass = password.getText().toString();

        if(!validateForm()){
            return;
        }

        mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    success();
                } else {
                    fail();
                }
            }
        });
    }
    /**
     * Initialializes the local database with the firestore's documents. Then goes to the mainActivity..
     * */
    public void success(){

        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore dbFire = FirebaseFirestore.getInstance();

        dbFire.collection(user.getEmail().split("@")[0]).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    DatabaseHelper db = new DatabaseHelper(ConnectActivity.this);
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        Map<String,Object> map = documentSnapshot.getData();
                        Recipe recipe = new Recipe();
                        recipe.setRecipeName(map.get(NAME).toString());
                        recipe.setSteps(map.get(STEPS).toString());
                        List<String> quantities = (ArrayList<String>)map.get(QUANTIIES);
                        List<String> units = (ArrayList<String>)map.get(UNITS);
                        List<String> ingredientNames = (ArrayList<String>)map.get(INGREDIENTNAME);
                        ArrayList<Ingredient> ingredients = new ArrayList<>();
                        for (int i=0; i<units.size();i++  ) {
                            ingredients.add(new Ingredient(ingredientNames.get(i),(Integer.parseInt(quantities.get(i))),units.get(i)));
                        }
                        recipe.setListIngredientts(ingredients);
                        recipe.setPhotos((ArrayList<String>) map.get(PHOTOS));
                        long idRecipe = db.addRecipe(recipe.getRecipeName(), recipe.getSteps());
                        for (Ingredient ingredient : recipe.getListIngredientts() ) {

                            long idIngredient = db.addIngredient(ingredient);
                            db.addIngredientRecipe(idIngredient,idRecipe);
                        }
                        for (String photo : recipe.getPhotos() ) {
                            int nb = db.getNbPhotos()+1;
                            db.updateNumberPhoto(nb);
                            db.addPhoto(photo);
                            db.addPhotoRecipe(photo,idRecipe);
                        }
                    }
                    db.close();
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.signInSuccess), Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(ConnectActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.erorDocs), Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d("fail", "Error getting documents: ", task.getException());
                }

            }
        });

    }

    /**
     * Displays a message if connection failed
     * */

    public void fail(){
        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.signInError), Toast.LENGTH_SHORT);
        toast.show();

    }
    /**
     * Calls the activity register.
     * @param view The view.
     * */
    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Verify if the form is correctly filled
     * @return return a boolean, true if the form is ok else true
     */
    private boolean validateForm(){
        boolean valid = true;

        String mail = email.getText().toString();
        if(TextUtils.isEmpty(mail)){
            valid = false;
        } else {
            email.setError(null);
        }

        String pass = password.getText().toString();
        if(TextUtils.isEmpty(pass)){
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }
}
