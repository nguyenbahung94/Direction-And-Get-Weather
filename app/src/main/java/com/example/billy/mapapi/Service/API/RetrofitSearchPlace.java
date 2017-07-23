package com.example.billy.mapapi.Service.API;

import com.example.billy.mapapi.Ui.Model.ResultsSearchPlace;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Billy on 7/22/2017.
 */

public interface RetrofitSearchPlace {
    @GET("maps/api/place/nearbysearch/json?")
    Observable<ResultsSearchPlace> getPlace(@Query("location") String location, @Query("radius") String radius, @Query("type") String type, @Query("key") String key);
}
