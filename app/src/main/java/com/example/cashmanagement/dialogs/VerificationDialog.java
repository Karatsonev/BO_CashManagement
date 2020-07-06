package com.example.cashmanagement.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cashmanagement.R;
import com.example.cashmanagement.ui.SettingsScreen;

public class VerificationDialog extends AppCompatDialogFragment {


    private TextView tvTitle;
    private EditText etPassword;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_verification_dialog,null);
        etPassword = view.findViewById(R.id.et_password);
        tvTitle = view.findViewById(R.id.tv_dialog_title);
        builder.setView(view)
                .setNegativeButton(getString(R.string.btn_cancel), (dialog, which) -> dialog.dismiss())
                .setPositiveButton(getString(R.string.button_submit), (dialog, which) -> {
                    if(etPassword.getText().toString().equals("admin12345")){
                        dialog.dismiss();
                        startActivity(new Intent(getActivity(), SettingsScreen.class));
                    }else {
                        Toast.makeText(getActivity(), R.string.wrong_password_status, Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();
    }
}
