package com.example.cashmanagement.comm.request;

import com.example.cashmanagement.comm.ClientRequest;
import com.google.gson.annotations.SerializedName;

public class GetBanknotesInventoryReq extends ClientRequest {

    @SerializedName("$type")
    public String type = "CashManagementServer.GetBanknotesInventoryReq, CashManagementServer";
}
