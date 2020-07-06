package com.example.cashmanagement.comm.answer;

import com.example.cashmanagement.comm.SyncTransaction;
import com.example.cashmanagement.models.CashModel;
import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.util.List;

public class SyncDepositResult extends SyncTransaction {

    public int machineCount;
    public BigDecimal depositAmount;
    public List<CashModel> cashModelList;

    @Override
    public void execute() {
        if(machineCount == 1){
            sendMessageAboutDepositBanknotesResult(String.valueOf(depositAmount), cashModelList);
        }else{
            sendMessageAboutDepositBanknotesResultTwoMachines(String.valueOf(depositAmount), cashModelList);
        }

    }

    @SerializedName("$type")
    public String type;
}
