package com.example.cashmanagement.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.cashmanagement.R;
import com.example.cashmanagement.fiscal_receipts.FiscalReceiptTotalInventory;
import com.example.cashmanagement.models.CashModel;
import com.example.cashmanagement.utils.FormatUtils;

import java.util.List;
import java.util.Objects;

public class TotalInventoryDialog {

    private Dialog dialog;
    private Context _context;
    private TextView tv_totalInventorySum;
    private Button btnPrint,btnGoBack;

    //for banknotes table
    private TextView denom002b,denom005b,denom010b,denom020b,denom050b,denom100b;
    private TextView count002b,count005b,count010b,count020b,count050b,count100b;
    private TextView sum002b,sum005b,sum010b,sum020b,sum050b,sum100b;
    private TextView totalAmount_b;
    private List<CashModel> _cashModels_b;
    private double _totalBanknotes;

    //for coins table
    private TextView denom001c,denom002c,denom005c,denom010c,denom020c,denom050c,denom100c,denom200c;
    private TextView count001c,count002c,count005c,count010c,count020c,count050c,count100c,count200c;
    private TextView sum001c,sum002c,sum005c,sum010c,sum020c,sum050c,sum100c,sum200c;
    private TextView totalAmount_c;
    private List<CashModel> _cashModels_c;
    private double _totalCoins;


    public TotalInventoryDialog(Context context,
                                List<CashModel> cashModels_b,
                                List<CashModel> cashModels_c,
                                double totalBanknotes,
                                double totalCoins)
    {
        _context = context;
        _cashModels_b = cashModels_b;
        _cashModels_c = cashModels_c;
        _totalBanknotes = totalBanknotes;
        _totalCoins = totalCoins;
    }

    public void showDialog(Activity activity, Context context ){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_total_inventory_dialog);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        initControls(dialog);

        btnPrint.setOnClickListener(v -> {

            FiscalReceiptTotalInventory receiptHelper = new FiscalReceiptTotalInventory(_context,_cashModels_b,_cashModels_c,_totalBanknotes,_totalCoins);
            receiptHelper.printReceipt();
            dialog.dismiss();
        });

        btnGoBack.setOnClickListener(v -> dialog.dismiss());

        if (_cashModels_b != null) {

            denom002b.setText((FormatUtils.formatDouble(_cashModels_b.get(0).Denomination)));
            count002b.setText(String.valueOf(_cashModels_b.get(0).Count));
            sum002b.setText((FormatUtils.formatDouble(_cashModels_b.get(0).Denomination * _cashModels_b.get(0).Count)));
            denom005b.setText(FormatUtils.formatDouble(_cashModels_b.get(1).Denomination));
            count005b.setText(String.valueOf(_cashModels_b.get(1).Count));
            sum005b.setText((FormatUtils.formatDouble(_cashModels_b.get(1).Denomination * _cashModels_b.get(1).Count)));
            denom010b.setText(FormatUtils.formatDouble(_cashModels_b.get(2).Denomination));
            count010b.setText(String.valueOf(_cashModels_b.get(2).Count));
            sum010b.setText((FormatUtils.formatDouble(_cashModels_b.get(2).Denomination * _cashModels_b.get(2).Count)));
            denom020b.setText(FormatUtils.formatDouble(_cashModels_b.get(3).Denomination));
            count020b.setText(String.valueOf(_cashModels_b.get(3).Count));
            sum020b.setText((FormatUtils.formatDouble(_cashModels_b.get(3).Denomination * _cashModels_b.get(3).Count)));
            denom050b.setText(FormatUtils.formatDouble(_cashModels_b.get(4).Denomination));
            count050b.setText(String.valueOf(_cashModels_b.get(4).Count));
            sum050b.setText((FormatUtils.formatDouble(_cashModels_b.get(4).Denomination * _cashModels_b.get(4).Count)));
            denom100b.setText(FormatUtils.formatDouble(_cashModels_b.get(5).Denomination));
            count100b.setText(String.valueOf(_cashModels_b.get(5).Count));
            sum100b.setText((FormatUtils.formatDouble(_cashModels_b.get(5).Denomination * _cashModels_b.get(5).Count)));

        }

        if (_cashModels_c != null) {

            denom001c.setText((FormatUtils.formatDouble(_cashModels_c.get(0).Denomination)));
            count001c.setText(String.valueOf(_cashModels_c.get(0).Count));
            sum001c.setText((FormatUtils.formatDouble(_cashModels_c.get(0).Denomination * _cashModels_c.get(0).Count)));
            denom002c.setText((FormatUtils.formatDouble(_cashModels_c.get(1).Denomination)));
            count002c.setText(String.valueOf(_cashModels_c.get(1).Count));
            sum002c.setText((FormatUtils.formatDouble(_cashModels_c.get(1).Denomination * _cashModels_c.get(1).Count)));
            denom005c.setText(FormatUtils.formatDouble(_cashModels_c.get(2).Denomination));
            count005c.setText(String.valueOf(_cashModels_c.get(2).Count));
            sum005c.setText((FormatUtils.formatDouble(_cashModels_c.get(2).Denomination * _cashModels_c.get(2).Count)));
            denom010c.setText(FormatUtils.formatDouble(_cashModels_c.get(3).Denomination));
            count010c.setText(String.valueOf(_cashModels_c.get(3).Count));
            sum010c.setText((FormatUtils.formatDouble(_cashModels_c.get(3).Denomination * _cashModels_c.get(3).Count)));
            denom020c.setText(FormatUtils.formatDouble(_cashModels_c.get(4).Denomination));
            count020c.setText(String.valueOf(_cashModels_c.get(4).Count));
            sum020c.setText((FormatUtils.formatDouble(_cashModels_c.get(5).Denomination * _cashModels_c.get(5).Count)));
            denom050c.setText(FormatUtils.formatDouble(_cashModels_c.get(5).Denomination));
            count050c.setText(String.valueOf(_cashModels_c.get(5).Count));
            sum050c.setText((FormatUtils.formatDouble(_cashModels_c.get(5).Denomination * _cashModels_c.get(5).Count)));
            denom100c.setText(FormatUtils.formatDouble(_cashModels_c.get(6).Denomination));
            count100c.setText(String.valueOf(_cashModels_c.get(6).Count));
            sum100c.setText((FormatUtils.formatDouble(_cashModels_c.get(6).Denomination * _cashModels_c.get(6).Count)));
            denom200c.setText(FormatUtils.formatDouble(_cashModels_c.get(7).Denomination));
            count200c.setText(String.valueOf(_cashModels_c.get(7).Count));
            sum200c.setText((FormatUtils.formatDouble(_cashModels_c.get(7).Denomination * _cashModels_c.get(7).Count)));
        }

        totalAmount_b.setText(_context.getString(R.string.total_bon_layout) + " " + FormatUtils.formatDouble(_totalBanknotes));
        totalAmount_c.setText(_context.getString(R.string.total_bon_layout) + " " + FormatUtils.formatDouble(_totalCoins));
        tv_totalInventorySum.setText(_context.getString(R.string.totalDepositSum) + ": " + FormatUtils.formatDouble(_totalCoins +_totalBanknotes) + " BGN");
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

    private void initControls(Dialog  dialog){
        //banknotes
        denom002b = dialog.findViewById(R.id.tvNominal002b);
        denom005b = dialog.findViewById(R.id.tvNominal005b);
        denom010b = dialog.findViewById(R.id.tvNominal010b);
        denom020b = dialog.findViewById(R.id.tvNominal020b);
        denom050b = dialog.findViewById(R.id.tvNominal050b);
        denom100b = dialog.findViewById(R.id.tvNominal100b);

        count002b = dialog.findViewById(R.id.tvCount002b);
        count005b = dialog.findViewById(R.id.tvCount005b);
        count010b = dialog.findViewById(R.id.tvCount010b);
        count020b = dialog.findViewById(R.id.tvCount020b);
        count050b = dialog.findViewById(R.id.tvCount050b);
        count100b = dialog.findViewById(R.id.tvCount100b);

        sum002b = dialog.findViewById(R.id.tvSum002b);
        sum005b = dialog.findViewById(R.id.tvSum005b);
        sum010b = dialog.findViewById(R.id.tvSum010b);
        sum020b = dialog.findViewById(R.id.tvSum020b);
        sum050b = dialog.findViewById(R.id.tvSum050b);
        sum100b = dialog.findViewById(R.id.tvSum100b);

        totalAmount_b = dialog.findViewById(R.id.tvTotal_b);

        //coins
        denom001c = dialog.findViewById(R.id.tvNominal001);
        denom002c = dialog.findViewById(R.id.tvNominal002);
        denom005c = dialog.findViewById(R.id.tvNominal005);
        denom010c = dialog.findViewById(R.id.tvNominal010);
        denom020c = dialog.findViewById(R.id.tvNominal020);
        denom050c = dialog.findViewById(R.id.tvNominal050);
        denom100c = dialog.findViewById(R.id.tvNominal100);
        denom200c = dialog.findViewById(R.id.tvNominal200);

        count001c = dialog.findViewById(R.id.tvCount001);
        count002c = dialog.findViewById(R.id.tvCount002);
        count005c = dialog.findViewById(R.id.tvCount005);
        count010c = dialog.findViewById(R.id.tvCount010);
        count020c = dialog.findViewById(R.id.tvCount020);
        count050c = dialog.findViewById(R.id.tvCount050);
        count100c = dialog.findViewById(R.id.tvCount100);
        count200c = dialog.findViewById(R.id.tvCount200);

        sum001c = dialog.findViewById(R.id.tvSum001);
        sum002c = dialog.findViewById(R.id.tvSum002);
        sum005c = dialog.findViewById(R.id.tvSum005);
        sum010c = dialog.findViewById(R.id.tvSum010);
        sum020c = dialog.findViewById(R.id.tvSum020);
        sum050c = dialog.findViewById(R.id.tvSum050);
        sum100c = dialog.findViewById(R.id.tvSum100);
        sum200c = dialog.findViewById(R.id.tvSum200);


        totalAmount_c = dialog.findViewById(R.id.tvTotalCoins);

        tv_totalInventorySum = dialog.findViewById(R.id.tv_totalInventorySum);

        btnPrint = dialog.findViewById(R.id.btnPrintTotalInventorySum);
        btnGoBack = dialog.findViewById(R.id.btnBack_from_totalInventory_dialog);
    }
}
