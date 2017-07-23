package com.example.billy.mapapi.Ui.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nbhung on 6/21/2017.
 */

public class leg {
    @SerializedName("distance")
    private Distance distance;
    @SerializedName("duration")
    private Duration duration;
    @SerializedName("end_address")
    private String endAdd;
    @SerializedName("start_address")
    private String startAdd;
    @SerializedName("end_location")
    private OneLocation endLocation;
    @SerializedName("start_location")
    private OneLocation startLocation;

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getEndAdd() {
        return endAdd;
    }

    public void setEndAdd(String endAdd) {
        this.endAdd = endAdd;
    }

    public String getStartAdd() {
        return startAdd;
    }

    public void setStartAdd(String startAdd) {
        this.startAdd = startAdd;
    }

    public OneLocation getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(OneLocation endLocation) {
        this.endLocation = endLocation;
    }

    public OneLocation getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(OneLocation startLocation) {
        this.startLocation = startLocation;
    }

    @Override
    public String toString() {
        return "leg{" +
                "distance=" + distance +
                ", duration=" + duration +
                ", endAdd='" + endAdd + '\'' +
                ", startAdd='" + startAdd + '\'' +
                ", endLocation=" + endLocation +
                ", startLocation=" + startLocation +
                '}';
    }
}
