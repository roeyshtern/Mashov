package com.example.user.mashov;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 1/16/18.
 */

public class ClassesListAdapter extends ArrayAdapter<Class> {
    TextView tv_class_name;
    public ClassesListAdapter(Context context, ArrayList<Class> classes)
    {
        super(context,0,classes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Class aClass = getItem(position);
        if(convertView==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_class, parent, false);
        }

        //do the image code hex to image

        tv_class_name = (TextView)convertView.findViewById(R.id.list_item_class_name);

        tv_class_name.setText(aClass.className);
        return convertView;
    }
}
