package com.shakenbeer.bestsalmon.recipe;


import com.shakenbeer.bestsalmon.model.Recipe;
import com.shakenbeer.bestsalmon.presentation.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class RecipeListPresenter extends BasePresenter<RecipeListView> {

    private final InitialRecipeListInteractor initialRecipeListInteractor;

    private final RecipeListInteractor recipeListInteractor;

    @Inject
    public RecipeListPresenter(InitialRecipeListInteractor initialRecipeListInteractor,
                               RecipeListInteractor recipeListInteractor) {
        this.initialRecipeListInteractor = initialRecipeListInteractor;
        this.recipeListInteractor = recipeListInteractor;
    }

    @Override
    public void detachView() {
        initialRecipeListInteractor.unsubscribe();
        recipeListInteractor.unsubscribe();
        super.detachView();
    }

    public void loadItems() {
        getMvpView().showLoadingIndicator();
        initialRecipeListInteractor.execute(new InitialRecipeListSubscriber());
    }

    public void loadMoreItems() {
        getMvpView().showLoadingIndicator();
        recipeListInteractor.execute(new RecipeListSubscriber());
    }

    @Override
    public void onDestroyed() {

    }

    private class RecipeListSubscriber extends Subscriber<List<Recipe>> {
        @Override
        public void onCompleted() {
            getMvpView().hideLoadingIndicator();
        }

        @Override
        public void onError(Throwable e) {
            getMvpView().hideLoadingIndicator();
            getMvpView().showMessage(e.getLocalizedMessage());
        }

        @Override
        public void onNext(List<Recipe> recipes) {
            getMvpView().hideLoadingIndicator();
            getMvpView().showItems(recipes);
        }
    }

    private final class InitialRecipeListSubscriber extends RecipeListSubscriber {

        @Override
        public void onNext(List<Recipe> recipes) {
            getMvpView().hideLoadingIndicator();
            getMvpView().clearItems();
            getMvpView().showItems(recipes);
        }
    }
}
