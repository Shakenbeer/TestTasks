package com.shakenbeer.bestsalmon.recipe;


import dagger.Component;

@Component(modules = RecipeModule.class)
public interface RecipeComponent {
    void inject(RecipeFragment recipeFragment);
}
