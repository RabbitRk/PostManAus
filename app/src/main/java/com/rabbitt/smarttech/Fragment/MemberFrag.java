package com.rabbitt.smarttech.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rabbitt.smarttech.Adapter.MTimeLineAdapter;
import com.rabbitt.smarttech.Adapter.MemberAdapter;
import com.rabbitt.smarttech.Adapter.RecycleAdapter;
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
import java.util.Random;

import static com.rabbitt.smarttech.OtpActivity.TAG;
import static com.rabbitt.smarttech.PrefsManager.PrefsManager.ID_KEY;
import static com.rabbitt.smarttech.PrefsManager.PrefsManager.USER_PREFS;

public class MemberFrag extends Fragment implements MemberAdapter.OnRecycleItemListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView member_;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    List<RecycleAdapter> productAdapter;
    MemberAdapter recycleadapter;
    List<RecycleAdapter> data = new ArrayList<>();
    RecycleAdapter model = null;
    String user_id;
    int member_code;

    public MemberFrag() {
        // Required empty public constructor
    }

    public static MemberFrag newInstance(String param1, String param2) {
        MemberFrag fragment = new MemberFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        SharedPreferences shrp = getActivity().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        user_id = shrp.getString(ID_KEY,"");

        recyclerView = view.findViewById(R.id.member_recycler);
        productAdapter = new ArrayList<>();
        getCompanyList();
        updaterecyclershit(data);

        Random rand = new Random();
        member_ = view.findViewById(R.id.parent_id);

        // Generate random integers in range 0 to 999
        member_code = rand.nextInt(10000);
        member_.setText(String.valueOf(member_code));

        updateMemberCode();
    }

    private void updateMemberCode() {
        Log.i(TAG, "Current thread: get " + Thread.currentThread().getId());
        //progressdialog until the data retrieved
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Updating Member code", "Please wait...", false, false);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPDATE_MEMBER_CODE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //cancel the progress dialog
                        loading.dismiss();
                        Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
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
                params.put("MEMBER_CODE", String.valueOf(member_code));
                return params;
            }
        };

        //Adding request the the queue
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void getCompanyList() {

        Log.i(TAG, "Current thread: get " + Thread.currentThread().getId());

        //progressdialog until the data retrieved
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Collecting Information", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MEMBER_LIST,
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
                                model.setUser_id(jb.getString("user_id"));
                                model.setMember_name(jb.getString("name"));
                                model.setMember_phone(jb.getString("phone"));
                                data.add(model);
                            }
//{"id":"1","item_date":"2020-02-13 06:12:20"}]
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
    }
    private void updaterecyclershit(List<RecycleAdapter> datam) {


        Log.i(TAG, "Current thread:update " + Thread.currentThread().getId());
        if (datam != null) {

            recycleadapter = new MemberAdapter(datam, this, this);
            Log.i("HIteshdata", "" + datam);

            LinearLayoutManager reLayoutManager = new LinearLayoutManager(getActivity());
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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
