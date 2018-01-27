package com.example.user.mashov;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 1/9/18.
 */

public class NavigationBarHomeFragment extends Fragment {

    final int GENDER_MALE = 0;
    final int GENDER_FEMALE = 1;
    CircleImageView user_profile_image;
    String jsonOfUserDetails;
    TextView tv_first_name;
    TextView tv_username;
    ListView listview_posts;

    public static Fragment newInstance(String jsonOfUserDetailsParameter) {
        Fragment frag = new NavigationBarHomeFragment();
        Bundle args = new Bundle();
        args.putString("jsonOfUserDetails", jsonOfUserDetailsParameter);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_navigation_bar_fragment_layout, container, false);
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
        tv_first_name = (TextView)view.findViewById(R.id.hfl_user_first_name);
        tv_username = (TextView)view.findViewById(R.id.hfl_username);
        tv_first_name.setText(jsonObject.data.firstName);
        tv_username.setText("@"+jsonObject.data.username);

        //put here the hex to image

        user_profile_image = (CircleImageView)view.findViewById(R.id.hfl_user_profile_image);
        switch(jsonObject.data.gender)
        {
            case GENDER_MALE:
            {
                user_profile_image.setBorderColor(Color.BLACK);
                break;
            }
            case GENDER_FEMALE:
            {
                user_profile_image.setBorderColor(Color.YELLOW);
                break;
            }
        }
        PostsListAdapter postsListAdapter = new PostsListAdapter(getActivity(),jsonObject.data.posts);
        listview_posts = (ListView)view.findViewById(R.id.hfl_list_view);
        if(jsonObject.data.posts==null)
        {
            String[] arr = {};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,android.R.id.text1, arr);
            listview_posts.setAdapter(adapter);
        }
        else
        {
            listview_posts.setAdapter(postsListAdapter);
        }



    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("jsonOfUserDetails", jsonOfUserDetails);
        super.onSaveInstanceState(outState);
    }
}
