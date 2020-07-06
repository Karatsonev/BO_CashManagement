package com.example.cashmanagement.comm.answer.crud;

import com.example.cashmanagement.comm.ServerAnswer;
import com.google.gson.annotations.SerializedName;

public class EditUserAns extends ServerAnswer {

    public int editUserResult = 0;

    @SerializedName("$type")
    public String type;
}
