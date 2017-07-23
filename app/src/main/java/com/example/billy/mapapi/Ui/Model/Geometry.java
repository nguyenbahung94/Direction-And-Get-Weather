package com.example.billy.mapapi.Ui.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Billy on 7/23/2017.
 */

public class Geometry {
    @SerializedName("location")
    public OneLocation location;

    public OneLocation getLocation() {
        return location;
    }
}
