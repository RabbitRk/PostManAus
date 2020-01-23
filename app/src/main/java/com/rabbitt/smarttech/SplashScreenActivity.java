package com.rabbitt.smarttech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.rabbitt.smarttech.BroadCastReceiver.InternetBroadCast;
import com.rabbitt.smarttech.MainNavigation.MainActivity;
import com.rabbitt.smarttech.PrefsManager.PrefsManager;

public class SplashScreenActivity extends AppCompatActivity {

    public static final String LOG_TAG = "rkd";

    private Handler splash = new Handler();

    InternetBroadCast receiver;
    IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        checkInternetConnectivity();

        splash.postDelayed(new Runnable() {
            @Override
            public void run() {
                try
                {
                    PrefsManager prefsManager = new PrefsManager(getApplicationContext());
                    if (!prefsManager.isFirstTimeLaunch()) {
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        if (prefsManager.isFlashResult())
                        {
                            Intent intent = new Intent(getApplicationContext(),MemberActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
                catch (Exception ex)
                {
                    Log.i(LOG_TAG, "Error in handler");
                    ex.printStackTrace();
                }
            }
        }, 1000);
    }

    private void checkInternetConnectivity() {
        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new InternetBroadCast();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        try {
            if (receiver != null)
                unregisterReceiver(receiver);
        } catch (Exception ignored) {
        }
        super.onDestroy();
    }
}
