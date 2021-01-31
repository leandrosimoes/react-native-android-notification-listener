package com.lesimoes.androidnotificationlistener;

import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.app.Notification;
import android.util.Log;
import java.util.ArrayList;

public class RNNotification {
    private static final String TAG = "RNAndroidNotificationListener";
    
    protected String app;
    protected String title;
    protected String titleBig;
    protected String text;
    protected String subText;
    protected String summaryText;
    protected String bigText;
    protected ArrayList<RNGroupedNotification> groupedMessages;
    protected String audioContentsURI;
    protected String imageBackgroundURI;
    protected String extraInfoText;

    public RNNotification(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();

        if (notification != null && notification.extras != null) {
            String packageName = sbn.getPackageName();

            CharSequence titleChars = notification.extras.getCharSequence(Notification.EXTRA_TITLE);
            CharSequence titleBigChars = notification.extras.getCharSequence(Notification.EXTRA_TITLE_BIG);
            CharSequence textChars = notification.extras.getCharSequence(Notification.EXTRA_TEXT);
            CharSequence subTextChars = notification.extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
            CharSequence summaryTextChars = notification.extras.getCharSequence(Notification.EXTRA_SUMMARY_TEXT);
            CharSequence bigTextChars = notification.extras.getCharSequence(Notification.EXTRA_BIG_TEXT);
            CharSequence audioContentsURIChars = notification.extras.getCharSequence(Notification.EXTRA_AUDIO_CONTENTS_URI);
            CharSequence imageBackgroundURIChars = notification.extras.getCharSequence(Notification.EXTRA_BACKGROUND_IMAGE_URI);
            CharSequence extraInfoTextChars = notification.extras.getCharSequence(Notification.EXTRA_INFO_TEXT);
            CharSequence[] lines = notification.extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);

            this.app = TextUtils.isEmpty(packageName) ? "Unknown App" : packageName;
            this.title = titleChars != null ? titleChars.toString().trim() : "";
            this.titleBig = titleBigChars != null ? titleBigChars.toString().trim() : "";
            this.text = textChars != null ? textChars.toString().trim() : "";
            this.subText = subTextChars != null ? subTextChars.toString().trim() : "";
            this.summaryText = summaryTextChars != null ? summaryTextChars.toString().trim() : "";
            this.bigText = bigTextChars != null ? bigTextChars.toString().trim() : "";
            this.audioContentsURI = audioContentsURIChars != null ? audioContentsURIChars.toString().trim() : "";
            this.imageBackgroundURI = imageBackgroundURIChars != null ? imageBackgroundURIChars.toString().trim() : "";
            this.extraInfoText = extraInfoTextChars != null ? extraInfoTextChars.toString().trim() : "";
            this.groupedMessages = new ArrayList<RNGroupedNotification>();

            if (lines != null && lines.length > 0) {
                for (CharSequence line : lines) {
                    if (!TextUtils.isEmpty(line)) {
                        RNGroupedNotification groupedNotification = new RNGroupedNotification(this, line);
                        this.groupedMessages.add(groupedNotification);
                    }
                }
            }
        } else {
            Log.d(TAG, "The notification received has no data");
        }
    }
}