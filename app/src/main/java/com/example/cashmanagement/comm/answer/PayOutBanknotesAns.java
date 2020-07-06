package com.example.cashmanagement.comm.answer;

import com.example.cashmanagement.comm.ServerAnswer;
import com.google.gson.annotations.SerializedName;

public class PayOutBanknotesAns extends ServerAnswer {


    @SerializedName("$type")
    public String type;
}
