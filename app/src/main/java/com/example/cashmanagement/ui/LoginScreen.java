package com.example.cashmanagement.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.cashmanagement.App;
import com.example.cashmanagement.BuildConfig;
import com.example.cashmanagement.R;
import com.example.cashmanagement.comm.CommHelper;
import com.example.cashmanagement.comm.answer.SyncGetUserAns;
import com.example.cashmanagement.comm.request.SyncGetUserReq;
import com.example.cashmanagement.databinding.ActivityLoginScreenBinding;
import com.example.cashmanagement.dialogs.CustomCommonProgressDialog;
import com.example.cashmanagement.utils.Ini;
import java.util.Objects;


public class LoginScreen extends AppCompatActivity {

    private static ActivityLoginScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_screen);
        App.writeToLog("Login Screen started.");
        getUserInfo();
        binding.btnSubmit.setOnClickListener(v -> logInAsync(this));
    }

    private void getUserInfo() {
        if (App.commHelper.isConnected()) {
            getUserInfoAsync(this,this);
        } else {
            openDialog();
        }
    }

    private void openDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginScreen.this).create();
        alertDialog.setTitle(R.string.warning);
        alertDialog.setIcon(R.drawable.ic_exclamation_mark);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(getString(R.string.connection_status_not_connected));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                (dialog, which) -> {
                    dialog.dismiss();
                    startActivity(new Intent(LoginScreen.this, LaunchScreen.class));
                });
        alertDialog.show();
    }

    /**
     * LOGIC FOR THE LOGIN
     * @param activity
     */
    private static void logInAsync(Activity activity) {

        String username = Ini.Server.SavedUser;
        if (!Objects.requireNonNull(binding.passwordEditText.getText()).toString().isEmpty()) {
            String userPass = binding.passwordEditText.getText().toString();
            binding.passwordTextInput.setError(null);
            if (CommHelper.isConnected) {
                App.writeToLog("Device is connected to server...");
                App.writeToLog("loginAsync is about to start...");
                new AsyncTask<Void, Void, SyncGetUserAns>() {

                    CustomCommonProgressDialog dialog = new CustomCommonProgressDialog();

                    @Override
                    protected SyncGetUserAns doInBackground(Void... voids) {
                        SyncGetUserReq syncGetUserReq = new SyncGetUserReq();
                        syncGetUserReq.userName = username;
                        syncGetUserReq.userPass = App.EncryptPassword(userPass);
                        return App.commHelper.getAnswer(syncGetUserReq, SyncGetUserAns.class);
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        this.dialog.showDialog(activity,activity.getString(R.string.please_wait),R.style.DialogAnimation);
                    }

                    @Override
                    protected void onPostExecute(SyncGetUserAns syncGetUserAns) {
                        super.onPostExecute(syncGetUserAns);
                        if (syncGetUserAns != null) {

                            if (syncGetUserAns.commError.isEmpty() && syncGetUserAns.opError.isEmpty()) {
                                binding.result.setTextColor(activity.getColor(R.color.green));
                                binding.result.setText(R.string.successfully_logged);
                                Ini.Server.LoggedUsername = syncGetUserAns.userName;
                                Ini.Server.LoggedUserPass = App.EncryptPassword(userPass);
                                Ini.Server.LoggedUserType = syncGetUserAns.typeId;
                                App.saveSettings();
                                //isLoginSuccessfully = true;
                                decideWhichScreenToStart(activity);


                                App.writeToLog("syncGetUserAns: " + R.string.successfully_logged);
                            } else if (!syncGetUserAns.opError.isEmpty()) {
                                binding.result.setTextColor(activity.getColor(R.color.colorAccent));
                                binding.result.setText(activity.getString(R.string.invalid_password));
                                App.writeToLog("syncGetUserAns: " + syncGetUserAns.opError);
                            } else {
                                binding.result.setTextColor(activity.getColor(R.color.colorAccent));
                                binding.result.setText(syncGetUserAns.commError);
                                App.writeToLog("syncGetUserAns: " + syncGetUserAns.commError);
                            }
                        } else {
                            binding.result.setTextColor(activity.getColor(R.color.colorAccent));
                            binding.result.setText(R.string.server_answer_null);
                            App.writeToLog("syncGetUserAns: " + R.string.server_answer_null);

                        }
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    protected void onProgressUpdate(Void... values) {
                        super.onProgressUpdate(values);
                        this.dialog.showDialog(activity,activity.getString(R.string.please_wait),R.style.DialogAnimation);

                    }
                }.execute();

            }
        } else {
            binding.passwordTextInput.setError((activity.getString(R.string.error_password_required)));
        }
    }

    private static void decideWhichScreenToStart(Context context) {
      if(Ini.Server.LoggedUserType != 5){
          context.startActivity(new Intent(context,CoolActivity.class));
      }else{
          context.startActivity(new Intent(context,InkasoScreen.class));
      }

    }

    /**
     * FIND OUT WHICH USER IS TRYING TO LOGIN
     * @param activity
     * @param context
     */
    private static void getUserInfoAsync(Activity activity,Context context) {
        if (CommHelper.isConnected) {
            App.writeToLog("Device is connected to server...");
            App.writeToLog("getUserInfoAsync is about to start...");
            new AsyncTask<Void, Void, SyncGetUserAns>() {

                //ProgressDialog dialog = new ProgressDialog(context);
                CustomCommonProgressDialog progressDialog = new CustomCommonProgressDialog();

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                   this.progressDialog.showDialog(activity,context.getString(R.string.please_wait),R.style.DialogAnimation);
                }

                @Override
                protected SyncGetUserAns doInBackground(Void... voids) {
                    SyncGetUserReq syncGetUserReq = new SyncGetUserReq();
                    Ini.Server.Password = "PGqE6U79AmFlB8V8yNt8V38Lk0Lb7bxaNf7k5CXxH7U="; // this can be in App.java
                    App.saveSettings();
                    syncGetUserReq.userPass = Ini.Server.Password;
                    syncGetUserReq.userCard = Ini.Server.CardNumber;

                    return App.commHelper.getAnswer(syncGetUserReq, SyncGetUserAns.class);
                }

                @Override
                protected void onPostExecute(SyncGetUserAns syncGetUserAns) {
                    super.onPostExecute(syncGetUserAns);
                    if (syncGetUserAns != null) {
                        binding.result.setText(context.getString(R.string.hello) + " " + syncGetUserAns.userName);
                        Ini.Server.SavedUser = syncGetUserAns.userName;
                        App.saveSettings();
                        if (BuildConfig.DEBUG) {
                            if (Ini.Server.SavedUser != null) {
                                switch (Ini.Server.SavedUser) {
                                    case "cashier":
                                        binding.passwordEditText.setText("cashier");
                                        break;
                                    case "main cashier":
                                        binding.passwordEditText.setText("maincashier");
                                        break;
                                    case "inkaso":
                                        binding.passwordEditText.setText("incaso");
                                        break;
                                }
                            }
                        }
                    } else {
                        binding.result.setText("Server not respond!");
                        App.writeToLog("syncGetUserAns: null");
                    }
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }

                @Override
                protected void onProgressUpdate(Void... values) {
                    super.onProgressUpdate(values);
                    this.progressDialog.showDialog(activity,context.getString(R.string.please_wait),R.style.DialogAnimation);

                }
            }.execute();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,LaunchScreen.class));
        finish();
    }


}
