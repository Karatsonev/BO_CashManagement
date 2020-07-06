package com.example.cashmanagement.models;

import java.io.Serializable;
import java.util.UUID;

public class CashFlowModel implements Serializable {

    public int cashFlowId;
    public int userId;
    public int typeId;
    public String dateTimeStart;
    public String dateTimeEnd;
    public UUID ident;
    public double amount;
    public String fullName;
}
