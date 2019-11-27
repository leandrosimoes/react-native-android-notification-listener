 
package com.lesimoes.androidnotificationlistener;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.app.Notification;
import android.util.Log;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

public class RNAndroidNotificationListener extends NotificationListenerService {
    private static final String TAG = "RNAndroidNotificationListener";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        String app = sbn.getPackageName();

        if (notification.extras.getCharSequence(Notification.EXTRA_TITLE) == null) return;
        if (notification.extras.getCharSequence(Notification.EXTRA_TEXT) == null) return;
        
        String title = notification.extras.getCharSequence(Notification.EXTRA_TITLE).toString();
        String text = notification.extras.getCharSequence(Notification.EXTRA_TEXT).toString();

        if (text == null) return;

        Log.d(TAG, "Notification received: " + app + " | " + title + " | " + text);

        WritableMap params = Arguments.createMap();
        params.putString("app", app);
        params.putString("title", title);
        params.putString("text", text);

        RNAndroidNotificationListenerModule.sendEvent("notificationReceived", params);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {}
}