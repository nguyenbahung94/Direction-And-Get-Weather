package com.example.billy.mapapi.Ui.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nbhung on 6/23/2017.
 */

public class weather {
    @SerializedName("main")
    private String mainwt;

    @SerializedName("description")
    private String description;

    @SerializedName("icon")
    private String icon;

    public String getMainwt() {
        return mainwt;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }
}
