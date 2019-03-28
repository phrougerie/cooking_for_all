package com.roufaurie.cookingforall.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.roufaurie.cookingforall.R;
import com.roufaurie.cookingforall.Tasks.RecupImage;


/**
 * The fragment for a photo
 * @author phrougerie
 */
public class FragmentPhoto extends Fragment {
    private String photo;


    /**
     * newInstance constructor for creating fragment with arguments
     */

    public static FragmentPhoto newInstance(String photo) {
        FragmentPhoto fragmentPhoto = new FragmentPhoto();
        Bundle args = new Bundle();
        args.putString("photo", photo);
        Log.d("coucou1",photo);
        fragmentPhoto.setArguments(args);
        return fragmentPhoto;
    }

    /**
     * Store instance variables based on arguments passed
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photo = getArguments().getString("photo");
        Log.d("coucou2",photo);
    }


    /**
     * Inflate the view for the fragment based on layout XML
      */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        ImageView imgView = (ImageView) view.findViewById(R.id.photo);
        new RecupImage(imgView).execute(photo);


        return view;
    }
}