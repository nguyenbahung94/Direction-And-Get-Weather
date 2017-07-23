package com.example.billy.mapapi.Ui.main;

import android.app.Application;

import com.example.billy.mapapi.Di.component.ActivityComponent;


/**
 * Created by nbhung on 7/19/2017.
 */

public class App extends Application {
    private ActivityComponent mActivityComponent;

    @Override
    public void onCreate() {
        super.onCreate();
  //      mActivityComponent = DaggerActivityComponent.builder().appModule(new AppModule(this)).build();
    }

    public ActivityComponent proActivityComponent() {
        return mActivityComponent;
    }
}
