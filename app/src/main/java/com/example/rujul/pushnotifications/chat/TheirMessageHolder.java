package com.example.rujul.pushnotifications.chat;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.rujul.pushnotifications.R;

/**
 * Created by rujul on 4/11/2016.
 */
public class TheirMessageHolder extends RecyclerView.ViewHolder {

    TextView username,theirMessage;

    public TheirMessageHolder(View itemView) {
        super(itemView);
        username = (TextView) itemView.findViewById(R.id.tvTheirName);
        theirMessage = (TextView) itemView.findViewById(R.id.tvTheirMessage);
    }

    public void setTheirChat(String user,String message){
        username.setText(user);
        theirMessage.setText(message);
    }
}
