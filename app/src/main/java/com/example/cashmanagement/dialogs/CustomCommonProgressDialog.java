package com.example.cashmanagement.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.TextView;

import com.example.cashmanagement.R;

import java.util.Objects;

public class CustomCommonProgressDialog {

    private Dialog dialog;

    public void showDialog(Activity activity,String message, int animStyle){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_custom_progress_bar);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = animStyle;
        TextView msg = dialog.findViewById(R.id.message);
        msg.setText(message);
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
