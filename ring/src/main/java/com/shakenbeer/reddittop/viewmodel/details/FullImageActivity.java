package com.shakenbeer.reddittop.viewmodel.details;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.shakenbeer.reddittop.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FullImageActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 3259;
    private static final String THUMBNAIL_EXTRA = "com.shakenbeer.reddittop.viewmodel.details.FullImageActivity.thumbnail";
    private static final String FULL_SIZE_EXTRA = "com.shakenbeer.reddittop.viewmodel.details.FullImageActivity.fullSize";
    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private ProgressBar progressBar;
    private ImageView imageView;
    private String thumbnail;
    private String fullSize;
    private FullImageViewModel fullImageViewModel;
    private Disposable disposable;
    private MenuItem download;
    private volatile boolean isFullSizeEnabled = false;

    public static void start(Context context, String thumbnail, String fullSize) {
        Intent starter = new Intent(context, FullImageActivity.class);
        starter.putExtra(THUMBNAIL_EXTRA, thumbnail);
        starter.putExtra(FULL_SIZE_EXTRA, fullSize);
        context.startActivity(starter);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);
        thumbnail = getIntent().getStringExtra(THUMBNAIL_EXTRA);
        fullSize = getIntent().getStringExtra(FULL_SIZE_EXTRA);

        fullImageViewModel = ViewModelProviders.of(this).get(FullImageViewModel.class);

        disposable = fullImageViewModel.getDownloadingObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(path -> {
                    hideLoading();
                    showImageLoadingSuccess(path);
                }, throwable -> {
                    hideLoading();
                    showImageLoadingError(throwable);
                });

        loadFullSize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_download, menu);
        download = menu.findItem(R.id.download);
        download.setVisible(isFullSizeEnabled);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.download) {
            if (ensureWritePermission()) {
                loadImage();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }

    private void loadFullSize() {
        showLoading();

        RequestOptions requestOptions = new RequestOptions()
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .dontTransform()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(this).load(fullSize).listener(new RequestListener<Drawable>() {

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                updateMenu(false);
                hideLoading();
                showLoadingError();
                loadThumbnail();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                updateMenu(true);
                hideLoading();
                return false;
            }
        }).apply(requestOptions).into(imageView);
    }

    private void updateMenu(boolean enabled) {
        isFullSizeEnabled = enabled;
        if (download != null) {
            download.setVisible(enabled);
        }
    }

    private void loadThumbnail() {
        RequestOptions requestOptions = new RequestOptions()
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .dontTransform()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(this).load(thumbnail).apply(requestOptions).into(imageView);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showLoadingError() {
        Toast.makeText(this, "Error occurred while loading full-size image.", Toast.LENGTH_SHORT).show();
    }


    private void showImageLoadingSuccess(String path) {
        Toast.makeText(this, "Image loaded to " + path, Toast.LENGTH_SHORT).show();
    }

    private void showImageLoadingError(Throwable throwable) {
        Toast.makeText(this, "Error: " + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    private boolean ensureWritePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                new AlertDialog.Builder(this)
                        .setTitle(R.string.write_external_storage_request_title)
                        .setMessage(R.string.write_external_storage_request_rationale)
                        .setPositiveButton(R.string.permission_button_accept, (dialog1, which) ->
                                ActivityCompat.requestPermissions(FullImageActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE))
                        .setNegativeButton(R.string.permission_button_deny, null)
                        .show();


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImage();
                }
            }
        }
    }

    private void loadImage() {
        showLoading();
        fullImageViewModel.downloadImage(fullSize);
    }
}
