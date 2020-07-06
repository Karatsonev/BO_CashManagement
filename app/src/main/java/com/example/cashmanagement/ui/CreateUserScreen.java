package com.example.cashmanagement.ui;


import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.comm.answer.crud.CreateUserAns;
import com.example.cashmanagement.comm.request.crud.CreateUserReq;
import com.example.cashmanagement.dialogs.CustomCommonProgressDialog;
import com.example.cashmanagement.models.UserModel;
import com.example.cashmanagement.utils.Hex;
import com.example.cashmanagement.utils.Ini;
import com.example.cashmanagement.utils.TimeUtil;
import java.util.ArrayList;
import java.util.List;



public class CreateUserScreen extends AppCompatActivity {

    private Spinner spinner_userType,spinner_user_enabled;
    private List<String> typeOfUsers,usersStatus;
    private String userType,userEnabled;
    private Button btnCreateUser,btnCancel;
    EditText loginName,fullName,password,repeatPassword,posCode,card,phone,email;
    private CustomCommonProgressDialog dialog;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_screen);
        initControls();
        setupUserTypeSpinner();
        setupUsersStatusSpinner();
        btnCreateUser.setOnClickListener(v -> createOperator());
        btnCancel.setOnClickListener(v -> navigateBack());
        initNFC(this);
    }

    private void navigateBack() {
    finish();
    }

    private void createOperator() {
        try {
            new CreateUserAsyncTask().execute();
        }catch (Exception e){
            App.writeToLog("Error while executing CreateUserAsyncTask: " + e.getMessage());
        }
    }


    private void initControls() {
        btnCreateUser = findViewById(R.id.btn_create_operator);
        btnCancel = findViewById(R.id.btn_back_from_creating_operator);
        spinner_userType = findViewById(R.id.spinner_userType);
        spinner_user_enabled = findViewById(R.id.spinner_enabled_create_user);
        typeOfUsers = new ArrayList<>();
        usersStatus = new ArrayList<>();

        typeOfUsers.add(getString(R.string.select_type));
        typeOfUsers.add(getString(R.string.administrator));
        typeOfUsers.add(getString(R.string.inkaso));
        typeOfUsers.add(getString(R.string.cashier));
        typeOfUsers.add(getString(R.string.main_cashier));

        usersStatus.add(getString(R.string.select_status));
        usersStatus.add(getString(R.string.user_enabled));
        usersStatus.add(getString(R.string.user_disabled));

        loginName = findViewById(R.id.et_loginName_create_user);
        fullName = findViewById(R.id.et_fullName_create_user);
        password = findViewById(R.id.et_password_create_user);
        repeatPassword = findViewById(R.id.et_repeat_password_create_user);
        posCode = findViewById(R.id.et_posCode);
        card = findViewById(R.id.et_card_create_user);
        phone = findViewById(R.id.et_phone_create_user);
        email = findViewById(R.id.et_email_create_user);

        dialog = new CustomCommonProgressDialog();
    }

    private void setupUserTypeSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeOfUsers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_userType.setAdapter(adapter);
        spinner_userType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).equals(getString(R.string.select_type))) {
                    userType = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupUsersStatusSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, usersStatus);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_user_enabled.setAdapter(adapter);
        spinner_user_enabled.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).equals(getString(R.string.select_status))) {
                    userEnabled = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

     private class CreateUserAsyncTask extends AsyncTask<Void,Void,CreateUserAns>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.showDialog(CreateUserScreen.this,getString(R.string.please_wait),R.style.DialogAnimation);
        }

        @Override
        protected CreateUserAns doInBackground(Void... voids) {

            UserModel model = new UserModel();

            model.loginName = loginName.getText().toString();


            if(userType.equals(getString(R.string.administrator))){
                model.typeId = 1;
            }else if(userType.equals(getString(R.string.cashier))){
                model.typeId = 3;
            }else if(userType.equals(getString(R.string.main_cashier))){
                model.typeId = 4;
            }else if(userType.equals(getString(R.string.inkaso))){
                model.typeId = 5;
            }

            if(userEnabled.equals(getString(R.string.user_enabled))){
                model.enabled = true;
            }else{
                model.enabled = false;
            }
            model.password = App.EncryptPassword(password.getText().toString());
            model.lastEdit = TimeUtil.getTime();
            model.policy = true;
            model.fullName = fullName.getText().toString();
            model.posCode = Integer.parseInt(posCode.getText().toString());
            model.dateCreated = TimeUtil.getTime();
            model.card = card.getText().toString();
            model.email = email.getText().toString();
            model.phone = phone.getText().toString();

            CreateUserReq req = new CreateUserReq();
            req.userName = Ini.Server.LoggedUsername;
            req.userPass = Ini.Server.LoggedUserPass;
            req.model = model;
            return App.commHelper.getAnswer(req,CreateUserAns.class);
        }

        @Override
        protected void onPostExecute(CreateUserAns ans) {
            super.onPostExecute(ans);
            if(ans!=null){
                int result = ans.createUserResult;
                App.writeToLog("Create User Result: " + result);
            }
            dialog.dismiss();
            Toast.makeText(CreateUserScreen.this, getString(R.string.user_successfuly_created), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            dialog.showDialog(CreateUserScreen.this,getString(R.string.please_wait),R.style.DialogAnimation);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableReaderMode(this, tag -> {
            new CreateUserScreen.NdefReaderTask(this).execute(tag);
        }, NfcAdapter.FLAG_READER_NFC_A , null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableReaderMode(this);
    }
    /**
     * INITIALIZE NFC CARD READER
     * @param context
     */
    public void initNFC(Context context) {
        App.writeToLog("Initializing NFC...");
        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if (mNfcAdapter == null || !mNfcAdapter.isEnabled()) {
            Toast.makeText(context, "NFC not available", Toast.LENGTH_SHORT).show();
            App.writeToLog("NFC not available");
        }
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        handleIntent(intent);
//    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            new CreateUserScreen.NdefReaderTask(this).execute(tag);
        }
    }

    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        Tag tag;
        Context context;

        public NdefReaderTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Tag... params) {
            tag = params[0];
            App.writeToLog("Evaluating NFC Tag...");
//            Ini.Server.CardNumber = Hex.bytesToHex(tag.getId());
//            App.saveSettings();

            return Hex.bytesToHex(tag.getId());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            card.setText(s);
        }
    }
}
