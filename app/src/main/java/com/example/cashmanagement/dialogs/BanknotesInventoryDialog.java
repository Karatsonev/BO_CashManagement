package com.example.cashmanagement.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.cashmanagement.R;
import com.example.cashmanagement.fiscal_receipts.FiscalReceiptBanknotesInventory;
import com.example.cashmanagement.fiscal_receipts.FiscalReceiptCoinInventory;
import com.example.cashmanagement.models.CashModel;
import com.example.cashmanagement.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BanknotesInventoryDialog extends AppCompatDialogFragment {

    TextView denom002,denom005,denom010,denom020,denom050,denom100;
    TextView count002,count005,count010,count020,count050,count100;
    TextView sum002,sum005,sum010,sum020,sum050,sum100;
    TextView totalAmount;
    private List<CashModel> cashModels;
    private double total;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_banknote_inventory_dialog,null);
        initControls(view);

        if(getArguments() != null) {
            cashModels = (List<CashModel>) getArguments().getSerializable("banknotesInventoryList");

            if (cashModels != null) {

                denom002.setText((FormatUtils.formatDouble(cashModels.get(0).Denomination)));
                count002.setText(String.valueOf(cashModels.get(0).Count));
                sum002.setText((FormatUtils.formatDouble(cashModels.get(0).Denomination * cashModels.get(0).Count)));
                denom005.setText(FormatUtils.formatDouble(cashModels.get(1).Denomination));
                count005.setText(String.valueOf(cashModels.get(1).Count));
                sum005.setText((FormatUtils.formatDouble(cashModels.get(1).Denomination * cashModels.get(1).Count)));
                denom010.setText(FormatUtils.formatDouble(cashModels.get(2).Denomination));
                count010.setText(String.valueOf(cashModels.get(2).Count));
                sum010.setText((FormatUtils.formatDouble(cashModels.get(2).Denomination * cashModels.get(2).Count)));
                denom020.setText(FormatUtils.formatDouble(cashModels.get(3).Denomination));
                count020.setText(String.valueOf(cashModels.get(3).Count));
                sum020.setText((FormatUtils.formatDouble(cashModels.get(3).Denomination * cashModels.get(3).Count)));
                denom050.setText(FormatUtils.formatDouble(cashModels.get(4).Denomination));
                count050.setText(String.valueOf(cashModels.get(4).Count));
                sum050.setText((FormatUtils.formatDouble(cashModels.get(4).Denomination * cashModels.get(4).Count)));
                denom100.setText(FormatUtils.formatDouble(cashModels.get(5).Denomination));
                count100.setText(String.valueOf(cashModels.get(5).Count));
                sum100.setText((FormatUtils.formatDouble(cashModels.get(5).Denomination * cashModels.get(5).Count)));

            }
            total = getArguments().getDouble("totalAmount");
            totalAmount.setText(getString(R.string.total_bon_layout) + " " +FormatUtils.formatDouble(total) + " BGN");
        }

        builder.setView(view)
                .setNegativeButton(getString(R.string.btn_ok), (dialog, which) -> dialog.dismiss())
                .setPositiveButton(getString(R.string.btn_print), (dialog, which) -> {

                    FiscalReceiptBanknotesInventory receiptHelper = new FiscalReceiptBanknotesInventory(cashModels,getContext(),total);
                    receiptHelper.printReceipt();

                    dialog.dismiss();
                });

        return builder.create();
    }

    private void initControls(View view){
        denom002 = view.findViewById(R.id.tvNominal002);
        denom005 = view.findViewById(R.id.tvNominal005);
        denom010 = view.findViewById(R.id.tvNominal010);
        denom020 = view.findViewById(R.id.tvNominal020);
        denom050 = view.findViewById(R.id.tvNominal050);
        denom100 = view.findViewById(R.id.tvNominal100);

        count002 = view.findViewById(R.id.tvCount002);
        count005 = view.findViewById(R.id.tvCount005);
        count010 = view.findViewById(R.id.tvCount010);
        count020 = view.findViewById(R.id.tvCount020);
        count050 = view.findViewById(R.id.tvCount050);
        count100 = view.findViewById(R.id.tvCount100);

        sum002 = view.findViewById(R.id.tvSum002);
        sum005 = view.findViewById(R.id.tvSum005);
        sum010 = view.findViewById(R.id.tvSum010);
        sum020 = view.findViewById(R.id.tvSum020);
        sum050 = view.findViewById(R.id.tvSum050);
        sum100 = view.findViewById(R.id.tvSum100);


        totalAmount = view.findViewById(R.id.tvTotal);

        cashModels = new ArrayList<>();
    }
}
