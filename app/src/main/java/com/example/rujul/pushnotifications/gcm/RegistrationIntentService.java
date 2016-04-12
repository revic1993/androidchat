package com.example.rujul.pushnotifications.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.rujul.pushnotifications.Constants;
import com.example.rujul.pushnotifications.PushApp;
import com.example.rujul.pushnotifications.R;
import com.example.rujul.pushnotifications.services.SharedPreferenceService;
import com.example.rujul.pushnotifications.services.VolleyService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.rujul.pushnotifications.Constants.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rujul on 4/3/2016.
 */
public class RegistrationIntentService extends IntentService implements VolleyService.VolleyInterface{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private static final String NAME = RegistrationIntentService.class.getSimpleName();
    SharedPreferenceService spf;

    public RegistrationIntentService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        InstanceID instaceId = InstanceID.getInstance(this);
        String senderId = getResources().getString(R.string.gcm_sender_id);
        String token = "";

        try {
            token = instaceId.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
            sendTokenToServer(token);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            Log.d("GCMTOKEN", token);
        }

    }

    private void sendTokenToServer(String token) {
        VolleyService service = PushApp.getApp().getVolley();
        spf = PushApp.getApp().getSPF();

        Map<String,String> params = new HashMap<String,String>();
        params.put(Constants.MOBILE,spf.getStringData(Constants.TEMP_MOBILE));
        params.put(Constants.NAME,spf.getStringData(Constants.TEMP_NAME));
        params.put(Constants.GCM,token);

        service.makePostRequest(Constants.SIGNUP_URL,params,this);
    }

    @Override
    public void onSuccess(JSONObject response) {
        Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        Log.d("preference",response.toString());
        try {
            JSONObject user = response.getJSONObject(Constants.DATA).getJSONObject(Constants.USER);
            Log.d("preference",user.toString());
            spf.putData(Constants.ANAME,user.getString(Constants.NAME));
            spf.putData(Constants.AMOBILE,""+user.getLong(Constants.MOBILE));
            spf.putData(Constants.USER_EXIST, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(VolleyError error) {
        Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        spf.putData(Constants.USER_EXIST, false);
        error.printStackTrace();
        Log.d("VOLLEY", new String(error.networkResponse.data));
    }
}
