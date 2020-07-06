package com.example.cashmanagement.comm;

import android.annotation.SuppressLint;
import android.provider.Settings;

import com.example.cashmanagement.App;
import com.example.cashmanagement.utils.Ini;


public class ClientRequest extends BaseCommClass {
    public String productName = "";
    public String communicationVersion = "";
    public String databaseVersion = "";
    public String userCard = Ini.Server.CardNumber;
    public String preferredCulture = "en";
    public String userName = Ini.Server.Username;
    public String userPass = Ini.Server.Password;
    @SuppressLint("HardwareIds")
    public String terminalSerial = Settings.Secure.getString(App.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

    public ClientRequest(){
        productName = ClientServer.productName;
        communicationVersion = ClientServer.communicationVersion;
        databaseVersion = ClientServer.databaseVersion;
    }
}
