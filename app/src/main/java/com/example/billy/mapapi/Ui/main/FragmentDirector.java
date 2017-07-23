package com.example.billy.mapapi.Ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billy.mapapi.Di.component.DaggerFragmentComponent;
import com.example.billy.mapapi.Di.component.Direction;
import com.example.billy.mapapi.Di.component.Weather;
import com.example.billy.mapapi.Di.module.ActivityModule;
import com.example.billy.mapapi.Di.module.NetWorkModule;
import com.example.billy.mapapi.R;
import com.example.billy.mapapi.Ui.Presenter.PresenterImp;
import com.example.billy.mapapi.Ui.View.MainView;
import com.example.billy.mapapi.Ui.View.navigation.FragmentDrawerListener;
import com.example.billy.mapapi.utils.NetWorkUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;


public class FragmentDirector extends Fragment implements OnMapReadyCallback, MainView, GoogleMap.OnMapLongClickListener, FragmentDrawerListener {
    @Inject
    PresenterImp mPrisenter;
    @Inject
    @Direction
    Retrofit retrofitDerection;
    @Inject
    @Weather
    Retrofit retrofitWeather;
    @Inject
    @com.example.billy.mapapi.Di.component.Place
    Retrofit retrofitPlace;
    private LocationManager locationManager;
    private Location mlocation;
    private Button btn_find, btnGetWeather, btnSearchPlace, btnSwitchDirection;
    private boolean booleanOrigin = false, booleanDestination = false, boolearnGetPlace = false, gps = false, isBooleanGetWeather = false, isBooleanShowAndHide = false;
    private TextView edtDestination, edtOrigin, tvLocationWeather, tvDistance, tvDuration;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODES = 200;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE_WEATHER = 300;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE_SEARCHPLACE = 400;
    private LatLng latLngor, latLnged, latLngWeather, latLngSearchPlace;
    private View view, viewWearther, viewDirection, viewSearchPlace;
    private GoogleMap mMap;
    private LocationListener locationListener;
    private MarkerOptions markerOptions;
    private TextView tvShowweather, tvSearchplace;
    private List<String> listType, listDistance;
    private Spinner placeSpiner, spinerDistance;
    private String stringTypeOfplace, stringDistanceToFind;
    private ProgressDialog progressDialog;
    private Animation aniShow, aniHide;
    private RelativeLayout rllContain;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerFragmentComponent.builder().activityModule(new ActivityModule(this))
                .netWorkModule(new NetWorkModule(getContext())).build().inject(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_directions, container, false);
        init();
        return view;
    }

    public void init() {
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        edtDestination = (TextView) view.findViewById(R.id.edtDestination);
        edtOrigin = (TextView) view.findViewById(R.id.edtOrigin);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        edtDestination = (TextView) view.findViewById(R.id.edtDestination);
        btn_find = (Button) view.findViewById(R.id.btnFind);
        tvDistance = (TextView) view.findViewById(R.id.tvDistance);
        tvDuration = (TextView) view.findViewById(R.id.tvDuration);
        viewDirection = view.findViewById(R.id.layout_linear);
        rllContain = (RelativeLayout) view.findViewById(R.id.rll_content);

        //weather
        viewWearther = view.findViewById(R.id.layout_partof_wearther);
        btnGetWeather = (Button) viewWearther.findViewById(R.id.btn_getwearther);
        tvLocationWeather = (TextView) viewWearther.findViewById(R.id.tv_location_getwearther);
        tvShowweather = (TextView) viewWearther.findViewById(R.id.tv_showtemp);


        //place
        viewSearchPlace = view.findViewById(R.id.layout_partof_place);
        placeSpiner = (Spinner) viewSearchPlace.findViewById(R.id.spin_typeplace);
        spinerDistance = (Spinner) viewSearchPlace.findViewById(R.id.spin_distance);
        btnSearchPlace = (Button) viewSearchPlace.findViewById(R.id.btn_searchplace);
        tvSearchplace = (TextView) viewSearchPlace.findViewById(R.id.tv_searchplace);
        btnSwitchDirection = (Button) viewSearchPlace.findViewById(R.id.btn_switchdirection);

        //anim
        aniHide = AnimationUtils.loadAnimation(getActivity(), R.anim.viewhide);
        aniShow = AnimationUtils.loadAnimation(getActivity(), R.anim.viewshow);


        markerOptions = new MarkerOptions();


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        eventDirection();
        evenWeather();
        eventSearchPlace();
    }

    public void eventDirection() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocation();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        edtOrigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                booleanOrigin = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
        edtDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                booleanDestination = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODES);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (booleanOrigin && booleanDestination) {
                    mMap.clear();
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    if (edtOrigin.getText().toString().equalsIgnoreCase("YOUR LOCATION!")) {
                        mPrisenter.direction(retrofitDerection, new LatLng(10.854039, 106.628387), latLnged, mMap, tvDistance, tvDuration);
                    } else {
                        mPrisenter.direction(retrofitDerection, latLngor, latLnged, mMap, tvDistance, tvDuration);
                    }

                } else {
                    showDialog("Error", "Destination and Origin not empty!");
                }
            }
        });
    }

    public void evenWeather() {
        btnGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBooleanGetWeather) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngWeather, 15));
                    mPrisenter.getWeather(retrofitWeather, latLngWeather);
                } else {
                    showError("Location can not be empty!");
                }

            }
        });
        tvLocationWeather.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isBooleanGetWeather = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvLocationWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                try {
                    intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_WEATHER);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void checkGps() {
        gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gps) {
            showDialog("Gps not enable", "you need to enable gps");
        }
    }

    public void eventSearchPlace() {
        addMapTypePlace();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        placeSpiner.setAdapter(arrayAdapter);
        placeSpiner.setId(0);
        placeSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                stringTypeOfplace = listType.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> arrayAdapteDistance = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listDistance);
        arrayAdapteDistance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerDistance.setAdapter(arrayAdapteDistance);
        spinerDistance.setId(0);
        spinerDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                stringDistanceToFind = listDistance.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnSearchPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boolearnGetPlace) {
                    mMap.clear();
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Loading....");
                    progressDialog.show();
                    addMaker(latLngSearchPlace);

                    mPrisenter.getPlaceAround(retrofitPlace, mMap, latLngSearchPlace, stringTypeOfplace, stringDistanceToFind, getActivity());
                } else {
                    showDialog("note", "Location can not be empty!");
                }

            }
        });
        tvSearchplace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolearnGetPlace = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvSearchplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                try {
                    intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_SEARCHPLACE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        btnSwitchDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDirection.setVisibility(View.VISIBLE);
                viewDirection.startAnimation(aniShow);
                viewSearchPlace.setVisibility(View.GONE);
                viewSearchPlace.startAnimation(aniHide);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                edtOrigin.setText(place.getAddress());
                latLngor = place.getLatLng();
                latLngSearchPlace = place.getLatLng();
                tvSearchplace.setText(place.getAddress());
            }
            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODES) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                edtDestination.setText(place.getAddress());
                latLnged = place.getLatLng();
                tvLocationWeather.setText(place.getAddress());
                latLngWeather = place.getLatLng();
            }
            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_WEATHER) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                tvLocationWeather.setText(place.getAddress());
                markerOptions.title(place.getAddress().toString());
                latLngWeather = place.getLatLng();
                addMaker(place.getLatLng());
            }
            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_SEARCHPLACE) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                tvSearchplace.setText(place.getAddress());
                latLngSearchPlace = place.getLatLng();
                addMaker(place.getLatLng());
            }
        }
    }

    public void addMaker(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.place));
        markerOptions.position(latLng);
        mMap.addMarker(markerOptions);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        Log.e("ok i'm here", "say hi");
        if (NetWorkUtils.isNetWorkConnected(getActivity())) {
            checkGps();
            updateLocation();
        } else {
            Toast.makeText(getActivity(), "No Network To Connect", Toast.LENGTH_SHORT).show();
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                edtDestination.setText(marker.getSnippet());
                latLnged = marker.getPosition();
                return false;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetWorkUtils.isNetWorkConnected(getActivity())) {
            checkGps();
            updateLocation();
        } else {
            Toast.makeText(getActivity(), "No Network To Connect", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mPrisenter.getLocation(mlocation, mMap);
    }

    @Override
    public void showMessage(String message) {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String error) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            showDialog("error", error);
        }

    }

    @Override
    public void showWeather(String temp) {
        tvShowweather.setVisibility(View.VISIBLE);
        tvShowweather.setText(temp);
    }

    @Override
    public void showPlace(int number) {
        progressDialog.dismiss();
        if (number == 0) {
            Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
        }
        if (number == 1) {
            Toast.makeText(getContext(), "There is no " + stringTypeOfplace + " within " + stringDistanceToFind + "km radius", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void showDialog(String title, String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onMapLongClick(LatLng latLng) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

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
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.place));
        markerOptions.title(strAdd);
        markerOptions.position(latLng);
        mMap.addMarker(markerOptions);
        latLngWeather = latLng;
        latLnged = latLng;
        edtDestination.setText(strAdd);
        tvLocationWeather.setText(strAdd);
        tvSearchplace.setText(strAdd);
        latLngSearchPlace = latLng;
        Toast.makeText(getActivity(), strAdd, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        if (position == 0) {
            viewWearther.setVisibility(View.GONE);
            viewWearther.startAnimation(aniHide);
            viewDirection.setVisibility(View.VISIBLE);
            viewDirection.startAnimation(aniShow);
            viewSearchPlace.setVisibility(View.GONE);
            viewSearchPlace.startAnimation(aniHide);
        }
        if (position == 1) {
            viewDirection.setVisibility(View.GONE);
            viewDirection.startAnimation(aniHide);
            viewWearther.setVisibility(View.VISIBLE);
            viewWearther.startAnimation(aniShow);
            viewSearchPlace.setVisibility(View.GONE);
            viewSearchPlace.startAnimation(aniHide);
        }
        if (position == 2) {
            viewSearchPlace.setVisibility(View.VISIBLE);
            viewSearchPlace.startAnimation(aniShow);
            viewWearther.setVisibility(View.GONE);
            viewWearther.startAnimation(aniHide);
            viewDirection.setVisibility(View.GONE);
            viewDirection.startAnimation(aniHide);
        }
    }

    @Override
    public void getLocation() {
        checkGps();
        if (gps) {
            booleanOrigin = true;
            edtOrigin.setText("Your Location!");
            mPrisenter.getLocation(new Location("gps"), mMap);
            tvLocationWeather.setText("Your Location!");
            latLngWeather = new LatLng(10.854039, 106.628387);
            latLngSearchPlace = new LatLng(10.854039, 106.628387);
            tvSearchplace.setText("Your Location!");
        }
    }

    @Override
    public void showAndHide() {
        if (!isBooleanShowAndHide) {
            rllContain.setVisibility(View.GONE);
            isBooleanShowAndHide = true;

        } else {
            isBooleanShowAndHide = false;
            rllContain.setVisibility(View.VISIBLE);
        }

    }

    public void addMapTypePlace() {
        listType = new ArrayList<>();
        listType.add("hospital");
        listType.add("atm");
        listType.add("bank");
        listType.add("cafe");
        listType.add("gym");
        listType.add("restaurant");
        listType.add("library");

        listDistance = new ArrayList<>();
        listDistance.add("1");
        listDistance.add("2");
        listDistance.add("3");
        listDistance.add("4");
        listDistance.add("5");
        listDistance.add("6");
        listDistance.add("7");
        listDistance.add("8");
        listDistance.add("9");
        listDistance.add("10");
        listDistance.add("20");
        listDistance.add("30");
        listDistance.add("40");
        listDistance.add("50");
    }
}
