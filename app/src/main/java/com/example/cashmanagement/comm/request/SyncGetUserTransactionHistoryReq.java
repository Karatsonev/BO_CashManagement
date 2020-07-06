package com.example.cashmanagement.comm.request;

import com.example.cashmanagement.comm.ClientRequest;
import com.google.gson.annotations.SerializedName;

public class SyncGetUserTransactionHistoryReq extends ClientRequest {

    public String usernameReq;

    @SerializedName("$type")
    public String type = "CashManagementServer.SyncGetUserTransactionHistoryReq, CashManagementServer";




}
