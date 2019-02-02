package com.example.cookingforall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //findViewById(R.id.listing);
    }

    public void launchCreateRecipeActivity(View view){
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, CreateRecipeActivity.class);
        startActivity(intent);
    }

}
