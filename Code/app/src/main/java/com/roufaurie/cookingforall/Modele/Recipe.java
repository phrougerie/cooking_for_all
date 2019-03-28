package com.roufaurie.cookingforall.Modele;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A recipe componed of a name, a list of ingredients, a list aof photos. And a list of steps.
 * @author phrougerie
 */
public class Recipe implements Parcelable {
    private String recipeName;
    private ArrayList<Ingredient> listIngredientts;
    private String steps;
    private ArrayList<String> photos;

    public Recipe(String recipeName, ArrayList<Ingredient> listIngredientts, String steps, ArrayList<String> photos) {
        this.recipeName = recipeName;
        this.listIngredientts = listIngredientts;
        this.steps = steps;
        this.photos = photos;
    }


    protected Recipe(Parcel in) {
        recipeName = in.readString();
        listIngredientts = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.readString();
        photos = in.createStringArrayList();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public String toString() {
        return  recipeName ;
    }

    public Recipe() {
    }

    public String getRecipeName() {
        return recipeName;
    }

    public List<Ingredient> getListIngredientts() {
        return Collections.unmodifiableList(listIngredientts);
    }

    public String getSteps() {
        return steps;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setListIngredientts(ArrayList<Ingredient> listIngredientts) {
        this.listIngredientts = listIngredientts;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recipeName);
        dest.writeTypedList(listIngredientts);
        dest.writeString(steps);
        dest.writeStringList(photos);
    }
}
