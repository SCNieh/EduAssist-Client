package com.example.shichaonie.eduassist.MainActivityFragments.MyAnswersUtil;

import android.content.Context;
import android.text.TextUtils;

import com.example.shichaonie.eduassist.UserData.AnswerData;
import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.Utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.R.attr.category;
import static android.R.attr.value;
import static com.example.shichaonie.eduassist.Utils.ConstantContract.URL_ANSWER_BASE;
import static com.example.shichaonie.eduassist.Utils.ConstantContract.URL_QUESTIONS_BASE;

/**
 * Created by Shichao Nie on 2017/2/14.
 */

public class MyAnswersLoader extends android.support.v4.content.AsyncTaskLoader<ArrayList<AnswerData>> {

    private String mUserId;

    public MyAnswersLoader(Context context, String userId) {
        super(context);
        mUserId = userId;
    }

    @Override
    public ArrayList<AnswerData> loadInBackground() {
        String is;
        String newUrl = URL_ANSWER_BASE + "abstract/myAnswer/" + mUserId + "/";

        is = HttpUtil.myConnectionGET(newUrl);

        ArrayList<AnswerData> info = extractFeatureFromJson(is);

        return info;
    }

    private ArrayList<AnswerData> extractFeatureFromJson(String answerJSON) {
        ArrayList<AnswerData> answerData = new ArrayList<>();
        if (TextUtils.isEmpty(answerJSON)) {
            return answerData;
        }
        try {
            JSONArray jsonArray = new JSONArray(answerJSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectAnswers = jsonArray.getJSONObject(i);
                int id = jsonObjectAnswers.getInt("id");
                int question_id = jsonObjectAnswers.getInt("question_id");
                String questionTitle = jsonObjectAnswers.getString("question_title");
                String answerText = jsonObjectAnswers.getString("content_text");
                int answer_status = jsonObjectAnswers.getInt("status");
                int questionAttr = jsonObjectAnswers.getInt("question_attr");
                float questionValue = (float) jsonObjectAnswers.getDouble("question_value");

                answerData.add(new AnswerData(id, question_id, questionTitle, answerText, answer_status, questionAttr, questionValue));
            }
            return answerData;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return answerData;
    }
}
