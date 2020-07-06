package com.example.cashmanagement.comm.answer.crud;

import com.example.cashmanagement.comm.ServerAnswer;
import com.google.gson.annotations.SerializedName;

public class CreateUserAns extends ServerAnswer {

    public int createUserResult = 0;

    @SerializedName("$type")
    public String type;
}
