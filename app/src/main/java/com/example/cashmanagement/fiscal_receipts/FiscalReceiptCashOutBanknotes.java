package com.example.cashmanagement.fiscal_receipts;

import android.content.Context;

import com.example.cashmanagement.R;
import com.example.cashmanagement.helpers.PrintHelper;
import com.example.cashmanagement.models.BonLayoutElement;
import com.example.cashmanagement.models.CashModel;
import com.example.cashmanagement.models.LastInventoryItemModel;
import com.example.cashmanagement.utils.FormatUtils;
import com.example.cashmanagement.utils.Ini;
import com.example.cashmanagement.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class FiscalReceiptCashOutBanknotes {
    private Context _context;
    private List<LastInventoryItemModel> _inventoryModelList;

    public FiscalReceiptCashOutBanknotes(Context context, List<LastInventoryItemModel> inventoryModelList){
        _context = context;
        _inventoryModelList = inventoryModelList;
    }


    public void printReceipt(Double amount){
        String currentDateAndTime = TimeUtil.getTime();
        List<BonLayoutElement> denominationList = new ArrayList<>();
        List<BonLayoutElement> bonLayoutElementList = new ArrayList<>();

        BonLayoutElement bonHeader = new BonLayoutElement(_context.getString(R.string.cash_out_receipt), 40,true,false);
        BonLayoutElement bDate = new BonLayoutElement(currentDateAndTime,26,false,false);
        BonLayoutElement bOwner = new BonLayoutElement( _context.getString(R.string.operator) + " " +  Ini.Server.LoggedUsername,26,false,false);
        BonLayoutElement bStarsTop = new BonLayoutElement("--------------------------",25,false,false);
        BonLayoutElement bTotal = new BonLayoutElement(_context.getString(R.string.total_amount) + " " + FormatUtils.formatDouble(amount) + " BGN",25,false,false);
        BonLayoutElement bStarBottom = new BonLayoutElement("--------------------------",25,false,false);

        bonLayoutElementList.add(bonHeader);
        bonLayoutElementList.add(bDate);
        bonLayoutElementList.add(bOwner);

        denominationList.add(bStarsTop);
        String denominationHeader =  _context.getString(R.string.denomination_bon) + "     " + _context.getString(R.string.count_bon) + "     " + _context.getString(R.string.sum);
        denominationList.add(new BonLayoutElement(denominationHeader,25,false,false));

        for(LastInventoryItemModel model : _inventoryModelList){
            String content;
            int denomination = (int)model.nominal;
            if(model.quantity < 10){
                if(denomination < 10){
                    content = denomination + "           " + model.quantity + "        " + (int)(model.nominal * model.quantity);
                }else if(denomination < 100){
                    content = denomination + "          " + model.quantity + "        " + (int)(model.nominal * model.quantity);
                }else{
                    content = denomination + "         " + model.quantity + "        " + (int)(model.nominal * model.quantity);
                }

            }else if(model.quantity < 100){
                if(denomination < 10){
                    content = denomination + "           " + model.quantity + "       " + (int)(model.nominal * model.quantity);
                }else if(denomination < 100){
                    content = denomination + "          " + model.quantity + "       " + (int)(model.nominal * model.quantity);
                }else {
                    content = denomination + "         " + model.quantity + "       " + (int)(model.nominal * model.quantity);
                }
            }else{
                if(denomination<10){
                    content = denomination + "           " + model.quantity + "       " + (int)(model.nominal * model.quantity);
                }else if(denomination<100){
                    content = denomination + "          " + model.quantity + "       " + (int)(model.nominal * model.quantity);
                }else{
                    content = denomination + "         " + model.quantity + "       " + (int)(model.nominal * model.quantity);
                }
            }
            denominationList.add( new BonLayoutElement(content,25,false,false));
        }
        denominationList.add(bStarBottom);
        denominationList.add(bTotal);

        for(BonLayoutElement element : bonLayoutElementList){

            PrintHelper.getInstance().printText(
                    element.getmContent(),
                    element.getmTextSize(),
                    element.isBold(),
                    element.isUnderlined());
        }

        for (BonLayoutElement element : denominationList){
            PrintHelper.getInstance().printText(
                    element.getmContent(),
                    element.getmTextSize(),
                    element.isBold(),
                    element.isUnderlined()
            );
        }
        PrintHelper.getInstance().print3Line();

//                    ************************************************************************************
    }
}
