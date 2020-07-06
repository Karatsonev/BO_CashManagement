package com.example.cashmanagement.comm.answer;

import com.example.cashmanagement.comm.ServerAnswer;
import com.google.gson.annotations.SerializedName;

public class SyncGetUserAns extends ServerAnswer {

    public String uid;
    public String userName;
    public String userCard;
    public String userPass;
    public int typeId;

    @SerializedName("$type")
    public String type;
}
