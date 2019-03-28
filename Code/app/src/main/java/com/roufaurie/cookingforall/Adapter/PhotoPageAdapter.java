package com.roufaurie.cookingforall.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.roufaurie.cookingforall.Fragments.FragmentPhoto;

import java.util.ArrayList;
import java.util.List;

/**
 * The adapter used dor the the pageViewer of fragments.
 * @author phrougerie
 */
public  class PhotoPageAdapter extends FragmentStatePagerAdapter {
    private  int numItems;
    private List<String> photos;

    public PhotoPageAdapter(FragmentManager fragmentManager, ArrayList<String> photos) {
        super(fragmentManager);
        this.photos = photos;
        numItems = photos.size();
    }

    /**
     * Counts the items.
     * @return Returns total number of pages
     */

    @Override
    public int getCount() {
        return numItems;
    }

    /**
     *
     * @param position
     * @return Returns the fragment to display for that page
     */
    @Override
    public Fragment getItem(int position) {
        if(photos.get(position) != null)
            return FragmentPhoto.newInstance(photos.get(position));
        else
            return null;
    }

    /**
     *
     * @param position
     * @return  Returns the page title for the top indicator
     */

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}