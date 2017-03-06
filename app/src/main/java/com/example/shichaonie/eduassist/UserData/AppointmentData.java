package com.example.shichaonie.eduassist.UserData;

import android.support.v7.app.AppCompatActivity;

import com.example.shichaonie.eduassist.Utils.SubmitUtil;

/**
 * Created by Shichao Nie on 2017/3/5.
 */

public class AppointmentData {
    private int mId;
    private int mStu_id;
    private int mTeach_id;
    private int mStatus;
    private String mInfo;
    private String mRefuseReason;
    private String mUserName;
    private String mSelfIntro;

    public AppointmentData(int id, int stu_id, int teach_id, int status, String info, String refuseReason, String userName, String selfIntro){
        mId = id;
        mStu_id = stu_id;
        mTeach_id = teach_id;
        mStatus = status;
        mInfo = info;
        mRefuseReason = refuseReason;
        mUserName = userName;
        mSelfIntro = selfIntro;
    }
    public AppointmentData(int id, int stu_id, int teach_id, int status, String info){
        mId = id;
        mStu_id = stu_id;
        mTeach_id = teach_id;
        mStatus = status;
        mInfo = info;
    }
    public AppointmentData(int stu_id, int teach_id, int status, String info){
        mStu_id = stu_id;
        mTeach_id = teach_id;
        mStatus = status;
        mInfo = info;
    }
    public int getmId() {
        return mId;
    }

    public int getmStu_id() {
        return mStu_id;
    }

    public int getmTeach_id() {
        return mTeach_id;
    }

    public int getmStatus() {
        return mStatus;
    }

    public String getmInfo() {
        return mInfo;
    }

    public String getmRefuseReason() {
        return mRefuseReason;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmSelfIntro() {
        return mSelfIntro;
    }
}
