package com.example.cashmanagement.ui;


import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.databinding.ActivitySettingsScreenBinding;
import com.example.cashmanagement.utils.Ini;
import java.util.Objects;

public class SettingsScreen extends AppCompatActivity {

    private ActivitySettingsScreenBinding binding;
    private TextInputEditText username, password, ipAddress;
    private TextInputLayout til_username, til_password, til_ipAddress;
    private Button btnSaveSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings_screen);
        App.writeToLog("Settings Screen started.");
        bindViews();
        setViews();
        btnSaveSettings.setOnClickListener(v -> validateFields());
    }

    private void setViews() {
        username.setText(Ini.Server.Username);
        ipAddress.setText(Ini.Server.Address);

    }

    private void bindViews() {
        username = binding.nameEditText;
        password = binding.passwordEditText;
        ipAddress = binding.ipEditText;
        til_username = binding.nameTextInput;
        til_password = binding.passwordTextInput;
        til_ipAddress = binding.ipTextInput;
        btnSaveSettings = binding.btnSaveSettings;
    }

    private void validateFields() {
        boolean isValid = true;
        if (Objects.requireNonNull(username.getText()).toString().isEmpty()) {
            til_username.setError(getString(R.string.error_username_required));
            isValid = false;
        } else {
            til_username.setError(null);
        }
        if (Objects.requireNonNull(password.getText()).toString().isEmpty()) {
            til_password.setError(getString(R.string.error_password_required));
            isValid = false;
        } else {
            til_password.setError(null);
        }
        if (Objects.requireNonNull(ipAddress.getText()).toString().isEmpty()) {
            til_ipAddress.setError(getString(R.string.error_ip_address_required));
            isValid = false;
        } else {
            til_ipAddress.setError(null);
        }
        if (isValid) {
            if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
                Ini.Server.Username = username.getText().toString();
                Ini.Server.Password = password.getText().toString();
                Ini.Server.Address = ipAddress.getText().toString();
                App.saveSettings();
                //App.getCommHelper().InitSignalR();
                ConnectAsyncTask task = new ConnectAsyncTask();
                task.execute();
                Toast.makeText(this, getString(R.string.settings_saved_successfully), Toast.LENGTH_SHORT).show();
                App.writeToLog("Settings successfully changed");
            } else {
                Toast.makeText(this, getString(R.string.wrong_username_or_password), Toast.LENGTH_SHORT).show();
            }
        }
    }

    static class ConnectAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            App.connect();
//            GetParametersReq req = new GetParametersReq();
//            req.userPass = "PGqE6U79AmFlB8V8yNt8V38Lk0Lb7bxaNf7k5CXxH7U=";
//            GetParametersAns ans = App.commHelper.getAnswer(req, GetParametersAns.class);
//            if (ans != null) {
//                if (ans.commError.isEmpty() && ans.opError.isEmpty()) {
//                    App.writeToLog("Settings successfully changed");
//                    return "Settings successfully changed!";
//                } else if (!ans.opError.isEmpty()) {
//                    App.writeToLog(ans.opError);
//                    return "Settings successfully changed!";
//                } else {
//                    App.writeToLog(ans.commError);
//                    return ans.commError;
//                }
//            } else {
//                return "Can't connect to server!";
//            }
            return null;
        }

    }
}
