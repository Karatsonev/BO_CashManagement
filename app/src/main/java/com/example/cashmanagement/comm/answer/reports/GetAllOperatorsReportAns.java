package com.example.cashmanagement.comm.answer.reports;

import com.example.cashmanagement.comm.ServerAnswer;
import com.example.cashmanagement.models.CashFlowModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetAllOperatorsReportAns extends ServerAnswer {

    public List<CashFlowModel> cashFlowList;

    public GetAllOperatorsReportAns(){
        cashFlowList = new ArrayList<>();
    }

    @SerializedName("$type")
    public String type;
}
