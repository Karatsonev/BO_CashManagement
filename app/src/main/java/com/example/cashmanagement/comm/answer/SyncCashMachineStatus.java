package com.example.cashmanagement.comm.answer;

import com.example.cashmanagement.comm.SyncTransaction;
import com.example.cashmanagement.models.Status;
import com.google.gson.annotations.SerializedName;

public class SyncCashMachineStatus extends SyncTransaction {

    public Status cashMachineStatus;

    @Override
    public void execute() {
      sendMessageAboutCashMachineStatus(cashMachineStatus);
    }

    @SerializedName("$type")
    public String type;
}
