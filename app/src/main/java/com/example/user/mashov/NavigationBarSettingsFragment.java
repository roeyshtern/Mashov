package com.example.user.mashov;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by User on 1/21/18.
 */

public class NavigationBarSettingsFragment extends Fragment implements View.OnClickListener{

    Button bt_sign_out;

    public static Fragment newInstance() {
        Fragment frag = new NavigationBarSettingsFragment();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_navigation_bar_fragment_layout, container, false);
        bt_sign_out = (Button)view.findViewById(R.id.sfl_bt_sign_out);
        bt_sign_out.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.sfl_bt_sign_out:
            {
                SaveSharedPreference sp = new SaveSharedPreference();
                sp.signoutUser(getActivity().getApplicationContext());
                Toast.makeText(getActivity().getApplicationContext(), "Sign out successfully!", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
    public class SaveSharedPreference
    {
        static final String PREF_USER_DETAILS= "user_details";

        public SharedPreferences getSharedPreferences(Context ctx) {
            return PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        public void signoutUser(Context ctx)
        {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.remove(PREF_USER_DETAILS);
            editor.commit();
        }
    }
}
