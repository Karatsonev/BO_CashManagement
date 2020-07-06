package com.example.cashmanagement.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.comm.answer.GetBanknotesInventoryAns;
import com.example.cashmanagement.comm.answer.PayOutBanknotesAns;
import com.example.cashmanagement.comm.answer.SyncGetInventoryAns;
import com.example.cashmanagement.comm.answer.SyncStartDepositAns;
import com.example.cashmanagement.comm.answer.SyncStartPayInAns;
import com.example.cashmanagement.comm.answer.SyncStartPayOutAns;
import com.example.cashmanagement.comm.answer.SyncStopPayInAns;
import com.example.cashmanagement.comm.request.GetBanknotesInventoryReq;
import com.example.cashmanagement.comm.request.PayOutBanknotesReq;
import com.example.cashmanagement.comm.request.SyncGetInventoryReq;
import com.example.cashmanagement.comm.request.SyncStartDepositReq;
import com.example.cashmanagement.comm.request.SyncStartPayInReq;
import com.example.cashmanagement.comm.request.SyncStartPayOutReq;
import com.example.cashmanagement.comm.request.SyncStopPayInReq;
import com.example.cashmanagement.dialogs.BanknotesInventoryDialog;
import com.example.cashmanagement.dialogs.CoinsInventoryDialog;
import com.example.cashmanagement.dialogs.CustomCommonProgressDialog;
import com.example.cashmanagement.dialogs.CustomProgressDialog;
import com.example.cashmanagement.dialogs.CustomProgressDialogWithButton;
import com.example.cashmanagement.models.CashModel;
import com.example.cashmanagement.ui.CashierScreen;
import com.example.cashmanagement.utils.Ini;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PaymentsHelper {

    private static ArrayList<CashModel> inventoryList;
    private static List<CashModel> _cashModelList;

    /**
     * START PAY IN COINS (STATIC METHOD)
     * @param context
     * @param machineCount
     */
    public static void startPayInAsync(Context context,int machineCount) {

        new AsyncTask<Void, Void, SyncStartPayInAns>() {

            //ProgressDialog dialog = new ProgressDialog(context);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //this.dialog.setMessage(context.getString(R.string.please_wait));
                //this.dialog.show();
            }

            @Override
            protected SyncStartPayInAns doInBackground(Void... voids) {
                SyncStartPayInReq syncStartPayInReq = new SyncStartPayInReq();
                syncStartPayInReq.userName = Ini.Server.LoggedUsername;
                syncStartPayInReq.userPass = Ini.Server.LoggedUserPass;
                syncStartPayInReq.machineCount = machineCount;
                return App.commHelper.getAnswer(syncStartPayInReq, SyncStartPayInAns.class);
            }


            @Override
            protected void onPostExecute(SyncStartPayInAns syncStartPayInAns) {
                super.onPostExecute(syncStartPayInAns);
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
//                this.dialog.setMessage(context.getString(R.string.please_wait));
//                this.dialog.show();
            }
        }.execute();

    }

    /**
     * STOP PAY IN COINS
     */
    public static void stopPayInAsync() {

        new AsyncTask<Void, Void, SyncStopPayInAns>() {

            @Override
            protected SyncStopPayInAns doInBackground(Void... voids) {
                SyncStopPayInReq syncStopPayInReq = new SyncStopPayInReq();
                syncStopPayInReq.userName = Ini.Server.LoggedUsername;
                syncStopPayInReq.userPass = Ini.Server.LoggedUserPass;
                return App.commHelper.getAnswer(syncStopPayInReq, SyncStopPayInAns.class);
            }
        }.execute();

    }

    /**
     * START PAY OUT COINS
     * @param activity
     * @param context
     * @param currency
     * @param denomination
     * @param count
     */
    public static void startPayOutAsync(Activity activity,Context context, String currency, double denomination, int count) {

        new AsyncTask<Void, Void, SyncStartPayOutAns>() {

            //ProgressDialog dialog = new ProgressDialog(context);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               // this.dialog.setMessage(context.getString(R.string.please_wait));
               // this.dialog.show();

            }

            @Override
            protected SyncStartPayOutAns doInBackground(Void... voids) {
                SyncStartPayOutReq syncStartPayOutReq = new SyncStartPayOutReq();
                syncStartPayOutReq.userName = Ini.Server.LoggedUsername;
                syncStartPayOutReq.userPass = Ini.Server.LoggedUserPass;
                CashModel cashModel = new CashModel();
                cashModel.Count = count;
                cashModel.CurrencyISOCode = currency;
                cashModel.Denomination = denomination;
                syncStartPayOutReq.cashModelList.add(cashModel);
                return App.commHelper.getAnswer(syncStartPayOutReq, SyncStartPayOutAns.class);
            }

            @Override
            protected void onPostExecute(SyncStartPayOutAns syncStartPayOutAns) {
                super.onPostExecute(syncStartPayOutAns);
               // if (dialog.isShowing()) {
               //     dialog.dismiss();
               // }
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
               // this.dialog.setMessage(context.getString(R.string.please_wait));
               // this.dialog.show();
            }

        }.execute();

    }


    /**
     * GET COINS INVENTORY
     * @param activity
     */
    public static void getCoinsInventoryInfoAsync(Activity activity) {

        App.writeToLog("GetCoinsInventory Information about to start...");
        try {
            if (App.commHelper.isConnected()) {
                new AsyncTask<Void, Void, SyncGetInventoryAns>() {

                    CustomCommonProgressDialog dialog = new CustomCommonProgressDialog();
                    double totalAmount = 0;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        this.dialog.showDialog(activity,activity.getString(R.string.please_wait),R.style.DialogAnimation);
                    }

                    @Override
                    protected SyncGetInventoryAns doInBackground(Void... voids) {
                        SyncGetInventoryReq syncGetInventoryReq = new SyncGetInventoryReq();
                        syncGetInventoryReq.userName = Ini.Server.LoggedUsername;
                        syncGetInventoryReq.userPass = Ini.Server.LoggedUserPass;
                        return App.commHelper.getAnswer(syncGetInventoryReq, SyncGetInventoryAns.class);
                    }


                    @Override
                    protected void onPostExecute(SyncGetInventoryAns syncGetInventoryAns) {
                        super.onPostExecute(syncGetInventoryAns);
                        if (syncGetInventoryAns != null) {
                            if(syncGetInventoryAns.cashModelList.size() != 0){
                                inventoryList = new ArrayList<>();
                                inventoryList.addAll(syncGetInventoryAns.cashModelList);
                                for (CashModel cashModel : inventoryList) {
                                    totalAmount += cashModel.Count * cashModel.Denomination;
                                }
                            }
                        }
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        CoinsInventoryDialog dialog = new CoinsInventoryDialog();
                        Bundle args = new Bundle();
                        args.putSerializable("coinsInventoryList",inventoryList);
                        args.putDouble("totalAmount",totalAmount);
                        dialog.setArguments(args);
                        FragmentManager fragmentManager = ((FragmentActivity)activity).getSupportFragmentManager();
                        dialog.show( fragmentManager, "coin-inventory-dialog");

                    }

                    @Override
                    protected void onProgressUpdate(Void... values) {
                        super.onProgressUpdate(values);
                        this.dialog.showDialog(activity,activity.getString(R.string.please_wait),R.style.DialogAnimation);

                    }
                }.execute();
                inventoryList.clear();

                App.writeToLog("GetCoinsInventory Information successfully finished...");

            } else {
                Toast.makeText(activity, activity.getString(R.string.not_connected_to_server), Toast.LENGTH_SHORT).show();
                App.writeToLog("Not connected to the server");
            }
        } catch (Exception e) {
            App.writeToLog(e.getMessage());
        }

    }

    /**
     * GET BANKNOTES INVENTORY
     * @param activity
     */
    public static void getBanknotesInventoryAsync(Activity activity) {
        App.writeToLog("GetBanknotesInventory  Information about to start...");
        try {
            if (App.commHelper.isConnected()) {

                new AsyncTask<Void, Void, GetBanknotesInventoryAns>() {

                    CustomCommonProgressDialog dialog = new CustomCommonProgressDialog();
                    double totalAmount = 0;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        this.dialog.showDialog(activity,activity.getString(R.string.please_wait),R.style.DialogAnimation);
                    }

                    @Override
                    protected GetBanknotesInventoryAns doInBackground(Void... voids) {
                        GetBanknotesInventoryReq req = new GetBanknotesInventoryReq();
                        req.userName = Ini.Server.LoggedUsername;
                        req.userPass = Ini.Server.LoggedUserPass;
                        return App.commHelper.getAnswer(req, GetBanknotesInventoryAns.class);
                    }


                    @Override
                    protected void onPostExecute(GetBanknotesInventoryAns getBanknotesInventoryAns) {
                        super.onPostExecute(getBanknotesInventoryAns);
                        if (getBanknotesInventoryAns != null) {
                            if(getBanknotesInventoryAns.cashModelList.size() != 0){
                                _cashModelList = new ArrayList<>();
                                _cashModelList.addAll(getBanknotesInventoryAns.cashModelList);
                                for (CashModel model : _cashModelList) {
                                    totalAmount += model.Count * model.Denomination;
                                }
                            }
                        }
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        BanknotesInventoryDialog dialog = new BanknotesInventoryDialog();
                        Bundle args = new Bundle();
                        args.putSerializable("banknotesInventoryList", (Serializable) _cashModelList);
                        args.putDouble("totalAmount",totalAmount);
                        dialog.setArguments(args);
                        FragmentManager fragmentManager = ((FragmentActivity)activity).getSupportFragmentManager();
                        dialog.show( fragmentManager, "banknotes-inventory-dialog");

                    }

                    @Override
                    protected void onProgressUpdate(Void... values) {
                        super.onProgressUpdate(values);
                        this.dialog.showDialog(activity,activity.getString(R.string.please_wait),R.style.DialogAnimation);

                    }
                }.execute();
                _cashModelList.clear();

            } else {
                Toast.makeText(activity, activity.getString(R.string.not_connected_to_server), Toast.LENGTH_SHORT).show();
                App.writeToLog("Not connected to the server");
            }
        } catch (Exception e) {
            App.writeToLog(e.getMessage());
        }



    }

    /**
     * DEPOSIT BANKNOTES (STATIC METHOD)
     * @param machineCount
     */
    public static void depositAsync(int machineCount){
        try {
            Handler handler = new Handler();
            handler.post(() -> {
                SyncStartDepositReq req = new SyncStartDepositReq();
                req.userName = Ini.Server.LoggedUsername;
                req.userPass = Ini.Server.LoggedUserPass;
                req.machineCount = machineCount;
                App.commHelper.getAnswer(req, SyncStartDepositAns.class);
            });
        }catch (Exception e){
            App.writeToLog("Error while deposit banknotes: " + e.getMessage());
        }

  }


    /**
     * CASH OUT BANKNOTES
     * @param activity
     */
    public static void cashOutBanknotesAsync(Activity activity){

        try {
            if (App.commHelper.isConnected()) {
                //show progress
                Handler handler = new Handler();
                handler.post(() -> {
                    PayOutBanknotesReq req = new PayOutBanknotesReq();
                    req.userName = Ini.Server.LoggedUsername;
                    req.userPass = Ini.Server.LoggedUserPass;
                    App.getCommHelper().getAnswer(req,PayOutBanknotesAns.class);
                });
                App.writeToLog("Deposit cash successfully finished");
            } else {
                Toast.makeText(activity, R.string.not_connected_to_server, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            App.writeToLog("Error while executing cash out banknotes: " + e.getMessage());
        }
 }

    /**
     * DEPOSIT BANKNOTES (NON-STATIC METHOD)
     * @param machineCount
     * @param activity
     * @param dialogForBanknotes
     */
    public void depositBanknotesFromPaymentHelper(int machineCount, Activity activity,   CustomProgressDialog dialogForBanknotes) {
        try {

            App.writeToLog("Deposit cash is about to start...");
            if (App.commHelper.isConnected()) {
                dialogForBanknotes.showDialog(activity);

                PaymentsHelper.depositAsync(machineCount);
                App.writeToLog("Deposit cash successfully finished");
            } else {
                Toast.makeText(activity, R.string.not_connected_to_server, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            App.writeToLog("Deposit error: " + e.getMessage());
        }
    }

    /**
     * PAY IN COINS (NON-STATIC METHOD) -> FOR COMBINED DEPOSIT FUNCTION
     * @param activity
     * @param dialogPayInOnlyCoins
     */
    public void payInCoins(Activity activity, CustomProgressDialogWithButton dialogPayInOnlyCoins){
        try {
            dialogPayInOnlyCoins.showDialog(activity, 1);
        }catch (Exception e){
            App.writeToLog("Error while executing Pay In Coins : " + e.getMessage());
        }
    }
}
