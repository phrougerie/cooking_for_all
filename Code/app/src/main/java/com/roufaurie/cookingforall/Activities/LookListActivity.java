package com.roufaurie.cookingforall.Activities;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.roufaurie.cookingforall.Adapter.RecipeAdapter;
import com.roufaurie.cookingforall.Database.DatabaseHelper;
import com.roufaurie.cookingforall.Modele.Recipe;
import com.roufaurie.cookingforall.R;

import java.util.ArrayList;

/**
 *This activity displays a list of recipes. If you toch one recipe it opens a recipe in a new activity.
 * @author phrougerie
 */

public class LookListActivity extends ListActivity {
    private ListView lRecipes;
    private ArrayList<Recipe> listRecipe;
    private RecipeAdapter recipeAdapter;
    public static final String EXTRA_MESSAGE_RECIPE = "recipe";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_list);
        getWindow().getDecorView().setBackgroundColor(Color.rgb(214, 201, 62));

        DatabaseHelper db = new DatabaseHelper(this);
        listRecipe = db.getAllRecipes();
        recipeAdapter = new RecipeAdapter(this, android.R.layout.simple_expandable_list_item_1, listRecipe);
        setListAdapter(recipeAdapter);
        db.close();
    }

    /**
     * Used to launch the CreateRecipeActivity. If there is no connection display informations.
     * @param l the list of recipes
     * @param v the view
     * @param position the position in the list
     * @param id the id
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Intent intent = new Intent(this,DisplayRecipesActivity.class);
        intent.putExtra(EXTRA_MESSAGE_RECIPE,listRecipe.get(position));
        startActivity(intent);
    }
}
