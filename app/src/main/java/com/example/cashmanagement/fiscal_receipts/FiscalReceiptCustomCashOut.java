package com.example.cashmanagement.fiscal_receipts;

import android.content.Context;

import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.helpers.PrintHelper;
import com.example.cashmanagement.models.BonLayoutElement;
import com.example.cashmanagement.utils.FormatUtils;
import com.example.cashmanagement.utils.Ini;
import com.example.cashmanagement.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class FiscalReceiptCustomCashOut {

 private String _denmonination;
 private String _count;
 private String _amount;
 private Context _context;

 public FiscalReceiptCustomCashOut(Context context, String denomination, String count, String amount){
     _context = context;
     _denmonination = denomination;
     _count = count;
     _amount = amount;
 }

 public void printReceipt() {
     try {

         double denomination = Double.parseDouble(_denmonination);
         double amount = Double.parseDouble(_amount);

         BonLayoutElement bHeaderTitle = new BonLayoutElement(_context.getString(R.string.cash_out_receipt), 40, true, false);
         BonLayoutElement bHeaderDate = new BonLayoutElement(TimeUtil.getTime(), 26, false, false);
         BonLayoutElement bOwner = new BonLayoutElement(_context.getString(R.string.operator) + " " + Ini.Server.LoggedUsername, 25, false, false);
         BonLayoutElement bStarsTop = new BonLayoutElement("-----------------------------", 25, false, false);
         BonLayoutElement bTitles = new BonLayoutElement(_context.getString(R.string.denomination_bon) + "     " + _context.getString(R.string.count_bon) + "     " + _context.getString(R.string.sum), 25, false, false);
         BonLayoutElement bContent;
          if(Integer.parseInt(_count) < 10){
              bContent = new BonLayoutElement(FormatUtils.formatDouble(denomination) + "        " + _count + "        " + FormatUtils.formatDouble(amount), 25, false, false);
          }else if(Integer.parseInt(_count)< 100){
              bContent = new BonLayoutElement(FormatUtils.formatDouble(denomination) + "        " + _count + "       " + FormatUtils.formatDouble(amount), 25, false, false);
          }else {
              bContent = new BonLayoutElement(FormatUtils.formatDouble(denomination) + "        " + _count + "      " + FormatUtils.formatDouble(amount), 25, false, false);
          }

         BonLayoutElement bStarsBottom = new BonLayoutElement("-----------------------------", 25, false, false);

         List<BonLayoutElement> bonLayoutElementList = new ArrayList<>();
         bonLayoutElementList.add(bHeaderTitle);
         bonLayoutElementList.add(bHeaderDate);
         bonLayoutElementList.add(bOwner);
         bonLayoutElementList.add(bStarsTop);
         bonLayoutElementList.add(bTitles);
         bonLayoutElementList.add(bContent);
         bonLayoutElementList.add(bStarsBottom);

         for (BonLayoutElement model : bonLayoutElementList) {
             PrintHelper.getInstance().printText(
                     model.getmContent(),
                     model.getmTextSize(),
                     model.isBold(),
                     model.isUnderlined());
         }
         PrintHelper.getInstance().print3Line();
     }catch (Exception ex){
         App.writeToLog("Error while printing Custom CashOut receipt: " + ex.getMessage());

     }
 }

}
