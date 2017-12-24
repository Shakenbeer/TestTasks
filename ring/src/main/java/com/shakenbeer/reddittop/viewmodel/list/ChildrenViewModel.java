package com.shakenbeer.reddittop.viewmodel.list;


import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.shakenbeer.reddittop.injection.ApplicationComponent;
import com.shakenbeer.reddittop.model.ChildData;
import com.shakenbeer.reddittop.model.Envelope;
import com.shakenbeer.reddittop.repo.RedditRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChildrenViewModel extends ViewModel implements ApplicationComponent.Injectable {

    private final List<ChildData> quickCache = new ArrayList<>();

    @Inject
    @VisibleForTesting
    RedditRepository redditRepository;

    @Override
    public void inject(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public Flowable<Envelope> loadChildren(boolean reload) {
        return redditRepository.getMoreItems(reload)
                .doOnNext(envelope -> {
                    if (reload) {
                        clearQuickCache();
                    }
                    quickCache.addAll(envelope.getChildren());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @VisibleForTesting
    void clearQuickCache() {
        quickCache.clear();
    }

    public Single<List<ChildData>> loadCache() {
        return Single.just(quickCache)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
