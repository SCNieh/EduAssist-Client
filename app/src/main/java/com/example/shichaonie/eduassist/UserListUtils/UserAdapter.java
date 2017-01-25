package com.example.shichaonie.eduassist.UserListUtils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.UserData.User;
import com.example.shichaonie.eduassist.Utils.ConvertUtil;

import java.util.ArrayList;

/**
 * Created by Shichao Nie on 2017/1/4.
 */

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(Context context, ArrayList<User> arrayList){
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if(listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.user_list_item, parent, false);
        }
        User userData = getItem(position);
        TextView name = (TextView) listView.findViewById(R.id.user_list_item_name);
        name.setText(userData.getmName());

        TextView title = (TextView) listView.findViewById(R.id.user_list_item_title);
        title.setText(ConvertUtil.toTitle(userData.getTitle()));

        TextView text = (TextView) listView.findViewById(R.id.user_list_item_text);
        text.setText(userData.getmSelfIntro());

        return listView;
    }
}
