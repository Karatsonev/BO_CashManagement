package com.example.cashmanagement.comm.answer;

import com.example.cashmanagement.comm.ServerAnswer;
import com.example.cashmanagement.models.CashModel;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class SyncStartDepositAns extends ServerAnswer {

    //public BigDecimal depositAmount;
    // List<CashModel> cashModelList;
    public int machineCount;

    @SerializedName("$type")
    public String type;
}
