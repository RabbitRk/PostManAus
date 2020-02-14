package com.rabbitt.smarttech;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rabbitt.smarttech.PrefsManager.Config;
import com.rabbitt.smarttech.PrefsManager.PrefsManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.rabbitt.smarttech.OtpActivity.TAG;
import static com.rabbitt.smarttech.PrefsManager.PrefsManager.ID_KEY;
import static com.rabbitt.smarttech.PrefsManager.PrefsManager.USER_PREFS;

public class SettingActivity extends AppCompatActivity {

    String user_id;
    PrefsManager prefsManager;
    Switch ems, notis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();

    }

    private void init() {

        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString(ID_KEY, "");

        prefsManager = new PrefsManager(this);

//        ems =  findViewById(R.id.email_mem);
//        ems.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    updateemail(1);
//                    prefsManager.setEmail(true);
//
//                } else {
//                    updateemail(0);
//                    prefsManager.setEmail(false);
//
//                }
//            }
//        });

        notis =  findViewById(R.id.notification);
        notis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updatenotis(1);
                    prefsManager.setNotis(true);
                } else {
                    updatenotis(0);
                    prefsManager.setNotis(false);
                }
            }
        });

        getSharedPrefs();
    }

    private void updatenotis(final int i) {
        Log.i(TAG, "Current thread: get " + Thread.currentThread().getId());

        //progressdialog until the data retrieved
        final ProgressDialog loading = ProgressDialog.show(this, "Collecting Information", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.M_NOTI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //cancel the progress dialog
                        loading.dismiss();
                        if (response.equals("success"))
                        {
                            Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Can't process your request now", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Log.i(TAG, "volley error.............................." + error.getMessage());
                        Toast.makeText(getApplicationContext(), "Server is not responding", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("USER_ID", user_id);
                params.put("VAL", String.valueOf(i));
                return params;
            }
        };

        //Adding request the the queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void updateemail(final int i) {
        Log.i(TAG, "Current thread: get " + Thread.currentThread().getId());

        //progressdialog until the data retrieved
        final ProgressDialog loading = ProgressDialog.show(getApplicationContext(), "Collecting Information", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.M_EMAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //cancel the progress dialog
                        loading.dismiss();
                        if (response.equals("success"))
                        {
                            Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Can't process your request now", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Log.i(TAG, "volley error.............................." + error.getMessage());
                        Toast.makeText(getApplicationContext(), "Server is not responding", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("USER_ID", user_id);
                params.put("VAL", String.valueOf(i));
                return params;
            }
        };

        //Adding request the the queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void getSharedPrefs() {
        SharedPreferences shrp = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
//        ems.setChecked(shrp.getBoolean("SET_EMAIL", true));
        notis.setChecked(shrp.getBoolean("SET_NOTIS", true));
    }
}
