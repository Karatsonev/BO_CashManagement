package com.example.cashmanagement.comm.answer;

import com.example.cashmanagement.comm.ServerAnswer;
import com.example.cashmanagement.models.CashFlowModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SyncGetUserTransactionHistoryAns extends ServerAnswer {

    public String username;

    public List<CashFlowModel> cashFlowModels;

    public SyncGetUserTransactionHistoryAns(){
        cashFlowModels = new ArrayList<>();
    }

    @SerializedName("$type")
    public String type;


}
