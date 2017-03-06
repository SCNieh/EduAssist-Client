package com.example.shichaonie.eduassist.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.shichaonie.eduassist.MyInvitationActivity.CategoryAdapter;
import com.example.shichaonie.eduassist.R;

/**
 * Created by Shichao Nie on 2017/3/5.
 */

public class MyInvitationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_invitation_activity);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pagers);
        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
