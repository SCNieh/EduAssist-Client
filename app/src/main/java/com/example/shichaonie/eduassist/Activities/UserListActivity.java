package com.example.shichaonie.eduassist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.User;
import com.example.shichaonie.eduassist.UserListUtils.UserAdapter;

/**
 * Created by Shichao Nie on 2017/1/4.
 */

public class UserListActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_activity);

        final FragmentManager fm = getSupportFragmentManager();
        final ListView userList = (ListView) fm.findFragmentById(R.id.user_list_fragment).getView().findViewById(R.id.user_list_list);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), Long.toString(id), Toast.LENGTH_SHORT).show();
                if (id < -1) { //headview || footview
                    return;
                }
                UserAdapter adapter = (UserAdapter) userList.getAdapter();
                User user = adapter.getItem((int) id);
                int userId = 0;
                if (user != null) {
                    userId = user.getId();
                }

                Intent intent = new Intent(UserListActivity.this, UserInfoActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);

            }
        });

    }

}
