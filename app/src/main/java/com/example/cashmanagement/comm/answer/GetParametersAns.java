package com.example.cashmanagement.comm.answer;

import com.example.cashmanagement.comm.ServerAnswer;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class GetParametersAns extends ServerAnswer {
    public static class GetParametersMember{
        public String name;
        public int value;
    }


    @SerializedName("$type")
    public String type;
    public String name;
    public String version;
    public Date build;


    public ArrayList<GetParametersMember> list = new ArrayList<>();
}
