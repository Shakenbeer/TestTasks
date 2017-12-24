package com.shakenbeer.bestsalmon.domain;

import com.shakenbeer.bestsalmon.injection.module.RxModule;
import com.shakenbeer.bestsalmon.model.Edamam;
import com.shakenbeer.bestsalmon.model.Recipe;
import com.shakenbeer.bestsalmon.rest.EdamamService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Func1;

public class CloudRecipeRepository implements RecipeRepository {

    private final static int OFFSET = 30;

    private final EdamamService service;
    private final Scheduler computationScheduler;
    private volatile int page = 0;

    public CloudRecipeRepository(EdamamService service,
                                 @Named(RxModule.COMPUTATION) Scheduler computationScheduler) {
        this.service = service;
        this.computationScheduler = computationScheduler;
    }


    @Override
    public Observable<List<Recipe>> initialRecipes() {
        page = 0;
        return recipes();
    }

    @Override
    public Observable<List<Recipe>> nextRecipes() {
        page++;
        return recipes();
    }

    private Observable<List<Recipe>> recipes() {
        return service
                .getRecipes(page * OFFSET, (page + 1) * OFFSET)
                .observeOn(computationScheduler)
                .map(new Func1<Edamam, List<Recipe>>() {
                    @Override
                    public List<Recipe> call(Edamam edamam) {
                        List<Recipe> result = new ArrayList<>(OFFSET);
                        for (int i = 0; i < edamam.getHits().size(); i++) {
                            result.add(edamam.getHits().get(i).getRecipe());
                        }
                        return result;
                    }
                });
    }
}
