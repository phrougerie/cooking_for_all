package com.roufaurie.cookingforall.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.roufaurie.cookingforall.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 *This activity can register a new user by email.
 * @author thlafaurie
 */

public class RegisterActivity extends AppCompatActivity {
    private EditText emailInscription;
    private EditText passwordInscription;
    private EditText passwordVerif;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setBackgroundColor(Color.rgb(214, 201, 62));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    /**
     * Register a new user.
     * @param view the view
     */

    public void register(View view) {

        emailInscription = findViewById(R.id.emailInscription);
        passwordInscription = findViewById(R.id.passwordInscription);
        passwordVerif = findViewById(R.id.passwordVerif);
        String mail = emailInscription.getText().toString();
        String pass = passwordInscription.getText().toString();

        if(!validateForm()){
            return;
        }

        mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    success();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.createFail), Toast.LENGTH_SHORT);
                    toast.show();

                }
            }
        });
    }
    /**
     * Relaunchs the MainActivity if successful.
     */

    private void success(){
        String mail = emailInscription.getText().toString();
        String pass = passwordInscription.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();
        connection(mail, pass);
        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.registerSuccess), Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    /**
     * Verify if the form is correctly filled
     * @return return a boolean, true if the form is ok else true
     */
    private boolean validateForm(){
        boolean valid = true;
        String pass1 = passwordInscription.getText().toString();
        String pass2 = passwordVerif.getText().toString();

        String mail = emailInscription.getText().toString();
        if(TextUtils.isEmpty(mail)){
            valid = false;
        } else {
            emailInscription.setError(null);
        }

        String pass = passwordInscription.getText().toString();
        if(TextUtils.isEmpty(pass) || pass.length()<8){
            valid = false;
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.errorPassword), Toast.LENGTH_SHORT);
            toast.show();
        } else {
            passwordInscription.setError(null);
        }

        if(!pass1.equals(pass2)){
            valid=false;
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.wrongPassword), Toast.LENGTH_SHORT);
            toast.show();
        }
        return valid;
    }

    /**
     * Connects the user to the firebase
     * @param mail the user mail
     * @param pass the user password
     */

    public void connection(String mail, String pass) {
        mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("Test", "SignIn Success");
                    FirebaseUser user = mAuth.getCurrentUser();
                } else {
                    Log.d("Test", "SignIn Failed");
                }
            }
        });
    }
}
