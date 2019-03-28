package com.roufaurie.cookingforall.Adapter;
import java.util.List;

import android.content.Context;

import android.graphics.Bitmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


import com.roufaurie.cookingforall.R;

/**
 * The adapter of the images
 * @author  phrougerie
 */

public class ImageAdapter extends ArrayAdapter<Bitmap> {
    private List<Bitmap> items;
    public ImageAdapter( Context context, int resource, List<Bitmap> objects) {
        super(context, resource, objects);
        this.items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.list_image,parent,false);
        ImageView imageView = layout.findViewById(R.id.photo);

        Bitmap it = items.get(position);
        imageView.setImageBitmap(it);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        layout.setLayoutParams(params);
        return layout;
    }


}
