package com.example.cashmanagement.fiscal_receipts;

import android.content.Context;

import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.helpers.PrintHelper;
import com.example.cashmanagement.models.BonLayoutElement;
import com.example.cashmanagement.models.SetDataModel;
import com.example.cashmanagement.utils.Ini;
import com.example.cashmanagement.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class FiscalReceiptPredefinedCashOut {

    private Context _context;
    private List<SetDataModel> _setDataModelList;
    private String _denomination;
    private String _count;
    private String _amount;

    public FiscalReceiptPredefinedCashOut(Context context, String denomination, String count, String amount, List<SetDataModel> setDataModelList){
        _context = context;
        _denomination = denomination;
        _count = count;
        _amount = amount;
        _setDataModelList = setDataModelList;
    }



    public void printReceiptHeader(){
        try{
            BonLayoutElement headerTitle = new BonLayoutElement(_context.getString(R.string.cash_out_receipt), 40, true, false);
            BonLayoutElement headerDate = new BonLayoutElement(TimeUtil.getTime(), 26, false, false);
            BonLayoutElement bOperator = new BonLayoutElement(_context.getString(R.string.operator) + " " + Ini.Server.LoggedUsername, 25, false, false);
            BonLayoutElement topStars = new BonLayoutElement("-----------------------------", 25, false, false);
            BonLayoutElement bTitles = new BonLayoutElement(_context.getString(R.string.denomination_bon) + "     " + _context.getString(R.string.count_bon) + "     " + _context.getString(R.string.sum), 25, false, false);

            List<BonLayoutElement> headerElements = new ArrayList<>();
            headerElements.add(headerTitle);
            headerElements.add(headerDate);
            headerElements.add(bOperator);
            headerElements.add(topStars);
            headerElements.add(bTitles);

            for (BonLayoutElement model : headerElements) {

                PrintHelper.getInstance().printText(
                        model.getmContent(),
                        model.getmTextSize(),
                        model.isBold(),
                        model.isUnderlined());
            }


        }catch (Exception ex){
            App.writeToLog("Error while printing CashOut receipt: " + ex.getMessage());

        }
    }
}
