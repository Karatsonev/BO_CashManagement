package com.example.cashmanagement.fiscal_receipts;

import android.app.Activity;

import com.example.cashmanagement.R;
import com.example.cashmanagement.helpers.PrintHelper;
import com.example.cashmanagement.models.BonLayoutElement;
import com.example.cashmanagement.models.CashFlowModel;
import com.example.cashmanagement.utils.FormatUtils;
import com.example.cashmanagement.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class FiscalReceiptTransactionsReport {

    private List<CashFlowModel> _cashFlowModels;
    private Activity _activity;

    public FiscalReceiptTransactionsReport(Activity activity, List<CashFlowModel> cashFlowModels){
        _cashFlowModels = cashFlowModels;
        _activity = activity;
    }

    public void printReceipt(){
        int trNumber = 1;
        String trType;
        String currentDateAndTime = TimeUtil.getTime();
        double totalCashIn = 0;
        double totalCashOut = 0;

        //----print bon header-----//
        BonLayoutElement headerTopLine = new BonLayoutElement("-----------------------------",25,false,false);
        BonLayoutElement header = new BonLayoutElement(_activity.getString(R.string.transaction_report_bon_layout),31,true,false);
        BonLayoutElement headerDate = new BonLayoutElement(_activity.getString(R.string.date_bon_layout) + currentDateAndTime,24,false,false);
        BonLayoutElement headerBottomLine = new BonLayoutElement("-----------------------------",25,false,false);

        List<BonLayoutElement> headerElements = new ArrayList<>();
        headerElements.add(headerTopLine);
        headerElements.add(header);
        headerElements.add(headerDate);
        headerElements.add(headerBottomLine);

        for(BonLayoutElement element : headerElements){
            PrintHelper.getInstance().printText(
                    element.getmContent(),
                    element.getmTextSize(),
                    element.isBold(),
                    element.isUnderlined());
        }
        PrintHelper.getInstance().printText("",22,false,false);

        //----print bon elements----//
        for(CashFlowModel transaction : _cashFlowModels){

            if(transaction.typeId == 1){
                totalCashIn += transaction.amount;
                trType = _activity.getString(R.string.cashIn);
            }else{
                trType = _activity.getString(R.string.cashOut);
                totalCashOut += transaction.amount;
            }

            BonLayoutElement tNum = new BonLayoutElement(String.valueOf(trNumber),25,true,false);
            BonLayoutElement tOperator = new BonLayoutElement(_activity.getString(R.string.operator_bon_layout) + transaction.fullName,24,false,false);
            BonLayoutElement tType = new BonLayoutElement(_activity.getString(R.string.tr_type_bon_layout) + trType,24,false,false );
            BonLayoutElement tDate = new BonLayoutElement(_activity.getString(R.string.date_bon_layout) + transaction.dateTimeStart,24,false,false);
            BonLayoutElement tAmount = new BonLayoutElement( _activity.getString(R.string.amount_bon_layout) + String.format("%.2f",transaction.amount) + " BGN",24,false,false);

            List<BonLayoutElement> bonLayoutElementList = new ArrayList<>();
            bonLayoutElementList.add(tNum);
            bonLayoutElementList.add(tOperator);
            bonLayoutElementList.add(tType);
            bonLayoutElementList.add(tDate);
            bonLayoutElementList.add(tAmount);

            for(BonLayoutElement element : bonLayoutElementList){
                PrintHelper.getInstance().printText(
                        element.getmContent(),
                        element.getmTextSize(),
                        element.isBold(),
                        element.isUnderlined());
            }
            PrintHelper.getInstance().printText("--------------------------------",23,false,false);
            trNumber++;

        }
        double total = totalCashIn - totalCashOut;
        PrintHelper.getInstance().printText(_activity.getString(R.string.total_amount_bon_layout) + FormatUtils.formatDouble(total) + " BGN",28,false,false);

        PrintHelper.getInstance().print3Line();
    }
}
