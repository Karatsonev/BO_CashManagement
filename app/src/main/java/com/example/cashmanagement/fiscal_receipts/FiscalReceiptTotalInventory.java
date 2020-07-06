package com.example.cashmanagement.fiscal_receipts;

import android.content.Context;

import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.helpers.PrintHelper;
import com.example.cashmanagement.models.BonLayoutElement;
import com.example.cashmanagement.models.CashModel;
import com.example.cashmanagement.utils.FormatUtils;
import com.example.cashmanagement.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class FiscalReceiptTotalInventory {
    private List<CashModel> _cashModelListBanknotes;
    private List<BonLayoutElement> bonElementsBanknotes;
    private Context _context;
    private double _totalBanknotes;
    private double _totalCoins;
    private List<CashModel> _cashModelListCoins;
    private List<BonLayoutElement> bonElementsCoins;

    public FiscalReceiptTotalInventory(Context context, List<CashModel> cashModelListBanknotes,List<CashModel> cashModelListCoins,  double totalBanknotes, double totalCoins){
        _cashModelListBanknotes = cashModelListBanknotes;
        _context = context;
        _totalBanknotes = totalBanknotes;
        _totalCoins = totalCoins;
        _cashModelListCoins = cashModelListCoins;
        bonElementsBanknotes = new ArrayList<>();
        bonElementsCoins = new ArrayList<>();
    }

    public void printReceipt() {

        try {

            BonLayoutElement headerTitle = new BonLayoutElement(_context.getString(R.string.inventory_receipt), 40, true, false);
            BonLayoutElement headerDate = new BonLayoutElement(TimeUtil.getTime(), 26, false, false);
            BonLayoutElement bEmptyLine = new BonLayoutElement("", 25, false, false);
            BonLayoutElement bType = new BonLayoutElement(_context.getString(R.string.cashTypeBanknotes), 30, false, false);
            BonLayoutElement starsTop = new BonLayoutElement("-----------------------------", 25, false, false);
            BonLayoutElement titles = new BonLayoutElement(_context.getString(R.string.denomination_bon) + "    " + _context.getString(R.string.count_bon) +"     " + _context.getString(R.string.sum) , 25, false, false);
            BonLayoutElement n001 = new BonLayoutElement((int)_cashModelListBanknotes.get(0).Denomination + "          " + _cashModelListBanknotes.get(0).Count   + "        "   + (int)( _cashModelListBanknotes.get(0).Denomination * _cashModelListBanknotes.get(0).Count), 25, false, false);
            BonLayoutElement n002 = new BonLayoutElement((int)_cashModelListBanknotes.get(1).Denomination + "          " + _cashModelListBanknotes.get(1).Count   + "        "   + (int)(_cashModelListBanknotes.get(1).Denomination * _cashModelListBanknotes.get(1).Count), 25, false, false);
            BonLayoutElement n005 = new BonLayoutElement((int)_cashModelListBanknotes.get(2).Denomination + "         "  + _cashModelListBanknotes.get(2).Count   + "        "   + (int)(_cashModelListBanknotes.get(2).Denomination * _cashModelListBanknotes.get(2).Count), 25, false, false);
            BonLayoutElement n010 = new BonLayoutElement((int)_cashModelListBanknotes.get(3).Denomination + "         "  + _cashModelListBanknotes.get(3).Count   + "        "   + (int)(_cashModelListBanknotes.get(3).Denomination * _cashModelListBanknotes.get(3).Count), 25, false, false);
            BonLayoutElement n020 = new BonLayoutElement((int)_cashModelListBanknotes.get(4).Denomination + "         "  + _cashModelListBanknotes.get(4).Count   + "        "   + (int)(_cashModelListBanknotes.get(4).Denomination * _cashModelListBanknotes.get(4).Count) , 25, false, false);
            BonLayoutElement n050 = new BonLayoutElement((int)_cashModelListBanknotes.get(5).Denomination + "        "   + _cashModelListBanknotes.get(5).Count   + "        "   + (int)(_cashModelListBanknotes.get(5).Denomination * _cashModelListBanknotes.get(5).Count), 25, false, false);
            BonLayoutElement starsBottom = new BonLayoutElement("-----------------------------", 25, false, false);
            BonLayoutElement total = new BonLayoutElement(_context.getString(R.string.total_bon_layout) + " " + FormatUtils.formatDouble(_totalBanknotes) + " BGN", 26, false, false);


            bonElementsBanknotes.add(headerTitle);
            bonElementsBanknotes.add(headerDate);
            bonElementsBanknotes.add(bEmptyLine);
            bonElementsBanknotes.add(bType);
            bonElementsBanknotes.add(starsTop);
            bonElementsBanknotes.add(titles);
            bonElementsBanknotes.add(n001);
            bonElementsBanknotes.add(n002);
            bonElementsBanknotes.add(n005);
            bonElementsBanknotes.add(n010);
            bonElementsBanknotes.add(n020);
            bonElementsBanknotes.add(n050);
            bonElementsBanknotes.add(starsBottom);
            bonElementsBanknotes.add(total);

            for (BonLayoutElement element : bonElementsBanknotes) {
                PrintHelper.getInstance().printText(
                        element.getmContent(),
                        element.getmTextSize(),
                        element.isBold(),
                        element.isUnderlined()
                );
            }

            BonLayoutElement bTypeC = new BonLayoutElement(_context.getString(R.string.cashTypeCoins),30,false,false);
            BonLayoutElement bEmptyLineC = new BonLayoutElement("",25,false,false);
            BonLayoutElement starsTopc = new BonLayoutElement("-----------------------------", 25, false, false);
            BonLayoutElement titlesc = new BonLayoutElement(_context.getString(R.string.denomination_bon) + "    " + _context.getString(R.string.count_bon) +"     " + _context.getString(R.string.sum) , 25, false, false);
            BonLayoutElement n001c = new BonLayoutElement(FormatUtils.formatDouble(_cashModelListCoins.get(0).Denomination) + "       " + _cashModelListCoins.get(0).Count + "      " + FormatUtils.formatDouble(_cashModelListCoins.get(0).Denomination * _cashModelListCoins.get(0).Count), 25, false, false);
            BonLayoutElement n002c = new BonLayoutElement(FormatUtils.formatDouble(_cashModelListCoins.get(1).Denomination) + "       " + _cashModelListCoins.get(1).Count + "      " + FormatUtils.formatDouble(_cashModelListCoins.get(1).Denomination * _cashModelListCoins.get(1).Count), 25, false, false);
            BonLayoutElement n005c = new BonLayoutElement(FormatUtils.formatDouble(_cashModelListCoins.get(2).Denomination) + "       " + _cashModelListCoins.get(2).Count + "      " + FormatUtils.formatDouble(_cashModelListCoins.get(2).Denomination * _cashModelListCoins.get(2).Count), 25, false, false);
            BonLayoutElement n010c = new BonLayoutElement(FormatUtils.formatDouble(_cashModelListCoins.get(3).Denomination) + "       " + _cashModelListCoins.get(3).Count + "      " + FormatUtils.formatDouble(_cashModelListCoins.get(3).Denomination * _cashModelListCoins.get(3).Count), 25, false, false);
            BonLayoutElement n020c = new BonLayoutElement(FormatUtils.formatDouble(_cashModelListCoins.get(4).Denomination) + "       " + _cashModelListCoins.get(4).Count + "      " +FormatUtils.formatDouble(_cashModelListCoins.get(4).Denomination * _cashModelListCoins.get(4).Count) , 25, false, false);
            BonLayoutElement n050c = new BonLayoutElement(FormatUtils.formatDouble(_cashModelListCoins.get(5).Denomination) + "       " + _cashModelListCoins.get(5).Count + "      " + FormatUtils.formatDouble(_cashModelListCoins.get(5).Denomination * _cashModelListCoins.get(5).Count), 25, false, false);
            BonLayoutElement n100c = new BonLayoutElement(FormatUtils.formatDouble(_cashModelListCoins.get(6).Denomination) + "       " + _cashModelListCoins.get(6).Count + "      " + FormatUtils.formatDouble(_cashModelListCoins.get(6).Denomination * _cashModelListCoins.get(6).Count), 25, false, false);
            BonLayoutElement n200c = new BonLayoutElement(FormatUtils.formatDouble(_cashModelListCoins.get(7).Denomination) + "       " + _cashModelListCoins.get(7).Count + "      " + FormatUtils.formatDouble(_cashModelListCoins.get(7).Denomination * _cashModelListCoins.get(7).Count), 25, false, false);
            BonLayoutElement starsBottomc = new BonLayoutElement("-----------------------------", 25, false, false);
            BonLayoutElement totalc = new BonLayoutElement(_context.getString(R.string.total_bon_layout) + " " + FormatUtils.formatDouble(_totalCoins) + " BGN", 26, false, false);

            bonElementsCoins.add(bEmptyLineC);
            bonElementsCoins.add(bTypeC);
            bonElementsCoins.add(starsTopc);
            bonElementsCoins.add(titlesc);
            bonElementsCoins.add(n001c);
            bonElementsCoins.add(n002c);
            bonElementsCoins.add(n005c);
            bonElementsCoins.add(n010c);
            bonElementsCoins.add(n020c);
            bonElementsCoins.add(n050c);
            bonElementsCoins.add(n100c);
            bonElementsCoins.add(n200c);
            bonElementsCoins.add(starsBottomc);
            bonElementsCoins.add(totalc);

            for (BonLayoutElement element : bonElementsCoins) {
                PrintHelper.getInstance().printText(
                        element.getmContent(),
                        element.getmTextSize(),
                        element.isBold(),
                        element.isUnderlined()
                );
            }
            PrintHelper.getInstance().printText("",25,false,false);
            PrintHelper.getInstance().printText(_context.getString(R.string.totalDepositSum) + ": " + FormatUtils.formatDouble(_totalCoins+_totalBanknotes) + " BGN",30,false,false);
            PrintHelper.getInstance().print3Line();



        }catch (Exception ex){
            App.writeToLog("Error while printing TotalInventory receipt: " + ex.getMessage());
        }
    }
}
