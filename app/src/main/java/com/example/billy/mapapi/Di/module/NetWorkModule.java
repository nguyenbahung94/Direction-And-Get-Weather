package com.example.billy.mapapi.Di.module;

import android.content.Context;

import com.example.billy.mapapi.Di.component.Direction;
import com.example.billy.mapapi.Di.component.Place;
import com.example.billy.mapapi.Di.component.Weather;
import com.example.billy.mapapi.utils.AppConstants;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nbhung on 6/30/2017.
 */
@Module
public class NetWorkModule {
    Context context;

    public NetWorkModule(Context context) {
        this.context = context;
    }

    @Provides
    Cache provCache() {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    Gson provGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    OkHttpClient provOkHttpClient(Cache cache) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.cache(cache);
        return client.build();
    }

    @Provides
    @Direction
    Retrofit provRetrofitDirection(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(AppConstants.URL_DIRECTION)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Weather
    Retrofit provRetrofitWeather(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(AppConstants.URL_WEATHER)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Place
    Retrofit proRetrofitPlace(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(AppConstants.URL_DIRECTION)
                .client(okHttpClient)
                .build();

    }

}
