package com.example.cashmanagement.comm.request;

import com.example.cashmanagement.comm.ClientRequest;
import com.google.gson.annotations.SerializedName;

public class SyncStartPayInReq extends ClientRequest {

//    public String dateFrom;
//    public String dateTo;
//    public String currentOperatorReport;

    public int machineCount;

    @SerializedName("$type")
    public String type = "CashManagementServer.SyncStartPayInReq, CashManagementServer";

}
