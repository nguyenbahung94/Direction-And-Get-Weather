package com.example.billy.mapapi.Ui.Interactor;

import android.content.Context;
import android.location.Location;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Retrofit;

/**
 * Created by nbhung on 6/29/2017.
 */

public interface Interactor {
    void moveLocation(Location mLocation, GoogleMap mMap);

    void getDirection(Retrofit retrofit, LatLng latOrigin, LatLng latEnd, GoogleMap mMap, LoginCallback call, TextView tvDistance, TextView tvDuration);

    void getWeatherCurrent(Retrofit retrofit, LatLng myLocation, LoginCallback call);

    void getPlaceAround( Retrofit retrofitPlace, GoogleMap map, LoginCallback callback, LatLng latLng, String stype, String radius, Context context);

    interface LoginCallback {
        void onfailed(String error);

        void onSuccess(String Success);

        void onSuccessWeather(String success);
    }

}
