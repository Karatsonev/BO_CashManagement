package com.example.cashmanagement.helpers;

import com.example.cashmanagement.errors.AccountNotActiveError;
import com.example.cashmanagement.errors.EnumError;
import com.example.cashmanagement.errors.InvalidCommunicationVersionError;
import com.example.cashmanagement.errors.InvalidDatabaseVersionError;

/**
 * Този клас не служи за КАКВИ ДА СА грешки
 * Задължение да се обработват грешките оства за този, който инициира комуникацията.
 * Created by christo.christov on 24.10.2017 г..
 */

public class ErrorHelper {
    private static volatile ErrorHelper instance;

    public static ErrorHelper getInstance(){
        if(instance == null){
            synchronized (ErrorHelper.class) {
                if(instance == null){
                    instance = new ErrorHelper();
                }
            }
        }
        return instance;
    }

    public void handleError(EnumError error){
        if(!error.isCritical())
            return;
        switch (error){
            case ACCOUNT_NOT_ACTIVE:
                new AccountNotActiveError().takeAction();
                break;
            case INVALID_COMMUNICATION_VERSION:
                new InvalidCommunicationVersionError().takeAction();
                break;
            case INVALID_DATABASE_VERSION:
                new InvalidDatabaseVersionError().takeAction();
                break;
        }
    }
}
