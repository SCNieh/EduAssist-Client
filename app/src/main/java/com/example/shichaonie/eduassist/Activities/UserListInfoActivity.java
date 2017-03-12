package com.example.shichaonie.eduassist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shichaonie.eduassist.MainActivityFragments.UserInfoFragmentUtil.UserInfoLoader;
import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.User;
import com.example.shichaonie.eduassist.Utils.ConvertUtil;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.view.View.GONE;

/**
 * Created by Shichao Nie on 2017/1/19.
 */

public class UserListInfoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<User> {
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
        if(user.getmPrivateCode() == 0){
            userSelect.setVisibility(GONE);
            Toast.makeText(UserListInfoActivity.this, R.string.private_mode_not_support, Toast.LENGTH_LONG).show();
        }else {
            userSelect.setVisibility(View.VISIBLE);
        }
        userSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserListInfoActivity.this, NewQuestionActivity.class);
                intent.putExtra("targetName", user.getmName());
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
    public void updateUI(final User data){
        TextView userInfoName = (TextView) findViewById(R.id.user_list_info_name);
        TextView userInfoTitle = (TextView) findViewById(R.id.user_list_info_title);
        TextView userInfoId = (TextView) findViewById(R.id.user_list_info_id);
        TextView userInfoEmail = (TextView) findViewById(R.id.user_list_info_email);
        TextView userInfoPrivateMode = (TextView) findViewById(R.id.user_list_info_private_mode);
        TextView userInfoSelfIntro = (TextView) findViewById(R.id.user_list_info_self_intro);
        TextView userCreditStatus = (TextView) findViewById(R.id.user_list_info_credit_status);
        userCreditStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserListInfoActivity.this, UserCreditActivity.class);
                intent.putExtra("userCredit", data.getmCredit());
                startActivity(intent);
            }
        });

        if(data != null){
            userInfoName.setText(data.getmName());
            userInfoTitle.setText(ConvertUtil.toTitle(data.getTitle()));
            userInfoId.setText(Integer.toString(data.getId()));
            if(data.getEmail() == null || data.getEmail().isEmpty() || data.getEmail().equals("null")){
                userInfoEmail.setVisibility(GONE);
            }else {
                userInfoEmail.setText(data.getEmail());
            }
            userInfoPrivateMode.setText(ConvertUtil.toPrivateMode(data.getmPrivateCode()));
            if(data.getmSelfIntro() == null || data.getmSelfIntro().isEmpty() || data.getmSelfIntro().equals("null")){
                userInfoSelfIntro.setText("无");
            }else {
                userInfoSelfIntro.setText(data.getmSelfIntro());
            }
        }

    }
}
