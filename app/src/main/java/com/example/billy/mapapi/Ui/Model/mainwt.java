package com.example.billy.mapapi.Ui.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nbhung on 6/23/2017.
 */

public class mainwt {
    @SerializedName("temp")
    private String temp;
    @SerializedName("humidity")
    private String humidity;
    @SerializedName("pressure")
    private String pressure;
    @SerializedName("temp_min")
    private String temp_min;
    @SerializedName("temp_max")
    private String temp_max;

    public String getTemp() {
        return temp;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public String getTemp_max() {
        return temp_max;
    }
}
