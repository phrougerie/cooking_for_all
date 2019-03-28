package com.roufaurie.cookingforall.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.roufaurie.cookingforall.Database.DatabaseHelper;
import com.roufaurie.cookingforall.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 *The first activity launched.
 * It's a menu. Permit to go to connection, create a recipe or go to LookList. *
 * @author phrougerie thlafaurie
 */

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private ConnectivityManager connectivityManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor(Color.rgb(214, 201, 62));
        mAuth = FirebaseAuth.getInstance();
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Button connect = (Button) findViewById(R.id.connect);
        if(currentUser==null){
            connect.setText(getString(R.string.connectButton));
        } else {
            connect.setText(getString(R.string.logOutButton));
        }
    }

    /**
     * Used to launch the CreateRecipeActivity. If there is no connection display informations.
     * @param view The wiew
     */

    public void launchCreateRecipeActivity(View view){
        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mData = (NetworkInfo) connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if( mWifi.isConnected() == true || mData.isConnected() == true){
            if(mAuth.getCurrentUser()!=null){
                Intent intent = new Intent(this, CreateRecipeActivity.class);
                startActivity(intent);
            }

            else {
                Intent intent = new Intent(this, ConnectActivity.class);
                startActivity(intent);
            }
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.notConnectect), Toast.LENGTH_SHORT).show();

        }

    }


    /**
     * Used to launch the LookListActivity. If there is no connection display informations.
     * @param view The wiew
     */
    public void lookRecipe(View view){
        if(mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(this, LookListActivity.class);
            startActivity(intent);
        }
        else {
            NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mData = (NetworkInfo) connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if( mWifi.isConnected() == true || mData.isConnected() == true){
                Intent intent = new Intent(this, ConnectActivity.class);
                startActivity(intent);

            }
            else {
                Toast.makeText(getApplicationContext(), getString(R.string.notConnectect), Toast.LENGTH_SHORT).show();

            }

        }

    }

    /**
     * Used to connect or disconnect.
     * @param view The wiew
     */

    public void connect(View view) {
        Log.d("test10", LOG_TAG);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Button connect = (Button) findViewById(R.id.connect);
        if(currentUser==null){
            Intent intent = new Intent(this, ConnectActivity.class);
            startActivity(intent);
        } else {
            mAuth.signOut();
            DatabaseHelper db = new DatabaseHelper(this);
            db.onUpgrade(db.getWritableDatabase(),db.getDatabaseVersion(),db.getDatabaseVersion());
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.signOut), Toast.LENGTH_SHORT);
            toast.show();
            connect.setText(getString(R.string.connectButton));
        }


    }
}