package com.shakenbeer.bestsalmon.injection.module;


import com.shakenbeer.bestsalmon.rest.AuthenticationInterceptor;
import com.shakenbeer.bestsalmon.rest.EdamamService;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("UnusedParameters")
@Module
public class ApiModule {

    @Provides
    EdamamService provideService(Retrofit retrofit) {
        return retrofit.create(EdamamService.class);
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {

        return new Retrofit.Builder()
                .baseUrl(EdamamService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    OkHttpClient provideClient(HttpLoggingInterceptor interceptor,
                               AuthenticationInterceptor authenticationInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(authenticationInterceptor)
                .build();
    }

    @Provides
    HttpLoggingInterceptor provideInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    AuthenticationInterceptor provideAuthenticationInterceptor() {
        return new AuthenticationInterceptor(EdamamService.APPLICATION_ID,
                EdamamService.APPLICATION_KEYS, EdamamService.SALMON);
    }
}
