package com.rabbitt.smarttech.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rabbitt.smarttech.Adapter.RecycleAdapter;
import com.rabbitt.smarttech.Adapter.TimeLineAdapter;
import com.rabbitt.smarttech.PrefsManager.Config;
import com.rabbitt.smarttech.R;
import com.rabbitt.smarttech.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.rabbitt.smarttech.OtpActivity.TAG;
import static com.rabbitt.smarttech.PrefsManager.PrefsManager.ID_KEY;
import static com.rabbitt.smarttech.PrefsManager.PrefsManager.USER_PREFS;

public class MainFragment extends Fragment implements TimeLineAdapter.OnRecycleItemListener {

    View view;

    RecyclerView recyclerView;
    List<RecycleAdapter> productAdapter;
    TimeLineAdapter recycleadapter;
    List<RecycleAdapter> data = new ArrayList<>();
    RecycleAdapter model = null;
    String user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        SharedPreferences shrp = Objects.requireNonNull(getActivity()).getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        user_id = shrp.getString(ID_KEY,"");
        Log.i(TAG, "init: user_id  "+user_id);

        recyclerView = view.findViewById(R.id.time_recycler);
        productAdapter = new ArrayList<>();

        getCompanyList();
        updaterecyclershit(data);
    }

    private void getCompanyList() {
        Log.i(TAG, "Current thread: get " + Thread.currentThread().getId());
        //progressdialog until the data retrieved
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Collecting Information", "Please wait...", false, false);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.TIMELINE,
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
                        Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_LONG).show();
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
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
//            }
//
//        }).start();



    }
    private void updaterecyclershit(List<RecycleAdapter> datam) {


        Log.i(TAG, "Current thread:update " + Thread.currentThread().getId());
        if (datam != null) {

            recycleadapter = new TimeLineAdapter(datam, this, this);
            Log.i("HIteshdata", "" + datam);

            LinearLayoutManager reLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(reLayoutManager);

            reLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            recyclerView.setItemAnimator(new DefaultItemAnimator());

            recyclerView.setAdapter(recycleadapter);

            recycleadapter.notifyDataSetChanged();
        }
        else
        {
            Log.i(TAG, "updaterecyclershit: else");
        }

//        recyclerView = findViewById(R.id.recycler_view);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setHasFixedSize(true);
//        adapter = new MyAdapter();
//        recyclerView.setAdapter(adapter);
    }


    @Override
    public void OnItemClick(int position) {

    }
}
