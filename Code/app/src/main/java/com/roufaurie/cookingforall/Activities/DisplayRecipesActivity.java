package com.roufaurie.cookingforall.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.roufaurie.cookingforall.Adapter.IngredientAdapter;
import com.roufaurie.cookingforall.Adapter.PhotoPageAdapter;
import com.roufaurie.cookingforall.Modele.Recipe;
import com.roufaurie.cookingforall.R;

/**
 *This activity displays a recipe. It uses a fragment (viewpager) to slide the photos.
 * @author phrougerie
 */

public class DisplayRecipesActivity extends FragmentActivity {
    private IngredientAdapter adapterIngredient;
    private ListView lIngredients;
    private Recipe recipe;
    FragmentStatePagerAdapter adapterViewPager;
    private ViewPager viewPager;

    /**
     * Launch the activity. It inializes the ingredient list and others. It also creates a fragment.
     * @param savedInstanceState the bundle
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipes);
        getWindow().getDecorView().setBackgroundColor(Color.rgb(214, 201, 62));
        Intent intent = getIntent();
        recipe = intent.getParcelableExtra(LookListActivity.EXTRA_MESSAGE_RECIPE);
        TextView name = findViewById(R.id.name);
        name.setText(recipe.getRecipeName());
        lIngredients = findViewById(R.id.listIngredients);
        adapterIngredient = new IngredientAdapter(this, android.R.layout.simple_expandable_list_item_1, recipe.getListIngredientts());
        lIngredients.setAdapter(adapterIngredient);
        TextView steps = findViewById(R.id.steps);
        steps.setText(recipe.getSteps());
        steps.setMovementMethod(new ScrollingMovementMethod());
        if(recipe.getPhotos().size() !=0){
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mData = (NetworkInfo) connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if( mWifi.isConnected() == true || mData.isConnected() == true){
                viewPager = findViewById(R.id.vpPager);
                adapterViewPager = new PhotoPageAdapter(getSupportFragmentManager(),recipe.getPhotos());
                viewPager.setAdapter(adapterViewPager);
                TabLayout tabLayout = findViewById(R.id.tab_layout);
                tabLayout.setupWithViewPager(viewPager);
            }
            else {
                Toast.makeText(getApplicationContext(), getString(R.string.noConnectImage), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
