package com.shakenbeer.bestsalmon.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.login.LoginManager;
import com.shakenbeer.bestsalmon.R;
import com.shakenbeer.bestsalmon.login.LoginActivity;
import com.shakenbeer.bestsalmon.model.Recipe;
import com.shakenbeer.bestsalmon.util.Utils;

public class RecipeListActivity extends AppCompatActivity implements RecipeListFragment.Callback {

    private static final String LIST_TAG = "list";
    private CoordinatorLayout coordinatorLayout;

    public static void start(Context context) {
        Intent starter = new Intent(context, RecipeListActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content, RecipeListFragment.newInstance(), LIST_TAG)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Utils.isNetworkAvailable(this)) {
            Snackbar.make(coordinatorLayout, R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.got_it, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_logout) {
            LoginManager.getInstance().logOut();
            LoginActivity.start(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecipeClick(Recipe recipe, View image) {
        if (Build.VERSION.SDK_INT < 21) {
            RecipeActivity.start(this, recipe);
        } else {
            RecipeActivity.startTransitions(this, recipe, image);
        }
    }
}

