package com.example.cashmanagement.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.adapters.RecyclerViewTransactionAdapter;
import com.example.cashmanagement.comm.answer.SyncGetUserTransactionHistoryAns;
import com.example.cashmanagement.comm.request.SyncGetUserTransactionHistoryReq;
import com.example.cashmanagement.models.CashFlowModel;
import com.example.cashmanagement.utils.Ini;
import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryScreen extends AppCompatActivity implements RecyclerViewTransactionAdapter.OnItemClickListener {

    private RecyclerViewTransactionAdapter adapter;
    private List<CashFlowModel> cashFlowModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history_screen);
        cashFlowModels = getTransactionHistory();
        setupRecyclerView();

    }

    private void setupRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view_screen);
        adapter = new RecyclerViewTransactionAdapter(cashFlowModels,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(TransactionHistoryScreen.this));
        recyclerView.setAdapter(adapter);
    }

    private List<CashFlowModel> getTransactionHistory() {
        SyncGetUserTransactionHistoryReq request = new SyncGetUserTransactionHistoryReq();
        request.usernameReq = Ini.Server.LoggedUsername;
        SyncGetUserTransactionHistoryAns answer = App.commHelper.getAnswer(request,SyncGetUserTransactionHistoryAns.class);
        return answer.cashFlowModels;
    }

    @Override
    public void onItemClick(int position) {
    CashFlowModel model = cashFlowModels.get(position);

    }
}
