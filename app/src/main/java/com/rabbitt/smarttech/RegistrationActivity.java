package com.rabbitt.smarttech;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rabbitt.smarttech.MainNavigation.MainActivity;
import com.rabbitt.smarttech.PrefsManager.Config;
import com.rabbitt.smarttech.PrefsManager.PrefsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "rkd";
    EditText user, mobile, address, email, password;
    String getId, userStr,phoneStr, token, emailStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        update_user();
        init();
    }

    private void init() {
        user = findViewById(R.id.name);
        mobile = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        email= findViewById(R.id.email);
        password = findViewById(R.id.password);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.TOKEN_PREF, MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
        Log.i(TAG, "TokenRegistration: " + token);
    }

    private void update_user() {
        //denote user visit this page
        PrefsManager prefsManager = new PrefsManager(getApplicationContext());
        prefsManager.setFirstTimeLaunch(true);
    }

    public void insertUser(View view) {

        userStr = user.getText().toString();
        phoneStr = mobile.getText().toString();
        emailStr = email.getText().toString();


        final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);

        //Again creating the string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.USER_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //cancel the progress dialog
                        loading.dismiss();
                        Log.i(TAG, "Responce.............." + response);
                        try {
                            JSONArray arr = new JSONArray(response);
                            JSONObject jb = arr.getJSONObject(0);
                            getId = jb.getString("id");
                            Log.i(TAG, "ID: " + getId);
                            if (!getId.equals("")) {
                                setPrefsdetails();
                            } else {
                                Toast.makeText(getApplicationContext(), "Registration...Please try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Server is not responding...Please Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Log.i(TAG, "volley error.............................." + error.getMessage());
                        Toast.makeText(getApplicationContext(), "Server is not responding...Please Try again!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("USER_NAME", user.getText().toString());
                params.put("MOBILE", phoneStr);
                params.put("EMAIL", email.getText().toString());
                params.put("ADDRESS", address.getText().toString());
                params.put("TOKEN", token);
                params.put("PASSWORD", password.getText().toString());
                return params;
            }
        };

        //Adding request the the queue
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void setPrefsdetails() {
        PrefsManager prefsManager = new PrefsManager(this);
        prefsManager.userPreferences(getId, userStr, phoneStr, emailStr);
        Log.i(TAG, "set preference Hid.............." + getId);
        startActivity(new Intent(this, MainActivity.class));
    }
}
