package com.example.cashmanagement.helpers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.example.cashmanagement.utils.ESCUtil;

import woyou.aidlservice.jiuiv5.IWoyouService;

public class PrintHelper implements ServiceConnection {

    private static final String SERVICE＿PACKAGE = "woyou.aidlservice.jiuiv5";
    private static final String SERVICE＿ACTION = "woyou.aidlservice.jiuiv5.IWoyouService";

    private IWoyouService woyouService;
    private static PrintHelper printHelper = new PrintHelper();
    private Context context;

    public static PrintHelper getInstance() {
        return printHelper;
    }

    public void connectPrinterService(Context context) {
        this.context = context.getApplicationContext();
        Intent intent = new Intent();
        intent.setPackage(SERVICE＿PACKAGE);
        intent.setAction(SERVICE＿ACTION);
        context.getApplicationContext().startService(intent);
        context.getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    public void disconnectPrinterService(Context context) {
        if (woyouService != null) {
            context.getApplicationContext().unbindService(connService);
            woyouService = null;
        }
    }

    public boolean isConnect() {
        return woyouService != null;
    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };

    public void initPrinter() {
        if (woyouService == null) {
            Toast.makeText(context, "Service has been disconnected!", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            woyouService.printerInit(null);
        } catch (RemoteException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Toast.makeText(context, "Service is connected!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Toast.makeText(context, "Service has been disconnected!", Toast.LENGTH_LONG).show();

    }

    public void printText(String content, float size, boolean isBold, boolean isUnderLine) {
        if (woyouService == null) {
            Toast.makeText(context, "Service has been disconnected!", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (isBold) {
                woyouService.sendRAWData(ESCUtil.boldOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.boldOff(), null);
            }

            if (isUnderLine) {
                woyouService.sendRAWData(ESCUtil.underlineWithOneDotWidthOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.underlineOff(), null);
            }

            woyouService.printTextWithFont(content, null, size, null);
            woyouService.lineWrap(1, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void printQr(String data, int modulesize, int errorlevel) {
        if (woyouService == null) {
            Toast.makeText(context, "Service has been disconnected!", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.setAlignment(1, null);
            woyouService.printQRCode(data, modulesize, errorlevel, null);
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void print3Line() {
        if (woyouService == null) {
            Toast.makeText(context, "Service has been disconnected!", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.lineWrap(8, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    } // print empty line ( first param is for the line-height )

}