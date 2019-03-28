package com.roufaurie.cookingforall.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.roufaurie.cookingforall.Modele.Recipe;
import com.roufaurie.cookingforall.R;

import java.util.List;


/**
 * The adapter of the recipes
 * @author  phrougerie
 */
public class RecipeAdapter extends ArrayAdapter<Recipe> {
    private List<Recipe> items;
    public RecipeAdapter(Context context, int resource, List<Recipe> objects) {
        super(context, resource, objects);
        this.items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.list_recipes,parent,false);
        TextView textView = layout.findViewById(R.id.recipe);
        Recipe recipe = items.get(position);
        textView.setText(recipe.toString());
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        layout.setLayoutParams(params);
        return layout;
    }

}