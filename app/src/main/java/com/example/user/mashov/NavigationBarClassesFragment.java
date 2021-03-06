package com.example.user.mashov;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by User on 1/16/18.
 */

public class NavigationBarClassesFragment extends Fragment{

    String jsonOfUserDetails;
    ListView listview_classes;

    public static Fragment newInstance(String jsonOfUserDetailsParameter) {
        Fragment frag = new NavigationBarClassesFragment();
        Bundle args = new Bundle();
        args.putString("jsonOfUserDetails", jsonOfUserDetailsParameter);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.classes_navigation_bar_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            Bundle args = getArguments();
            jsonOfUserDetails = args.getString("jsonOfUserDetails");
        } else {
            jsonOfUserDetails = savedInstanceState.getString("jsonOfUserDetails");
        }

        Gson gson = new Gson();
        JsonObject<LoginAndRegisterANS> jsonObject = gson.fromJson(jsonOfUserDetails, new TypeToken<JsonObject<LoginAndRegisterANS>>(){}.getType());
        ClassesListAdapter classesListAdapter = new ClassesListAdapter(getActivity(),jsonObject.data.classes);
        listview_classes = (ListView)view.findViewById(R.id.cfl_list_view);
        if(jsonObject.data.classes==null)
        {
            String[] arr = {};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,android.R.id.text1, arr);
            listview_classes.setAdapter(adapter);
        }
        else
        {
            listview_classes.setAdapter(classesListAdapter);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("jsonOfUserDetails", jsonOfUserDetails);
        super.onSaveInstanceState(outState);
    }
}
