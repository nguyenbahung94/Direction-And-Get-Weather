package com.example.billy.mapapi.Ui.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nbhung on 6/21/2017.
 */

public class Distance {
    @SerializedName("text")
    private String text;
    @SerializedName("value")
    private int value;

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
