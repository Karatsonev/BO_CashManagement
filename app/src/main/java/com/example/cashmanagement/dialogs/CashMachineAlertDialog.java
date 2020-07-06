package com.example.cashmanagement.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.TextView;
import com.example.cashmanagement.R;
import java.util.Objects;

public class CashMachineAlertDialog {

    private Dialog dialog;

    public void showDialog(Activity activity, int animStyle){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_cash_machine_alert_dialog);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = animStyle;
        TextView btnOk = dialog.findViewById(R.id.cahMachineAlertDialogOk);
        btnOk.setOnClickListener(v -> dismiss());
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
