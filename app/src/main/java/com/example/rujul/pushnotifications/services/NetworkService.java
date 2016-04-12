package com.example.rujul.pushnotifications.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by rujul on 4/4/2016.
 */
public class NetworkService  {
    public boolean wifiConnected;
    public boolean mobileConnected;

    public boolean isConnected(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            return true;
        } else {
            wifiConnected = false;
            mobileConnected = false;
            return false;
        }
    }
}
