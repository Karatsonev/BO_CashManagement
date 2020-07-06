package com.example.cashmanagement.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.Button;

import com.example.cashmanagement.R;
import com.example.cashmanagement.helpers.PrintHelper;

import java.util.Objects;

public class CustomDialogConfirmCoinsPayOut {
    private Dialog dialog;
    //private CustomProgressDialog _customProgressDialog;
    private CustomProgressDialogWithButton _customProgressDialogWithButton;
    public CustomDialogConfirmCoinsPayOut(CustomProgressDialog customProgressDialog, CustomProgressDialogWithButton customProgressDialogWithButton){
        //_customProgressDialog = customProgressDialog;
        _customProgressDialogWithButton = customProgressDialogWithButton;
    }

    public void showDialog(Activity activity){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_custom_dialog_confirm_coin_payout);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogTheme;
        Button btnConfirm = dialog.findViewById(R.id.btnProceedForward);
        Button btnNotConfirm = dialog.findViewById(R.id.btnNotProceedForward);
        btnConfirm.setOnClickListener(v -> {
                    _customProgressDialogWithButton.showDialog(activity,2);
                    dialog.dismiss();
                }
        );

        btnNotConfirm.setOnClickListener(v ->   {
            PrintHelper.getInstance().print3Line();
            dialog.dismiss();
        });

        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

}
