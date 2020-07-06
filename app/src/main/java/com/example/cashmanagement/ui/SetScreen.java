package com.example.cashmanagement.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.adapters.RecyclerViewSetAdapter;
import com.example.cashmanagement.dialogs.CustomCommonProgressDialog;
import com.example.cashmanagement.fiscal_receipts.FiscalReceiptPredefinedCashOut;
import com.example.cashmanagement.helpers.IntentHelper;
import com.example.cashmanagement.helpers.PaymentsHelper;
import com.example.cashmanagement.helpers.PrintHelper;
import com.example.cashmanagement.models.BonLayoutElement;
import com.example.cashmanagement.models.SetDataModel;
import com.example.cashmanagement.utils.FormatUtils;
import com.example.cashmanagement.utils.Ini;
import com.example.cashmanagement.viewmodels.SetScreenViewModel;
import java.util.ArrayList;
import java.util.List;

public class SetScreen extends AppCompatActivity {

    private List<SetDataModel> setDataModelList = new ArrayList<>();
    private int counter = 0;
    private int index = 0;
    private Handler handler = new Handler();
    //private ProgressDialog progressDialog;
    private SetScreenViewModel mViewModel;
    private RecyclerViewSetAdapter adapter;
    private CustomCommonProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_screen);
        Button buttonGo = findViewById(R.id.button_go2);
        mViewModel = ViewModelProviders.of(this).get(SetScreenViewModel.class);
        progressDialog = new CustomCommonProgressDialog();
        Ini.Server.SyncPayOutResult = 0;
        App.saveSettings();
        populateDataModelList();
        setupRecyclerView();
        registerReceiver();
        buttonGo.setOnClickListener(v -> payOut());

        //observe our List<SetDataModel>...
        mViewModel.getData().observe(this, setDataModels -> {
           if(setDataModels != null){
               setDataModelList = setDataModels;
              // adapter.setModelList(setDataModels);
           }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IntentHelper.INTENT_RESULT_CASH_OUT_COINS);
        registerReceiver(receiver, intentFilter);
    }

    private void populateDataModelList() {
        setDataModelList = mViewModel.getData().getValue();
    }

    private boolean isOk(double amount, int count, double denomination) {
        int amountCoins = (int) (amount * 100);
        int amountDenomination = (int) (count * denomination * 100);

        return amountCoins == amountDenomination;
    }

    private void leaveActivity(){
        handler.postDelayed(() -> {
            startActivity(new Intent(this, PayOutScreen.class));
            finish();
        },5000);
    }

    private void payOut() {
        showProgress();
        if (counter >= setDataModelList.size()) {
            hideProgress();
            return;
        }
        while (setDataModelList.get(counter).getModelCount() == 0) {
            counter++;
            if (counter >= setDataModelList.size()) {
                hideProgress();
                return;
            }
        }
        PaymentsHelper.startPayOutAsync(this,this,
                setDataModelList.get(counter).getModelCurrency(),
                setDataModelList.get(counter).getModelDenomination(),
                setDataModelList.get(counter).getModelCount());

        counter++;
    }

    private void setupRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view_set);
        adapter = new RecyclerViewSetAdapter(setDataModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(SetScreen.this));
        recyclerView.setAdapter(adapter);
    }

    private void showProgress(){
        progressDialog.showDialog(SetScreen.this,getString(R.string.please_wait),R.style.DialogAnimation);

    }

    private void hideProgress(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null)
                if (intent.getAction().equals(IntentHelper.INTENT_RESULT_CASH_OUT_COINS)) {

                    String amountStr = intent.getStringExtra(IntentHelper.INTENT_IMPORT_STRING_EXTRA_AMOUNT);
                    String countStr = intent.getStringExtra(IntentHelper.INTENT_IMPORT_STRING_EXTRA_COUNT);
                    String denominationStr = intent.getStringExtra(IntentHelper.INTENT_IMPORT_STRING_EXTRA_DENOMINATION);

                    double denomination = Double.valueOf(denominationStr);
                    double denominationDouble = Double.parseDouble(denominationStr);
                    double amount = Double.valueOf(amountStr);
                    int count = Integer.valueOf(countStr);

                    if (isOk(amount, count, denomination)) {
                        hideProgress();
                        setDataModelList.get(counter - 1).setStatusSuccess(getString(R.string.succeed));
                        adapter.notifyDataSetChanged();
                    }else {
                        hideProgress();
                        setDataModelList.get(counter -1).setStatusFailed(getString(R.string.failed));
                        adapter.notifyDataSetChanged();
                    }
                    // PRINT HEADER
                    if(index == 0) {
                        FiscalReceiptPredefinedCashOut headerReceiptHelper = new FiscalReceiptPredefinedCashOut(SetScreen.this,"","","",new ArrayList<SetDataModel>());
                        headerReceiptHelper.printReceiptHeader();
                        index = 1;
                    }
                    // PRINT RESULT FOR EVERY SINGLE LINE

                    BonLayoutElement bContent = new BonLayoutElement(FormatUtils.formatDouble(denominationDouble) + "        " + countStr + "        " + FormatUtils.formatDouble(amount), 25, false, false);
                    PrintHelper.getInstance().printText(bContent.getmContent(),bContent.getmTextSize(),bContent.isBold(),bContent.isUnderlined());

                    if(counter >= setDataModelList.size() ){
                        //PRINT TOTAL AMOUNT
                        String totalAmount = getString(R.string.total_amount)+ " " + FormatUtils.formatDouble(Ini.Server.SyncPayOutResult) + " BGN";

//                            *******************CONSTRUCTING THE BON*********************************
                        BonLayoutElement bottomStars = new BonLayoutElement("-----------------------------",25,false,false);
                        BonLayoutElement bAmount = new BonLayoutElement(totalAmount,25,false,false);

                        List<BonLayoutElement> bonLayoutElementList = new ArrayList<>();
                        bonLayoutElementList.add(bottomStars);
                        bonLayoutElementList.add(bAmount);

                        for (BonLayoutElement model : bonLayoutElementList){

                            PrintHelper.getInstance().printText(
                                    model.getmContent(),
                                    model.getmTextSize(),
                                    model.isBold(),
                                    model.isUnderlined());
                        }
                        PrintHelper.getInstance().print3Line();
//                            ************************************************************************
                        index = 0;
                        leaveActivity();
                    }
                }
        }
    };

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(this, CoolActivity.class));
//    }
}
