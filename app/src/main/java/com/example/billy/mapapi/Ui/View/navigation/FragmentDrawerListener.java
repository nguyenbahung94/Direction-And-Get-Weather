package com.example.billy.mapapi.Ui.View.navigation;

import android.view.View;

/**
 * Created by nbhung on 7/7/2017.
 */

public interface FragmentDrawerListener {
    void onDrawerItemSelected(View view, int position);

    void getLocation();

    void showAndHide();
}
