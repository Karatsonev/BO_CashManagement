package com.example.cashmanagement.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.databinding.ActivityPayOutScreenBinding;
import com.example.cashmanagement.utils.Ini;

public class PayOutScreen extends AppCompatActivity {

    ActivityPayOutScreenBinding binding;
    private Button btnSet1,btnSet2,btnCustomSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_pay_out_screen);
        App.writeToLog("PayOutScreen opened");
        initControls();
        btnSet1.setOnClickListener(v -> loadSet1Screen());
        btnSet2.setOnClickListener(v -> loadSet2Screen());
        btnCustomSet.setOnClickListener(v -> openCustomPayoutScreen());

    }

    private void initControls() {
        btnSet1 = binding.btnSet1;
        btnSet2 = binding.btnSet2;
        btnCustomSet = binding.btnCustomSet;
    }

    private void loadSet1Screen() {
//        Intent intent = new Intent(this,SetScreen.class);
//        intent.putExtra(IntentHelper.EXTRA_SET_KEY,IntentHelper.EXTRA_SET_1);
//        startActivity(intent);

//        MutableLiveData<List<SetDataModel>> mutableLiveData;
//        List<SetDataModel> setDataModels = new ArrayList<>();
//        setDataModels.add(new SetDataModel("BGN", 0.5, 1));
//        setDataModels.add(new SetDataModel("BGN", 0.1, 2));
//        Intent intent = new Intent(this,ActivityExperiment.class);
//        intent.putExtra( "key", (Serializable) setDataModels );
//        startActivity(intent);
        Ini.Server.SetKind = "set1";
        App.saveSettings();
        startActivity(new Intent(PayOutScreen.this, SetScreen.class));
        finish();
    }

    private void loadSet2Screen() {
//        Intent intent = new Intent(this,SetScreen.class);
//        intent.putExtra(IntentHelper.EXTRA_SET_KEY,IntentHelper.EXTRA_SET_2);
//        startActivity(intent);
        Ini.Server.SetKind = "set2";
        App.saveSettings();
        startActivity(new Intent(PayOutScreen.this, SetScreen.class));
        finish();

    }

    private void openCustomPayoutScreen() {
        startActivity(new Intent(this,CustomPayoutScreen.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
