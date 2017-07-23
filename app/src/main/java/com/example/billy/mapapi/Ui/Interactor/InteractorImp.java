package com.example.billy.mapapi.Ui.Interactor;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import com.example.billy.mapapi.R;
import com.example.billy.mapapi.Service.API.RetrofitMap;
import com.example.billy.mapapi.Service.API.RetrofitSearchPlace;
import com.example.billy.mapapi.Service.API.RetrofitWeather;
import com.example.billy.mapapi.Ui.Model.InforWeather;
import com.example.billy.mapapi.Ui.Model.MyRouter;
import com.example.billy.mapapi.Ui.Model.Results;
import com.example.billy.mapapi.Ui.Model.ResultsSearchPlace;
import com.example.billy.mapapi.Ui.Model.Routes;
import com.example.billy.mapapi.Ui.Model.leg;
import com.example.billy.mapapi.Ui.Model.mainwt;
import com.example.billy.mapapi.Ui.Model.overview_polylineTam;
import com.example.billy.mapapi.utils.AppConstants;
import com.example.billy.mapapi.utils.DecodePollyLine;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nbhung on 6/29/2017.
 */
public class InteractorImp implements Interactor {
    @Override
    public void moveLocation(Location mLocation, GoogleMap mMap) {
        Log.e("I'm here", "in the Interactor ,hello there");
        if (mLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.854039, 106.628387), 15));
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.melocation)).position(new LatLng(10.854039, 106.628387)));
        }

    }

    @Override
    public void getDirection(Retrofit retrofit, final LatLng latOrigin, final LatLng latEnd, final GoogleMap mMap, final LoginCallback call, final TextView tvDistance, final TextView tvDuration) {
        retrofit.create(RetrofitMap.class).getDirections(latOrigin.latitude + "," + latOrigin.longitude, latEnd.latitude + "," + latEnd.longitude, AppConstants.GOOGLE_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Routes>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException || e instanceof SocketTimeoutException || e instanceof IOException) {
                            call.onfailed("direction error ");
                        }


                    }

                    @Override
                    public void onNext(Routes routes) {
                        call.onSuccess("direction");
                        List<MyRouter> tam = routes.getRoutes();
                        directionOnMap(tam, mMap, tvDistance, tvDuration);
                    }
                });
    }

    @Override
    public void getWeatherCurrent(Retrofit retrofit, LatLng myLocation, final LoginCallback call) {
        retrofit.create(RetrofitWeather.class).getWeather(String.valueOf(myLocation.latitude), String.valueOf(myLocation.longitude), "metric", AppConstants.WEARTHER_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<InforWeather>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException || e instanceof SocketTimeoutException || e instanceof IOException) {
                            call.onfailed("get weather error");
                        }

                    }

                    @Override
                    public void onNext(InforWeather inforWeather) {
                        mainwt mainwt = inforWeather.getObmain();
                        Log.e("mainwt", mainwt.getPressure() + "/" + mainwt.getHumidity() + "/" + mainwt.getTemp());
                        call.onSuccessWeather("Nhiệt độ là :" + mainwt.getTemp() + "°C");

                    }
                });
    }

    @Override
    public void getPlaceAround(Retrofit retrofitPlace, final GoogleMap map, final LoginCallback callback, LatLng latLng, String type, String radius, final Context context) {
        String location = latLng.latitude + "," + latLng.longitude;
        retrofitPlace.create(RetrofitSearchPlace.class).getPlace(location, radius + "000", type, AppConstants.GOOGLE_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultsSearchPlace>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException || e instanceof SocketTimeoutException || e instanceof IOException) {
                            callback.onfailed("get place around error");
                        }
                    }

                    @Override
                    public void onNext(ResultsSearchPlace resultsSearchPlace) {
                        callback.onSuccess("place");
                        List<Results> listReuslt = resultsSearchPlace.getResults();
                        if (listReuslt.size() == 0) {
                            callback.onSuccess("empty");
                        } else {
                            searchPlace(listReuslt, map, context);
                        }

                    }
                });
    }

    private void searchPlace(List<Results> list, GoogleMap map, Context context) {
        for (int i = 0; i < list.size(); i++) {
            Results resultsTam = list.get(i);
            addPlace(resultsTam, map, context);
        }
    }

    private void addPlace(final Results results, final GoogleMap map, Context context) {
        final String address = getAddress(new LatLng(results.geometry.getLocation().getLat(), results.geometry.getLocation().getLng()), context);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(results.geometry.getLocation().getLat(), results.geometry.getLocation().getLng()))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.newloca))
                .title(results.getName())
                .snippet(address);
        map.addMarker(markerOptions);

    }

    private static String getAddress(LatLng latLng, Context context) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            Address returnedAddress = addresses.get(0);
            StringBuilder strReturnedAddress = new StringBuilder("");

            for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                strReturnedAddress.append(returnedAddress.getAddressLine(i));
            }
            strAdd = strReturnedAddress.toString();
        }
        return strAdd;
    }

    private void directionOnMap(List<MyRouter> tam2, GoogleMap mMap, TextView tvDistance, TextView tvDuration) {
        overview_polylineTam tamss = tam2.get(0).getStrline();
        List<LatLng> latLngs = DecodePollyLine.decodePollyLine(tamss.getPoints());
        leg leg = tam2.get(0).getLegs().get(0);
        Log.e("point", tamss.getPoints());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(leg.getStartLocation().getLat(), leg.getStartLocation().getLng()), 15));
        tvDuration.setText(leg.getDuration().getText());
        tvDistance.setText(leg.getDistance().getText());


        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.place)).title(leg.getStartAdd()).position(new LatLng(leg.getStartLocation().getLat(), leg.getStartLocation().getLng())));
        mMap.addMarker((new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.place)).title(leg.getEndAdd()).position(new LatLng(leg.getEndLocation().getLat(), leg.getEndLocation().getLng()))));

        PolylineOptions polylineOptions = new PolylineOptions().geodesic(true).color(Color.GRAY).width(5);
        for (int i = 0; i < latLngs.size(); i++) {
            polylineOptions.add(latLngs.get(i));
        }
        mMap.addPolyline(polylineOptions);
    }

}
