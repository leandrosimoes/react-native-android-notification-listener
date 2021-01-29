 
package com.lesimoes.androidnotificationlistener;

import android.content.Intent;
import android.content.Context;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.app.Notification;
import android.util.Log;
import android.text.TextUtils;

import com.facebook.react.HeadlessJsTaskService;

public class RNAndroidNotificationListener extends NotificationListenerService {
    private static final String TAG = "RNAndroidNotificationListener";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();

        if (notification == null || notification.extras == null) {
            Log.d(TAG, "The notification received has no data");
            return;
        }
        
        String packageName = sbn.getPackageName();

        CharSequence titleChars = notification.extras.getCharSequence(Notification.EXTRA_TITLE);
        CharSequence titleBigChars = notification.extras.getCharSequence(Notification.EXTRA_TITLE_BIG);
        CharSequence textChars = notification.extras.getCharSequence(Notification.EXTRA_TEXT);
        CharSequence subTextChars = notification.extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
        CharSequence summaryTextChars = notification.extras.getCharSequence(Notification.EXTRA_SUMMARY_TEXT);
        CharSequence bigTextChars = notification.extras.getCharSequence(Notification.EXTRA_BIG_TEXT);
        CharSequence audioContentsURIChars = notification.extras.getCharSequence(Notification.EXTRA_AUDIO_CONTENTS_URI);
        CharSequence imageBackgroundURIChars = notification.extras.getCharSequence(Notification.EXTRA_BACKGROUND_IMAGE_URI);
        CharSequence extraInfoChars = notification.extras.getCharSequence(Notification.EXTRA_INFO_TEXT);
        CharSequence[] lines = notification.extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);

        String app = "Unknown App";
        String title = "";
        String titleBig = "";
        String text = "";
        String subText = "";
        String summaryText = "";
        String bigText = "";
        String groupedMessages = "";
        String audioContentsURI = "";
        String imageBackgroundURI = "";
        String extraInfoText = "";

        if (packageName != null && !TextUtils.isEmpty(packageName)) {
            app = packageName.trim();
        }
        
        if (titleChars != null) {
            title = titleChars.toString().trim();
        }
        
        if (titleBigChars != null) {
            titleBig = titleBigChars.toString().trim();
        }

        if (textChars != null) {
            text = textChars.toString().trim();
        }

        if (subTextChars != null) {
            subText = subTextChars.toString().trim();
        }

        if (summaryTextChars != null) {
            summaryText = summaryTextChars.toString().trim();
        }

        if (bigTextChars != null) {
            bigText = bigTextChars.toString().trim();
        }

        if (audioContentsURIChars != null) {
            audioContentsURI = audioContentsURIChars.toString().trim();
        }

        if (imageBackgroundURIChars != null) {
            imageBackgroundURI = imageBackgroundURI.toString().trim();
        }

        if (extraInfoChars != null) {
            extraInfoText = extraInfoChars.toString().trim();
        }

        if (lines != null && lines.length > 0) { 
            StringBuilder sb = new StringBuilder(); 

            sb.append("");
            
            for (CharSequence line : lines) {
                if (!TextUtils.isEmpty(line)) { 
                    sb.append(line.toString()); 
                    sb.append('\n'); 
                } 
            }

            groupedMessages = sb.toString().trim(); 
        }

        Context context = getApplicationContext();

        Intent serviceIntent = new Intent(context, RNAndroidNotificationListenerHeadlessJsTaskService.class);

        serviceIntent.putExtra("app", app);
        serviceIntent.putExtra("title", title);
        serviceIntent.putExtra("titleBig", titleBig);
        serviceIntent.putExtra("text", text);
        serviceIntent.putExtra("subText", subText);
        serviceIntent.putExtra("summaryText", summaryText);
        serviceIntent.putExtra("groupedMessages", groupedMessages); 
        serviceIntent.putExtra("bigText", bigText); 
        serviceIntent.putExtra("audioContentsURI", audioContentsURI); 
        serviceIntent.putExtra("imageBackgroundURI", imageBackgroundURI); 
        serviceIntent.putExtra("extraInfoText", extraInfoText); 

        HeadlessJsTaskService.acquireWakeLockNow(context);

        context.startService(serviceIntent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {}
}