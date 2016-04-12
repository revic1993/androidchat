package com.example.rujul.pushnotifications.chat;




import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rujul.pushnotifications.Constants;
import com.example.rujul.pushnotifications.PushApp;
import com.example.rujul.pushnotifications.R;
import com.example.rujul.pushnotifications.database.chat;

import java.util.List;


/**
 * Created by rujul on 1/25/2016.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<chat> chats;
    String username;
    final int My=0,Their=1;

    public ChatAdapter(List<chat> chats) {
        this.chats = chats;
        this.username = PushApp.getApp().getSPF().getStringData(Constants.ANAME);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case My:
                View v1 = inflater.inflate(R.layout.my_chat_child, parent, false);
                viewHolder = new MyChatHolder(v1);
                break;
            case Their:
                View v2 = inflater.inflate(R.layout.their_chat_child, parent, false);
                viewHolder = new TheirMessageHolder(v2);
                break;
            default:
                View v = inflater.inflate(R.layout.my_chat_child, parent, false);
                viewHolder = new MyChatHolder(v);
                break;
        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case My:
                MyChatHolder myChatHolder = (MyChatHolder) holder;
                myChatHolder.setMyMessage(chats.get(position).getMessage());
                break;

            case Their:
                TheirMessageHolder theirMessageHolder = (TheirMessageHolder) holder;
                theirMessageHolder.setTheirChat(chats.get(position).getFrom(), chats.get(position).getMessage());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(username.equalsIgnoreCase(chats.get(position).getFrom()))
            return My;
        return Their;
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }


}
