package com.example.cashmanagement.comm;

import android.content.Intent;
import com.example.cashmanagement.App;
import com.example.cashmanagement.helpers.IntentHelper;
import com.example.cashmanagement.models.CashFlowItemModel;
import com.example.cashmanagement.models.CashModel;
import com.example.cashmanagement.models.LastInventoryItemModel;
import com.example.cashmanagement.models.Status;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * Базов клас за всички транзакции.
 */
public class SyncTransaction
{
    public String result;
    public String companyIdent;
    public String ident;


    public static SyncTransaction fromJson(String json){

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SyncTransaction.class, new SyncTransactionDeserializer());
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json, SyncTransaction.class);
    }

    // CHILD CLASSES WILL OVERRIDE THIS METHOD AND
    // INSIDE IT'S SCOPE WILL BE THE LOGIC FOR HANDLING THE SYNC MESSAGE
    public void execute(){

    }




    //region BANKNOTES
    protected void sendMessageAboutCashOutBanknotesResult(double amount, List<LastInventoryItemModel> modelList){
        Intent intent = new Intent(IntentHelper.INTENT_RESULT_CASH_OUT_BANKNOTES);
        intent.putExtra(IntentHelper.INTENT_IMPORT_EXTRA_DECIMAL_AMOUNT, amount);
        intent.putExtra(IntentHelper.INTENT_IMPORT_EXTRA_LIST_INVENTORY_ITEMS,(Serializable) modelList);
        App.getContext().sendBroadcast(intent);
    }
    protected void sendMessageAboutDepositBanknotesResult(String amount, List<CashModel> cashModelList){
        Intent intent = new Intent(IntentHelper.INTENT_RESULT_CASH_IN_BANKNOTES);
        intent.putExtra(IntentHelper.INTENT_IMPORT_STRING_EXTRA_AMOUNT, amount);
        intent.putExtra(IntentHelper.INTENT_IMPORT_EXTRA_LIST,(Serializable)cashModelList);
        App.getContext().sendBroadcast(intent);
    }

    protected void sendMessageAboutDepositBanknotesResultTwoMachines(String amount, List<CashModel> cashModelList){
        Intent intent = new Intent(IntentHelper.INTENT_RESULT_TWO_MACHINES);
        intent.putExtra(IntentHelper.INTENT_IMPORT_STRING_EXTRA_AMOUNT, amount);
        intent.putExtra(IntentHelper.INTENT_IMPORT_EXTRA_LIST,(Serializable)cashModelList);
        App.getContext().sendBroadcast(intent);
    }
    //endregion BANKNOTES


    //region COINS
    protected void sendMessageAboutPayInCoinsResult(String amount, List<CashFlowItemModel> cashFlowItemModels){
        Intent intent = new Intent(IntentHelper.INTENT_RESULT_CASH_IN_COINS);
        intent.putExtra(IntentHelper.INTENT_FINISH_PAY_IN_COINS, amount);
        intent.putExtra(IntentHelper.INTENT_IMPORT_EXTRA_LIST_COIN,(Serializable) cashFlowItemModels);
        App.getContext().sendBroadcast(intent);
    }
    protected void sendMessageAboutPayOutCoinsResult(String amount, String count, String denomination){
        Intent intent = new Intent(IntentHelper.INTENT_RESULT_CASH_OUT_COINS);
        intent.putExtra(IntentHelper.INTENT_IMPORT_STRING_EXTRA_AMOUNT, amount);
        intent.putExtra(IntentHelper.INTENT_IMPORT_STRING_EXTRA_COUNT,count);
        intent.putExtra(IntentHelper.INTENT_IMPORT_STRING_EXTRA_DENOMINATION,denomination);
        App.getContext().sendBroadcast(intent);
    }
    protected void sendMessageAboutDepositCoinsResultTwoMachines(String amount, List<CashFlowItemModel> cashFlowItemModels){
        Intent intent = new Intent(IntentHelper.INTENT_RESULT_TWO_MACHINES);
        intent.putExtra(IntentHelper.INTENT_FINISH_PAY_IN_COINS, amount);
        intent.putExtra(IntentHelper.INTENT_IMPORT_EXTRA_LIST_COIN,(Serializable) cashFlowItemModels);
        App.getContext().sendBroadcast(intent);
    }
    //endregion COINS

    //region STATUS
    protected void sendMessageAboutCashMachineStatus(Status cashMachineStatus){
        Intent intent = new Intent(IntentHelper.INTENT_RESULT_CASH_MACHINE_STATUS);
        intent.putExtra(IntentHelper.INTENT_IMPORT_STRING_EXTRA_CASHMACHINE_STATUS, cashMachineStatus);
        App.getContext().sendBroadcast(intent);
    }
    //endregion STATUS

}

