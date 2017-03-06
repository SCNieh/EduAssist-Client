package com.example.shichaonie.eduassist.MyInvitationActivity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.shichaonie.eduassist.R;

/**
 * Created by Shichao Nie on 2017/3/5.
 */

public class CategoryAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public CategoryAdapter(Context context, FragmentManager fm){
        super(fm);
        mContext = context;
    }
    public int getCount(){
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new MyInvitationQuestionsFragment();
            default: return new MyAppointmentFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.questions);
        }
        else {
            return mContext.getString(R.string.appointment);
        }
    }
}
