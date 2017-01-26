package com.example.shichaonie.eduassist.Activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shichaonie.eduassist.MainActivityFragments.UserInfoFragment;
import com.example.shichaonie.eduassist.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import static com.example.shichaonie.eduassist.Utils.ConstantContract.SP_NAME;
import static com.example.shichaonie.eduassist.Utils.ConstantContract.SP_TAG;
import static com.example.shichaonie.eduassist.Utils.ConstantContract.SP_USER_ID;


public class MainActivity extends AppCompatActivity {
    private long firstTime = 0;
    private boolean isSelected = false;
    private boolean mark;
    private SlidingMenu slidingMenu;
    private FloatingActionButton camera_fab;
    private Animation rotateAnimationAnticlockwise;
    private Animation rotateAnimationClockwise;
    private Animation scaleAnimatioAppear;
    private Animation scaleAnimationGone;
    private TextView shelter;
    private TextView askPublic;
    private TextView askPrivate;
    private SharedPreferences sp;
    private FragmentManager fm;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mark = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        iniView();
        iniSlidingMenu();
//        if(!mark){
//            FragmentManager fm = this.getSupportFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.replace(R.id.content_fragment, new ContentFragment());
//            ft.commit();
//        }
//        mark = false;
    }

    private void iniView(){
        sp = getSharedPreferences(SP_TAG, MODE_PRIVATE);
        camera_fab = (FloatingActionButton) findViewById(R.id.camera_fab);
        camera_fab.setVisibility(View.VISIBLE);
        rotateAnimationAnticlockwise = AnimationUtils.loadAnimation(this, R.anim.rotate_anticlockwise);
        rotateAnimationClockwise = AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise);
        scaleAnimatioAppear = AnimationUtils.loadAnimation(this, R.anim.scale_anim_appear);
        scaleAnimationGone = AnimationUtils.loadAnimation(this, R.anim.scale_anim_gone);
        shelter = (TextView) findViewById(R.id.shelter);
        askPublic = (TextView) findViewById(R.id.ask_public);
        askPrivate = (TextView) findViewById(R.id.ask_private);
        setListeners();

    }
    private void setListeners(){
        camera_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSelected){
                    shelter.setVisibility(View.VISIBLE);
                    camera_fab.startAnimation(rotateAnimationAnticlockwise);
                    askPrivate.setVisibility(View.VISIBLE);
                    askPublic.setVisibility(View.VISIBLE);
                    askPrivate.startAnimation(scaleAnimatioAppear);
                    askPublic.startAnimation(scaleAnimatioAppear);
                    isSelected = true;
                }
                else {
                    shelterViewGone();
                }
            }
        });
        shelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSelected){
                    shelterViewGone();
                }
            }
        });
        askPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sp.getString(SP_USER_ID, null) != null){
                    Intent intent = new Intent(MainActivity.this, NewQuestionActivity.class);
                    startActivity(intent);
                    shelterViewGone();
                }else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
        askPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sp.getString(SP_USER_ID, null) != null){
                    Intent intent = new Intent(MainActivity.this, UserListActivity.class);
                    startActivity(intent);
                    shelterViewGone();
                }else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    private void shelterViewGone(){
        shelter.setVisibility(View.INVISIBLE);
        camera_fab.startAnimation(rotateAnimationClockwise);
        askPrivate.startAnimation(scaleAnimationGone);
        askPublic.startAnimation(scaleAnimationGone);
        askPrivate.setVisibility(View.GONE);
        askPublic.setVisibility(View.GONE);
        isSelected = false;
    }
    private void iniSlidingMenu(){
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.sliding_menu);
        slidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                camera_fab.setVisibility(View.GONE);
            }
        });
        slidingMenu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                camera_fab.setVisibility(View.VISIBLE);
            }
        });

        fm = this.getSupportFragmentManager();
        fm.findFragmentById(R.id.content_fragment).getView().findViewById(R.id.menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.toggle();
            }
        });

        TextView SlideMenuUsername = (TextView) findViewById(R.id.slide_menu_username);
        TextView SlideMenuUserId = (TextView) findViewById(R.id.slide_menu_user_id);
        RelativeLayout SlideMenuUerInfo = (RelativeLayout) findViewById(R.id.slide_menu_user_info);

        TextView SlideMenuLoginRegister = (TextView) findViewById(R.id.slide_menu_login_register);
        SlideMenuLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        TextView SlideLogout = (TextView) findViewById(R.id.slide_menu_logout);
        SlideLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("mark", 0);
                startActivity(intent);
                sp.edit().putString(SP_USER_ID, null).apply();
                slidingMenu.toggle();
            }
        });

        if(sp.getString(SP_USER_ID, null) != null){
            SlideMenuUsername.setText(sp.getString(SP_NAME, null));
            SlideMenuUserId.setText(sp.getString(SP_USER_ID, null));
            SlideMenuLoginRegister.setVisibility(View.GONE);
            SlideMenuUerInfo.setVisibility(View.VISIBLE);
        }else {
            SlideMenuLoginRegister.setVisibility(View.VISIBLE);
            SlideMenuUerInfo.setVisibility(View.GONE);
        }
        iniSlidingList();

    }
    private void iniSlidingList(){
        TextView SlidingMenuMyInfo = (TextView) findViewById(R.id.slide_menu_info);
        SlidingMenuMyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sp.getString(SP_USER_ID, null) != null){
                    UserInfoFragment userInfoFragment = new UserInfoFragment();
                    //camera_fab.setVisibility(View.GONE);
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.activity_main, userInfoFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                    slidingMenu.toggle();
                }else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        TextView SlidingMenuMyQuenstion = (TextView) findViewById(R.id.slide_menu_question);
        TextView SlidingMenuMyAnswers = (TextView) findViewById(R.id.slide_menu_answers);
        TextView SlidingMenuMyAppointment = (TextView) findViewById(R.id.slide_menu_appointment);
    }

    @Override
    public void onBackPressed() {
        if (slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();
        } else {
            if (isSelected) {
                shelterViewGone();
            } else {
                fm = this.getSupportFragmentManager();
                if (fm.getBackStackEntryCount() == 0) {
                    long secondTime = System.currentTimeMillis();
                    if (secondTime - firstTime > 2000) {
                        Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                        firstTime = secondTime;
                    } else {
                        finish();
                    }
                } else {
                    fm.popBackStack();
                }
            }
        }
    }
}
