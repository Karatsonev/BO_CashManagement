package com.example.cashmanagement.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class LastInventoryItemModel implements Serializable {

    public int lastInventoryItemId;
    public int nominalId;
    public int lastInventoryId;
    public int quantity;
    public double amount;
    public double nominal;
}
