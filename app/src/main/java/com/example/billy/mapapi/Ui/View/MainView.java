package com.example.billy.mapapi.Ui.View;

/**
 * Created by nbhung on 6/29/2017.
 */

public interface MainView {
    void showMessage(String message);

    void showError(String error);

    void showWeather(String temp);

    void showPlace(int number);


}
