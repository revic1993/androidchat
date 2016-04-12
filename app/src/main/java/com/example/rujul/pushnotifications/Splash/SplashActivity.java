package com.example.rujul.pushnotifications.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rujul.pushnotifications.PushApp;
import com.example.rujul.pushnotifications.R;
import com.example.rujul.pushnotifications.grouplist.GroupListActivity;
import com.example.rujul.pushnotifications.userdetails.UserDetails;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity implements SplashPresenter.SplashViewInterface{


    private Button bTryAgain;
    private SplashPresenter presenter;
    private TextView tvSplash;
    private PushApp mApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        mApp = PushApp.getApp();
        presenter = new SplashPresenter(mApp.getNetworkService(),
                mApp.getSPF(),this,this);
        bTryAgain = (Button) findViewById(R.id.bTryAgain);
        tvSplash = (TextView) findViewById(R.id.tvSplash);

        bTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onTryClicked();
            }
        });

        presenter.startSplash();
    }


    @Override
    public void changeButtonVisibility() {
        bTryAgain.setVisibility(View.VISIBLE);
        PushApp.makeToast("No network connection.");
    }

    @Override
    public void startNewActivity(boolean userExist) {
        Intent i;
        if(!userExist)
            i = new Intent(SplashActivity.this,UserDetails.class);
        else
            i = new Intent(SplashActivity.this,GroupListActivity.class);
        startActivity(i);
        finish();
    }
}
