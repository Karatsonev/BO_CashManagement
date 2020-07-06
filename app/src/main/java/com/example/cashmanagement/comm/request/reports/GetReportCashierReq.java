package com.example.cashmanagement.comm.request.reports;

import com.example.cashmanagement.comm.ClientRequest;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class GetReportCashierReq extends ClientRequest {

    public Date dateFrom;
    public Date dateTo;
    public int operatorId;
    public boolean isTransactionTypeSelected;
    public int transactionType;

    @SerializedName("$type")
    public String type = "CashManagementServer.GetReportCashierReq, CashManagementServer";
}
