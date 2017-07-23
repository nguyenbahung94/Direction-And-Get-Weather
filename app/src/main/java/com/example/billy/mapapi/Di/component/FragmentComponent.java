package com.example.billy.mapapi.Di.component;


import com.example.billy.mapapi.Di.module.ActivityModule;
import com.example.billy.mapapi.Di.module.NetWorkModule;
import com.example.billy.mapapi.Ui.main.FragmentDirector;

import dagger.Component;

/**
 * Created by nbhung on 7/19/2017.
 */
@Component( modules = {NetWorkModule.class, ActivityModule.class})
public interface FragmentComponent {
    FragmentDirector inject(FragmentDirector fragmentDirector);
}
