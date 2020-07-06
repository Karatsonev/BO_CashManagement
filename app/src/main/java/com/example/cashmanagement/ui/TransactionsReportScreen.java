package com.example.cashmanagement.ui;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.adapters.RecyclerViewOperatorReportAdapter;
import com.example.cashmanagement.comm.answer.reports.GetAllOperatorsAns;
import com.example.cashmanagement.comm.answer.reports.GetAllOperatorsReportAns;
import com.example.cashmanagement.comm.answer.reports.GetReportCashierAns;
import com.example.cashmanagement.comm.request.reports.GetAllOperatorsReportReq;
import com.example.cashmanagement.comm.request.reports.GetAllOperatorsReq;
import com.example.cashmanagement.comm.request.reports.GetReportCashierReq;
import com.example.cashmanagement.dialogs.CustomCommonProgressDialog;
import com.example.cashmanagement.fiscal_receipts.FiscalReceiptTransactionsReport;
import com.example.cashmanagement.helpers.PrintHelper;
import com.example.cashmanagement.models.BonLayoutElement;
import com.example.cashmanagement.models.CashFlowModel;
import com.example.cashmanagement.models.UserModel;
import com.example.cashmanagement.utils.FormatUtils;
import com.example.cashmanagement.utils.Ini;
import com.example.cashmanagement.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TransactionsReportScreen extends AppCompatActivity {

    private RecyclerViewOperatorReportAdapter adapter;
    private TextView tvDateFrom, tvDateTo,tvSelectSearch;
    private DatePickerDialog.OnDateSetListener dateSetFromListener, dateSetToListener;
    private Spinner spinnerSelectOperator,spinnerSelectTransactionType;
    private List<String> operators,transactionTypes;
    private Button btnShowResult,btnPrintTransactions;
    private Date dDateFrom , dDateTo;
    private List<CashFlowModel> cashFlowModels;
    private  RelativeLayout layout;
    private List<UserModel> users;
    //private ProgressDialog progressDialog;
     private CustomCommonProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        initControls();
        btnPrintTransactions.setVisibility(View.INVISIBLE);
        setupDateFrom();
        setupDateTo();
        populateUsers();
        setupUsersSpinner();
        setupTransactionTypeSpinner();
        setupRecyclerView();
        btnShowResult.setOnClickListener(v -> showSearchResult());
        btnPrintTransactions.setOnClickListener(v -> printTransactions());
        progressDialog = new CustomCommonProgressDialog();
    }

    private void setupRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view_operator_report);
        adapter = new RecyclerViewOperatorReportAdapter(cashFlowModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(TransactionsReportScreen.this));
        recyclerView.setAdapter(adapter);
    }

    private void showSearchResult() {

       tvSelectSearch.setVisibility(View.GONE);

       progressDialog.showDialog(TransactionsReportScreen.this,getString(R.string.please_wait),R.style.DialogAnimation);

        Thread t = new Thread(){
            @Override
            public void run() {
                if(Ini.Server.IsOperatorSelected){
                    cashFlowModels = getOperatorReport();
                }else{
                    cashFlowModels = getAllOperatorsReport();
                }
              TransactionsReportScreen.this.runOnUiThread(() -> {
                  layout.setVisibility(View.VISIBLE);
                  adapter.notifyDataSetChanged();
                  btnPrintTransactions.setVisibility(View.VISIBLE);
              });
               if(progressDialog != null){
                   progressDialog.dismiss();
               }
            }
        };
        t.start();

    }

    private void printTransactions(){
      FiscalReceiptTransactionsReport receiptHelper = new FiscalReceiptTransactionsReport(this,cashFlowModels);
      receiptHelper.printReceipt();
    }

    private void initControls() {
        tvDateFrom = findViewById(R.id.tv_select_date_from);
        tvDateTo = findViewById(R.id.tv_select_date_to);
        spinnerSelectOperator = findViewById(R.id.spinner_select_operator);
        spinnerSelectTransactionType = findViewById(R.id.spinner_select_transaction_type);
        btnShowResult = findViewById(R.id.btnShowResult);
        btnPrintTransactions = findViewById(R.id.btnPrintTransactions);
        tvSelectSearch = findViewById(R.id.select_search);
        layout= findViewById(R.id.header);

        layout.setVisibility(View.INVISIBLE);
        users = new ArrayList<>();
        operators = new ArrayList<>();
        transactionTypes = new ArrayList<>();
        cashFlowModels = new ArrayList<>();
        transactionTypes.add(getString(R.string.select_transaction_type));
        operators.add(getString(R.string.select_operator));
    }

    private void setupDateFrom() {
        tvDateFrom.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);

            DatePickerDialog dialog = new DatePickerDialog(
                    TransactionsReportScreen.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateSetFromListener,
                    year, month, day);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        dateSetFromListener = (view, year, month, dayOfMonth) -> {
            int _month = month + 1;
             String dateFrom =  dayOfMonth + "-" + _month + "-" + year;
            tvDateFrom.setText(dateFrom);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dDateFrom = cal.getTime();
        };
    }

    private void setupDateTo() {
        tvDateTo.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);

            DatePickerDialog dialog = new DatePickerDialog(
                    TransactionsReportScreen.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateSetToListener,
                    year, month, day);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        dateSetToListener = (view, year, month, dayOfMonth) -> {
            int _month = month + 1;
             String dateTo =  dayOfMonth + "-" + _month + "-" + year;
            tvDateTo.setText(dateTo);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth + 1);
            dDateTo = cal.getTime();

        };
    }

    private void setupUsersSpinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, operators);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectOperator.setAdapter(adapter);
        spinnerSelectOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!parent.getItemAtPosition(position).equals(getString(R.string.select_operator_spinner_item))) {

                    Ini.Server.SelectedOperator = parent.getSelectedItem().toString();
                    Ini.Server.IsOperatorSelected = true;
                    App.saveSettings();
                }else {
                    // all operators
                    Ini.Server.IsOperatorSelected = false;
                    App.saveSettings();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Ini.Server.IsOperatorSelected = false;
                App.saveSettings();
            }
        });
    }

    private void setupTransactionTypeSpinner(){
        transactionTypes.add(getString(R.string.cashIn));
        transactionTypes.add(getString(R.string.cashOut));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, transactionTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectTransactionType.setAdapter(adapter);
        spinnerSelectTransactionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!parent.getItemAtPosition(position).equals(getString(R.string.select_transaction_type))) {

                    Ini.Server.SelectedTransactionType = parent.getSelectedItem().toString();
                    Ini.Server.IsTransactionTypeSelected = true;
                    App.saveSettings();
                }else {
                    // all operators
                    Ini.Server.IsTransactionTypeSelected = false;
                    App.saveSettings();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Ini.Server.IsTransactionTypeSelected = false;
                App.saveSettings();
            }
        });

    }

    private List<CashFlowModel> getOperatorReport() {
         int operatorID = 0;
        //match username by userID
        for(int i=0; i<users.size(); i++){
            if(users.get(i).loginName.equals(Ini.Server.SelectedOperator)){
                operatorID = users.get(i).userId;
            }
        }
        //-----------------------

        GetReportCashierReq req = new GetReportCashierReq();
        req.userName = Ini.Server.LoggedUsername;
        req.userPass = Ini.Server.LoggedUserPass;
        req.dateTo = dDateTo;
        req.dateFrom = dDateFrom;
        req.operatorId = operatorID;
        req.isTransactionTypeSelected = Ini.Server.IsTransactionTypeSelected;
        if(Ini.Server.SelectedTransactionType.equals(getString(R.string.cashIn)))
        {
            req.transactionType = 1;
        }else{
            req.transactionType = 2;
        }

        GetReportCashierAns ans = App.commHelper.getAnswer(req,GetReportCashierAns.class);
        if(ans != null){
            cashFlowModels.clear();
            for(int i=0; i<ans.cashFlowList.size(); i++){
                CashFlowModel model = new CashFlowModel();
                model.fullName = ans.cashFlowList.get(i).fullName;
                model.typeId = ans.cashFlowList.get(i).typeId;
                model.dateTimeStart = ans.cashFlowList.get(i).dateTimeStart;
              //  model.dateTimeEnd = ans.cashFlowList.get(i).dateTimeEnd;
                model.amount = ans.cashFlowList.get(i).amount;
                cashFlowModels.add(model);
            }
        }
        return cashFlowModels;
    }
    private void populateUsers(){
        operators.clear();
        operators.add(getString(R.string.select_operator));
        new AsyncTaskPopulateUsers(operators,users).execute();
    }

    private List<CashFlowModel> getAllOperatorsReport() {
        GetAllOperatorsReportReq request = new GetAllOperatorsReportReq();
        request.userName = Ini.Server.LoggedUsername;
        request.userPass = Ini.Server.LoggedUserPass;
        request.dateFrom = dDateFrom;
        request.dateTo = dDateTo;
        request.isTransactionTypeSelected = Ini.Server.IsTransactionTypeSelected;

        if(Ini.Server.SelectedTransactionType.equals(getString(R.string.cashIn)))
        {
            request.transactionType = 1;
        }else{
            request.transactionType = 2;
        }

        GetAllOperatorsReportAns ans = App.commHelper.getAnswer(request,GetAllOperatorsReportAns.class);
        cashFlowModels.clear();
        if(ans != null){
            for(int i=0; i<ans.cashFlowList.size(); i++){
                CashFlowModel model = new CashFlowModel();
                model.fullName = ans.cashFlowList.get(i).fullName;
                model.typeId = ans.cashFlowList.get(i).typeId;
                model.dateTimeStart = ans.cashFlowList.get(i).dateTimeStart;
                //model.dateTimeEnd = ans.cashFlowList.get(i).dateTimeEnd;
                model.amount = ans.cashFlowList.get(i).amount;
                cashFlowModels.add(model);
            }
        }
        return cashFlowModels;
    }

    static class AsyncTaskPopulateUsers extends AsyncTask<Void,Void,Void>{

        List<String> operators;
        List<UserModel> users;

        public  AsyncTaskPopulateUsers(List<String> _operators, List<UserModel> _users){
            operators = _operators;
            users = _users;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            GetAllOperatorsReq req = new GetAllOperatorsReq();
            req.userName = Ini.Server.LoggedUsername;
            req.userPass = Ini.Server.LoggedUserPass;
            GetAllOperatorsAns ans = App.commHelper.getAnswer(req, GetAllOperatorsAns.class);
            if (ans != null) {
                for (int i = 0; i < ans.usersList.size(); i++) {
                    operators.add(ans.usersList.get(i).loginName);
                    users.add((ans.usersList.get(i)));
                }
            }
            return null;
        }

    }

}


