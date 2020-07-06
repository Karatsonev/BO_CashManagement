package com.example.cashmanagement.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.adapters.ViewPagerAdapter;
import com.example.cashmanagement.comm.answer.GetBanknotesInventoryAns;
import com.example.cashmanagement.comm.answer.SyncGetInventoryAns;
import com.example.cashmanagement.comm.request.GetBanknotesInventoryReq;
import com.example.cashmanagement.comm.request.SyncGetInventoryReq;
import com.example.cashmanagement.dialogs.CashMachineAlertDialog;
import com.example.cashmanagement.dialogs.CustomCommonProgressDialog;
import com.example.cashmanagement.dialogs.CustomDialogConfirmCoinsPayOut;
import com.example.cashmanagement.dialogs.CustomProgressDialog;
import com.example.cashmanagement.dialogs.CustomProgressDialogWithButton;
import com.example.cashmanagement.dialogs.TotalInventoryDialog;
import com.example.cashmanagement.fiscal_receipts.FiscalReceiptCashInBanknotes;
import com.example.cashmanagement.fiscal_receipts.FiscalReceiptCashInCoins;
import com.example.cashmanagement.fiscal_receipts.FiscalReceiptCashOutBanknotes;
import com.example.cashmanagement.fiscal_receipts.FiscalReceiptCombinedDepositBanknotes;
import com.example.cashmanagement.fiscal_receipts.FiscalReceiptCombinedDepositCoins;
import com.example.cashmanagement.helpers.IntentHelper;
import com.example.cashmanagement.helpers.PaymentsHelper;
import com.example.cashmanagement.models.CashFlowItemModel;
import com.example.cashmanagement.models.CashModel;
import com.example.cashmanagement.models.LastInventoryItemModel;
import com.example.cashmanagement.models.Status;
import com.example.cashmanagement.ui.fragments.FragmentCashOut;
import com.example.cashmanagement.ui.fragments.FragmentDeposit;
import com.example.cashmanagement.ui.fragments.FragmentReports;
import com.example.cashmanagement.utils.FormatUtils;
import com.example.cashmanagement.utils.Ini;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.nfc.NfcAdapter.ACTION_TECH_DISCOVERED;

public class CoolActivity extends AppCompatActivity
        implements
        FragmentCashOut.OnFragmentInteractionListener,
        FragmentDeposit.OnFragmentInteractionListener,
        FragmentReports.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private PaymentsHelper paymentsHelper;
    private CustomProgressDialogWithButton dialogPayInOnlyCoins,dialogForCoins;
    private CustomProgressDialog dialogForBanknotes;
    private CustomCommonProgressDialog dialogForCashOutBanknotes;
    private double totalCoins,totalBanknotes;
    private ArrayList cashModels_c,cashModels_b;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cool_screen);
        initControls();
        registerReceiver();
        addFragments();

    }

    private void initControls() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        paymentsHelper = new PaymentsHelper();
    }

    private void addFragments() {
        adapter.addFragment(FragmentDeposit.getInstance(), getString(R.string.tab_deposit));
        adapter.addFragment(FragmentCashOut.getInstance(), getString(R.string.tab_cash_out));
        adapter.addFragment(new FragmentReports(), getString(R.string.tab_reports));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onFragmentCashOutInteraction(String type) {
        switch (type){
            case "banknotes":
                dialogForCashOutBanknotes = new CustomCommonProgressDialog();
                dialogForCashOutBanknotes.showDialog(this,getString(R.string.please_wait),R.style.DialogAnimation);
                PaymentsHelper.cashOutBanknotesAsync(this);
                break;
            case "coins":
                startActivity(new Intent(CoolActivity.this,PayOutScreen.class));
                break;
        }
    }

    @Override
    public void onFragmentDepositInteraction(String type) {
        switch (type){
            case "banknotes":
                dialogForBanknotes = new CustomProgressDialog();
                paymentsHelper.depositBanknotesFromPaymentHelper(1,this,dialogForBanknotes);
                break;
            case "coins":
                dialogPayInOnlyCoins = new CustomProgressDialogWithButton();
                paymentsHelper.payInCoins(this,dialogPayInOnlyCoins);
                break;
            case "all":
                dialogForBanknotes = new CustomProgressDialog();
                paymentsHelper.depositBanknotesFromPaymentHelper(2,this,dialogForBanknotes);
                break;
        }
    }

    @Override
    public void onFragmentReportsInteraction(String report) {
        switch (report){
            case "inventoryBanknotes":
                PaymentsHelper.getBanknotesInventoryAsync(this);
                break;
            case "inventoryCoins":
                PaymentsHelper.getCoinsInventoryInfoAsync(this);
                break;
            case "totalInventory":
                getTotalInventory();
                break;
            case "usersReport":
                startActivity(new Intent(this, UsersReportScreen.class));
                break;
            case "transactionsReport":
                startActivity(new Intent(this, TransactionsReportScreen.class));
                break;
        }
    }

    private void getTotalInventory() {
        try{
            CustomCommonProgressDialog progressDialog = new CustomCommonProgressDialog();
            progressDialog.showDialog(this,getString(R.string.please_wait),R.style.DialogAnimation);

            Thread t = new Thread(){
                @Override
                public void run() {
                    cashModels_c = new ArrayList(getCoinsInv());
                    cashModels_b = new ArrayList(getBanknotesInv());
                    TotalInventoryDialog dialog = new TotalInventoryDialog(CoolActivity.this,cashModels_b,cashModels_c,totalBanknotes,totalCoins);
                CoolActivity.this.runOnUiThread(() -> dialog.showDialog(CoolActivity.this,CoolActivity.this));
                progressDialog.dismiss();
                }
            };
            t.start();

            cashModels_b.clear();
            cashModels_c.clear();
            totalBanknotes = 0;
            totalCoins = 0;
        }catch (Exception ex){
            App.writeToLog("Error while Getting TotalInventory: " + ex.getMessage());
        }
    }

    public ArrayList<CashModel> getCoinsInv(){

        ArrayList<CashModel> cashModels_c = new ArrayList<>();

        SyncGetInventoryReq syncGetInventoryReq = new SyncGetInventoryReq();
        syncGetInventoryReq.userName = Ini.Server.LoggedUsername;
        syncGetInventoryReq.userPass = Ini.Server.LoggedUserPass;
        SyncGetInventoryAns ans = App.commHelper.getAnswer(syncGetInventoryReq, SyncGetInventoryAns.class);
        if (ans != null) {
            if(ans.cashModelList.size() != 0){
                cashModels_c.addAll(ans.cashModelList);
                for (CashModel cashModel : cashModels_c) {
                    totalCoins += cashModel.Count * cashModel.Denomination;
                }
            }
        }
        return cashModels_c;
    }

    public ArrayList<CashModel> getBanknotesInv(){

        ArrayList<CashModel> cashModels_b = new ArrayList<>();


        GetBanknotesInventoryReq req = new GetBanknotesInventoryReq();
        req.userName = Ini.Server.LoggedUsername;
        req.userPass = Ini.Server.LoggedUserPass;
        GetBanknotesInventoryAns ans = App.commHelper.getAnswer(req, GetBanknotesInventoryAns.class);
        if (ans != null) {
            if(ans.cashModelList.size() != 0){
                cashModels_b.addAll(ans.cashModelList);
                for (CashModel cashModel : cashModels_b) {
                    totalBanknotes += cashModel.Count * cashModel.Denomination;
                }
            }
        }

        return cashModels_b;
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
        intentFilter.addAction(IntentHelper.INTENT_RESULT_CASH_IN_BANKNOTES);
        intentFilter.addAction(IntentHelper.INTENT_RESULT_CASH_IN_COINS);
        intentFilter.addAction(IntentHelper.INTENT_RESULT_CASH_OUT_BANKNOTES);
        intentFilter.addAction(IntentHelper.INTENT_RESULT_CASH_OUT_COINS);
        intentFilter.addAction(IntentHelper.INTENT_RESULT_TWO_MACHINES);
        intentFilter.addAction(IntentHelper.INTENT_RESULT_CASH_MACHINE_STATUS);
        registerReceiver(receiver, intentFilter);
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                /**
                 * RESULT CASH IN BANKNOTES
                 */
                if (intent.getAction().equals(IntentHelper.INTENT_RESULT_CASH_IN_BANKNOTES)) {
                    String amountStr = intent.getStringExtra(IntentHelper.INTENT_IMPORT_STRING_EXTRA_AMOUNT);
                    double amount = Double.parseDouble(amountStr);
                    List<CashModel> cashModels = (List<CashModel>) intent.getSerializableExtra(IntentHelper.INTENT_IMPORT_EXTRA_LIST);
                    FiscalReceiptCashInBanknotes receiptHelper = new FiscalReceiptCashInBanknotes(CoolActivity.this, cashModels);
                    receiptHelper.printReceipt(amount);
                    if (dialogForBanknotes != null) {
                        dialogForBanknotes.dismiss();
                    }

                    /**
                     * RESULT CASH OUT BANKNOTES
                     */
                } else if (intent.getAction().equals(IntentHelper.INTENT_RESULT_CASH_OUT_BANKNOTES)) {
                    double amount = intent.getDoubleExtra(IntentHelper.INTENT_IMPORT_EXTRA_DECIMAL_AMOUNT, -1);
                    List<LastInventoryItemModel> inventoryItemModels = (List<LastInventoryItemModel>) intent.getSerializableExtra(IntentHelper.INTENT_IMPORT_EXTRA_LIST_INVENTORY_ITEMS);
                    if(amount == 0){
                        if (dialogForCashOutBanknotes != null) {
                            dialogForCashOutBanknotes.dismiss();
                        }
                        return;
                    }else {
                        FiscalReceiptCashOutBanknotes receiptHelper = new FiscalReceiptCashOutBanknotes(CoolActivity.this, inventoryItemModels);
                        receiptHelper.printReceipt(amount);
                    }
                    //hide progress
                    if (dialogForCashOutBanknotes != null) {
                        dialogForCashOutBanknotes.dismiss();
                    }

                 /**
                 * RESULT CASH IN COINS
                 */
            } else if (intent.getAction().equals(IntentHelper.INTENT_RESULT_CASH_IN_COINS)) {
                    String amountStr = intent.getStringExtra(IntentHelper.INTENT_FINISH_PAY_IN_COINS);
                    List<CashFlowItemModel> cashFlowItemModels = (List<CashFlowItemModel>) intent.getSerializableExtra(IntentHelper.INTENT_IMPORT_EXTRA_LIST_COIN);
                    String result = FormatUtils.formatDouble(Double.valueOf(amountStr));

                    FiscalReceiptCashInCoins receiptHelper = new FiscalReceiptCashInCoins(CoolActivity.this, cashFlowItemModels);
                    receiptHelper.printReceipt(result);
                    if (dialogForCoins != null) {
                        dialogForCoins.dismiss();
                    }
                    if (dialogPayInOnlyCoins != null) {
                        dialogPayInOnlyCoins.dismiss();
                    }

                /**
                 * RESULT CASH IN OF BOTH (COIN AND BANKNOTE) MACHINES
                 */
            } else if (intent.getAction().equals(IntentHelper.INTENT_RESULT_TWO_MACHINES)) {
                if (intent.getStringExtra(IntentHelper.INTENT_IMPORT_STRING_EXTRA_AMOUNT) != null) {
                    String amountStr = intent.getStringExtra(IntentHelper.INTENT_IMPORT_STRING_EXTRA_AMOUNT);
                    double amount = Double.parseDouble(amountStr);
                    Ini.Server.TotalDepositAmount = amount;
                    App.saveSettings();
                    List<CashModel> cashModels = (List<CashModel>) intent.getSerializableExtra(IntentHelper.INTENT_IMPORT_EXTRA_LIST);
                    FiscalReceiptCombinedDepositBanknotes receiptHelper = new FiscalReceiptCombinedDepositBanknotes(CoolActivity.this, cashModels);
                    receiptHelper.printReceipt(amount);
                    if (dialogForBanknotes != null) {
                        dialogForBanknotes.dismiss();
                    }
                    dialogForCoins = new CustomProgressDialogWithButton();
                    CustomDialogConfirmCoinsPayOut dialogForConfirmation = new CustomDialogConfirmCoinsPayOut(dialogForBanknotes, dialogForCoins);
                    dialogForConfirmation.showDialog(CoolActivity.this);

                }else if(intent.getStringExtra(IntentHelper.INTENT_FINISH_PAY_IN_COINS) != null){
                    String amountStr = intent.getStringExtra(IntentHelper.INTENT_FINISH_PAY_IN_COINS);
                    List<CashFlowItemModel> cashFlowItemModels = (List<CashFlowItemModel>) intent.getSerializableExtra(IntentHelper.INTENT_IMPORT_EXTRA_LIST_COIN);
                    String result = FormatUtils.formatDouble(Double.valueOf(amountStr));
                    double amount = Double.parseDouble(amountStr);
                    Ini.Server.TotalDepositAmount += amount;
                    App.saveSettings();
                    FiscalReceiptCombinedDepositCoins receiptHelper = new FiscalReceiptCombinedDepositCoins(CoolActivity.this, cashFlowItemModels);
                    receiptHelper.printReceipt(result);
                    if (dialogForCoins != null) {
                        dialogForCoins.dismiss();
                    }
                    if (dialogPayInOnlyCoins != null) {
                        dialogPayInOnlyCoins.dismiss();
                    }

                    Ini.Server.TotalDepositAmount = 0;
                    App.saveSettings();
                }

                    /**
                     * RESULT OF CASH MACHINE STATUS (ALARM, BAG STATE, DOOR STATE, KEY STATE
                     */
            }else if(intent.getAction().equals(IntentHelper.INTENT_RESULT_CASH_MACHINE_STATUS)){
                Status machineStatus = (Status) intent.getSerializableExtra(IntentHelper.INTENT_IMPORT_STRING_EXTRA_CASHMACHINE_STATUS);
                if(machineStatus.IsBagFull){
                    if(dialogForBanknotes!=null){
                        dialogForBanknotes.dismiss();
                    }
                    CashMachineAlertDialog dialog = new CashMachineAlertDialog();
                    dialog.showDialog(CoolActivity.this,R.style.DialogTheme);
                }
               }
           }
        }
    };
}