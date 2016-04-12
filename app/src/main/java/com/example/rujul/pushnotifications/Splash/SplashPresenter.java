package com.example.rujul.pushnotifications.Splash;

import android.content.Context;
import android.os.Handler;

import com.example.rujul.pushnotifications.Constants;
import com.example.rujul.pushnotifications.PushApp;
import com.example.rujul.pushnotifications.services.NetworkService;
import com.example.rujul.pushnotifications.services.SharedPreferenceService;

/**
 * Created by rujul on 4/4/2016.
 */
public class SplashPresenter {
    private final NetworkService nws;
    private final SharedPreferenceService spfs;
    private final SplashViewInterface view;
    private Context context;
    private boolean userExist;
    public SplashPresenter(NetworkService nws,
                           SharedPreferenceService spfs,
                           SplashViewInterface view,Context context) {
        this.nws = nws;
        this.spfs = spfs;
        this.view = view;
        this.context = context;
        this.userExist = spfs.getBoolData(Constants.USER_EXIST);
        saveUser();
    }


    public void onTryClicked() {
        if(nws.isConnected(context)){
            view.startNewActivity(spfs.getBoolData(Constants.USER_EXIST));
        }else{
            view.changeButtonVisibility();
        }
    }

    public void startSplash(){
        if(nws.isConnected(context)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.startNewActivity(userExist);
                }
            }, Constants.SPLASH_TIME_OUT);
        }else{
            view.changeButtonVisibility();
        }
    }

    private void saveUser(){
        PushApp.mUser.isSet = userExist;
        if(userExist){
            PushApp.mUser.mobileNo = spfs.getStringData(Constants.MOBILE);
            PushApp.mUser.username = spfs.getStringData(Constants.NAME);
        }
    }

    public interface SplashViewInterface{
        public void changeButtonVisibility();
        public void startNewActivity(boolean i);
    }
}
