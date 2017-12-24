package com.shakenbeer.bestsalmon.injection.component;


import android.content.Context;

import com.shakenbeer.bestsalmon.injection.module.ApplicationModule;
import com.shakenbeer.bestsalmon.injection.module.RepoModule;
import com.shakenbeer.bestsalmon.injection.module.RxModule;
import com.shakenbeer.bestsalmon.recipe.RecipeListFragment;
import com.shakenbeer.bestsalmon.injection.module.ApiModule;

import javax.inject.Singleton;

import dagger.Component;

@SuppressWarnings("unused")
@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class, RxModule.class, RepoModule.class})
public interface ApplicationComponent {

    Context context();

    void inject(RecipeListFragment recipeListFragment);
}
