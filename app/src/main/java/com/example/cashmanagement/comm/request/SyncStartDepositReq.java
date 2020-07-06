package com.example.cashmanagement.comm.request;

import com.example.cashmanagement.comm.ClientRequest;
import com.google.gson.annotations.SerializedName;

public class SyncStartDepositReq extends ClientRequest {

    public int machineCount;

    @SerializedName("$type")
    public String type = "CashManagementServer.SyncStartDepositReq, CashManagementServer";
}
