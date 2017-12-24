package com.shakenbeer.bestsalmon.recipe;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.shakenbeer.bestsalmon.R;
import com.shakenbeer.bestsalmon.model.Recipe;

import org.parceler.Parcels;

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.Callback {

    private static final String DETAILS_TAG = "details";

    public static void start(Context context, Recipe recipe) {
        Intent starter = new Intent(context, RecipeActivity.class);
        starter.putExtra("com.shakenbeer.bestsalmon.recipe.recipeExtra", Parcels.wrap(recipe));
        context.startActivity(starter);
    }

    @TargetApi((Build.VERSION_CODES.LOLLIPOP))
    public static void startTransitions(Activity activity, Recipe recipe, View shared) {
        Intent starter = new Intent(activity, RecipeActivity.class);
        starter.putExtra("com.shakenbeer.bestsalmon.recipe.recipeExtra", Parcels.wrap(recipe));
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(activity, shared, activity.getString(R.string.transition_recipe));
        activity.startActivity(starter, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Recipe recipe = Parcels.unwrap(getIntent().getParcelableExtra("com.shakenbeer.bestsalmon.recipe.recipeExtra"));
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content, RecipeFragment.newInstance(recipe), DETAILS_TAG)
                    .commit();
        }
    }

    @Override
    public void postponeTransition() {
        if (Build.VERSION.SDK_INT >= 21) {
            postponeEnterTransition();
        }
    }

    @Override
    public void resumeTransition() {
        if (Build.VERSION.SDK_INT >= 21) {
            startPostponedEnterTransition();
        }
    }
}
