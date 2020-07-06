package com.example.cashmanagement.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.example.cashmanagement.R;
import com.example.cashmanagement.adapters.RecyclerViewTransactionAdapter;
import com.example.cashmanagement.models.CashFlowModel;
import com.example.cashmanagement.utils.Ini;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class TransactionHistoryDialog extends AppCompatDialogFragment {

    RecyclerView recyclerView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<CashFlowModel> models = (List<CashFlowModel>) (Objects.requireNonNull(getArguments())).getSerializable("extra_model_list");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_transaction_history_dialog,null);
        TextView text_view_username = view.findViewById(R.id.text_view_name);
        recyclerView = view.findViewById(R.id.recycler_view);
        text_view_username.setText(Ini.Server.LoggedUsername);

       // RecyclerViewTransactionAdapter adapter = new RecyclerViewTransactionAdapter(models);
        recyclerView.setLayoutManager(new LinearLayoutManager(TransactionHistoryDialog.this.getContext()));
       // recyclerView.setAdapter(adapter);

        builder.setView(view)
                .setPositiveButton(getString(R.string.zxing_button_ok), (dialog, which) -> dialog.dismiss());

        return builder.create();
    }

    public static TransactionHistoryDialog getInstance(List<CashFlowModel> modelList ){
        final Bundle args = new Bundle(1);
        args.putSerializable("extra_model_list", (Serializable) modelList);
        TransactionHistoryDialog fragment = new TransactionHistoryDialog();
        fragment.setArguments(args);
        return fragment;
    }

}
