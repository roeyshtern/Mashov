package com.example.user.mashov;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User on 1/23/18.
 */

public class PostsListAdapter extends ArrayAdapter<Post> {
    TextView tv_post_status;
    public PostsListAdapter(Context context, List<Post> posts) {
        super(context, 0, posts);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Post post = getItem(position);
        if(convertView==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_post, parent, false);
        }

        //do the image code hex to image

        tv_post_status = (TextView)convertView.findViewById(R.id.list_item_post_status);
        tv_post_status.setText(post.data);
        return convertView;
    }
}
