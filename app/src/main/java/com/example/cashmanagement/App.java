package com.example.cashmanagement;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Base64;
import android.util.DisplayMetrics;

import com.example.cashmanagement.comm.CommHelper;
import com.example.cashmanagement.helpers.NotificationHelper;
import com.example.cashmanagement.helpers.PrintHelper;
import com.example.cashmanagement.utils.Ini;
import com.example.cashmanagement.utils.LogString;

import java.io.File;
import java.security.MessageDigest;
import java.util.Calendar;

import java.util.Locale;
import java.util.UUID;


public class App extends Application {
    private static Locale locale;
    private static Context mContext;
    private static LogString logString;
    Boolean isLaunched;
    public static CommHelper commHelper; //обект за комуникация със сървъра
    private static NotificationHelper notificationHelper; //обект за вътрешен обмен на данни в приложението
    public static CommHelper getCommHelper(){return commHelper;}
    public static NotificationHelper getNotificationHelper() {
        return notificationHelper;
    }
    private boolean isAidl;
    public boolean isAidl() {
        return isAidl;
    }
    public void setAidl(boolean aidl) {
        isAidl = aidl;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        logString = LogString.getLogString(Environment.getExternalStorageDirectory().toString() + "/Download/CashManagement/", "CashManagement.log");
        createDirs();
        loadSettings();
        isLaunched = false;
        String lang = Ini.Server.Language;
        setLang();
        writeToLog("App - Application started!");
       // getVersionInfo();
        DownloadSchedule(); // Starts file download schedule
        saveSettings();
        checkTerminalUUID();
        // Starts file download when app is launched
        isAidl = true;
        //PrintHelper.getInstance().connectPrinterService(this);
        connectToServer();
        isAidl = true;

        PrintHelper.getInstance().connectPrinterService(this);

    }

    public static void connectToServer(){
        commHelper = new CommHelper();
    }

    public static void connect(){
     try{
   App.commHelper.InitSignalR();
       }catch (Exception e){
         writeToLog(e.getMessage());
      }
    }

//    /**
//     * Show current app version.
//     */
//    private void getVersionInfo() {
//        String versionName = "";
//
//        try {
//            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//            versionName = packageInfo.versionName;
//        } catch(PackageManager.NameNotFoundException ex) {
//            App.writeToLog("ValidationActivity - getVersionInfo: " + ex.getMessage());
//        }
//        String svnRevision =  BuildConfig.SVN_REVISION;
//
//        writeToLog((String.format(getResources().getString(R.string.text_version) +  "%s", versionName) + " " + String.format("%s",svnRevision)));
//
//        Date buildDate = BuildConfig.BUILD_TIME;
//
//        writeToLog((String.format(getResources().getString(R.string.text_build_date) + "%s", buildDate)));
//
//    }

    // $Id: $
    public void createDirs() {
        File mainDir = new File(Environment.getExternalStorageDirectory() + "/Download/CashManagement");

        if(!mainDir.exists()) {
            try {
                mainDir.mkdirs();
                writeToLog("Directory Download is created.");
            } catch(Exception ex) {
                writeToLog("Creating directory Download failed: " + ex.toString());
            }
        }
    }

    public static Context getContext (){
        return mContext;
    }

    public static void saveSettings() {
        Ini.Save(Ini.Instance(), Environment.getExternalStorageDirectory().toString() + "/Download/CashManagement/settings.ini");
    }

    public static void loadSettings() {
        Ini.Load(Ini.Instance(), Environment.getExternalStorageDirectory().toString() + "/Download/CashManagement/settings.ini");
    }

    /**
     * Check terminal's UUID.
     */
    public static void checkTerminalUUID() {
        if(36!=Ini.Server.ApplicationIdent.length()) {
            Ini.Server.ApplicationIdent = UUID.randomUUID().toString();
            saveSettings();
        }
    }

    /**
     * writes to log
     * @param msg message to log
     */
    public static void writeToLog(String msg){
        logString.Add(msg);
    }

    public static String EncryptPassword(String rawPassword){
        try {
            rawPassword = "SiS_" + rawPassword + "_CashManagementServer";
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(rawPassword.getBytes("UTF-8"));
            return Base64.encodeToString( md.digest(),Base64.NO_WRAP);
        } catch (Exception ignore) {
            return "";
        }
    }

    /**
     * Set application language.
     */
    public static void setLang()
    {
        Resources res = mContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();

        if(Ini.Server.Language.equals("bg")) {     // BG
            conf.locale = new Locale("bg");
            res.updateConfiguration(conf, dm);
        }
        else {                                  // EN
            conf.locale = new Locale("en");
            res.updateConfiguration(conf, dm);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(locale != null && newConfig.locale.getLanguage().equals(locale.getLanguage())) {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    public void DownloadSchedule() { // Checks and downloads the missing files in specific time
        isLaunched = true;
        String hours = Ini.Server.DownloadScheduleHours;
        String minutes = Ini.Server.DownloadScheduleMinutes;

        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);

        String strDay = String.valueOf(day);
        String strMonth = String.valueOf(month);
        String DownloadedDay = Ini.Server.DownloadedDay;
        String DownloadedMonth = Ini.Server.DownloadedMonth;

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hours));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minutes));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND ,0);
    }


}
