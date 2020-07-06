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

public class FiscalReceiptBanknotesInventory {

    private List<CashModel> _cashModelList;
    private List<BonLayoutElement> bonElements;
    private Context _context;
    private double _total;

    public FiscalReceiptBanknotesInventory(List<CashModel> cashModelList, Context context, double total){
        _cashModelList = cashModelList;
        _context = context;
        _total = total;
        bonElements = new ArrayList<>();
    }

    public void printReceipt() {

        try {

            BonLayoutElement headerTitle = new BonLayoutElement(_context.getString(R.string.inventory_banknotes), 40, true, false);
            BonLayoutElement headerDate = new BonLayoutElement(TimeUtil.getTime(), 26, false, false);
            BonLayoutElement starsTop = new BonLayoutElement("-----------------------------", 25, false, false);
            BonLayoutElement titles = new BonLayoutElement(_context.getString(R.string.denomination_bon) + "    " + _context.getString(R.string.count_bon) +"     " + _context.getString(R.string.sum) , 25, false, false);
            BonLayoutElement n001 = new BonLayoutElement((int)_cashModelList.get(0).Denomination + "          " + _cashModelList.get(0).Count   + "        "   + (int)( _cashModelList.get(0).Denomination * _cashModelList.get(0).Count), 25, false, false);
            BonLayoutElement n002 = new BonLayoutElement((int)_cashModelList.get(1).Denomination + "          " + _cashModelList.get(1).Count   + "        "   + (int)(_cashModelList.get(1).Denomination * _cashModelList.get(1).Count), 25, false, false);
            BonLayoutElement n005 = new BonLayoutElement((int)_cashModelList.get(2).Denomination + "         "  + _cashModelList.get(2).Count   + "        "   + (int)(_cashModelList.get(2).Denomination * _cashModelList.get(2).Count), 25, false, false);
            BonLayoutElement n010 = new BonLayoutElement((int)_cashModelList.get(3).Denomination + "         "  + _cashModelList.get(3).Count   + "        "   + (int)(_cashModelList.get(3).Denomination * _cashModelList.get(3).Count), 25, false, false);
            BonLayoutElement n020 = new BonLayoutElement((int)_cashModelList.get(4).Denomination + "         "  + _cashModelList.get(4).Count   + "        "   + (int)(_cashModelList.get(4).Denomination * _cashModelList.get(4).Count) , 25, false, false);
            BonLayoutElement n050 = new BonLayoutElement((int)_cashModelList.get(5).Denomination + "        "   + _cashModelList.get(5).Count   + "        "   + (int)(_cashModelList.get(5).Denomination * _cashModelList.get(5).Count), 25, false, false);
            BonLayoutElement starsBottom = new BonLayoutElement("-----------------------------", 25, false, false);
            BonLayoutElement total = new BonLayoutElement(_context.getString(R.string.total_bon_layout) + " " + FormatUtils.formatDouble(_total) + " BGN", 26, false, false);

            bonElements.add(headerTitle);
            bonElements.add(headerDate);
            bonElements.add(starsTop);
            bonElements.add(titles);
            bonElements.add(n001);
            bonElements.add(n002);
            bonElements.add(n005);
            bonElements.add(n010);
            bonElements.add(n020);
            bonElements.add(n050);
            bonElements.add(starsBottom);
            bonElements.add(total);

            for (BonLayoutElement element : bonElements) {
                PrintHelper.getInstance().printText(
                        element.getmContent(),
                        element.getmTextSize(),
                        element.isBold(),
                        element.isUnderlined()
                );
            }
            PrintHelper.getInstance().print3Line();
        }catch (Exception ex){
            App.writeToLog("Error while printing CoinInventory receipt: " + ex.getMessage());
        }
    }
}
