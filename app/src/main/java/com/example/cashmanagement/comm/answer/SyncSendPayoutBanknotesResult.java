package com.example.cashmanagement.comm.answer;

import com.example.cashmanagement.comm.SyncTransaction;
import com.example.cashmanagement.models.LastInventoryItemModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SyncSendPayoutBanknotesResult extends SyncTransaction {

    public double amount;
    public List<LastInventoryItemModel> modelList;

    @Override
    public void execute() {
        sendMessageAboutCashOutBanknotesResult(amount,modelList);
    }

    @SerializedName("$type")
    public String type;
}
