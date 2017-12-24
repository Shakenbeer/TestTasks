package com.shakenbeer.bestsalmon.recipe;

import com.shakenbeer.bestsalmon.domain.Interactor;
import com.shakenbeer.bestsalmon.domain.RecipeRepository;
import com.shakenbeer.bestsalmon.injection.module.RxModule;
import com.shakenbeer.bestsalmon.model.Recipe;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;

public class InitialRecipeListInteractor extends Interactor {

    private final RecipeRepository recipeRepository;

    @Inject
    public InitialRecipeListInteractor(@Named(RxModule.IO) Scheduler workScheduler,
                                       @Named(RxModule.MAIN) Scheduler mainScheduler, RecipeRepository recipeRepository) {
        super(workScheduler, mainScheduler);
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Observable<List<Recipe>> buildInteractorObservable(String... params) {
        return recipeRepository.initialRecipes();
    }
}
