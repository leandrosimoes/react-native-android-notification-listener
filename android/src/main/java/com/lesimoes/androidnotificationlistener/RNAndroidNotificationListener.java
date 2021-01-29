 
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
        CharSequence textChars = notification.extras.getCharSequence(Notification.EXTRA_TEXT);
        CharSequence bigTextChars = notification.extras.getCharSequence(Notification.EXTRA_BIG_TEXT);
        CharSequence[] lines = notification.extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);

        String app = "Unknown App";
        String title = "";
        String text = "";
        String bigText = "";
        String groupedMessages = "";

        if (packageName != null && !TextUtils.isEmpty(packageName)) {
            app = packageName.toString().trim();
        }
        
        if (titleChars != null) {
            title = titleChars.toString().trim();
        }

        if (textChars != null) {
            text = textChars.toString().trim();
        }

        if (bigTextChars != null) {
            bigText = bigTextChars.toString().trim();
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

        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(text) && TextUtils.isEmpty(bigText) && TextUtils.isEmpty(groupedMessages)) {
            Log.d(TAG, "The notification received has no valid data");
            return;
        }
        
        Context context = getApplicationContext();

        Intent serviceIntent = new Intent(context, RNAndroidNotificationListenerHeadlessJsTaskService.class);

        Log.d(TAG, "Notification received: " + app + " | " + title + " | " + text);

        serviceIntent.putExtra("app", app);
        serviceIntent.putExtra("title", title);
        serviceIntent.putExtra("text", text);

        if (!TextUtils.isEmpty(groupedMessages)) {
            Log.d(TAG, "Grouped Messages: \r\r" + groupedMessages);

            serviceIntent.putExtra("groupedMessages", groupedMessages); 
        }

        if (!TextUtils.isEmpty(bigText)) { 
            Log.d(TAG, "Big Text: \r\r" + bigText);

            serviceIntent.putExtra("bigText", bigText); 
        }

        HeadlessJsTaskService.acquireWakeLockNow(context);

        context.startService(serviceIntent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {}
}