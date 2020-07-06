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

public class FiscalReceiptCashInCoins {

    private Context _context;
    private List<CashFlowItemModel> _cashFlowItemModels;

    public FiscalReceiptCashInCoins(Context context, List<CashFlowItemModel> cashFlowItemModels){
        _context = context;
        _cashFlowItemModels = cashFlowItemModels;
    }

    public void printReceipt(String amount){

        try {
            List<BonLayoutElement> denominationList = new ArrayList<>();
            List<BonLayoutElement> bonLayoutElementList = new ArrayList<>();
            BonLayoutElement bonHeader = new BonLayoutElement(_context.getString(R.string.deposit_receipt), 40, true, false);
            BonLayoutElement bDate = new BonLayoutElement(TimeUtil.getTime(), 26, false, false);
            BonLayoutElement bOwner = new BonLayoutElement(_context.getString(R.string.operator) + " " + Ini.Server.LoggedUsername, 26, false, false);
            BonLayoutElement bStarsTop = new BonLayoutElement("----------------------------", 25, false, false);
            BonLayoutElement bTotal = new BonLayoutElement(_context.getString(R.string.total_amount) + " " + amount + " BGN", 25, false, false);
            BonLayoutElement bStarBottom = new BonLayoutElement("----------------------------", 25, false, false);

            bonLayoutElementList.add(bonHeader);
            bonLayoutElementList.add(bDate);
            bonLayoutElementList.add(bOwner);

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

            for (BonLayoutElement element : bonLayoutElementList) {

                PrintHelper.getInstance().printText(
                        element.getmContent(),
                        element.getmTextSize(),
                        element.isBold(),
                        element.isUnderlined());
            }

            for (BonLayoutElement element : denominationList) {
                PrintHelper.getInstance().printText(
                        element.getmContent(),
                        element.getmTextSize(),
                        element.isBold(),
                        element.isUnderlined()
                );
            }
            PrintHelper.getInstance().print3Line();

        }catch (Exception ex){
            App.writeToLog("Error while printing CashInCoins receipt: " + ex.getMessage());

        }
    }
}
