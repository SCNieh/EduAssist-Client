package com.example.shichaonie.eduassist.MainActivityFragments.MyQuestionUtil;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.Utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.shichaonie.eduassist.Utils.ConstantContract.URL_QUESTIONS_BASE;

/**
 * Created by Shichao Nie on 2017/2/14.
 */

public class MyQuestionLoader extends android.support.v4.content.AsyncTaskLoader<ArrayList<QuestionData>> {

    private String mUserId;

    public MyQuestionLoader(Context context, String userId) {
        super(context);
        mUserId = userId;
    }

    @Override
    public ArrayList<QuestionData> loadInBackground() {
        String is;
        String newUrl = URL_QUESTIONS_BASE + mUserId + "/";

        is = HttpUtil.myConnectionGET(newUrl);

        return extractFeatureFromJson(is);
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
                String title = jsonObjectQuestion.getString("title");
                String text = jsonObjectQuestion.getString("content_text");
                int attribute = jsonObjectQuestion.getInt("attribute");
                int invited_id = -1;
                if(attribute == 0){ //private
                    invited_id = jsonObjectQuestion.getInt("invited_id");
                }
                int questionStatus = jsonObjectQuestion.getInt("question_status");
                float value = (float) jsonObjectQuestion.getDouble("value");
                int answer_status = jsonObjectQuestion.getInt("answer_status");

                questionData.add(new QuestionData(id, ask_id, invited_id, category, title, text,  attribute, questionStatus, value, answer_status));
            }
            return questionData;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return questionData;
    }
}
