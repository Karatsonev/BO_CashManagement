package com.example.cashmanagement.comm.answer;

import com.example.cashmanagement.comm.ServerAnswer;
import com.example.cashmanagement.models.CashModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SyncGetInventoryAns extends ServerAnswer implements Serializable {

    public List<CashModel> cashModelList;

//   public class CashModel{
//       public int CashType;
//       public int Count;
//       public String CurrencyISOCode;
//       public double Denomination;
//
//   }

    @SerializedName("$type")
    public String type;
}
