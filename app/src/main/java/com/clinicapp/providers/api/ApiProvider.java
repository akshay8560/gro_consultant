package com.clinicapp.providers.api;

import android.app.Application;

import com.clinicapp.BuildConfig;
import com.clinicapp.providers.prefs.PrefsProvider;
import com.squareup.moshi.Moshi;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiProvider {

    public static ApiProvider INSTANCE;
    public final ApiResource resource;
    public final OkHttpClient client;

    public static ApiProvider getInstance(Application app){
        synchronized (ApiProvider.class){
            if (INSTANCE == null) {
                INSTANCE = new ApiProvider(app);
            }
        }
        return INSTANCE;
    }



    private ApiProvider(Application application) {
        //Initialize OkHttpClient
        final long cacheSize = 20 * 1024 * 1024; //20mb cache
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        client = new OkHttpClient.Builder()
                .cache(cache)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    final Request.Builder requestBuilder = chain.request().newBuilder();
                    final String token = PrefsProvider.getInstance(application).getToken();
                    if(token.length() > 0)
                        requestBuilder.addHeader("Authorization", "Bearer " + token);
                    return chain.proceed(requestBuilder.build());
                })
                .build();


        final Moshi moshi = new Moshi.Builder()
                .build();

        final Retrofit rf = new Retrofit.Builder()
                .client(client)
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
                .build();

        resource =rf.create(ApiResource.class);

    }

}
