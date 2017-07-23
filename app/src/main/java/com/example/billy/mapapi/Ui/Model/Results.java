package com.example.billy.mapapi.Ui.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Billy on 7/23/2017.
 */

public class Results {
    @SerializedName("geometry")
    public Geometry geometry;
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;

    public Geometry getGeometry() {
        return geometry;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
