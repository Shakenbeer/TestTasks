package com.shakenbeer.reddittop.viewmodel.details;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import io.reactivex.subjects.PublishSubject;

public class FullImageViewModel extends AndroidViewModel {

    private PublishSubject<String> downloadingObservable;

    public FullImageViewModel(Application application) {
        super(application);
        downloadingObservable = PublishSubject.create();
    }

    public PublishSubject<String> getDownloadingObservable() {
        return downloadingObservable;
    }

    public void downloadImage(String fullSize) {
        Glide.with(getApplication())
                .asBitmap()
                .load(fullSize)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        saveImage(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        downloadingObservable.onError(new Throwable("Loading error"));
                        super.onLoadFailed(errorDrawable);
                    }
                });

    }

    private void saveImage(Bitmap bitmap) {
        String fileName = UUID.randomUUID().toString() + ".jpeg";
        File imagesFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/" + "Top50ForReddit");
        boolean success = true;
        if (!imagesFolder.exists()) {
            success = imagesFolder.mkdirs();
        }
        if (success) {
            File imageFile = new File(imagesFolder, fileName);
            try {
                OutputStream out = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                scanFile(imageFile);
                downloadingObservable.onNext(imageFile.getParent());
                out.close();
            } catch (Exception e) {
                downloadingObservable.onError(e);
            }
        } else {
            downloadingObservable.onError(new Throwable("Unable to create folder"));
        }
    }

    private void scanFile(File imageFile) {
        MediaScannerConnection.scanFile(getApplication(),
                new String[]{imageFile.getAbsolutePath()},
                new String[]{"image/jpeg"},
                null);
    }
}
