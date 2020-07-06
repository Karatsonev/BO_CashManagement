package com.example.cashmanagement.helpers;


import java.util.ArrayList;

/**
 * Created by christo.christov on 6.10.2017 г..
 */

public class NotificationHelper {
    private static final int MAX_NOTIFICATION_COUNT = 20;
    private ArrayList<BaseNotification> notifications = new ArrayList<>();
    private boolean isEnabled = true;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public ArrayList<BaseNotification> getNotifications() {
        return notifications;
    }

    synchronized public <T extends BaseNotification> void addNotification(T notification) {
        notifications.add(0, notification);
        for (int i = notifications.size() - 1; i >= MAX_NOTIFICATION_COUNT; i--) {
            notifications.remove(i);
        }
    }

    public int getUnreadCount() {
        int res = 0;
        for (int i = 0; i < notifications.size(); i++) {
            if (!notifications.get(i).isRead())
                res++;
        }
        return res;
    }

    public void markAllAsRead() {
        for (int i = 0; i < notifications.size(); i++) {
            notifications.get(i).setRead(true);
        }
    }
}
