package com.lesimoes.androidnotificationlistener;

import androidx.core.app.NotificationManagerCompat;
import android.provider.Settings;
import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

import java.util.List;
import java.util.Set;

public class RNAndroidNotificationListenerModule extends ReactContextBaseJavaModule {
    private static ReactApplicationContext reactContext;
    private static final String TAG = "RNAndroidNotificationListener";

    public RNAndroidNotificationListenerModule(ReactApplicationContext context) {
        super(context);
        
        reactContext = context;
    }

    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void getPermissionStatus(Promise promise) {
        if (reactContext == null) return;
        
        String packageName = reactContext.getPackageName();
        Set<String> enabledPackages = NotificationManagerCompat.getEnabledListenerPackages(reactContext);
        if (enabledPackages.contains(packageName)) {
            promise.resolve("authorized");
        } else {
            promise.resolve("denied");
        }
    }
    
    @ReactMethod
    public void requestPermission() {
        if (reactContext == null) return;

        final Intent i = new Intent();
        i.setAction(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        reactContext.startActivity(i);
    }

    public static void sendEvent(String event, WritableMap params) {
        if (reactContext == null) return;

        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(event, params);
    }
}
