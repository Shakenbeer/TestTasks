package com.shakenbeer.bestsalmon.domain;


import com.shakenbeer.bestsalmon.model.Recipe;

import java.util.List;

import rx.Observable;

public interface RecipeRepository {
    Observable<List<Recipe>> initialRecipes();
    Observable<List<Recipe>> nextRecipes();
}
