package com.example.user.mashov;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HomeActivity extends ServiceHandler {
    private static final String SELECTED_ITEM = "arg_selected_item";

    private BottomNavigationView mBottomNav;
    private int mSelectedItem;
    JsonObject<LoginAndRegisterANS> jsonObject;
    String jsonOfUserDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        //init all the variables
        init();

        Gson gson = new Gson();
        jsonOfUserDetails = getIntent().getStringExtra("LoginAndRegisterANS");
        jsonObject = gson.fromJson(jsonOfUserDetails, new TypeToken<JsonObject<LoginAndRegisterANS>>(){}.getType());


        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 1);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(1);
        }
        selectFragment(selectedItem);
    }
    public void init()
    {
        mBottomNav = (BottomNavigationView) findViewById(R.id.hl_navigation_bar);
        mSelectedItem = 0;
        JsonObject<LoginAndRegisterANS> jsonObject = new JsonObject<>();
        String jsonOfUserDetails = "";
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = mBottomNav.getMenu().getItem(1);
        if (mSelectedItem != homeItem.getItemId()) {
            // select home item
            selectFragment(homeItem);
        } else {
            super.onBackPressed();
        }
    }
    private void selectFragment(MenuItem item) {
        Fragment frag = null;
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.hl_navigation_bar_settings:
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.hl_navigation_bar_home:
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
                frag = NavigationBarHomeFragment.newInstance(jsonOfUserDetails);
                break;
            case R.id.hl_navigation_bar_show_class:
                Toast.makeText(this, "class", Toast.LENGTH_SHORT).show();
                frag = NavigationBarClassesFragment.newInstance(jsonOfUserDetails);
                break;
        }

        // update selected item
        mSelectedItem = item.getItemId();

        // uncheck the other items.
        for (int i = 0; i< mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }


        if (frag != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.hl_container, frag, frag.getTag());
            ft.commit();
        }
    }
    @Override
    public void onReceiveResult(String response) {

    }
}
