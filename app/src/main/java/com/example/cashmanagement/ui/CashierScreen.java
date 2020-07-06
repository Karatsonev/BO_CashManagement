package com.example.cashmanagement.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.comm.answer.GetBanknotesInventoryAns;
import com.example.cashmanagement.comm.answer.SyncGetInventoryAns;
import com.example.cashmanagement.comm.request.GetBanknotesInventoryReq;
import com.example.cashmanagement.comm.request.SyncGetInventoryReq;
import com.example.cashmanagement.databinding.ActivityCashierScreenBinding;
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
import com.example.cashmanagement.utils.FormatUtils;
import com.example.cashmanagement.utils.Ini;

import java.util.ArrayList;
import java.util.List;


public class CashierScreen extends AppCompatActivity {

    ActivityCashierScreenBinding binding;
    private Button btnPayIn, btnPayOut, btnGetInventory, btnGetTransactionHistory, btnDepositCash, btnTest, btnPayOutBanknotes, getBtnGetInventoryBanknotes, btnDeposit, btnGetTotalInventory;
    private CustomProgressDialog dialogForBanknotes;
    private CustomCommonProgressDialog dialogForCashOutBanknotes;
    private static CustomCommonProgressDialog dialogForTotalInventory;
    private CustomProgressDialogWithButton dialogForCoins;
    private CustomProgressDialogWithButton dialogPayInOnlyCoins;
    private double totalCoins,totalBanknotes;
    private ArrayList cashModels_c,cashModels_b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cashier_screen);
        App.writeToLog("CashierScreen opened");
        bindControls();
        registerReceiver();
        setOnClickListeners();

    }

    private void bindControls() {
        btnPayIn = binding.btnPayInCashier;
        btnPayOut = binding.btnPayOutCashier;
        btnGetInventory = binding.btnGetInventory;
        btnGetTransactionHistory = binding.btnTransactionHistory;
        btnDepositCash = binding.btnDepositCash;
        btnTest = binding.btnTEST;
        btnPayOutBanknotes = binding.btnCashOutBanknotes;
        getBtnGetInventoryBanknotes = binding.btnGetInventoryBanknotes;
        btnDeposit = binding.btnDepositAll;
        btnGetTotalInventory = binding.btnGetTotalInventory;

    }

    private void setOnClickListeners() {
        btnPayIn.setOnClickListener(v -> payInCoins());
        btnPayOut.setOnClickListener(v -> openPayOutScreen());
        btnGetInventory.setOnClickListener(v -> getCoinsInventory());
        btnGetTransactionHistory.setOnClickListener(v -> openTransactionsReportScreen());
        btnDepositCash.setOnClickListener(v -> depositBanknotes(1));
        btnTest.setOnClickListener(v -> startActivity(new Intent(CashierScreen.this, UsersReportScreen.class)));
        btnPayOutBanknotes.setOnClickListener(v -> cashOutBanknotes());
        getBtnGetInventoryBanknotes.setOnClickListener(v -> getBanknotesInventory());
        btnDeposit.setOnClickListener(v -> depositAll());
        btnGetTotalInventory.setOnClickListener(v -> getTotalInventory());
    }

    private void getTotalInventory() {
        try{
            Handler handler = new Handler();
            handler.post(() -> {

                cashModels_c = new ArrayList(getCoinsInv());
                cashModels_b = new ArrayList(getBanknotesInv());

                TotalInventoryDialog dialog = new TotalInventoryDialog(CashierScreen.this,cashModels_b,cashModels_c,totalBanknotes,totalCoins);
                dialog.showDialog(CashierScreen.this,CashierScreen.this);
            });
            cashModels_b.clear();
            cashModels_c.clear();
            totalBanknotes = 0;
            totalCoins = 0;
        }catch (Exception ex){
            App.writeToLog("Error while Getting TotalInventory: " + ex.getMessage());
        }
    }


    private void depositAll() {
        depositBanknotes(2);
    }

    private void getBanknotesInventory() {
        PaymentsHelper.getBanknotesInventoryAsync(this);
    }

    private void getCoinsInventory() {
        PaymentsHelper.getCoinsInventoryInfoAsync(this);
    }

    private void cashOutBanknotes() {
        try {
            if (App.commHelper.isConnected()) {
                showProgress();
                PaymentsHelper.cashOutBanknotesAsync(this);
                App.writeToLog("Deposit cash successfully finished");
            } else {
                Toast.makeText(this, R.string.not_connected_to_server, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            App.writeToLog("Error while executing cash out banknotes: " + e.getMessage());
        }
    }

    private void payInCoins() {
        dialogPayInOnlyCoins = new CustomProgressDialogWithButton();
        dialogPayInOnlyCoins.showDialog(CashierScreen.this,1);
    }

    private void openPayOutScreen() {
        startActivity(new Intent(CashierScreen.this, PayOutScreen.class));
    }

    private void openTransactionsReportScreen() {
        startActivity(new Intent(CashierScreen.this, TransactionsReportScreen.class));
    }

    private void depositBanknotes(int machineCount) {
        try {
            App.writeToLog("Deposit cash is about to start...");
            if (App.commHelper.isConnected()) {
                dialogForBanknotes = new CustomProgressDialog();
                dialogForBanknotes.showDialog(CashierScreen.this);

                PaymentsHelper.depositAsync(machineCount);
                App.writeToLog("Deposit cash successfully finished");
            } else {
                Toast.makeText(this, R.string.not_connected_to_server, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            App.writeToLog("Deposit error: " + e.getMessage());
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

    private void startPayIn() {
        try {
            if (App.commHelper.isConnected()) {
                PaymentsHelper.startPayInAsync(this,1);
                App.writeToLog("Pay In process successfully finished");
            } else {
                Toast.makeText(this, getString(R.string.not_connected_to_server), Toast.LENGTH_SHORT).show();
                App.writeToLog("Not connected to the server");
            }
        } catch (Exception e) {
            App.writeToLog(e.getMessage());
        }
    }

    private void showProgress() {
       dialogForCashOutBanknotes = new CustomCommonProgressDialog();
        dialogForCashOutBanknotes.showDialog(this,getString(R.string.please_wait),R.style.DialogAnimation);
    }

    private void hideProgress() {
        if (dialogForCashOutBanknotes != null) {
            dialogForCashOutBanknotes.dismiss();
        }
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
        intentFilter.addAction(IntentHelper.INTENT_RESULT_CASH_OUT_BANKNOTES);
        intentFilter.addAction(IntentHelper.INTENT_RESULT_CASH_IN_COINS);
        intentFilter.addAction(IntentHelper.INTENT_RESULT_TWO_MACHINES);
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
                        FiscalReceiptCashInBanknotes receiptHelper = new FiscalReceiptCashInBanknotes(CashierScreen.this, cashModels);
                        receiptHelper.printReceipt(amount);
                        if (dialogForBanknotes != null) {
                            dialogForBanknotes.dismiss();
                        }
//                    }else if(intent.getDoubleExtra(IntentHelper.INTENT_IMPORT_EXTRA_DECIMAL_AMOUNT,-1) != -1 && intent.getSerializableExtra(IntentHelper.INTENT_IMPORT_EXTRA_LIST_INVENTORY_ITEMS) != null){
//                        double amount = intent.getDoubleExtra(IntentHelper.INTENT_IMPORT_EXTRA_DECIMAL_AMOUNT,-1);
//                        List<LastInventoryItemModel> inventoryItemModels = (List<LastInventoryItemModel>) intent.getSerializableExtra(IntentHelper.INTENT_IMPORT_EXTRA_LIST_INVENTORY_ITEMS);
//                        FiscalReceiptCashOutBanknotes receiptHelper = new FiscalReceiptCashOutBanknotes(CashierScreen.this,inventoryItemModels);
//                        receiptHelper.printReceipt(amount);
//                        hideProgress();
//                    }else if(intent.getStringExtra(IntentHelper.INTENT_FINISH_PAY_IN_COINS) != null){
//                        String amountStr = intent.getStringExtra(IntentHelper.INTENT_FINISH_PAY_IN_COINS);
//                        List<CashFlowItemModel> cashFlowItemModels = (List<CashFlowItemModel>)intent.getSerializableExtra(IntentHelper.INTENT_IMPORT_EXTRA_LIST_COIN);
//                        String result = FormatUtils.formatDouble(Double.valueOf(amountStr));
//
//                        FiscalReceiptCashInCoins receiptHelper = new FiscalReceiptCashInCoins(CashierScreen.this,cashFlowItemModels);
//                        receiptHelper.printReceipt(result);
//                        if(dialogForCoins != null){
//                            dialogForCoins.dismiss();
//                        }
//                        if(dialogPayInOnlyCoins != null){
//                            dialogPayInOnlyCoins.dismiss();
//                        }
//                    }
                    /**
                     * RESULT CASH OUT BANKNOTES
                     */
                    } else if (intent.getAction().equals(IntentHelper.INTENT_RESULT_CASH_OUT_BANKNOTES)) {
                        double amount = intent.getDoubleExtra(IntentHelper.INTENT_IMPORT_EXTRA_DECIMAL_AMOUNT, -1);
                        List<LastInventoryItemModel> inventoryItemModels = (List<LastInventoryItemModel>) intent.getSerializableExtra(IntentHelper.INTENT_IMPORT_EXTRA_LIST_INVENTORY_ITEMS);
                        FiscalReceiptCashOutBanknotes receiptHelper = new FiscalReceiptCashOutBanknotes(CashierScreen.this, inventoryItemModels);
                        receiptHelper.printReceipt(amount);
                        hideProgress();

                    /**
                     * RESULT CASH IN COINS
                     */
                    } else if (intent.getAction().equals(IntentHelper.INTENT_RESULT_CASH_IN_COINS)) {
                        String amountStr = intent.getStringExtra(IntentHelper.INTENT_FINISH_PAY_IN_COINS);
                        List<CashFlowItemModel> cashFlowItemModels = (List<CashFlowItemModel>) intent.getSerializableExtra(IntentHelper.INTENT_IMPORT_EXTRA_LIST_COIN);
                        String result = FormatUtils.formatDouble(Double.valueOf(amountStr));

                        FiscalReceiptCashInCoins receiptHelper = new FiscalReceiptCashInCoins(CashierScreen.this, cashFlowItemModels);
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
                            FiscalReceiptCombinedDepositBanknotes receiptHelper = new FiscalReceiptCombinedDepositBanknotes(CashierScreen.this, cashModels);
                            receiptHelper.printReceipt(amount);
                            if (dialogForBanknotes != null) {
                                dialogForBanknotes.dismiss();
                            }
                            dialogForCoins = new CustomProgressDialogWithButton();
                            CustomDialogConfirmCoinsPayOut dialogForConfirmation = new CustomDialogConfirmCoinsPayOut(dialogForBanknotes, dialogForCoins);
                            dialogForConfirmation.showDialog(CashierScreen.this);

                        }else if(intent.getStringExtra(IntentHelper.INTENT_FINISH_PAY_IN_COINS) != null){
                            String amountStr = intent.getStringExtra(IntentHelper.INTENT_FINISH_PAY_IN_COINS);
                            List<CashFlowItemModel> cashFlowItemModels = (List<CashFlowItemModel>) intent.getSerializableExtra(IntentHelper.INTENT_IMPORT_EXTRA_LIST_COIN);
                            String result = FormatUtils.formatDouble(Double.valueOf(amountStr));
                            double amount = Double.parseDouble(amountStr);
                            Ini.Server.TotalDepositAmount += amount;
                            App.saveSettings();
                            FiscalReceiptCombinedDepositCoins receiptHelper = new FiscalReceiptCombinedDepositCoins(CashierScreen.this, cashFlowItemModels);
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
                    }
                }
            }
        };
    }

