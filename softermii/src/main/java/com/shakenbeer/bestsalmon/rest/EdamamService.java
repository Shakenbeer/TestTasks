package com.shakenbeer.bestsalmon.rest;

import com.shakenbeer.bestsalmon.model.Edamam;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface EdamamService {
    String SALMON = "salmon";
    String ENDPOINT = "https://api.edamam.com/";
    String APPLICATION_ID = "028d0f66";
    String APPLICATION_KEYS = "a9651907fc1dff90c06ec63a6825908d";

    @GET("search")
    Observable<Edamam> getRecipes(@Query("from") int from, @Query("to") int to);
}
