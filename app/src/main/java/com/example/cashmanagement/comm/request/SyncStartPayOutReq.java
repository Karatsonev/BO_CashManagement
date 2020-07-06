package com.example.cashmanagement.comm.request;

import com.example.cashmanagement.comm.ClientRequest;
import com.example.cashmanagement.models.CashModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SyncStartPayOutReq extends ClientRequest {

    public SyncStartPayOutReq(){
        cashModelList = new ArrayList<>();

    }

    public List<CashModel> cashModelList;

    @SerializedName("$type")
    public String type = "CashManagementServer.SyncStartPayOutReq, CashManagementServer";
}
