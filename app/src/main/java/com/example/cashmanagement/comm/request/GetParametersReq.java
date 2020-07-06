package com.example.cashmanagement.comm.request;

import com.example.cashmanagement.comm.ClientRequest;
import com.google.gson.annotations.SerializedName;

/**
 * Тестови колтайп за проверка на връзка.
 * Извлича базови параметри
 *
 */
public class GetParametersReq extends ClientRequest {

    @SerializedName("$type")
    public String type = "CashManagementServer.GetParametersReq, CashManagementServer";

}
