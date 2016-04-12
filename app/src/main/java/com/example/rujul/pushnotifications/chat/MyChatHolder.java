package com.example.rujul.pushnotifications.chat;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.rujul.pushnotifications.R;

/**
 * Created by rujul on 4/11/2016.
 */
public class MyChatHolder extends RecyclerView.ViewHolder {

    TextView myMessage;
    public MyChatHolder(View itemView) {
        super(itemView);
        myMessage = (TextView) itemView.findViewById(R.id.tvMyMessage);
    }

    public void setMyMessage(String message) {
        myMessage.setText(message);
    }
}
