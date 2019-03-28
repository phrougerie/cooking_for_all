package com.roufaurie.cookingforall.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.roufaurie.cookingforall.Adapter.IngredientAdapter;
import com.roufaurie.cookingforall.Modele.Ingredient;
import com.roufaurie.cookingforall.R;

import java.util.ArrayList;

/**
 *This activity allows the to create an ingredient. The user can select the name, quantity and unity .
 * @author phrougerie
 */

public class AddIngredientsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView textView;
    public static final String EXTRA_MESSAGE_NAME = "@string/name";
    public static final String EXTRA_MESSAGE_LIST = "liste";
    public static final String KEY_LIST ="1111";
    private ListView lIngredients;
    private IngredientAdapter adapterIngredient;
    private ArrayList<Ingredient> listIngredients = new ArrayList<>();

    /**
     * At the creation of the activity. Initialize the list of ingredients and its adapter
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);
        getWindow().getDecorView().setBackgroundColor(Color.rgb(214, 201, 62));
        Intent intent = getIntent();
        String message = intent.getStringExtra(CreateRecipeActivity.EXTRA_MESSAGE);
        textView = findViewById(R.id.name);
        Spinner spinner = findViewById(R.id.unitSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.unit_array, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        lIngredients = findViewById(R.id.listIngredients);
        adapterIngredient = new IngredientAdapter (this, android.R.layout.simple_expandable_list_item_1, listIngredients);
        lIngredients.setAdapter(adapterIngredient);
        textView.setText(message);
    }

    /**
     * When onDestroy is called. Saves the list of ingredients
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("listIngre", listIngredients);
    }

    /**
     * When onCreate is called. Restores the list of ingredients
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        listIngredients.clear();
        listIngredients.addAll((ArrayList<Ingredient>) savedInstanceState.getSerializable("listIngre"));
        adapterIngredient.notifyDataSetChanged();
    }


    /**
     * Called when button addIngredient pushed. Add an ingredient to the list.
     * @param view
     */
    public void addIngredient(View view) {
        EditText editIName = findViewById(R.id.nameIng);
        String nameIngredient = editIName.getText().toString();
        EditText editQuantity = findViewById(R.id.quantity);
        lIngredients = findViewById(R.id.listIngredients);
        Spinner unitSpin = findViewById(R.id.unitSpinner);
        String unit = unitSpin.getSelectedItem().toString();
        if(TextUtils.isEmpty(nameIngredient) == false && (editQuantity.getText().toString().isEmpty()) == false ){
            int quantity = Integer.parseInt(editQuantity.getText().toString());
            listIngredients.add(new Ingredient(nameIngredient,quantity,unit));
            this.adapterIngredient.notifyDataSetChanged();
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.noIngredientOrQuantity), Toast.LENGTH_SHORT);
            toast.show();
        }
        editQuantity.setText("");
        editIName.setText("");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Validate and go to the photo and description activity if there is at least one ingredient
     * @param view
     */

    public void validateIng(View view){
        if(listIngredients.isEmpty() == false){
            Intent intent = new Intent(this,AddPhotoAndActionsActivity.class);
            String name = textView.getText().toString();
            intent.putExtra(EXTRA_MESSAGE_NAME,name);
            intent.putParcelableArrayListExtra(EXTRA_MESSAGE_LIST,listIngredients);
            startActivity(intent);
            PreferenceManager.getDefaultSharedPreferences(this).edit().remove(KEY_LIST);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.validateListIngredients), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
