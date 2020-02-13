package com.rabbitt.smarttech.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rabbitt.smarttech.Adapter.RecycleAdapter;
import com.rabbitt.smarttech.PrefsManager.Config;
import com.rabbitt.smarttech.PrefsManager.PrefsManager;
import com.rabbitt.smarttech.R;
import com.rabbitt.smarttech.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.rabbitt.smarttech.OtpActivity.TAG;
import static com.rabbitt.smarttech.PrefsManager.PrefsManager.ID_KEY;
import static com.rabbitt.smarttech.PrefsManager.PrefsManager.USER_NAME;
import static com.rabbitt.smarttech.PrefsManager.PrefsManager.USER_PREFS;

public class SettingFrag extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    Switch ems, notis;

    private OnFragmentInteractionListener mListener;
    PrefsManager prefsManager;
    String user_id;

    public SettingFrag() {
        // Required empty public constructor
    }

    public static SettingFrag newInstance(String param1, String param2) {
        SettingFrag fragment = new SettingFrag();
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString(ID_KEY, "");

        prefsManager = new PrefsManager(Objects.requireNonNull(getActivity()));

        ems =  view.findViewById(R.id.email);
        ems.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateemail(1);
                    prefsManager.setEmail(true);

                } else {
                    updateemail(0);
                    prefsManager.setEmail(false);

                }
            }
        });

        notis =  view.findViewById(R.id.notification);
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
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Collecting Information", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.NOTI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //cancel the progress dialog
                        loading.dismiss();
                        if (response.equals("success"))
                        {
                            Toast.makeText(getActivity(), "Updated successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Can't process your request now", Toast.LENGTH_SHORT).show();
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
                params.put("VAL", String.valueOf(i));
                return params;
            }
        };

        //Adding request the the queue
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void updateemail(final int i) {
        Log.i(TAG, "Current thread: get " + Thread.currentThread().getId());

        //progressdialog until the data retrieved
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Collecting Information", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.EMAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //cancel the progress dialog
                        loading.dismiss();
                        if (response.equals("success"))
                        {
                            Toast.makeText(getActivity(), "Updated successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Can't process your request now", Toast.LENGTH_SHORT).show();
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
                params.put("VAL", String.valueOf(i));
                return params;
            }
        };

        //Adding request the the queue
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void getSharedPrefs() {
        SharedPreferences shrp = getActivity().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        ems.setChecked(shrp.getBoolean("SET_EMAIL", true));
        notis.setChecked(shrp.getBoolean("SET_NOTIS", true));
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
