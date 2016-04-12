package com.example.rujul.pushnotifications.chat;

import android.util.Log;

import com.android.volley.VolleyError;
import com.example.rujul.pushnotifications.Constants;
import com.example.rujul.pushnotifications.database.DaoSession;
import com.example.rujul.pushnotifications.database.Group;
import com.example.rujul.pushnotifications.database.chat;
import com.example.rujul.pushnotifications.services.SharedPreferenceService;
import com.example.rujul.pushnotifications.services.VolleyService;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rujul on 4/8/2016.
 */
public class ChatPresenter implements VolleyService.VolleyInterface{


    VolleyService volley;
    SharedPreferenceService spf;
    ChatInterface view;
    public ChatPresenter(VolleyService volley, SharedPreferenceService spf,ChatInterface view){
        this.volley = volley;
        this.spf = spf;
        this.view = view;
    }

    public void sendChat(String message, Group group, DaoSession session){
        String mobile = spf.getStringData(Constants.AMOBILE);
        Map<String,String> params = new HashMap<String,String>();
        params.put(Constants.MOBILE,mobile);
        params.put(Constants.AT,getDate().toString());
        params.put(Constants.MESSAGE,message);
        params.put(Constants.GROUP_NAME,group.getGroupName());

        chat mChat = new chat();
        mChat.setGroupId(group.getGroupId());
        mChat.setAt(getDate());
        mChat.setFrom(spf.getStringData(Constants.ANAME));
        mChat.setMessage(message);

        session.insert(mChat);

        volley.makePostRequest(Constants.CHAT_URL,params,this);
        view.updateChat(mChat);
    }

    @Override
    public void onSuccess(JSONObject response) {
        Log.d("VOLLEY",response.toString());
    }

    @Override
    public void onError(VolleyError error) {
        Log.d("VOLLEY",new String(error.networkResponse.data));
    }


    public static Date convertStringToDate(String dateString){
        DateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date date=null;
        try{
            date = df.parse(dateString);
        }
        catch ( Exception ex ){
            System.out.println(ex);
        }
        return date;
    }

    public static Date getDate(){
        Date date = new Date();
        return date;
    }

    public interface ChatInterface{
        public void updateChat(chat mChat);
    }
}

