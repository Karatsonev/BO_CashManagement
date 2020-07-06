package com.example.cashmanagement.comm.request.crud;

import com.example.cashmanagement.comm.ClientRequest;
import com.example.cashmanagement.models.UserModel;
import com.google.gson.annotations.SerializedName;

public class EditUserReq extends ClientRequest {

    public String loginName;
    public String fullName;
    public boolean enabled;

    @SerializedName("$type")
    public String type = "CashManagementServer.EditUserReq, CashManagementServer";
}
