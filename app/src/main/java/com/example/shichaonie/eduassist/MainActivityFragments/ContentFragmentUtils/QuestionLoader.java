package com.example.shichaonie.eduassist.MainActivityFragments.ContentFragmentUtils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;
import android.util.FloatProperty;
import android.util.Log;

import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.Utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import static android.R.attr.category;
import static android.R.attr.keepScreenOn;

import static com.example.shichaonie.eduassist.Utils.ConstantContract.URL_QUESTIONS_BASE;

/**
 * Created by Shichao Nie on 2017/1/1.
 */

public class QuestionLoader extends android.support.v4.content.AsyncTaskLoader<ArrayList<QuestionData>> {
    private String mKeywords = null;


    public QuestionLoader(Context context, String Keywords) {
        super(context);
        mKeywords = Keywords;
    }

    @Override
    public ArrayList<QuestionData> loadInBackground() {
        String is, Info = null;
        if(mKeywords == null || mKeywords.isEmpty() || mKeywords.equals("null")){
            is = HttpUtil.myConnectionGET(URL_QUESTIONS_BASE);
        }
        else {
            String newUrl = URL_QUESTIONS_BASE + "search/";
            try{
                JSONObject json = new JSONObject();
                json.put("keywords", mKeywords);
                Info = json.toString();
            }catch (JSONException e){
                e.printStackTrace();
            }
           is = HttpUtil.myConnectionPOST(Info, newUrl);
        }
        ArrayList<QuestionData> questionData = extractFeatureFromJson(is);

        //Code below just for test
//        ArrayList<QuestionData> test = new ArrayList<>();
//        test.add(new QuestionData(0, 1, 3, "测试测试测试标题!@#$%^&*_+~", 0, 1, (float) 3.99));
//        test.add(new QuestionData(1, 0, 4, "测试测试测试标题asdxnuihqfsopjkm", 1, 0, (float) 10));
//
//        return test;

        return questionData;
    }

    private ArrayList<QuestionData> extractFeatureFromJson(String questionJSON) {
        ArrayList<QuestionData> questionData = new ArrayList<>();
        if (TextUtils.isEmpty(questionJSON)) {
            return questionData;
        }
        try {
            JSONObject jsonObject = new JSONObject(questionJSON);
            JSONArray jsonArray = jsonObject.getJSONArray("Questions_data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectQuestion = jsonArray.getJSONObject(i);
                int id = jsonObjectQuestion.getInt("id");
                int ask_id = jsonObjectQuestion.getInt("ask_id");
                int category = jsonObjectQuestion.getInt("category");
                String titile = jsonObjectQuestion.getString("title");
                int attribute = jsonObjectQuestion.getInt("attribute");
                int questionStatus = jsonObjectQuestion.getInt("question_status");
                float value = (float) jsonObjectQuestion.getDouble("value");

                questionData.add(new QuestionData(id, ask_id, category, titile, attribute, questionStatus, value));
            }
            return questionData;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return questionData;
    }
}
