package com.example.cashmanagement.comm.answer;

import com.example.cashmanagement.comm.ServerAnswer;
import com.example.cashmanagement.models.CashFlowItemModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SyncGetDetailedUserTransactionHistoryAns extends ServerAnswer {

    public List<CashFlowItemModel> modelList = new ArrayList<>();

    public SyncGetDetailedUserTransactionHistoryAns(List<CashFlowItemModel> modelList){
        this.modelList = modelList;
    }

    @SerializedName("$type")
    public String type;
}
