package com.shakenbeer.reddittop.repo;


import android.support.annotation.VisibleForTesting;

import com.shakenbeer.reddittop.model.Child;
import com.shakenbeer.reddittop.model.ChildData;
import com.shakenbeer.reddittop.model.Envelope;
import com.shakenbeer.reddittop.model.ImageData;
import com.shakenbeer.reddittop.source.local.RedditDatabase;
import com.shakenbeer.reddittop.source.remote.RestApi;
import com.shakenbeer.reddittop.util.ContextUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class RedditRepositoryImpl implements RedditRepository {

    public static final int LIMIT = 10;
    public static final int PAGES = 5;
    public static final String YOU_NEED_ONLY_TOP_50 = "You need only top 50!";
    public static final String NO_NETWORK_SHOW_CACHED_DATA = "No network, show cached data";
    private final RestApi restApi;
    private final RedditDatabase database;
    private final ContextUtils contextUtils;

    @VisibleForTesting
    volatile String after = null;
    @VisibleForTesting
    volatile int page = 0;

    public RedditRepositoryImpl(RestApi restApi, RedditDatabase database, ContextUtils contextUtils) {
        this.restApi = restApi;
        this.database = database;
        this.contextUtils = contextUtils;
    }

    @Override
    public Flowable<Envelope> getMoreItems(boolean forceReload) {
        if (forceReload) {
            resetPage();
        }
        if (page == PAGES) {
            return Flowable.just(new Envelope(true, YOU_NEED_ONLY_TOP_50, Collections.emptyList()));
        }
        if (contextUtils.isNetworkAvailable()) {
            return restApi.loadMore(LIMIT, after)
                    .doOnNext(top -> after = top.getData().getAfter())
                    .map(top -> top.getData().getChildren())
                    .flatMap(children -> Flowable.fromIterable(children)
                            .map(Child::getData)
                            .doOnNext(this::processChildData)
                            .toList()
                            .doOnSuccess(this::saveToDb)
                            .toFlowable()
                            .map(childDatas -> new Envelope(false, childDatas))
                            .doOnNext(envelope -> page++));
        } else {
            return Flowable.fromCallable(() -> {
                List<ChildData> children = database.childDao().getChildren(page);
                return new Envelope(true, NO_NETWORK_SHOW_CACHED_DATA, children);
            })
                    .doOnNext(envelope -> page++);

        }
    }

    @VisibleForTesting
    void resetPage() {
        after = null;
        page = 0;
    }

    @VisibleForTesting
    void processChildData(ChildData childData) {
        childData.setPage(page);
        if (childData.getPreview() != null &&
                childData.getPreview().getImages() != null &&
                childData.getPreview().getImages().length > 0) {
            ImageData imageData = childData.getPreview().getImages()[0];
            if (imageData.getSource() != null && imageData.getSource().getUrl() != null) {
                childData.setImage(imageData.getSource().getUrl());
            }
        }
    }

    @VisibleForTesting
    void saveToDb(List<ChildData> children) {
        database.childDao().cleanPage(page);
        database.childDao().addChildren(children);
    }
}
