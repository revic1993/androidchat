package com.example.rujul.pushnotifications.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rujul.pushnotifications.Constants;
import com.example.rujul.pushnotifications.PushApp;
import com.example.rujul.pushnotifications.R;
import com.example.rujul.pushnotifications.database.DaoSession;
import com.example.rujul.pushnotifications.database.Group;
import com.example.rujul.pushnotifications.database.GroupDao;
import com.example.rujul.pushnotifications.database.chat;
import com.example.rujul.pushnotifications.services.NotificationUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class ChatActivity extends AppCompatActivity implements ChatPresenter.ChatInterface{

    EditText etChat;
    ImageButton ibSend;
    ChatPresenter presenter;
    PushApp mApp;
    DaoSession session;
    GroupDao groupDao;
    Group mGroup;
    TextView tvTitle;
    BroadcastReceiver mPushReceiver;
    List<chat> chats;
    ChatAdapter chatAdapter;
    RecyclerView rvChats;
    boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initChat();

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etChat.getText().toString();
                if(TextUtils.isEmpty(message))
                    return;
                presenter.sendChat(message,mGroup,session);
                etChat.setText("");
            }
        });


    }

    private void initChat(){
        etChat = (EditText) findViewById(R.id.etChatBox);
        ibSend = (ImageButton) findViewById(R.id.ibSend);
        tvTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        rvChats = (RecyclerView) findViewById(R.id.rvChat);

        mApp = PushApp.getApp();
        presenter = new ChatPresenter(mApp.getVolley(),mApp.getSPF(),this);
        session = mApp.getDaoSession();
        groupDao = session.getGroupDao();
        mGroup = groupDao.queryBuilder()
                         .where(GroupDao.Properties.GroupId.eq(getIntent().getExtras().getString(Constants.ID, "")))
                         .unique();

        chats = session.getChatDao()._queryGroup_Chats(mGroup.getGroupId());

        if(chats.size() == 0){
            chats = new ArrayList<chat>();
        }

        chatAdapter = new ChatAdapter(chats);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvChats.setLayoutManager(layoutManager);
        rvChats.setAdapter(chatAdapter);

        tvTitle.setText(mGroup.getGroupName());
        mPushReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handlePush(intent);
            }
        };
        registerReceiver();

    }

    private void handlePush(Intent intent) {
        String groupId = intent.getStringExtra(Constants.MESSAGE_GROUP_ID);
        chat mChat = (chat) intent.getSerializableExtra(Constants.WHOLE_MESSAGE);

        if(mGroup.getGroupId().equalsIgnoreCase(groupId)){
            updateChat(mChat);
        }else{
            Bundle data = new Bundle();
            data.putString(Constants.MESSAGE_FROM,mChat.getFrom());
            data.putString(Constants.MESSAGE,mChat.getMessage());
            data.putString(Constants.MESSAGE_TO,intent.getStringExtra(Constants.MESSAGE_TO));

            Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
            resultIntent.putExtra(Constants.ID,intent.getStringExtra(Constants.MESSAGE_GROUP_ID));
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.showNotification(resultIntent,data);
        }
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mPushReceiver,
                    new IntentFilter(Constants.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPushReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    public void updateChat(final chat mChat) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chats.add(mChat);
                    chatAdapter.notifyDataSetChanged();
                }
            });

            Log.d("PUSHNOTIFS",chats.size()+" post update chat size");
    }
}
