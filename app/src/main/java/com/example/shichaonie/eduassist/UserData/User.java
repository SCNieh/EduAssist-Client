package com.example.shichaonie.eduassist.UserData;

import android.widget.EditText;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.R.attr.addPrintersActivity;
import static android.R.attr.fastScrollPreviewBackgroundLeft;
import static android.R.attr.id;
import static android.R.attr.thickness;


/**
 * Created by Shichao Nie on 2016/12/13.
 */

public final class User {

    public static final class genderInfo{
        public static final int GNDER_MALE = 0;
        public static final int GENDER_FEMALE = 1;
        public static final int TITLE_STUDENT = 0;
        public static final int TITLE_TEACHER = 1;
    }

    private int mId;
    private String mUsername;
    private String mPassword;
    private String mName;
    private int mTitle;
    private String mTel;
    private String mNickname;
    private String mEmail;
    private int mGender;
    private String mSelfIntro;
    private int mPrivateCode;
    private float mScore;
    private int mStatusCode = 1;

    public User(String username, String password){
        mUsername = username;
        mPassword = md5(password);
    }
    public User(String username, String password, String nickname, int title, String tel, String Email, int gender, int privateCode, float score){
        mUsername = username;
        mPassword = md5(password);
        mNickname = nickname;
        mTitle = title;
        mTel = tel;
        mEmail = Email;
        mGender = gender;
        mPrivateCode = privateCode;
        mScore = score;
    }
    public User(int id, String name, String username,  String selfIntro, int title){
        mId = id;
        mName = name;
        mUsername = username;
        mSelfIntro = selfIntro;
        mTitle = title;
    }
    public User(int id, String name, String username, int title, String email, String selfIntro, int privateMode, int gender, String score){
        mId = id;
        mName = name;
        mUsername = username;
        mTitle = title;
        mEmail = email;
        mPrivateCode = privateMode;
        mSelfIntro = selfIntro;
        mGender = gender;
        if(score.equals("null") || score == null || score.isEmpty()){
            mScore = (float) 0.0;
        }else {
            mScore = Float.parseFloat(score);
        }
    }

    public static final String md5(String string){
        String md5String = null;
        try {
            MessageDigest msInst = MessageDigest.getInstance("MD5");
            msInst.update(string.getBytes());
            md5String =  new BigInteger(1, msInst.digest()).toString(16);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return md5String;
    }

    public int getId(){
        return mId;
    }
    public String getUsername(){
        return mUsername;
    }
    public String getPassword(){
        return mPassword;
    }
    public String getmName() {
        return mName;
    }
    public String getmSelfIntro() {
        return mSelfIntro;
    }
    public int getTitle(){
        return mTitle;
    }
    public String getTel(){
        return mTel;
    }
    public String getEmail(){
        return mEmail;
    }
    public int getGender(){
        return mGender;
    }
    public int getmPrivateCode() {
        return mPrivateCode;
    }
    public float getmScore() {
        return mScore;
    }
    public void setPassword(String newPassword){
        mPassword = md5(newPassword);
    }
    public String getmNickname() {
        return mNickname;
    }
}
