package com.example.cashmanagement.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.adapters.RecyclerViewAllOperatorsReportAdapter;
import com.example.cashmanagement.comm.answer.reports.GetAllOperatorsAns;
import com.example.cashmanagement.comm.request.reports.GetAllOperatorsReq;
import com.example.cashmanagement.dialogs.CustomCommonProgressDialog;
import com.example.cashmanagement.dialogs.EditUserDialog;
import com.example.cashmanagement.models.UserModel;
import com.example.cashmanagement.utils.Ini;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsersReportScreen extends AppCompatActivity
        implements RecyclerViewAllOperatorsReportAdapter.OnItemClickListener {

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private static RecyclerViewAllOperatorsReportAdapter adapter;
    private RecyclerView recyclerView;
    private List<UserModel> users;
    private int adapterPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_report);
        users = new ArrayList<>();
        initControls();
        setupRecyclerView();
        loadUsers();

        fab.setOnClickListener(v -> startActivity(new Intent(UsersReportScreen.this, CreateUserScreen.class)));
    }

    private void loadUsers() {
    //new AsyncTaskPopulateUsers(users).execute();
        GetAllOperators(this,this,users);
    }

    private void initControls() {
    fab = findViewById(R.id.fab_add_user);
    toolbar = findViewById(R.id.users_report_toolbar);
    setSupportActionBar(toolbar);
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("");
    recyclerView = findViewById(R.id.recycler_view_operators_report);
    }

    private void setupRecyclerView(){
        adapter = new RecyclerViewAllOperatorsReportAdapter(this,users,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(UsersReportScreen.this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEditClick(UserModel model, ImageView imageView,int position) {
        try {
            adapterPosition = position;
            EditUserDialog dialog = new EditUserDialog();
            Bundle args = new Bundle();
            args.putString("loginName",model.loginName);
            args.putString("fullName", model.fullName);
            args.putBoolean("enabled", model.enabled);
            args.putInt("position", position);
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "edit-user-dialog");
        }catch (Exception e){App.writeToLog(e.getMessage());}
    }

    @Override
    public void onDetailsClick() {

    }

    public void notifyAdapter(boolean _enabled, String _lastEdit){
        //users.clear();
        users.get(adapterPosition).enabled = _enabled;
        users.get(adapterPosition).lastEdit = _lastEdit;
        adapter.notifyItemChanged(adapterPosition);
    }

    private static void GetAllOperators(Activity activity, Context context, List<UserModel> users){
        new AsyncTask<Void,Void,GetAllOperatorsAns>(){

            //private ProgressDialog dialog = new ProgressDialog(context);
            CustomCommonProgressDialog dialog = new CustomCommonProgressDialog();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               dialog.showDialog(activity,context.getString(R.string.please_wait),R.style.DialogAnimation);
            }

            @Override
            protected GetAllOperatorsAns doInBackground(Void... voids) {
                GetAllOperatorsReq req = new GetAllOperatorsReq();
                req.userName = Ini.Server.LoggedUsername;
                req.userPass = Ini.Server.LoggedUserPass;
                return App.commHelper.getAnswer(req, GetAllOperatorsAns.class);
            }

            @Override
            protected void onPostExecute(GetAllOperatorsAns ans) {
                super.onPostExecute(ans);

                if(ans!=null){
                    for(int i=0; i<ans.usersList.size(); i++){
                        UserModel model = new UserModel();
                        model.userId = ans.usersList.get(i).userId;
                       // model.loginName = ans.usersList.get(i).loginName;
                        model.card = ans.usersList.get(i).card;
                        model.posCode = ans.usersList.get(i).posCode;
                        model.egn = ans.usersList.get(i).egn;
                        model.email = ans.usersList.get(i).email;
                        model.enabled = ans.usersList.get(i).enabled;
                        model.expiration = ans.usersList.get(i).expiration;
                        model.expired = ans.usersList.get(i).expired;
                        model.fullName = ans.usersList.get(i).fullName;
                        model.lastEdit = ans.usersList.get(i).lastEdit;
                        model.phone = ans.usersList.get(i).phone;
                        model.policy = ans.usersList.get(i).policy;
                        model.typeId = ans.usersList.get(i).typeId;
                        model.validity = ans.usersList.get(i).validity;
                        users.add(model);
                    }
                    adapter.notifyDataSetChanged();
                }

                if(dialog != null){
                    dialog.dismiss();
                }
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
                dialog.showDialog(activity,context.getString(R.string.please_wait),R.style.DialogAnimation);

            }
        }.execute();
    }

    static class AsyncTaskPopulateUsers extends AsyncTask<Void,Void,GetAllOperatorsAns> {

        List<UserModel> users;

        public  AsyncTaskPopulateUsers( List<UserModel> _users){
            users = _users;
        }

        @Override
        protected GetAllOperatorsAns doInBackground(Void... voids) {
            GetAllOperatorsReq req = new GetAllOperatorsReq();
            req.userName = Ini.Server.LoggedUsername;
            req.userPass = Ini.Server.LoggedUserPass;
            return App.commHelper.getAnswer(req, GetAllOperatorsAns.class);
        }

        @Override
        protected void onPostExecute(GetAllOperatorsAns ans) {
            super.onPostExecute(ans);
            if(ans!=null){
                for(int i=0; i<ans.usersList.size(); i++){
                    UserModel model = new UserModel();
                    model.userId = ans.usersList.get(i).userId;
                    model.loginName = ans.usersList.get(i).loginName;
                    model.card = ans.usersList.get(i).card;
                    model.dateCreated = ans.usersList.get(i).dateCreated;
                    model.egn = ans.usersList.get(i).egn;
                    model.email = ans.usersList.get(i).email;
                    model.enabled = ans.usersList.get(i).enabled;
                    model.expiration = ans.usersList.get(i).expiration;
                    model.expired = ans.usersList.get(i).expired;
                    model.fullName = ans.usersList.get(i).fullName;
                    model.lastEdit = ans.usersList.get(i).lastEdit;
                    model.phone = ans.usersList.get(i).phone;
                    model.policy = ans.usersList.get(i).policy;
                    model.typeId = ans.usersList.get(i).typeId;
                    model.validity = ans.usersList.get(i).validity;
                    users.add(model);
                }
                adapter.notifyDataSetChanged();
            }

        }
    }
}
