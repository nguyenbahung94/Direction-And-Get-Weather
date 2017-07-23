package com.example.billy.mapapi.Ui.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Billy on 7/22/2017.
 */

public class ResultsSearchPlace {
    @SerializedName("results")
    public List<Results> results;
    @SerializedName("status")
    public String status;

    public List<Results> getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }
}
