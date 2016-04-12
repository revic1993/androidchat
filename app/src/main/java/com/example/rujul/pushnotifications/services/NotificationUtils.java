package com.example.rujul.pushnotifications.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.example.rujul.pushnotifications.Constants;
import com.example.rujul.pushnotifications.PushApp;
import com.example.rujul.pushnotifications.R;

import java.util.Arrays;
import java.util.List;


/**
 * Created by rujul on 4/11/2016.
 */
public class NotificationUtils {

    private Context mContext;
    private final String TAG=NotificationUtils.class.getSimpleName();
    private final int icon;
    private SharedPreferenceService spfs;

    public NotificationUtils(Context context){
        this.mContext = context;
        icon = R.mipmap.ic_launcher;
        this.spfs = PushApp.getApp().getSPF();
    }

    public void showNotification(Intent intent,Bundle data){

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext);

        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + mContext.getPackageName() + "/raw/notification");
        String title = data.getString(Constants.MESSAGE_FROM)+"@"+data.getString(Constants.MESSAGE_TO);

        showSmallNotification(mBuilder,icon,title,data.getString(Constants.MESSAGE),resultPendingIntent,alarmSound);
    }



    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, PendingIntent resultPendingIntent, Uri alarmSound) {

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        if(!spfs.getStringData(Constants.NOTIFICATIONS).equalsIgnoreCase(Constants.DEFAULT)){
            String oldNotification = addNotification(message);;

            List<String> messages = Arrays.asList(oldNotification.split("\\|"));

            for (int i = messages.size() - 1; i >= 0; i--) {
                inboxStyle.addLine(messages.get(i));
            }
        }else{
            spfs.putData(Constants.NOTIFICATIONS,message);
            inboxStyle.addLine(message);
        }

        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                .setSmallIcon(icon)
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.MESSAGE_NOTIFICATION_ID, notification);
    }

    public static void clearNotifications() {
        NotificationManager notificationManager = (NotificationManager) PushApp.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

    }
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + PushApp.getApp().getApplicationContext().getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(PushApp.getApp().getApplicationContext(), alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String addNotification(String notification) {

        // get old notifications
        String oldNotifications = spfs.getStringData(Constants.NOTIFICATIONS);

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }
        spfs.putData(Constants.NOTIFICATIONS,oldNotifications);
        return oldNotifications;
    }
}
