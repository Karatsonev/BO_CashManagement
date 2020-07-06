package com.example.cashmanagement.comm.answer.crud;

import com.example.cashmanagement.comm.ServerAnswer;
import com.google.gson.annotations.SerializedName;

public class DeleteUserAns extends ServerAnswer {

    public int deleteUserResult = 0;

    @SerializedName("$type")
    public String type;
}
