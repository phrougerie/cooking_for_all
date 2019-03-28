package com.roufaurie.cookingforall.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.roufaurie.cookingforall.R;

/**
 *This activity allows the user to set a name for a new Recipe.
 * @author phrougerie
 */

public class CreateRecipeActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_MESSAGE = "com.example.cookingforall";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        getWindow().getDecorView().setBackgroundColor(Color.rgb(214, 201, 62));
    }
    /**
     * Validate the name. Ask the user to set a name if empty.
     * @param view The view.
     */
    public void sendName(View view){
        Intent intent = new Intent(this, AddIngredientsActivity.class);
        EditText editText = findViewById(R.id.name);
        String name = editText.getText().toString();
        Log.d(LOG_TAG, name);
        if(TextUtils.isEmpty((CharSequence) name) == false){
            intent.putExtra(EXTRA_MESSAGE, name);
            startActivity(intent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.emptyRecipeField), Toast.LENGTH_SHORT);
            toast.show();
        }

    }


}
