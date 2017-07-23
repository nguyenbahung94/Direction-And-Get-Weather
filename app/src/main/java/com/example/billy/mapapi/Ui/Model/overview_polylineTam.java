package com.example.billy.mapapi.Ui.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nbhung on 6/21/2017.
 */

public class overview_polylineTam {
    @SerializedName("points")
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
