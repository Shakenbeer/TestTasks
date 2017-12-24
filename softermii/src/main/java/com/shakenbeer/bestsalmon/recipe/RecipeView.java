package com.shakenbeer.bestsalmon.recipe;

import com.shakenbeer.bestsalmon.model.Recipe;
import com.shakenbeer.bestsalmon.presentation.MvpView;


interface RecipeView extends MvpView {
    void showRecipe(Recipe recipe);
}
