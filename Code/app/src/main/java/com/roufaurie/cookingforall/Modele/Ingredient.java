package com.roufaurie.cookingforall.Modele;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * A ingredient composed of a name, quantity and unit.
 * @author phrougerie
 */
public class Ingredient implements Parcelable, Serializable {

    private String name;
    private int quantity;
    private String unit ;

    public Ingredient(String name, int quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    protected Ingredient(Parcel in) {
        name = in.readString();
        quantity = in.readInt();
        unit = in.readString();
    }

    public Ingredient() {
    }

    /**
     * Used to parcel an ingredient.
     */
    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeString(unit);
    }

    @Override
    public String toString() {
        return quantity+" " + unit + " " + name  ;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
