package com.example.cashmanagement.models;

import java.io.Serializable;

public class CashFlowItemModel implements Serializable {

    public int cashFlowItemId;
    public int cashFlowId;
    public int nominalId;
    public int quantity;
    public double amount;
    public double nominal;
}
