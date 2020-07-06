package com.example.cashmanagement.errors;

import android.content.Intent;

import com.example.cashmanagement.App;
import com.example.cashmanagement.helpers.BaseNotification;
import com.example.cashmanagement.enums.EnumNotificationStatus;

import java.util.Calendar;

/**
 * Created by christo.christov on 24.10.2017 г..
 */

public class InvalidDatabaseVersionError extends BaseError {
    @Override
    public void takeAction(){
        App.getNotificationHelper().addNotification(new BaseNotification(
                EnumError.INVALID_DATABASE_VERSION.getErrorMessage(),
                Calendar.getInstance().getTime().toString(),
                EnumNotificationStatus.NEGATIVE));
        Intent intent = new Intent("auroraposmobile.base.notification");
        App.getContext().sendBroadcast(intent);
    }
}
