package com.shakenbeer.reddittop.injection;


import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.shakenbeer.reddittop.repo.RedditRepository;
import com.shakenbeer.reddittop.repo.RedditRepositoryImpl;
import com.shakenbeer.reddittop.source.local.RedditDatabase;
import com.shakenbeer.reddittop.source.remote.RestApi;
import com.shakenbeer.reddittop.util.ContextUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {

    private static final String ENDPOINT = "https://www.reddit.com/top/";
    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    public RestApi providesRestApi() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(RestApi.class);
    }

    @Provides
    @Singleton
    public RedditDatabase provideRedditDatabase() {
        return Room.databaseBuilder(application.getApplicationContext(),
                RedditDatabase.class, "reddit.db").build();
    }

    @Provides
    @Singleton
    public RedditRepository provideRedditRepository(RestApi restApi, RedditDatabase redditDatabase,
                                                    ContextUtils contextUtils) {
        return new RedditRepositoryImpl(restApi, redditDatabase, contextUtils);
    }
}
