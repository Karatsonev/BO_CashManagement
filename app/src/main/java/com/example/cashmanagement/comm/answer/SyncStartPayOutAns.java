package com.example.cashmanagement.comm.answer;

import com.example.cashmanagement.comm.ServerAnswer;
import com.google.gson.annotations.SerializedName;

public class SyncStartPayOutAns extends ServerAnswer {


    public double amount;
    public String dummyAnswer;

    @SerializedName("$type")
    public String type;
}
