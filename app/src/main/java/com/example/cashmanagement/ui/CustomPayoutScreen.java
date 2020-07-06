package com.example.cashmanagement.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.databinding.ActivityCustomPayoutScreenBinding;
import com.example.cashmanagement.dialogs.CustomCommonProgressDialog;
import com.example.cashmanagement.fiscal_receipts.FiscalReceiptCustomCashOut;
import com.example.cashmanagement.helpers.IntentHelper;
import com.example.cashmanagement.helpers.PaymentsHelper;

import java.util.Objects;

public class CustomPayoutScreen extends AppCompatActivity {

    ActivityCustomPayoutScreenBinding binding;
    private Spinner spinnerDenomination;
    private Button btnPayout;
    private TextInputEditText etCount;
    private TextInputLayout til_count;
    private double selectedDenomination;
    //ProgressDialog progressDialog;
    private CustomCommonProgressDialog progressDialog = new CustomCommonProgressDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_custom_payout_screen);
        App.writeToLog("CustomPayOutScreen opened");
        bindControls();
        setupSpinnerDenomination();
        btnPayout.setOnClickListener(v -> executePayout());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IntentHelper.INTENT_RESULT_CASH_OUT_COINS);
        registerReceiver(receiver,intentFilter);

    }

    private void bindControls() {
        spinnerDenomination = binding.spinnerDenomination;
        btnPayout = binding.btnStartPayout;
        etCount = binding.etCount;
        til_count=  binding.textInputLayoutCount;
        //progressDialog = new ProgressDialog(this);
        progressDialog = new CustomCommonProgressDialog();
    }

    private void setupSpinnerDenomination() {
        ArrayAdapter<CharSequence> spinnerDenominationAdapter;
        spinnerDenominationAdapter = ArrayAdapter.createFromResource(this,R.array.denomination_list,android.R.layout.simple_spinner_item);
        spinnerDenominationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDenomination.setAdapter(spinnerDenominationAdapter);
        spinnerDenomination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDenomination = Double.valueOf(spinnerDenomination.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void executePayout() {
        if(App.commHelper.isConnected()) {
            try{
               // progressDialog.setMessage(this.getString(R.string.please_wait));
               // progressDialog.show();
                progressDialog.showDialog(CustomPayoutScreen.this,getString(R.string.please_wait),R.style.DialogAnimation);
                if (!Objects.requireNonNull(etCount.getText()).toString().isEmpty()) {
                    int count = Integer.valueOf(etCount.getText().toString());
                    PaymentsHelper.startPayOutAsync(this,this, "BGN", selectedDenomination, count);
                    til_count.setError(null);
                    etCount.setText("");
                } else {
                    til_count.setError(getString(R.string.error_enter_count));
                   // if(progressDialog.isShowing()){
                   //     progressDialog.dismiss();
                   // }
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                }
            }catch (Exception e){
                App.writeToLog(e.getMessage());}

        }else {
            Toast.makeText(this, getString(R.string.not_connected_to_server), Toast.LENGTH_SHORT).show();
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                if(intent.getAction()!=null){
                    if(intent.getAction().equals(IntentHelper.INTENT_RESULT_CASH_OUT_COINS)){

                        String amount = intent.getStringExtra(IntentHelper.INTENT_IMPORT_STRING_EXTRA_AMOUNT);
                        String count = intent.getStringExtra(IntentHelper.INTENT_IMPORT_STRING_EXTRA_COUNT);
                        String denomination = intent.getStringExtra(IntentHelper.INTENT_IMPORT_STRING_EXTRA_DENOMINATION);

                        FiscalReceiptCustomCashOut receiptHelper = new FiscalReceiptCustomCashOut(CustomPayoutScreen.this,denomination,count,amount);
                        receiptHelper.printReceipt();

                       // if(progressDialog.isShowing()){
                       //     progressDialog.dismiss();
                       // }
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                    }
                }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IntentHelper.INTENT_RESULT_CASH_IN_BANKNOTES);
        registerReceiver(receiver,intentFilter);
    }
}
