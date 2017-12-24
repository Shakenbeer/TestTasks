package com.shakenbeer.bestsalmon.injection.module;


import com.shakenbeer.bestsalmon.domain.CloudRecipeRepository;
import com.shakenbeer.bestsalmon.domain.RecipeRepository;
import com.shakenbeer.bestsalmon.rest.EdamamService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;

@Module
public class RepoModule {

    @Provides
    @Singleton
    RecipeRepository provideRecipeRepository(EdamamService service,
                                             @Named(RxModule.COMPUTATION) Scheduler computationScheduler) {
        return new CloudRecipeRepository(service, computationScheduler);
    }
}
