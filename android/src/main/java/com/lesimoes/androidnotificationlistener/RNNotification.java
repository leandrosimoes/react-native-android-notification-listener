package com.lesimoes.androidnotificationlistener;

import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.app.Notification;
import android.util.Log;
import java.util.ArrayList;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;
import android.util.Base64;
import android.content.Context;
import java.lang.Exception;
import android.graphics.drawable.Icon;
import android.graphics.drawable.BitmapDrawable;

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
    protected String icon;
    protected String image;

    public RNNotification(Context context, StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();

        if (notification != null && notification.extras != null) {
            String packageName = sbn.getPackageName();

            this.app = TextUtils.isEmpty(packageName) ? "Unknown App" : packageName;
            this.title = this.getPropertySafely(notification, Notification.EXTRA_TITLE);
            this.titleBig = this.getPropertySafely(notification, Notification.EXTRA_TITLE_BIG);
            this.text = this.getPropertySafely(notification, Notification.EXTRA_TEXT);
            this.subText = this.getPropertySafely(notification, Notification.EXTRA_SUB_TEXT);
            this.summaryText = this.getPropertySafely(notification, Notification.EXTRA_SUMMARY_TEXT);
            this.bigText = this.getPropertySafely(notification, Notification.EXTRA_BIG_TEXT);
            this.audioContentsURI = this.getPropertySafely(notification, Notification.EXTRA_AUDIO_CONTENTS_URI);
            this.imageBackgroundURI = this.getPropertySafely(notification, Notification.EXTRA_BACKGROUND_IMAGE_URI);
            this.extraInfoText = this.getPropertySafely(notification, Notification.EXTRA_INFO_TEXT);

            this.icon = this.getNotificationIcon(context, notification, packageName);
            this.image = this.getNotificationImage(notification);
            this.groupedMessages = this.getGroupedNotifications(notification);
        } else {
            Log.d(TAG, "The notification received has no data");
        }
    }

    private String getPropertySafely(Notification notification, String propKey) {
        try {
            CharSequence propCharSequence = notification.extras.getCharSequence(propKey);

            return propCharSequence == null ? "" : propCharSequence.toString().trim();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return "";
        }
    }

    private ArrayList<RNGroupedNotification> getGroupedNotifications(Notification notification) {
        ArrayList<RNGroupedNotification> result = new ArrayList<RNGroupedNotification>();

        try {
            CharSequence[] lines = notification.extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);

            if (lines != null && lines.length > 0) {
                for (CharSequence line : lines) {
                    if (!TextUtils.isEmpty(line)) {
                        RNGroupedNotification groupedNotification = new RNGroupedNotification(this, line);
                        this.groupedMessages.add(groupedNotification);
                    }
                }
            }

            return result;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return result;
        }
    }

    private String getNotificationIcon(Context context, Notification notification, String packageName) {
        try {
            int iconId = notification.extras.getInt(Notification.EXTRA_SMALL_ICON);

            String result = ""

            if (iconId <= 0) {
                Icon iconInstance = notification.getSmallIcon();
                Drawable iconDrawable = iconInstance.loadDrawable(context);
                Bitmap iconBitmap = ((BitmapDrawable) iconDrawable).getBitmap();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                result = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
            } else {
                PackageManager manager = context.getPackageManager();
                Resources resources = manager.getResourcesForApplication(packageName);

                Drawable iconDrawable = resources.getDrawable(iconId);
                Bitmap iconBitmap = ((BitmapDrawable) iconDrawable).getBitmap();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                result = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
            }

            return TextUtils.isEmpty(result) ? result : "data:image/png;base64," + result;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return "";
        }
    }

    private String getNotificationImage(Notification notification) {
        try {
            if (!notification.extras.containsKey(Notification.EXTRA_PICTURE)) return "";

            Bitmap imageBitmap = (Bitmap) notification.extras.get(Notification.EXTRA_PICTURE);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            String result = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

            return TextUtils.isEmpty(result) ? result : "data:image/png;base64," + result;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return "";
        }
    }
}