package com.example.shichaonie.eduassist.UserListUtils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.UserData.User;
import com.example.shichaonie.eduassist.Utils.HttpUtil;
import com.google.gson.internal.bind.TreeTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TreeMap;

import static android.R.attr.category;
import static android.R.attr.value;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.shichaonie.eduassist.Utils.ConstantContract.URL_QUESTIONS_BASE;
import static com.example.shichaonie.eduassist.Utils.ConstantContract.URL_USER_BASE;

/**
 * Created by Shichao Nie on 2017/1/4.
 */

public class UserLoader extends AsyncTaskLoader<ArrayList<User>> {
    private String mKeywords;

    public UserLoader(Context context, String keywords) {
        super(context);
        mKeywords = keywords;
    }

    @Override
    public ArrayList<User> loadInBackground() {
        String is, Info = null;
        if(mKeywords == null || mKeywords.equals("null") || mKeywords.isEmpty()){
            is = HttpUtil.myConnectionGET(URL_USER_BASE);
        }
        else {
            String newUrl = URL_USER_BASE + "search/";
            try{
                JSONObject json = new JSONObject();
                json.put("keywords", mKeywords);
                Info = json.toString();
            }catch (JSONException e){
                e.printStackTrace();
            }
            is = HttpUtil.myConnectionPOST(Info, newUrl);
        }
        ArrayList<User> userData = extractFeatureFromJson(is);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        ArrayList<User> userData = new ArrayList<>();
//        userData.add(new User(0, "张三", "测试信息测试信息测试信息测试信息测试信息测试信息", 0));
//        userData.add(new User(1, "李四", "测试信息测试信息测试信息测试信息测试信息测试信息\n测试信息测试信息测试信息测试信息测试信息测试信息", 1));

        return userData;
    }
    private ArrayList<User> extractFeatureFromJson(String UserJSON) {
        ArrayList<User> userData = new ArrayList<>();
        if (TextUtils.isEmpty(UserJSON)) {
            return userData;
        }
        try {
            JSONObject jsonObject = new JSONObject(UserJSON);
            JSONArray jsonArray = jsonObject.getJSONArray("User");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectUser = jsonArray.getJSONObject(i);
                int id = jsonObjectUser.getInt("id");
                String name = jsonObjectUser.getString("mName");
                String username = jsonObjectUser.getString("username");
                String selfIntro = jsonObjectUser.getString("selfIntro");
                int title = jsonObjectUser.getInt("title");

                userData.add(new User(id, name, username, selfIntro, title));
            }
            return userData;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userData;
    }
}
