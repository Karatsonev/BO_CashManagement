package com.example.cashmanagement.comm.answer;

import com.example.cashmanagement.App;
import com.example.cashmanagement.comm.SyncTransaction;
import com.example.cashmanagement.models.CashModel;
import com.example.cashmanagement.utils.Ini;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SyncSendPayOutResult extends SyncTransaction {

    public double amount;
    public List<CashModel> requestedModel;


    @SerializedName("$type")
    public String type;

    @Override
    public void execute() {
        if(requestedModel!=null){
            Ini.Server.SyncPayOutResult += amount;
            App.saveSettings();

             sendMessageAboutPayOutCoinsResult(
                String.valueOf(amount),
                String.valueOf(requestedModel.get(0).Count),
                String.valueOf(requestedModel.get(0).Denomination)
             );
        }
    }

}
