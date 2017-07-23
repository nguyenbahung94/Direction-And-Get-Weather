package com.example.billy.mapapi.Ui.Presenter;

import android.content.Context;
import android.location.Location;
import android.widget.TextView;

import com.example.billy.mapapi.Ui.Interactor.Interactor;
import com.example.billy.mapapi.Ui.Interactor.InteractorImp;
import com.example.billy.mapapi.Ui.View.MainView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by nbhung on 6/29/2017.
 */

public class PresenterImp implements Presenter, Interactor.LoginCallback {
    private InteractorImp interactorImp;
    private MainView view;

    @Inject
    public PresenterImp(MainView view, InteractorImp interactorImp) {
        this.view = view;
        this.interactorImp = interactorImp;
    }

    @Override
    public void getLocation(Location myLocation, GoogleMap mMap) {
        interactorImp.moveLocation(myLocation, mMap);
    }

    @Override
    public void direction(Retrofit retrofit, LatLng latOrigin, LatLng latEnd, GoogleMap mMap, TextView tvDistance, TextView tvDuration) {
        interactorImp.getDirection(retrofit, latOrigin, latEnd, mMap, this, tvDistance, tvDuration);
    }

    @Override
    public void getWeather(Retrofit retrofit, LatLng latLng) {
        interactorImp.getWeatherCurrent(retrofit, latLng, this);
    }

    @Override
    public void getPlaceAround( Retrofit retrofitPlace, GoogleMap mMap, LatLng latLng, String type, String radius, Context context) {
        interactorImp.getPlaceAround(retrofitPlace, mMap, this, latLng, type, radius, context);
    }


    @Override
    public void onfailed(String error) {
        view.showError(error);
    }

    @Override
    public void onSuccess(String Success) {
        if (Success.equals("place")) {
            view.showPlace(0);
        }
        if (Success.equals("direction")) {
            view.showMessage(Success);
        }
        if (Success.equals("empty")){
            view.showPlace(1);
        }
    }

    @Override
    public void onSuccessWeather(String success) {
        view.showWeather(success);
    }
}
