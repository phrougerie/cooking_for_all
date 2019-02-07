package com.example.cookingforall;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.cookingforall.Modele.Ingredient;

import java.util.ArrayList;

public class AddIngredientsActivity extends AppCompatActivity {
    private ArrayList<Ingredient> listIngredient;
    private String ingredients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);


        Intent intent = getIntent();
        String message = intent.getStringExtra(CreateRecipeActivity.EXTRA_MESSAGE);
        TextView textView = findViewById(R.id.name);
        textView.setText(message);


    }

}
