package com.example.cashmanagement.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cashmanagement.App;
import com.example.cashmanagement.R;
import com.example.cashmanagement.comm.CommHelper;
import com.example.cashmanagement.databinding.ActivityMainBinding;
import com.example.cashmanagement.utils.Hex;
import com.example.cashmanagement.utils.Ini;
import com.example.cashmanagement.dialogs.VerificationDialog;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.TouchTypeDetector;


public class LaunchScreen extends AppCompatActivity {

    ActivityMainBinding binding;
    private NfcAdapter mNfcAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        App.writeToLog("Launch Screen started.");
        initNFC(this);
        Sensey.getInstance().init(this);
        Sensey.getInstance().startTouchTypeDetection(this,touchTypListener);
        binding.ibCheckConnectionStatus.setOnClickListener(v -> checkConnectionStatus());
        binding.ibChangeLanguage.setOnClickListener(v -> changeLanguage());
        setUpFadeAnimation();
    }

    private void changeLanguage() {
        final String[] listItems = {"Български", "English"};
        AlertDialog.Builder builder = new AlertDialog.Builder(LaunchScreen.this);
        builder.setTitle(getString(R.string.choose_language_title));
        builder.setSingleChoiceItems(listItems, -1, (dialog, position) -> {
            switch (position) {

                case 0:
                    Ini.Server.Language = "bg";
                    App.saveSettings();
                    App.setLang();
                    LaunchScreen.this.restartActivity();
                    break;
                case 1:
                    Ini.Server.Language = "en";
                    App.saveSettings();
                    App.setLang();
                    LaunchScreen.this.restartActivity();
                    break;
            }
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void checkConnectionStatus() {
       if(CommHelper.isConnected){
           Toast.makeText(this, R.string.connection_status_ok, Toast.LENGTH_SHORT).show();
       }else
           Toast.makeText(this, R.string.connection_status_not_connected, Toast.LENGTH_SHORT).show();
    }

    @Override public boolean dispatchTouchEvent(MotionEvent event) {
        // Setup onTouchEvent for detecting type of touch gesture
        Sensey.getInstance().setupDispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    private TouchTypeDetector.TouchTypListener touchTypListener = new TouchTypeDetector.TouchTypListener() {
        @Override
        public void onDoubleTap() {

        }

        @Override
        public void onLongPress() {
        }

        @Override
        public void onScroll(int i) {

        }

        @Override
        public void onSingleTap() {

        }

        @Override
        public void onSwipe(int i) {

        }

        @Override
        public void onThreeFingerSingleTap() {
            openDialog();
        }

        @Override
        public void onTwoFingerSingleTap() {

        }
    };

    private void openDialog() {
        try {
            VerificationDialog dialog = new VerificationDialog();
            dialog.show(getSupportFragmentManager(), "verification-dialog");
        }catch (Exception e){App.writeToLog(e.getMessage());}
    }

    /**
     * Restart activity.
     * Use this to set strings for the chosen language.
     */
    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void setUpFadeAnimation() {
        final TextView blinking_text = findViewById(R.id.blinking_text);
        final Animation fadeIn = new AlphaAnimation(1.0f, 0.0f);
        fadeIn.setDuration(800);
        fadeIn.setStartOffset(400);
        final Animation fadeOut = new AlphaAnimation(0.0f, 1.0f);
        fadeOut.setDuration(800);
        fadeOut.setStartOffset(400);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start fadeOut when fadeIn ends (continue)
                blinking_text.startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start fadeIn when fadeOut ends (repeat)
                blinking_text.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });
        blinking_text.startAnimation(fadeOut);
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
            new LaunchScreen.NdefReaderTask(this).execute(tag);
        }
    }

    protected void onStart() {
        super.onStart();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableReaderMode(this, tag -> {
            new LaunchScreen.NdefReaderTask(this).execute(tag);
        }, NfcAdapter.FLAG_READER_NFC_A, null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableReaderMode(this);
    }

    /**
     * Background task for reading the data. Do not block the UI thread while reading.
     */
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
            Ini.Server.CardNumber = Hex.bytesToHex(tag.getId());
            App.saveSettings();
            return Hex.bytesToHex(tag.getId());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            startActivity(new Intent(context,LoginScreen.class));

        }
    }
}
