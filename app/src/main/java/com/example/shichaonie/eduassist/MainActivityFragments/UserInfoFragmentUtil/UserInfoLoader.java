package com.example.shichaonie.eduassist.MainActivityFragments.UserInfoFragmentUtil;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.UserData.User;
import com.example.shichaonie.eduassist.Utils.ConstantContract;
import com.example.shichaonie.eduassist.Utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Shichao Nie on 2017/1/4.
 */

public class UserInfoLoader extends AsyncTaskLoader<User> {
    private String mUserId;

    public UserInfoLoader(Context context, String userId) {
        super(context);
        mUserId = userId;
    }

    @Override
    public User loadInBackground() {
        String url = ConstantContract.URL_USER_BASE + mUserId + "/";
        String is = HttpUtil.myConnectionGET(url);
        User user = extractFeatureFromJson(is);

        return user;
    }
    public static User extractFeatureFromJson(String userInfoJSON) {
        if (TextUtils.isEmpty(userInfoJSON)) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(userInfoJSON);
            JSONObject userInfo = jsonObject.getJSONObject("User");
            String name = userInfo.getString("mName");
            String username = userInfo.getString("username");
            String email = userInfo.getString("Email");
            String selfIntro = userInfo.getString("selfIntro");
            String score = userInfo.getString("score");
            int id;
            int title;
            int privateMode;
            int gender;
            if(userInfo.getString("id") != null){
                id = userInfo.getInt("id");
            }else {
                id = -1;
            }
            if(userInfo.getString("title") != null){
                title = userInfo.getInt("title");
            }else {
                title = 0;
            }
            if(userInfo.getString("private_mode") != null){
                privateMode = userInfo.getInt("private_mode");
            }else {
                privateMode = 0;
            }
            if(userInfo.getString("gender") != null && userInfo.getString("gender") != "null"){
                gender = Integer.parseInt(userInfo.getString("gender"));
            }else {
                gender = 0;
            }
            int userCredit = userInfo.getInt("credit");

            User user = new User(id, name, username, title, email, selfIntro, privateMode, gender, score, userCredit);
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
