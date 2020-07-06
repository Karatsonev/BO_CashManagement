package com.example.cashmanagement.comm.request.reports;

import com.example.cashmanagement.comm.ClientRequest;
import com.google.gson.annotations.SerializedName;

public class GetAllOperatorsReq extends ClientRequest {


    @SerializedName("$type")
    public String type = "CashManagementServer.GetAllOperatorsReq, CashManagementServer";
}
