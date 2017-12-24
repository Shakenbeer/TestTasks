package com.shakenbeer.bestsalmon.recipe;

import com.shakenbeer.bestsalmon.model.Recipe;

import dagger.Module;
import dagger.Provides;

@Module
public class RecipeModule {

    private final Recipe recipe;

    public RecipeModule(Recipe recipe) {
        this.recipe = recipe;
    }

    @Provides
    Recipe provideRecipe() {
        return recipe;
    }
}
