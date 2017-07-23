package com.example.billy.mapapi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by nbhung on 6/29/2017.
 */

public final class NetWorkUtils {
    private NetWorkUtils() {

    }

    public static boolean isNetWorkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo acNetworkInfo = cm.getActiveNetworkInfo();
        return acNetworkInfo != null && acNetworkInfo.isConnectedOrConnecting();
    }
}
