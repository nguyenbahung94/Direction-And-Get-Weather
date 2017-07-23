package com.example.billy.mapapi.Di.module;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nbhung on 6/29/2017.
 */
@Module
public class AppModule {
    private Application mApplication;

    public AppModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }


}
