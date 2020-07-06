package com.example.cashmanagement.models;

import com.example.cashmanagement.enums.CashType;

import java.io.Serializable;

public class CashModel implements Serializable {
    public CashType cashType;
    public int Count;
    public String CurrencyISOCode;
    public double Denomination;
}
