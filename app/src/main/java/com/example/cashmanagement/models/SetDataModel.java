package com.example.cashmanagement.models;

import java.io.Serializable;

public class SetDataModel implements Serializable {

    private String modelCurrency;
    private double modelDenomination;
    private int modelCount;
    private String statusSuccess;
    private String statusFailed;

    public SetDataModel(){ }

    public SetDataModel(String modelCurrency, double modelDenomination, int modelCount, String statusSuccess,String statusFailed) {
        this.modelCurrency = modelCurrency;
        this.modelDenomination = modelDenomination;
        this.modelCount = modelCount;
        this.statusSuccess = statusSuccess;
        this.statusFailed = statusFailed;
    }

    public String getModelCurrency() {
        return modelCurrency;
    }

    public void setModelCurrency(String modelCurrency) {
        this.modelCurrency = modelCurrency;
    }

    public double getModelDenomination() {
        return modelDenomination;
    }

    public void setModelDenomination(double modelDenomination) {
        this.modelDenomination = modelDenomination;
    }
    public int getModelCount() {
        return modelCount;
    }

    public void setModelCount(int modelCount) {
        this.modelCount = modelCount;
    }

    public String getStatusSuccess() {
        return statusSuccess;
    }

    public void setStatusSuccess(String statusSuccess) {
        this.statusSuccess = statusSuccess;
    }

    public String getStatusFailed() {
        return statusFailed;
    }

    public void setStatusFailed(String statusFailed) {
        this.statusFailed = statusFailed;
    }
}

