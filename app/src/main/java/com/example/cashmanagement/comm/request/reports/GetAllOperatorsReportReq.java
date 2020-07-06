package com.example.cashmanagement.comm.request.reports;

import com.example.cashmanagement.comm.ClientRequest;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class GetAllOperatorsReportReq extends ClientRequest {

    public Date dateFrom;
    public Date dateTo;
    public boolean isTransactionTypeSelected;
    public int transactionType;

    @SerializedName("$type")
    public String type = "CashManagementServer.GetAllOperatorsReportReq, CashManagementServer";
}
