package com.example.rujul.pushnotifications.gcm;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.rujul.pushnotifications.Constants;
import com.example.rujul.pushnotifications.PushApp;
import com.example.rujul.pushnotifications.R;
import com.example.rujul.pushnotifications.chat.ChatActivity;
import com.example.rujul.pushnotifications.chat.ChatPresenter;
import com.example.rujul.pushnotifications.database.DaoSession;
import com.example.rujul.pushnotifications.database.chat;
import com.example.rujul.pushnotifications.database.chatDao;
import com.example.rujul.pushnotifications.services.NotificationUtils;
import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

    private DaoSession session;
    private chatDao chatdao;
    private chat mChat;
    @Override
    public void onMessageReceived(String from, Bundle data) {
        pushHandler(data);
    }


    private void pushHandler(Bundle data){
        Log.d("PUSHNOTIFS",data.toString()+" push data");
        if(data.getString(Constants.MESSAGE_FROM).equalsIgnoreCase(PushApp.getApp().getSPF().getStringData(Constants.ANAME)))
            return;
        session = PushApp.getApp().getDaoSession();
        chatdao = session.getChatDao();
        mChat = new chat();
        mChat.setAt(ChatPresenter.convertStringToDate(data.getString(Constants.MESSAGE_AT)));
        mChat.setFrom(data.getString(Constants.MESSAGE_FROM));
        mChat.setMessage(data.getString(Constants.MESSAGE));
        mChat.setGroupId(data.getString(Constants.MESSAGE_GROUP_ID));
        session.insert(mChat);

        if(PushApp.isAppIsInBackground(getApplicationContext())){
            Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
            resultIntent.putExtra(Constants.ID,data.getString(Constants.MESSAGE_GROUP_ID));
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.showNotification(resultIntent,data);
        }else{
            Intent pushIntent = new Intent(Constants.PUSH_NOTIFICATION);
            pushIntent.putExtra(Constants.MESSAGE_GROUP_ID,data.getString(Constants.MESSAGE_GROUP_ID));
            pushIntent.putExtra(Constants.MESSAGE_TO,data.getString(Constants.MESSAGE_TO));
            pushIntent.putExtra(Constants.WHOLE_MESSAGE,mChat);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushIntent);

            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }
    }

}
