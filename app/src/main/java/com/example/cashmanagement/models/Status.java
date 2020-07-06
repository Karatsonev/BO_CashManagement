package com.example.cashmanagement.models;

import java.io.Serializable;

public class Status implements Serializable {

    public boolean Alarm;
    public String AlarmText;
    public boolean IsBagFull;
    public boolean IsDoorClosed;
    public boolean IsKeyClosed;
    public boolean OutOfPaper;
}
