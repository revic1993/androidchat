package com.example.rujul.pushnotifications.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.rujul.pushnotifications.Constants;

/**
 * Created by rujul on 4/4/2016.
 */
public class SharedPreferenceService {

    private SharedPreferences spf;
    private Context mContext;
    private SharedPreferences.Editor editor;

    public SharedPreferenceService(Context mContext) {
        this.mContext = mContext;
        spf = mContext.getSharedPreferences(Constants.SHARED_FILE_NAME,Context.MODE_PRIVATE);
        editor = spf.edit();
    }

    public String getStringData(String key){
        return spf.getString(key,Constants.DEFAULT);
    }

    public boolean getBoolData(String key){
        return spf.getBoolean(key,false);
    }

    public int getIntData(String key){
        return spf.getInt(key,0);
    }

    public boolean putData(String key,String data){
        editor.putString(key,data);
        return editor.commit();
    }

    public boolean putData(String key,boolean data){
        editor.putBoolean(key, data);
        return editor.commit();
    }

    public boolean putData(String key,int data){
        editor.putInt(key, data);
        return editor.commit();
    }

}
