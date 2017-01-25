package com.example.shichaonie.eduassist.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shichaonie.eduassist.MainActivityFragments.UserInfoFragmentUtil.UserInfoLoader;
import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.User;
import com.example.shichaonie.eduassist.Utils.ConvertUtil;

import static java.security.AccessController.getContext;

/**
 * Created by Shichao Nie on 2017/1/19.
 */

public class UserInfoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<User> {
    public FragmentManager fm;
    public User user;
    public String userId = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_info_activity);

        Intent intent = getIntent();
        userId = Integer.toString(intent.getIntExtra("userId", -1));

        ImageView imageView = (ImageView) findViewById(R.id.user_list_info_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public Loader<User> onCreateLoader(int id, Bundle args) {
        return new UserInfoLoader(this, userId);
    }

    @Override
    public void onLoadFinished(Loader<User> loader, User data) {
        user = data;
        final ImageView userSelect = (ImageView) findViewById(R.id.user_list_info_select);
        userSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, NewQuestionActivity.class);
                intent.putExtra("targetUsername", user.getUsername());
                intent.putExtra("targetId", user.getId());
                startActivity(intent);
            }
        });
        updateUI(user);
    }

    @Override
    public void onLoaderReset(Loader<User> loader) {
        updateUI(null);
    }
    public void updateUI(User data){
        TextView userInfoName = (TextView) findViewById(R.id.user_list_info_name);
        TextView userInfoTitle = (TextView) findViewById(R.id.user_list_info_title);
        TextView userInfoId = (TextView) findViewById(R.id.user_list_info_id);
        TextView userInfoEmail = (TextView) findViewById(R.id.user_list_info_email);
        TextView userInfoPrivateMode = (TextView) findViewById(R.id.user_list_info_private_mode);
        TextView userInfoSelfIntro = (TextView) findViewById(R.id.user_list_info_self_intro);

        if(data != null){
            userInfoName.setText(data.getmName());
            userInfoTitle.setText(ConvertUtil.toTitle(data.getTitle()));
            userInfoId.setText(Integer.toString(data.getId()));
            userInfoEmail.setText(data.getEmail());
            userInfoPrivateMode.setText(ConvertUtil.toPrivateMode(data.getmPrivateCode()));
            userInfoSelfIntro.setText(data.getmSelfIntro());
        }

    }
}
