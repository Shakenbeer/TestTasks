package com.shakenbeer.bestsalmon.rest;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@SuppressWarnings("SameParameterValue")
public class AuthenticationInterceptor implements Interceptor {

    private final String applicationId;
    private final String applicationKey;
    private final String query;

    @SuppressWarnings("SameParameterValue")
    public AuthenticationInterceptor(String applicationId, String applicationKey, String query) {
        this.applicationId = applicationId;
        this.applicationKey = applicationKey;
        this.query = query;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();

        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter("app_id", applicationId)
                .addQueryParameter("app_key", applicationKey)
                .addQueryParameter("q", query)
                .build();

        // Request customization: add request headers
        Request.Builder requestBuilder = original.newBuilder()
                .url(url);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
