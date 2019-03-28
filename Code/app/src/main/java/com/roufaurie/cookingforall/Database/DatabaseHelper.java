package com.roufaurie.cookingforall.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.roufaurie.cookingforall.Modele.Ingredient;
import com.roufaurie.cookingforall.Modele.Recipe;

import java.util.ArrayList;

/**
 * The dataBaseHelper. It is called to use the dataBase
 * @author phrougerie
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "myDatabase";
    private final String ID_NB_PHOTO = "0";

    //Les tables
    private static final String TABLE_RECIPE = "recipe";
    private static final String TABLE_INGREDIENT = "ingredient";
    private static final String TABLE_PHOTO = "photo";
    private static final String TABLE_PHOTO_RECIPE = "photoRecipe";
    private static final String TABLE_INGREDIENT_RECIPE = "ingredientRecipe";
    private static final String TABLE_NB_PHOTOS = "nbPhotos";

    //Table recette
    private static final String KEY_RECIPE = "keyRecipe";
    private static final String RECIPE_NAME = "recipeName";
    private static final String STEPS = "steps";


    //Table ingredients

    private static final String KEY_INGREDIENT = "keyIngredient";
    private static final String INGREDIENT_NAME = "ingredientName";
    private static final String QUANTITY = "quantity";
    private static final String UNIT = "unit";

    //Table photos
    private static final String KEY_PHOTO = "keyPhoto";

    //TABLE photosRecipe
    private static final String KEY_PHOTO_PR = "keyPhotoPR";
    private static final String KEY_RECIPE_PR = "keyRecipePR" ;

    //TABLE ingredientRecipe
    private static final String KEY_INGREDIENT_IR = "keyIngredientIR";
    private static final String KEY_RECIPE_IR = "keyRecipeIR";

    //TABLE nbPhotostock
    private static final String KEY_NB_PHOTO = "keyNblPhotos";
    private static final String NB_PHOTOS = "nbPhotos";

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    //String creation table recette
    public static final String TABLE_CREATE_RECIPE =
            "CREATE TABLE " + TABLE_RECIPE + " (" +
                    KEY_RECIPE + " INTEGER PRIMARY KEY," +
                    RECIPE_NAME +" TEXT,"+
                    STEPS + " TEXT)";

    //String creation table ingredient


    private static final String TABLE_CREATE_INGREDIENT =
            "CREATE TABLE " + TABLE_INGREDIENT + " (" +
                    KEY_INGREDIENT + " INTEGER PRIMARY KEY," +
                    INGREDIENT_NAME + " TEXT,"+
                    QUANTITY + " REAL ," +
                    UNIT + " TEXT)";

    private static final String TABLE_CREATE_PHOTO =
            "CREATE TABLE " + TABLE_PHOTO + " (" +
                    KEY_PHOTO + " TEXT PRIMARY KEY)";



    private static final String TABLE_CREATE_PHOTO_RECIPE =
            "CREATE TABLE " + TABLE_PHOTO_RECIPE + " (" +
                    KEY_PHOTO_PR + " TEXT REFERENCES "+KEY_PHOTO+"," +
                    KEY_RECIPE_PR + " INTEGER REFERENCES "+ KEY_RECIPE +"," +
                    "PRIMARY KEY ("+KEY_PHOTO_PR+","+ KEY_RECIPE_PR +"))";

    private static final String TABLE_CREATE_INGREDIENT_RECIPE =
            "CREATE TABLE " + TABLE_INGREDIENT_RECIPE + " (" +
                    KEY_INGREDIENT_IR + " INTEGER REFERENCES "+KEY_INGREDIENT+"," +
                    KEY_RECIPE_IR + " INTEGER REFERENCES "+ KEY_RECIPE +"," +
                    "PRIMARY KEY ("+KEY_INGREDIENT_IR+","+ KEY_RECIPE_IR +"))";

    private static final String TABLE_CREATE_NB_PHOTOS =
            "CREATE TABLE " + TABLE_NB_PHOTOS + " (" +
                    KEY_NB_PHOTO + " TEXT PRIMARY KEY," +
                    NB_PHOTOS + " INTEGER)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private void InsertNbPhotoIfNull(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(KEY_NB_PHOTO,ID_NB_PHOTO);
        values.put(NB_PHOTOS,0);

        //insert row
        db.insert(TABLE_NB_PHOTOS,null,values);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creation des tables
        db.execSQL(TABLE_CREATE_INGREDIENT);
        db.execSQL(TABLE_CREATE_RECIPE);
        db.execSQL(TABLE_CREATE_PHOTO);
        db.execSQL(TABLE_CREATE_PHOTO_RECIPE);
        db.execSQL(TABLE_CREATE_INGREDIENT_RECIPE);
        db.execSQL(TABLE_CREATE_NB_PHOTOS);
        InsertNbPhotoIfNull(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO_RECIPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENT_RECIPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NB_PHOTOS);
        onCreate(db);
    }

    public long addRecipe(String name, String steps){
        
        SQLiteDatabase db = this.getWritableDatabase();
        long id =  getCount(TABLE_RECIPE)+1;
        ContentValues values = new ContentValues();
        values.put(KEY_RECIPE,id);
        values.put(RECIPE_NAME,name);
        values.put(STEPS,steps);
        db.insert(TABLE_RECIPE,null,values);
        return id;
    }

    public Recipe getRecipe(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPE, new String[]{KEY_RECIPE,RECIPE_NAME,STEPS},KEY_RECIPE+ "=?",
                new String[]{Long.toString(id)},null,null,null,null);
        if(cursor != null)
            cursor.moveToFirst();
        Recipe recipe = new Recipe(cursor.getString(1),getAllIngredientsRecipe(id),cursor.getString(2),getAllPhotosRecipe(id));
        return recipe;
    }

    public int getNbPhotos(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NB_PHOTOS, new String[]{KEY_NB_PHOTO,NB_PHOTOS},KEY_NB_PHOTO+ "=?",
                new String[]{ID_NB_PHOTO},null,null,null,null);
        if(cursor != null)
            cursor.moveToFirst();

        int num = Integer.parseInt(cursor.getString(1));
        Log.d("test2000",cursor.getString(0));
        return num;
    }

    public ArrayList<Recipe> getAllRecipes(){
        ArrayList<Recipe> recipeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RECIPE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do {
                Recipe recipe = new Recipe();
                long id = Long.parseLong(cursor.getString(0));
                recipe.setRecipeName(cursor.getString(1));
                recipe.setSteps(cursor.getString(2));
                recipe.setListIngredientts(getAllIngredientsRecipe(id));
                recipe.setPhotos(getAllPhotosRecipe(id));
                //A completer avec liste photos
                recipeList.add(recipe);
            }while (cursor.moveToNext());
        }
        return recipeList;

    }

    public long getCount(String nameTable){
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, nameTable);
        return count;
    }

    public long addIngredient(Ingredient ingredient){

        SQLiteDatabase db = this.getWritableDatabase();
        long id =  getCount(TABLE_INGREDIENT)+1;

        ContentValues values = new ContentValues();
        values.put(KEY_INGREDIENT,id);
        values.put(INGREDIENT_NAME,ingredient.getName());
        values.put(QUANTITY,ingredient.getQuantity());
        values.put(UNIT,ingredient.getUnit());
        db.insert(TABLE_INGREDIENT,null,values);
        return id;
    }

    public void addIngredientRecipe(long idIngredient, long idRecipe){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_INGREDIENT_IR,idIngredient);
        values.put(KEY_RECIPE_IR,idRecipe);
        db.insert(TABLE_INGREDIENT_RECIPE,null,values);
    }

    public Ingredient getIngredient(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INGREDIENT, new String[]{KEY_INGREDIENT,INGREDIENT_NAME,QUANTITY,UNIT},KEY_INGREDIENT+ "=?",
                new String[]{Long.toString(id)},null,null,null,null);
        if(cursor != null)
            cursor.moveToFirst();
        Ingredient ingredient = new Ingredient(cursor.getString(1),Integer.parseInt(cursor.getString(2)),cursor.getString(3));
        return ingredient;
    }
    public void updateNumberPhoto(int nb){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NB_PHOTOS,nb);
        db.update(TABLE_NB_PHOTOS, contentValues, KEY_NB_PHOTO+" = ?",new String[] { ID_NB_PHOTO });
    }

    public ArrayList<Ingredient> getAllIngredientsRecipe(long idRecipe){
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_INGREDIENT+", "+TABLE_INGREDIENT_RECIPE+" WHERE "+KEY_INGREDIENT_IR+ " = " +KEY_INGREDIENT+ " AND "+KEY_RECIPE_IR+" ="+idRecipe+" GROUP BY "+KEY_INGREDIENT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(cursor.getString(1));
                ingredient.setQuantity(Integer.parseInt(cursor.getString(2)));
                ingredient.setUnit(cursor.getString(3));
                ingredientList.add(ingredient);
            }while (cursor.moveToNext());
        }
        return ingredientList;
    }

    public void addPhoto(String photo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PHOTO,photo);
        db.insert(TABLE_PHOTO,null,values);
    }

    public void addPhotoRecipe(String idPhoto, long idRecipe){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PHOTO_PR,idPhoto);
        values.put(KEY_RECIPE_PR,idRecipe);
        db.insert(TABLE_PHOTO_RECIPE,null,values);
    }

    public ArrayList<String> getAllPhotosRecipe(long idRecipe){
        ArrayList<String> imagesList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PHOTO+", "+TABLE_PHOTO_RECIPE+" WHERE "+KEY_PHOTO_PR+ "=" +KEY_PHOTO+ " AND "+KEY_RECIPE_PR+"="+idRecipe+" GROUP BY "+KEY_PHOTO;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do {
                String image = ((cursor.getString(0)));
                imagesList.add(image);


            }while (cursor.moveToNext());
        }
        return imagesList;
    }

}
