package com.example.cashmanagement.comm.answer;

import com.example.cashmanagement.App;
import com.example.cashmanagement.comm.SyncTransaction;
import com.example.cashmanagement.models.CashFlowItemModel;
import com.example.cashmanagement.utils.Ini;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SyncSendPayInResult extends SyncTransaction {

    public double amount;
    public List<CashFlowItemModel> cashFlowItems;
    public int machineCount;

    @SerializedName("$type")
    public String type;

    @Override
    public void execute() {
        Ini.Server.SyncPayInResult = amount;
        App.saveSettings();
        if(machineCount == 1){
            sendMessageAboutPayInCoinsResult(String.valueOf(amount), cashFlowItems);
        }else{
            sendMessageAboutDepositCoinsResultTwoMachines(String.valueOf(amount), cashFlowItems);
        }
    }

}
