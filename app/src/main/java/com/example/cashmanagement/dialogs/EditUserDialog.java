package com.example.cashmanagement.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.comm.answer.crud.EditUserAns;
import com.example.cashmanagement.comm.request.crud.EditUserReq;
import com.example.cashmanagement.ui.UsersReportScreen;
import com.example.cashmanagement.utils.Ini;
import com.example.cashmanagement.utils.TimeUtil;

import java.util.Objects;


public class EditUserDialog extends AppCompatDialogFragment {

    private TextView fullName;
    private EditText etLoginName;
    private Switch switch_user_enabled;
    private boolean isEnabled;
    private int position;
    //private Button btnSubmit,btnCancel;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_edit_user_dialog,null);

        fullName = view.findViewById(R.id.tv_fullname_edit_user);
        //etLoginName = view.findViewById(R.id.et_loginName_edit_user);
        switch_user_enabled = view.findViewById(R.id.switch_enabled_edit_user);
       // btnSubmit = view.findViewById(R.id.btn_submit_edit_user);
        //btnCancel = view.findViewById(R.id.btn_back_from_editing_operator);

        if(getArguments() != null){
            position = getArguments().getInt(("position"));
            fullName.setText(getArguments().getString("fullName"));
            isEnabled = getArguments().getBoolean("enabled");
            if(isEnabled){
                switch_user_enabled.setText(getString(R.string.user_enabled));
                switch_user_enabled.setChecked(true);
            }else{
                switch_user_enabled.setText(getString(R.string.user_disabled));
                switch_user_enabled.setChecked(false);
            }
        }
        switch_user_enabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                switch_user_enabled.setText(getString(R.string.user_enabled));
                isEnabled = true;
            }else {
                switch_user_enabled.setText(getString(R.string.user_disabled));
                isEnabled = false;
            }
        });

        builder.setView(view)
                .setNegativeButton(getString(R.string.btn_cancel), (dialog, which) -> dialog.dismiss())
                .setPositiveButton(getString(R.string.button_submit), (dialog, which) -> {
                 // btn submit action ...
                    EditUserReq req = new EditUserReq();
                    req.userName = Ini.Server.LoggedUsername;
                    req.userPass = Ini.Server.LoggedUserPass;
                    req.enabled = isEnabled;
                    req.fullName = fullName.getText().toString().trim();
                    EditUserAns ans = App.commHelper.getAnswer(req,EditUserAns.class);
                    if(ans.editUserResult == 1){
                        String now = TimeUtil.getDateOnly();
                        ((UsersReportScreen)getActivity()).notifyAdapter(req.enabled,now);
                        dialog.dismiss();
                    }

                });
        return builder.create();
    }
}
