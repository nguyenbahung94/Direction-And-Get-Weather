package com.example.billy.mapapi.Ui.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nbhung on 6/23/2017.
 */

public class InforWeather {
    @SerializedName("weather")
    private List<weather> obweather =new ArrayList<weather>();
    @SerializedName("main")
    private mainwt obmain;

    public List<weather> getObweather() {
        return obweather;
    }

    public mainwt getObmain() {
        return obmain;
    }

    @Override
    public String toString() {
        return "InforWeather{" +
                "obweather=" + obweather +
                ", obmain=" + obmain +
                '}';
    }
}
