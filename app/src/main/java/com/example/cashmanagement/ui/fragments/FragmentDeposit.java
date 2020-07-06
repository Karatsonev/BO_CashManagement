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
import android.widget.ImageView;

import com.example.cashmanagement.R;
import com.example.cashmanagement.helpers.PaymentsHelper;

import java.util.Objects;

public class FragmentDeposit extends Fragment {

    private OnFragmentInteractionListener mListener;
    private CardView btnDepositAll,btnDepositBanknotes,btnDepositCoins;
    private static FragmentDeposit INSTANCE;

    public FragmentDeposit() {
        // Required empty public constructor
    }

    public static FragmentDeposit getInstance(){
        if(INSTANCE == null){
            INSTANCE = new FragmentDeposit();
        }
        return INSTANCE;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deposit_layout, container, false);
        initControls(view);
        setOnCLickListeners();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initControls(@NonNull View view) {
        btnDepositAll = view.findViewById(R.id.cardViewDepositAll);
        btnDepositBanknotes = view.findViewById(R.id.cardViewDepositBanknotes);
        btnDepositCoins = view.findViewById(R.id.cardViewDepositCoins);
    }

    private void setOnCLickListeners(){
        btnDepositAll.setOnClickListener(v -> depositAll());
        btnDepositBanknotes.setOnClickListener(v -> depositBanknotes());
        btnDepositCoins.setOnClickListener(v -> depositCoins());
    }

    private void depositCoins() {
        deposit("coins");
    }

    private void depositBanknotes() {
      deposit("banknotes");
    }

    private void depositAll() {
        deposit("all");
    }

    public void deposit(String type) {
        if (mListener != null) {
            mListener.onFragmentDepositInteraction(type);
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

    public interface OnFragmentInteractionListener {
        void onFragmentDepositInteraction(String type);
    }
}
