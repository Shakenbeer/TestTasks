package com.shakenbeer.bestsalmon.recipe;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shakenbeer.bestsalmon.base.BaseBindingAdapter;
import com.shakenbeer.bestsalmon.base.BaseBindingViewHolder;
import com.shakenbeer.bestsalmon.databinding.ItemRecipeBinding;
import com.shakenbeer.bestsalmon.model.Recipe;

import javax.inject.Inject;


public class RecipeAdapter extends BaseBindingAdapter<Recipe> implements RecipeViewHolder.RecipeClickListener {

    private RecipeClickListener recipeClickListener;

    @Inject
    public RecipeAdapter(Context context) {
        Context context1 = context;
    }

    public void setRecipeClickListener(RecipeClickListener recipeClickListener) {
        this.recipeClickListener = recipeClickListener;
    }

    @Override
    public BaseBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = bind(inflater, parent, viewType);
        return new RecipeViewHolder(binding, this);
    }

    @Override
    protected ViewDataBinding bind(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return ItemRecipeBinding.inflate(inflater, parent, false);
    }

    @Override
    public void onBindViewHolder(BaseBindingViewHolder holder, int position) {
        ((ItemRecipeBinding) holder.binding).setRecipe(items.get(position));

    }

    @Override
    public void onViewClick(int position, View sharedView) {
        if (recipeClickListener != null) {
            recipeClickListener.onClick(items.get(position), position, sharedView);
        }
    }

    @SuppressWarnings("UnusedParameters")
    public interface RecipeClickListener {
        void onClick(Recipe item, int position, View image);
    }
}
