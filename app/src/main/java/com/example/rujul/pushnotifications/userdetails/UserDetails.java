package com.example.rujul.pushnotifications.userdetails;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.rujul.pushnotifications.Constants;
import com.example.rujul.pushnotifications.PushApp;
import com.example.rujul.pushnotifications.R;
import com.example.rujul.pushnotifications.grouplist.GroupListActivity;
import com.example.rujul.pushnotifications.gcm.RegistrationIntentService;
import com.example.rujul.pushnotifications.services.SharedPreferenceService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.regex.Pattern;

public class UserDetails extends AppCompatActivity {


    EditText etName,etPhone;
    Button bNext;
    Pattern pName,pNumber;
    SharedPreferenceService spf;
    ProgressDialog progressDialog;
    boolean isReceiverRegistered=false;
    BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        pName =  Pattern.compile("^[a-zA-Z]+$");
        pNumber = Pattern.compile("\\d{10}");
        etName = (EditText) findViewById(R.id.etUsername);
        etPhone = (EditText) findViewById(R.id.etPhone);
        bNext = (Button) findViewById(R.id.bDone);
        spf = PushApp.getApp().getSPF();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                progressDialog.dismiss();
                if (spf.getBoolData(Constants.USER_EXIST)) {
                    Intent nextActivity = new Intent(UserDetails.this, GroupListActivity.class);
                    startActivity(nextActivity);
                    finish();
                } else {
                    PushApp.makeToast("There was error on server");
                }
            }
        };
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               clickPerformed();
            }
        });
    }

    private void clickPerformed() {
        if(!pNumber.matcher(etPhone.getText().toString()).matches()){
            etPhone.setError("Please enter 10 digit phone number");
            return;
        }

        if(!pName.matcher(etName.getText().toString()).matches()){
            etName.setError("Please enter alphabets only");
            return;
        }
        startTokenOperation();
    }

    private void startTokenOperation() {
        spf.putData(Constants.TEMP_MOBILE,etPhone.getText().toString());
        spf.putData(Constants.TEMP_NAME, etName.getText().toString());

        if(checkPlayServices()){
            startProgressBar();
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private void startProgressBar(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Creating account");
        progressDialog.show();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Constants.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, Constants.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("GCMService", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }



}
