package com.example.cashmanagement.enums;


public enum EnumNotificationStatus {
    POSIVITIVE(0)
    ,NEGATIVE(1)
    ,NEUTRAL(2)
    ;
    private int value;

    EnumNotificationStatus(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
