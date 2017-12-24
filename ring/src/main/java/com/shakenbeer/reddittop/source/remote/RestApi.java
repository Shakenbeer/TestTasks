package com.shakenbeer.reddittop.source.remote;


import com.shakenbeer.reddittop.model.Top;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApi {

    @GET("/.json")
    Flowable<Top> loadMore(@Query("limit") int limit, @Query("after") String after);
}
