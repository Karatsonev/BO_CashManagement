package com.example.cashmanagement.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cashmanagement.R;
import com.example.cashmanagement.utils.Ini;

public class FragmentReports extends Fragment {


    private OnFragmentInteractionListener mListener;
    private static FragmentReports INSTANCE;
    private CardView btnCoinsInventory,btnBanknotesInventory,btnTotalInventory,btnUsersReport,btnTransactionsReport;

    public FragmentReports() {
        // Required empty public constructor
    }

    public static FragmentReports getInstance(){
        if(INSTANCE == null){
            INSTANCE = new FragmentReports();
        }
        return INSTANCE;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports_layout, container, false);
        initControls(view);
        setOnClickListeners();
        if(Ini.Server.LoggedUserType != 2){
            btnUsersReport.setVisibility(View.GONE);
        }
    return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void getReport(String report) {
        if (mListener != null) {
            mListener.onFragmentReportsInteraction(report);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initControls(View view){
        btnBanknotesInventory = view.findViewById(R.id.cardViewInventoryBanknotes);
        btnCoinsInventory = view.findViewById(R.id.cardViewInventoryCoins);
        btnTotalInventory = view.findViewById(R.id.cardViewInventoryTotal);
        btnUsersReport = view.findViewById(R.id.cardViewUsersReport);
        btnTransactionsReport = view.findViewById(R.id.cardViewTransactionsReport);
    }

    private void setOnClickListeners(){
        btnBanknotesInventory.setOnClickListener(v -> getReport("inventoryBanknotes"));
        btnCoinsInventory.setOnClickListener(v -> getReport("inventoryCoins"));
        btnTotalInventory.setOnClickListener(v -> getReport("totalInventory"));
        btnUsersReport.setOnClickListener(v -> getReport("usersReport"));
        btnTransactionsReport.setOnClickListener(v -> getReport("transactionsReport"));

    }

    public interface OnFragmentInteractionListener {
        void onFragmentReportsInteraction(String report);
    }
}
