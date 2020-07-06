package com.example.cashmanagement.comm.answer.reports;

import com.example.cashmanagement.comm.ServerAnswer;
import com.example.cashmanagement.models.UserModel;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetAllOperatorsAns extends ServerAnswer {

    public List<UserModel> usersList;

    public GetAllOperatorsAns(List<UserModel> _userModelList){
        this.usersList = _userModelList;
    }

    @SerializedName("$type")
    public String type;
}
