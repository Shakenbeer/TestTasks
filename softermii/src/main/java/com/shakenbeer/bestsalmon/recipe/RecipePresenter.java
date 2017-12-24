package com.shakenbeer.bestsalmon.recipe;

import com.shakenbeer.bestsalmon.model.Recipe;
import com.shakenbeer.bestsalmon.presentation.BasePresenter;

import javax.inject.Inject;


public class RecipePresenter extends BasePresenter<RecipeView> {

    private final Recipe recipe;

    @Inject
    public RecipePresenter(Recipe recipe) {
        this.recipe = recipe;
    }

    public void obtainRecipe() {
        getMvpView().showRecipe(recipe);
    }

    @Override
    public void onDestroyed() {

    }


}
