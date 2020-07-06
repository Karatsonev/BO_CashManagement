package com.example.cashmanagement.fiscal_receipts;

import android.content.Context;

import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.helpers.PrintHelper;
import com.example.cashmanagement.models.BonLayoutElement;
import com.example.cashmanagement.models.CashFlowItemModel;
import com.example.cashmanagement.utils.FormatUtils;
import com.example.cashmanagement.utils.Ini;
import com.example.cashmanagement.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class FiscalReceiptCombinedDepositCoins {

    private Context _context;
    private List<CashFlowItemModel> _cashFlowItemModels;

    public FiscalReceiptCombinedDepositCoins(Context context, List<CashFlowItemModel> cashFlowItemModels){
        _context = context;
        _cashFlowItemModels = cashFlowItemModels;
    }

    public void printReceipt(String amount){
        try{
            List<BonLayoutElement> denominationList = new ArrayList<>();
            BonLayoutElement bType = new BonLayoutElement(_context.getString(R.string.cashTypeCoins),30,false,false);
            BonLayoutElement bEmptyLine = new BonLayoutElement("",25,false,false);
            BonLayoutElement bStarsTop = new BonLayoutElement("----------------------------", 25, false, false);
            BonLayoutElement bTotal = new BonLayoutElement(_context.getString(R.string.total_amount) + " " + amount + " BGN", 25, false, false);
            BonLayoutElement bStarBottom = new BonLayoutElement("----------------------------", 25, false, false);


            denominationList.add(bEmptyLine);
            denominationList.add(bType);
            denominationList.add(bStarsTop);
            String denominationHeader = _context.getString(R.string.denomination_bon) + "    " + _context.getString(R.string.count_bon) + "    " + _context.getString(R.string.sum);
            denominationList.add(new BonLayoutElement(denominationHeader, 25, false, false));

            for (CashFlowItemModel model : _cashFlowItemModels) {
                String content;
                if (model.quantity < 10) {
                    content = FormatUtils.formatDouble(model.nominal) + "       " + model.quantity + "       " + FormatUtils.formatDouble(model.nominal * model.quantity);
                    denominationList.add(new BonLayoutElement(content, 25, false, false));
                }else if( model.quantity < 100 ){
                    content = FormatUtils.formatDouble(model.nominal) + "       " + model.quantity + "      " + FormatUtils.formatDouble(model.nominal * model.quantity);
                    denominationList.add(new BonLayoutElement(content, 25, false, false));
                }else {
                    content = FormatUtils.formatDouble(model.nominal) + "       " + model.quantity + "     " + FormatUtils.formatDouble(model.nominal * model.quantity);
                    denominationList.add(new BonLayoutElement(content, 25, false, false));
                }
            }
            denominationList.add(bStarBottom);
            denominationList.add(bTotal);


            for (BonLayoutElement element : denominationList) {
                PrintHelper.getInstance().printText(
                        element.getmContent(),
                        element.getmTextSize(),
                        element.isBold(),
                        element.isUnderlined()
                );
            }
            BonLayoutElement emptyLine = new BonLayoutElement("",25,false,false);
            BonLayoutElement totalDepositSum = new BonLayoutElement(_context.getString(R.string.totalDepositSum) + ": " + FormatUtils.formatDouble(Ini.Server.TotalDepositAmount) + " BGN",30,false,false);
            PrintHelper.getInstance().printText(emptyLine.getmContent(),emptyLine.getmTextSize(),emptyLine.isBold(),emptyLine.isUnderlined());
            PrintHelper.getInstance().printText(totalDepositSum.getmContent(),totalDepositSum.getmTextSize(),totalDepositSum.isBold(),totalDepositSum.isUnderlined());

            PrintHelper.getInstance().print3Line();
        }catch (Exception ex){
            App.writeToLog("Error while printing CombinedDepositCoins receipt: " + ex.getMessage());

        }
    }
}
