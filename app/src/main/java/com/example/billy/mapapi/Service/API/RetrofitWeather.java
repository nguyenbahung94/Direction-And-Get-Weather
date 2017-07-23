package com.example.billy.mapapi.Service.API;


import com.example.billy.mapapi.Ui.Model.InforWeather;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by nbhung on 6/23/2017.
 */

public interface RetrofitWeather {
    @GET("data/2.5/weather")
    Observable<InforWeather> getWeather(@Query("lat") String lat, @Query("lon") String longt, @Query("units") String units, @Query("appid") String tam);

}

