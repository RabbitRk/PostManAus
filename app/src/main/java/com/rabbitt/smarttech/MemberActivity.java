package com.rabbitt.smarttech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rabbitt.smarttech.Adapter.MTimeLineAdapter;
import com.rabbitt.smarttech.Adapter.RecycleAdapter;
import com.rabbitt.smarttech.Adapter.TimeLineAdapter;
import com.rabbitt.smarttech.PrefsManager.Config;
import com.rabbitt.smarttech.PrefsManager.PrefsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rabbitt.smarttech.OtpActivity.TAG;
import static com.rabbitt.smarttech.PrefsManager.PrefsManager.ID_KEY;
import static com.rabbitt.smarttech.PrefsManager.PrefsManager.USER_PREFS;

public class MemberActivity extends AppCompatActivity implements MTimeLineAdapter.OnRecycleItemListener{

//    View view;

    RecyclerView recyclerView;
    List<RecycleAdapter> productAdapter;
    MTimeLineAdapter recycleadapter;
    List<RecycleAdapter> data = new ArrayList<>();
    RecycleAdapter model = null;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        PrefsManager prefsManager = new PrefsManager(this);
        prefsManager.setFirstTimeLaunch(true);
        prefsManager.setFlashResult(true);

        SharedPreferences shrp = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        user_id = shrp.getString(ID_KEY,"");
        getCompanyList();

        Toolbar toolbar = findViewById(R.id.tool);
        // Setting toolbar as the ActionBar with setSupportActionBar() call
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.member_recycler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item1:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
//            case R.id.item2:
//                Toast.makeText(getApplicationContext(),"Log off",Toast.LENGTH_LONG).show();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getCompanyList() {


        Log.i(TAG, "Current thread: get " + Thread.currentThread().getId());
        //progressdialog until the data retrieved
        final ProgressDialog loading = ProgressDialog.show(this, "Collecting Information", "Please wait...", false, false);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MTIMELINE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //cancel the progress dialog
                        loading.dismiss();

                        Log.i(TAG, "Responce.............." + response);
                        try {
                            JSONArray arr = new JSONArray(response);
                            JSONObject jb;// = arr.getJSONObject(0);
                            int n = arr.length();

                            for (int i = 0; i < n; i++) {
                                jb = arr.getJSONObject(i);
                                model = new RecycleAdapter();
                                model.setItem_date(jb.getString("item_date"));
                                data.add(model);
                            }

                            updaterecyclershit(data);

                        } catch (JSONException e) {
                            Log.i(TAG, "Error: " + e.getMessage());
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
                return params;
            }
        };

        //Adding request the the queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void updaterecyclershit(List<RecycleAdapter> datam) {


        Log.i(TAG, "Current thread:update " + Thread.currentThread().getId());
        if (datam != null) {

            recycleadapter = new MTimeLineAdapter(datam, this, this);
            Log.i("HIteshdata", "" + datam);

            LinearLayoutManager reLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(reLayoutManager);

            reLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            recyclerView.setItemAnimator(new DefaultItemAnimator());

            recyclerView.setAdapter(recycleadapter);

            recycleadapter.notifyDataSetChanged();
        }
    }

    @Override
    public void OnItemClick(int position) {

    }
}