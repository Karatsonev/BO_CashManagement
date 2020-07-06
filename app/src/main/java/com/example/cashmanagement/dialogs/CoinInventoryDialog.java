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
import com.example.cashmanagement.fiscal_receipts.FiscalReceiptCoinInventory;
import com.example.cashmanagement.models.CashModel;
import com.example.cashmanagement.utils.FormatUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CoinsInventoryDialog extends AppCompatDialogFragment {

    TextView denom001,denom002,denom005,denom010,denom020,denom050,denom100,denom200;
    TextView count001,count002,count005,count010,count020,count050,count100,count200;
    TextView sum001,sum002,sum005,sum010,sum020,sum050,sum100,sum200;
    TextView totalAmount;
    private List<CashModel> cashModels;
    private double total;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_coin_inventory_dialog,null);
        initControls(view);

       if(getArguments() != null) {
           cashModels = (List<CashModel>) getArguments().getSerializable("coinsInventoryList");

           if (cashModels != null) {
               denom001.setText((FormatUtils.formatDouble(cashModels.get(0).Denomination)));
               count001.setText(String.valueOf(cashModels.get(0).Count));
               sum001.setText((FormatUtils.formatDouble(cashModels.get(0).Denomination * cashModels.get(0).Count)));
               denom002.setText((FormatUtils.formatDouble(cashModels.get(1).Denomination)));
               count002.setText(String.valueOf(cashModels.get(1).Count));
               sum002.setText((FormatUtils.formatDouble(cashModels.get(1).Denomination * cashModels.get(1).Count)));
               denom005.setText(FormatUtils.formatDouble(cashModels.get(2).Denomination));
               count005.setText(String.valueOf(cashModels.get(2).Count));
               sum005.setText((FormatUtils.formatDouble(cashModels.get(2).Denomination * cashModels.get(2).Count)));
               denom010.setText(FormatUtils.formatDouble(cashModels.get(3).Denomination));
               count010.setText(String.valueOf(cashModels.get(3).Count));
               sum010.setText((FormatUtils.formatDouble(cashModels.get(3).Denomination * cashModels.get(3).Count)));
               denom020.setText(FormatUtils.formatDouble(cashModels.get(4).Denomination));
               count020.setText(String.valueOf(cashModels.get(4).Count));
               sum020.setText((FormatUtils.formatDouble(cashModels.get(5).Denomination * cashModels.get(5).Count)));
               denom050.setText(FormatUtils.formatDouble(cashModels.get(5).Denomination));
               count050.setText(String.valueOf(cashModels.get(5).Count));
               sum050.setText((FormatUtils.formatDouble(cashModels.get(5).Denomination * cashModels.get(5).Count)));
               denom100.setText(FormatUtils.formatDouble(cashModels.get(6).Denomination));
               count100.setText(String.valueOf(cashModels.get(6).Count));
               sum100.setText((FormatUtils.formatDouble(cashModels.get(6).Denomination * cashModels.get(6).Count)));
               denom200.setText(FormatUtils.formatDouble(cashModels.get(7).Denomination));
               count200.setText(String.valueOf(cashModels.get(7).Count));
               sum200.setText((FormatUtils.formatDouble(cashModels.get(7).Denomination * cashModels.get(7).Count)));
           }
          total = getArguments().getDouble("totalAmount");
           totalAmount.setText(getString(R.string.total_bon_layout) + " " +FormatUtils.formatDouble(total) + " BGN");
       }

        builder.setView(view)
                .setNegativeButton(getString(R.string.btn_ok), (dialog, which) -> dialog.dismiss())
                .setPositiveButton(getString(R.string.btn_print), (dialog, which) -> {

                    FiscalReceiptCoinInventory receiptHelper = new FiscalReceiptCoinInventory(cashModels,getContext(),total);
                    receiptHelper.printReceipt();

                 dialog.dismiss();
                });

        return builder.create();
    }

    private void initControls(View view){
        denom001 = view.findViewById(R.id.tvNominal001);
        denom002 = view.findViewById(R.id.tvNominal002);
        denom005 = view.findViewById(R.id.tvNominal005);
        denom010 = view.findViewById(R.id.tvNominal010);
        denom020 = view.findViewById(R.id.tvNominal020);
        denom050 = view.findViewById(R.id.tvNominal050);
        denom100 = view.findViewById(R.id.tvNominal100);
        denom200 = view.findViewById(R.id.tvNominal200);

        count001 = view.findViewById(R.id.tvCount001);
        count002 = view.findViewById(R.id.tvCount002);
        count005 = view.findViewById(R.id.tvCount005);
        count010 = view.findViewById(R.id.tvCount010);
        count020 = view.findViewById(R.id.tvCount020);
        count050 = view.findViewById(R.id.tvCount050);
        count100 = view.findViewById(R.id.tvCount100);
        count200 = view.findViewById(R.id.tvCount200);

        sum001 = view.findViewById(R.id.tvSum001);
        sum002 = view.findViewById(R.id.tvSum002);
        sum005 = view.findViewById(R.id.tvSum005);
        sum010 = view.findViewById(R.id.tvSum010);
        sum020 = view.findViewById(R.id.tvSum020);
        sum050 = view.findViewById(R.id.tvSum050);
        sum100 = view.findViewById(R.id.tvSum100);
        sum200 = view.findViewById(R.id.tvSum200);


        totalAmount = view.findViewById(R.id.tvTotal);

        cashModels = new ArrayList<>();
    }

}
