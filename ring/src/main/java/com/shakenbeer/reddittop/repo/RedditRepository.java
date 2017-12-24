package com.shakenbeer.reddittop.repo;


import com.shakenbeer.reddittop.model.Envelope;

import io.reactivex.Flowable;


public interface RedditRepository {

    Flowable<Envelope> getMoreItems(boolean reload);
}
