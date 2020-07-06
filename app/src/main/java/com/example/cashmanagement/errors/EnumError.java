package com.example.cashmanagement.errors;
import com.example.cashmanagement.App;
import com.example.cashmanagement.R;

/**
 * Created by christo.christov on 20.10.2017 г..
 * How to use enums :
 * int x to EnumError => EnumError.values()[x]
 * EnumError x to int  => x.getErrorCode();
 */

public enum EnumError {
    NO_ERROR(0, R.string.error_message_no_error)
    ,WRONG_AUTH_DATA(1, R.string.error_message_wrong_auth_data)
    ,TERMINAL_NOT_SIGNED(2, R.string.error_message_terminal_not_signed, true)
    ,TERMINAL_NOT_ACTIVE(3, R.string.error_message_terminal_not_active, true)
    ,ACCOUNT_NOT_ACTIVE(4, R.string.error_message_account_not_active, true)
    ,COMMUNICATION_ERROR(5, R.string.error_message_communication_error)
    ,REGISTER_TERMINAL_FAILED(6, R.string.error_message_register_terminal_failed)
    ,UNREGISTER_TERMINAL_FAILED(7, R.string.error_message_unregister_terminal_failed)
    ,INVALID_PRODUCT_NAME(8, R.string.error_message_invalid_product_name, true)
    ,INVALID_COMMUNICATION_VERSION(9, R.string.error_message_invalid_communication_version, true)
    ,INVALID_DATABASE_VERSION(10, R.string.error_message_invalid_database_version, true)
    ,INVALID_USERNAME_OR_PASSWORD(11, R.string.error_message_invalid_username_or_password)
    ;
    private int errorCode; //код за грешка
    private int errorResourceId; //код на съобщението от таблицата с преводи
    private boolean isCritical; //критични грешки трябва да се отстранят преди да се продължи с работата на приложението

    EnumError(int errorCode, int errorResourceId){
        this(errorCode, errorResourceId, false);
    }

    EnumError(int errorCode, int errorResourceId, boolean isCritical){
        this.errorCode = errorCode;
        this.errorResourceId = errorResourceId;
        this.isCritical = isCritical;
    }

    public int getErrorCode(){
        return errorCode;
    }

    public String getErrorMessage(){
        return App.getContext().getString(errorResourceId);
    }

    public boolean isCritical(){
        return isCritical;
    }
}
