package com.example.cashmanagement.comm;

public class TransactionResult {
    public String ident;
    public String result;

    public TransactionResult(String ident, String result) {
        this.ident = ident;
        this.result = result;
    }
}
