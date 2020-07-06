package com.example.cashmanagement.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.databinding.ActivityPayInScreenBinding;
import com.example.cashmanagement.fiscal_receipts.FiscalReceiptCashInCoins;
import com.example.cashmanagement.helpers.IntentHelper;
import com.example.cashmanagement.helpers.PaymentsHelper;
import com.example.cashmanagement.models.CashFlowItemModel;
import com.example.cashmanagement.utils.FormatUtils;

import java.util.List;


public class PayInScreen extends AppCompatActivity {

    ActivityPayInScreenBinding binding;
    private Button btnStartPayIn,btnStopPayIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_pay_in_screen);
        App.writeToLog("PayInScreen opened");
        bindControls();
        registerReceiver();
        btnStartPayIn.setOnClickListener(v -> startPayIn());
        btnStopPayIn.setOnClickListener(v -> stopPayIn());

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void bindControls() {
        btnStartPayIn = binding.btnStartPayIn;
        btnStopPayIn = binding.btnStopPayIn;
    }

    private void startPayIn() {
        App.writeToLog("Pay In process about to start...");
        try{
            if(App.commHelper.isConnected()){
                PaymentsHelper.startPayInAsync(this,1);
                App.writeToLog("Pay In process successfully finished");
            }else {
                Toast.makeText(this, getString(R.string.not_connected_to_server), Toast.LENGTH_SHORT).show();
                App.writeToLog("Not connected to the server");
            }
        }catch (Exception e){
            App.writeToLog(e.getMessage());
        }
    }

    private void stopPayIn() {
        App.writeToLog("Stop Pay In process about to start...");
        try{
            if(App.commHelper.isConnected()){
                PaymentsHelper.stopPayInAsync();
                App.writeToLog("Stop Pay In process successfully finished");

            }else {
                Toast.makeText(this, getString(R.string.not_connected_to_server), Toast.LENGTH_SHORT).show();
                App.writeToLog("Not connected to the server");
            }
        }catch (Exception e){
            App.writeToLog(e.getMessage());
        }
    }

    private void registerReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IntentHelper.INTENT_RESULT_CASH_IN_BANKNOTES);
        registerReceiver(myReceiver, intentFilter);
    }

    //RECEIVING BROADCAST MESSAGE WITH THE APPROPRIATE DATA , WHEN SYNC MESSAGE IS RECEIVED
    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
              if(intent.getAction()!=null){
                  if(intent.getAction().equals(IntentHelper.INTENT_RESULT_CASH_IN_BANKNOTES)){
                      String amountStr = intent.getStringExtra(IntentHelper.INTENT_FINISH_PAY_IN_COINS);
                      List<CashFlowItemModel> cashFlowItemModels = (List<CashFlowItemModel>)intent.getSerializableExtra(IntentHelper.INTENT_IMPORT_EXTRA_LIST_COIN);
                      String result = FormatUtils.formatDouble(Double.valueOf(amountStr));

                      FiscalReceiptCashInCoins receiptHelper = new FiscalReceiptCashInCoins(PayInScreen.this,cashFlowItemModels);
                      receiptHelper.printReceipt(result);
                  }
              }
        }
    };

}
