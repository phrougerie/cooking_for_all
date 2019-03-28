package com.roufaurie.cookingforall.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.roufaurie.cookingforall.Modele.Ingredient;

import java.util.List;

/**
 * The adapter of the ingredients
 * @author  phrougerie
 */
public class IngredientAdapter extends ArrayAdapter<Ingredient> {
    public IngredientAdapter( Context context, int resource, List<Ingredient> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View layout = super.getView(position,convertView,parent);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.height = 60;
        layout.setLayoutParams(params);
        return layout;
    }

}
