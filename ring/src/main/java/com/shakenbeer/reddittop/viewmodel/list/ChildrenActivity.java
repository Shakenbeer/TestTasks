package com.shakenbeer.reddittop.viewmodel.list;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.shakenbeer.reddittop.R;
import com.shakenbeer.reddittop.TopApplication;
import com.shakenbeer.reddittop.databinding.ActivityChildrenBinding;
import com.shakenbeer.reddittop.injection.ViewModelFactory;
import com.shakenbeer.reddittop.model.ChildData;
import com.shakenbeer.reddittop.model.Envelope;
import com.shakenbeer.reddittop.ui.EmptyObserver;
import com.shakenbeer.reddittop.ui.EndlessRecyclerViewScrollListener;
import com.shakenbeer.reddittop.viewmodel.details.FullImageActivity;

import io.reactivex.disposables.CompositeDisposable;

public class ChildrenActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    private static final String LIST_STATE_KEY = "LIST_STATE";
    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private ActivityChildrenBinding binding;
    private ChildrenAdapter adapter;
    private ChildrenViewModel childrenViewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private EmptyObserver observer;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_children);
        initRecycledView();
        initViewModel(savedInstanceState);
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            endlessRecyclerViewScrollListener.resetState();
            reloadData();
        });
    }

    @Override
    protected void onDestroy() {
        adapter.unregisterAdapterDataObserver(observer);
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveListState(outState);
    }

    private void saveListState(Bundle outState) {
        Parcelable listState = binding.recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, listState);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

    }

    private void initRecycledView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                showLoading();
                compositeDisposable.add(childrenViewModel.loadChildren(false)
                        .subscribe(
                                envelope -> {
                                    hideLoading();
                                    processEnvelope(envelope);
                                },
                                throwable -> {
                                    hideLoading();
                                    showError(throwable.getLocalizedMessage());
                                }));
            }
        };
        binding.recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
        binding.recyclerView.setSaveEnabled(true);
        adapter = new ChildrenAdapter();
        binding.recyclerView.setAdapter(adapter);
        observer = new EmptyObserver(this::processVisibility, adapter);
        adapter.registerAdapterDataObserver(observer);
        adapter.setThumbnailListener(this::onThumbnailClick);
        processVisibility(adapter.getItemCount() == 0);
    }

    private void processEnvelope(Envelope envelope) {
        adapter.addItems(envelope.getChildren());
        if (envelope.isNotify()) {
            showNotification(envelope.getMessage());
        }
    }

    private void showNotification(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void initViewModel(Bundle savedInstanceState) {
        childrenViewModel = ViewModelProviders
                .of(this, new ViewModelFactory(TopApplication.get(this)))
                .get(ChildrenViewModel.class);

        if (savedInstanceState == null) {
            reloadData();
        } else {
            loadCache(savedInstanceState);
        }
    }

    private void reloadData() {
        showLoading();
        compositeDisposable.add(childrenViewModel.loadChildren(true)
                .subscribe(
                        envelope -> {
                            hideRefreshing();
                            hideLoading();
                            adapter.clear();
                            processEnvelope(envelope);
                        }, throwable -> {
                            hideRefreshing();
                            hideLoading();
                            showError(throwable.getLocalizedMessage());
                        }));
    }

    private void loadCache(Bundle savedInstanceState) {
        final Parcelable listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        compositeDisposable.add(childrenViewModel.loadCache().subscribe(
                children -> {
                    adapter.clear();
                    adapter.addItems(children);
                    binding.recyclerView.getLayoutManager().onRestoreInstanceState(listState);
                }, throwable -> showError(throwable.getLocalizedMessage())));
    }

    private void processVisibility(boolean isEmpty) {
        if (isEmpty) {
            binding.recyclerView.setVisibility(View.INVISIBLE);
            binding.emptyView.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.emptyView.setVisibility(View.INVISIBLE);
        }
    }

    private void showError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }

    private void hideRefreshing() {
        if (binding.swipeRefreshLayout.isRefreshing()) {
            binding.swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void onThumbnailClick(ChildData childData) {
        if (childData.getImage() != null) {
            FullImageActivity.start(this, childData.getThumbnail(), childData.getImage());
        }
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
    }

}
