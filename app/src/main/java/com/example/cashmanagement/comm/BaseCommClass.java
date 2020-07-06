package com.example.cashmanagement.comm;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by christo.christov on 8.9.2016 г..
 */
public class BaseCommClass {
    public UUID ident = UUID.randomUUID();
    public Date dateTime = Calendar.getInstance().getTime();
}
