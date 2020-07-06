package com.example.cashmanagement.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.cashmanagement.App;


public class CheckConnection {
    ConnectivityManager connectivityManager;

    public boolean isConnected() {
        boolean isConnected = false;

        try {
            connectivityManager = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            isConnected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        }
        catch (Exception ignore) {}

        return isConnected;
    }
}
