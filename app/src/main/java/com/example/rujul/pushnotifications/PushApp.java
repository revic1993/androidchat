package com.example.rujul.pushnotifications;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.rujul.pushnotifications.database.DaoMaster;
import com.example.rujul.pushnotifications.database.DaoSession;
import com.example.rujul.pushnotifications.services.NetworkService;
import com.example.rujul.pushnotifications.services.SharedPreferenceService;
import com.example.rujul.pushnotifications.services.VolleyService;

import java.util.List;

public class PushApp extends Application {

    private static PushApp mInstance;
    private RequestQueue mRequestQueue;
    private VolleyService mVolleyService;
    private NetworkService mNetworkService;
    public static User mUser;
    private SharedPreferenceService mSpf;
    private DaoSession daoSession;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mUser = new User();
    }

    public static synchronized PushApp getApp(){
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue==null)
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req,String tag) {
        req.setTag(TextUtils.isEmpty(tag)? Constants.TAG : tag);
        getRequestQueue().add(req);
    }


    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static void makeToast(String message){
        Toast.makeText(mInstance, message, Toast.LENGTH_LONG).show();
    }

    public NetworkService getNetworkService(){
        if(mNetworkService == null)
            mNetworkService = new NetworkService();
        return mNetworkService;
    }
    public VolleyService getVolley(){
        if(mVolleyService==null)
            mVolleyService = new VolleyService();
        return mVolleyService;
    }
    
    public SharedPreferenceService getSPF(){
        if(mSpf == null)
            mSpf = new SharedPreferenceService(this);
        return mSpf;
    }

    public DaoSession getDaoSession(){
        if(daoSession==null){
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, Constants.DB_NAME, null);
            SQLiteDatabase db = helper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}
