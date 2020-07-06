package com.example.cashmanagement.helpers;



import com.example.cashmanagement.enums.EnumNotificationStatus;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by christo.christov on 6.10.2017 г..
 */

public class BaseNotification {
    String baseInfo;
    String subInfo;

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    Integer imageId;

    EnumNotificationStatus status;
    boolean isRead = false;
    Date date;

    public BaseNotification(){

    }

    public BaseNotification(String baseInfo, String subInfo, EnumNotificationStatus status) {
        this.baseInfo = baseInfo;
        this.subInfo = subInfo;
        this.status = status;
        this.date = Calendar.getInstance().getTime();
        this.imageId = null;
    }

    public BaseNotification(String baseInfo, String subInfo, EnumNotificationStatus status, Integer imageId) {
        this.baseInfo = baseInfo;
        this.subInfo = subInfo;
        this.status = status;
        this.date = Calendar.getInstance().getTime();
        this.imageId = imageId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(String baseInfo) {
        this.baseInfo = baseInfo;
    }

    public String getSubInfo() {
        return subInfo;
    }

    public void setSubInfo(String subInfo) {
        this.subInfo = subInfo;
    }



    public EnumNotificationStatus getStatus() {
        return status;
    }

    public void setStatus(EnumNotificationStatus status) {
        this.status = status;
    }
}
