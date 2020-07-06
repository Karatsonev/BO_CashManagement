package com.example.cashmanagement.comm.request.crud;

import com.example.cashmanagement.comm.ClientRequest;
import com.google.gson.annotations.SerializedName;

public class DeleteUserReq extends ClientRequest {

    public int userID;

    @SerializedName("$type")
    public String type = "CashManagementServer.DeleteUserReq, CashManagementServer";
}
