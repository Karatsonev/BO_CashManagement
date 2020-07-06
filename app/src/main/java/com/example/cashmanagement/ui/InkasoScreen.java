package com.example.cashmanagement.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.cashmanagement.R;
import com.example.cashmanagement.databinding.ActivityInkasoScreenBinding;
import com.example.cashmanagement.utils.Ini;

public class InkasoScreen extends AppCompatActivity {

    ActivityInkasoScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_inkaso_screen);
        Toast.makeText(this, Ini.Server.LoggedUsername,Toast.LENGTH_LONG).show();
    }

}
