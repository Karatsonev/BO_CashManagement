package com.example.cashmanagement.dialogs;

import android.app.Activity;
import android.app.Dialog;

import android.view.Window;
import android.widget.Button;

import com.example.cashmanagement.R;
import com.example.cashmanagement.helpers.PaymentsHelper;

import java.util.Objects;

public class CustomProgressDialogWithButton  {

    private Dialog dialog;

    public void showDialog(Activity activity,int machineCount){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_custom_progress_dialog_with_button);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogTheme;
        Button btnFinishDeposit = dialog.findViewById(R.id.btnFinishWithDeposit);
        PaymentsHelper.startPayInAsync(activity,machineCount);
        btnFinishDeposit.setOnClickListener(v -> PaymentsHelper.stopPayInAsync());
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
