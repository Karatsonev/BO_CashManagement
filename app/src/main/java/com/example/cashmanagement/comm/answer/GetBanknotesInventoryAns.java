package com.example.cashmanagement.comm.answer;

import com.example.cashmanagement.comm.ServerAnswer;
import com.example.cashmanagement.models.CashModel;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class GetBanknotesInventoryAns extends ServerAnswer implements Serializable {

    public List<CashModel> cashModelList;

    @SerializedName("$type")
    public String type;
}
