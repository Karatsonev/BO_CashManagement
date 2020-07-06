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


public class FragmentCashOut extends Fragment {

    private OnFragmentInteractionListener mListener;
    private static FragmentCashOut INSTANCE;
    private CardView btnCashOutBanknotes,btnCashOutCoins;

    public FragmentCashOut() {
        // Required empty public constructor
    }

    public static FragmentCashOut getInstance(){
        if(INSTANCE == null){
            INSTANCE = new FragmentCashOut();
        }
        return INSTANCE;
    }

    public static FragmentCashOut newInstance(String param1, String param2) {
        return new FragmentCashOut();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cash_out_layout, container, false);
        initControls(view);
        setOnClickListeners();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void cashOut(String type) {
        if (mListener != null) {
            mListener.onFragmentCashOutInteraction(type);
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
        btnCashOutBanknotes = view.findViewById(R.id.cardViewCashOutBanknotes);
        btnCashOutCoins = view.findViewById(R.id.cardViewCashOutCoins);
    }

    private void setOnClickListeners(){
        btnCashOutBanknotes.setOnClickListener(v -> cashOutBanknotes());
        btnCashOutCoins.setOnClickListener(v -> cashOutCoins());
    }

    private void cashOutBanknotes(){
        cashOut("banknotes");
    }

    private void cashOutCoins(){
        cashOut("coins");
    }

    public interface OnFragmentInteractionListener {
        void onFragmentCashOutInteraction(String type);
    }
}
