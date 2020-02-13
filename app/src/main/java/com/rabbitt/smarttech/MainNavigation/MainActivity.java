package com.rabbitt.smarttech.MainNavigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.rabbitt.smarttech.Fragment.AboutFrag;
import com.rabbitt.smarttech.Fragment.MainFragment;
import com.rabbitt.smarttech.Fragment.MemberFrag;
import com.rabbitt.smarttech.Fragment.SettingFrag;
import com.rabbitt.smarttech.PrefsManager.PrefsManager;
import com.rabbitt.smarttech.R;
import com.rabbitt.smarttech.SettingActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class MainActivity extends AppCompatActivity implements DuoMenuView.OnMenuClickListener, MemberFrag.OnFragmentInteractionListener, SettingFrag.OnFragmentInteractionListener, AboutFrag.OnFragmentInteractionListener {

    private static final String TAG = "rkd";
    private MenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;

    private ArrayList<String> mTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));

        // Initialize the views
        mViewHolder = new ViewHolder();

        // Handle toolbar actions
        handleToolbar();

        // Handle menu actions
        handleMenu();

        // Handle drawer actions
        handleDrawer();

        // Show main fragment in container
        goToFragment(new MainFragment());
        mMenuAdapter.setViewSelected(0, true);
        setTitle(mTitles.get(0));
        //denote user visit this page

        PrefsManager prefsManager = new PrefsManager(getApplicationContext());
        prefsManager.setFirstTimeLaunch(true);
    }

    private void handleToolbar() {
        setSupportActionBar(mViewHolder.mToolbar);
    }

    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mViewHolder.mDuoDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();

    }

    private void handleMenu() {
        mMenuAdapter = new MenuAdapter(mTitles);

        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
    }

    @Override
    public void onFooterClicked() {

    }

    @Override
    public void onHeaderClicked() {

    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
        setTitle(mTitles.get(position));

        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true);

        // Navigate to the right fragment
        Log.i(TAG, "onOptionClicked: "+position);
        switch (position) {
            case 0:
                goToFragment(new MainFragment());
                break;
            case 1:
                goToFragment(new MemberFrag());
                break;
            case 2:
                goToFragment(new SettingFrag());
//                startActivity(new Intent(this, SettingActivity.class));
                break;
            case 3:
                goToFragment(new AboutFrag());
                break;
            default:
                break;
        }

        // Close the drawer
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

    private void goToFragment(Fragment fragment) {
        if (fragment != null) {
            Log.i(TAG, "Current thread: " + Thread.currentThread().getId());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mDuoDrawerLayout = findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar = findViewById(R.id.toolbar);
        }
    }
}