package com.shakenbeer.bestsalmon.recipe;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shakenbeer.bestsalmon.R;
import com.shakenbeer.bestsalmon.base.BaseFragment;
import com.shakenbeer.bestsalmon.databinding.FragmentListBinding;
import com.shakenbeer.bestsalmon.model.Recipe;

import java.util.List;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeListFragment extends BaseFragment implements RecipeListView {

    @Inject
    RecipeListPresenter presenter;

    @Inject
    RecipeAdapter adapter;

    private FragmentListBinding binding;
    private GridLayoutManager layoutManager;
    private final RecyclerView.OnScrollListener recyclerViewOnScrollListener =
            new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                        presenter.loadMoreItems();
                    }
                }
            };
    private Callback callback;
    private final RecipeAdapter.RecipeClickListener recipeClickListener =
            new RecipeAdapter.RecipeClickListener() {
                @Override
                public void onClick(Recipe item, int position, View sharedView) {
                    callback.onRecipeClick(item, sharedView);
                }
            };

    public RecipeListFragment() {
        setRetainInstance(true);
    }

    public static Fragment newInstance() {
        return new RecipeListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeListFragment.Callback) {
            callback = (RecipeListFragment.Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RecipeListFragment.Callback");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("RecipeListFragment", "onCreate");
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);

        layoutManager = new GridLayoutManager(getContext(), 2);
        binding.itemsRecyclerView.setLayoutManager(layoutManager);
        binding.itemsRecyclerView.setAdapter(adapter);
        binding.itemsRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        binding.swipeRefreshLayout.setEnabled(true);
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadItems();
            }
        });

        adapter.setRecipeClickListener(recipeClickListener);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        Log.d("RecipeListFragment", "onViewCreated");
        if (savedInstanceState == null) {
            Log.d("RecipeListFragment", "onViewCreated savedInstanceState == null");
            presenter.loadItems();
        }
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        presenter.onDestroyed();
        super.onDestroy();
    }

    @Override
    public void showLoadingIndicator() {
        binding.progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        hideRefreshingIndicator();
        binding.progressBar.setVisibility(GONE);
    }

    private void hideRefreshingIndicator() {
        if (binding.swipeRefreshLayout.isRefreshing()) {
            binding.swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showItems(List<Recipe> items) {
        adapter.addItems(items);
    }

    @Override
    public void clearItems() {
        adapter.clear();
    }

    @Override
    public void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    public interface Callback {
        void onRecipeClick(Recipe recipe, View view);
    }
}
