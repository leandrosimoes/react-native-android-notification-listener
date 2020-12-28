 
package com.lesimoes.androidnotificationlistener;

import android.content.Intent;
import android.content.Context;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.app.Notification;
import android.util.Log;

import com.facebook.react.HeadlessJsTaskService;

public class RNAndroidNotificationListener extends NotificationListenerService {
    private static final String TAG = "RNAndroidNotificationListener";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();

        if (notification == null || notification.extras == null) return;
        
        String app = sbn.getPackageName();

        if (app == null) app = "Unknown";

        CharSequence titleChars = notification.extras.getCharSequence(Notification.EXTRA_TITLE);
        CharSequence textChars = notification.extras.getCharSequence(Notification.EXTRA_TEXT);

        if (titleChars == null || textChars == null) return;
        
        String title = titleChars.toString();
        String text = textChars.toString();

        if (text == null || text == "" || title == null || title == "") return;

        Log.d(TAG, "Notification received: " + app + " | " + title + " | " + text);
        
        Context context = getApplicationContext();

        Intent serviceIntent = new Intent(context, RNAndroidNotificationListenerHeadlessJsTaskService.class);
        serviceIntent.putExtra("app", app);
        serviceIntent.putExtra("title", title);
        serviceIntent.putExtra("text", text);

        HeadlessJsTaskService.acquireWakeLockNow(context);

        context.startService(serviceIntent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {}
}