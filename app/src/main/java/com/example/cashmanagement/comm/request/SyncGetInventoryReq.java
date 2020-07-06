package com.example.cashmanagement.comm.request;

import com.example.cashmanagement.comm.ClientRequest;
import com.google.gson.annotations.SerializedName;

public class SyncGetInventoryReq extends ClientRequest {

    @SerializedName("$type")
    public String type = "CashManagementServer.SyncGetInventoryReq, CashManagementServer";
}
