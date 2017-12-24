package com.shakenbeer.bestsalmon.recipe;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.shakenbeer.bestsalmon.R;
import com.shakenbeer.bestsalmon.databinding.FragmentRecipeBinding;
import com.shakenbeer.bestsalmon.model.Recipe;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class RecipeFragment extends Fragment implements RecipeView {

    @Inject
    RecipePresenter presenter;
    private FragmentRecipeBinding binding;
    private Callback callback;

    public RecipeFragment() {
        setRetainInstance(true);
    }

    public static RecipeFragment newInstance(Recipe recipe) {
        Bundle args = new Bundle();
        args.putParcelable("com.shakenbeer.bestsalmon.recipe.Recipe", Parcels.wrap(recipe));
        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeFragment.Callback) {
            callback = (RecipeFragment.Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RecipeFragment.Callback");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Recipe recipe =
                Parcels.unwrap(getArguments().getParcelable("com.shakenbeer.bestsalmon.recipe.Recipe"));
        DaggerRecipeComponent
                .builder()
                .recipeModule(new RecipeModule(recipe))
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.obtainRecipe();
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        presenter.onDestroyed();
        super.onDestroy();
    }

    @Override
    public void showRecipe(final Recipe recipe) {
        callback.postponeTransition();
        Glide.with(getContext())
                .load(recipe.getImage())
                .error(R.drawable.food_salmon_800px)
                .dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        callback.resumeTransition();
                        return false;
                    }
                }).into(binding.imageView);
        binding.nameTextView.setText(recipe.getLabel());
        binding.sourceTextView.setText(recipe.getSource());
        binding.ingredientsTextView.setText(buildIngredientsText(recipe.getIngredientLines()));
        binding.urlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(recipe.getUrl()));
                startActivity(i);
            }
        });
    }

    private String buildIngredientsText(List<String> lines) {
        StringBuilder sb = new StringBuilder(getString(R.string.ingredients));
        for (int i = 0; i < lines.size(); i++) {
            sb.append("\n").append(lines.get(i));
        }
        return sb.toString();
    }


    interface Callback {
        void postponeTransition();

        void resumeTransition();
    }
}
