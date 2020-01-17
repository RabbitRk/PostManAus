package com.rabbitt.smarttech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.rabbitt.smarttech.MainNavigation.MainActivity;
import com.rabbitt.smarttech.PrefsManager.PrefsManager;

public class SplashScreenActivity extends AppCompatActivity {

    public static final String LOG_TAG = "rkd";

    private Handler splash = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
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
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
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
}
