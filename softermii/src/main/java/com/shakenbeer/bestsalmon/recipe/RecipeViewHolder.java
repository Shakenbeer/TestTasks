package com.shakenbeer.bestsalmon.recipe;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.View;

import com.shakenbeer.bestsalmon.base.BaseBindingViewHolder;
import com.shakenbeer.bestsalmon.databinding.ItemRecipeBinding;


public class RecipeViewHolder extends BaseBindingViewHolder {

    private final RecipeClickListener recipeClickListener;

    public interface RecipeClickListener extends ClickListener {
        void onViewClick(int position, View sharedView);

        @Override
        void onViewClick(int position);
    }

    public RecipeViewHolder(ViewDataBinding binding, @NonNull RecipeClickListener recipeClickListener) {
        super(binding, recipeClickListener);
        this.recipeClickListener = recipeClickListener;
    }

    @Override
    public void onClick(View v) {
        recipeClickListener.onViewClick(getAdapterPosition(), ((ItemRecipeBinding) binding).imageView);
    }
}
