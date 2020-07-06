package com.example.cashmanagement.comm.answer;

import com.example.cashmanagement.comm.ServerAnswer;
import com.google.gson.annotations.SerializedName;

public class SyncStopPayInAns extends ServerAnswer {

    public String dummyAnswer;
    public double amount;
    public int machineCount;

    @SerializedName("$type")
    public String type;
}
