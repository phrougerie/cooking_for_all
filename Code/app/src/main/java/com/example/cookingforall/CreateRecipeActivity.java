package com.example.cookingforall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.cookingforall.Modele.Ingredient;

import java.util.ArrayList;

public class CreateRecipeActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.cookingforall";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
    }

    public void sendMessage(View view){
        Intent intent = new Intent(this, AddIngredientsActivity.class);
        EditText editText = findViewById(R.id.name);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }


}
