package com.example.cashmanagement.utils;

import org.simpleframework.xml.transform.Transform;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTransformer implements Transform<Date> {
    private final DateFormat dateFormat;

    public DateTransformer() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getDefault());
    }

    @Override
    public Date read(String value) throws Exception {
        return dateFormat.parse(value);
    }


    @Override
    public String write(Date value) {
        return dateFormat.format(value);
    }
}
