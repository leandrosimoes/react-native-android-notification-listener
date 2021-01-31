package com.lesimoes.androidnotificationlistener;

import android.text.TextUtils;

public class RNGroupedNotification {
    protected String title;
    protected String text;

    public RNGroupedNotification(RNNotification mainNotification, CharSequence message) {
        String formatedMessage = message.toString().trim();

        this.title = !TextUtils.isEmpty(mainNotification.title) ? mainNotification.title : "";
        this.text = !TextUtils.isEmpty(mainNotification.text) ? mainNotification.text : "";

        int endIndex = formatedMessage.indexOf(":");
    
        if (endIndex != -1) {
            this.title = formatedMessage.substring(0, endIndex).trim();
            this.text = formatedMessage.substring(endIndex + 1).trim();
        } else {
            this.text = formatedMessage;
        }
    }
}