package com.example.cashmanagement.comm.request.crud;

import com.example.cashmanagement.comm.ClientRequest;
import com.example.cashmanagement.models.UserModel;
import com.google.gson.annotations.SerializedName;

public class CreateUserReq extends ClientRequest {

    public UserModel model;

    @SerializedName("$type")
    public String type = "CashManagementServer.CreateUserReq, CashManagementServer";
}
