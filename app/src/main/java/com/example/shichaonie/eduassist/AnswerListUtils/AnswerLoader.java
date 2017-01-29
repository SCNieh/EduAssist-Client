package com.example.shichaonie.eduassist.AnswerListUtils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import com.example.shichaonie.eduassist.UserData.AnswerData;
import com.example.shichaonie.eduassist.UserData.User;
import com.example.shichaonie.eduassist.Utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.shichaonie.eduassist.Utils.ConstantContract.URL_ANSWER_BASE;
import static com.example.shichaonie.eduassist.Utils.ConstantContract.URL_USER_BASE;

/**
 * Created by Shichao Nie on 2017/1/29.
 */

public class AnswerLoader extends AsyncTaskLoader<ArrayList<AnswerData>> {
    private int questionId;

    public AnswerLoader(Context context, int question_id) {
        super(context);
        questionId = question_id;
    }

    @Override
    public ArrayList<AnswerData> loadInBackground() {
        String is, Info = null;
        String newUrl = URL_ANSWER_BASE + "abstract/" + questionId + "/";
        is = HttpUtil.myConnectionGET(newUrl);

        return extractFeatureFromJson(is);
    }
    private ArrayList<AnswerData> extractFeatureFromJson(String answerJSON) {
        ArrayList<AnswerData> answerData = new ArrayList<>();
        if (TextUtils.isEmpty(answerJSON)) {
            return answerData;
        }
        try {
            JSONObject jsonObject = new JSONObject(answerJSON);
            JSONArray jsonArray = jsonObject.getJSONArray("Answers_data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectAnswers = jsonArray.getJSONObject(i);
                int id = jsonObjectAnswers.getInt("id");
                int question_id = jsonObjectAnswers.getInt("question_id");
                int answer_id = jsonObjectAnswers.getInt("answers_id");
                String answer_name = jsonObjectAnswers.getString("answer_name");
                String answer_selfIntro = jsonObjectAnswers.getString("answer_selfIntro");
                String contextText = jsonObjectAnswers.getString("content_text");
                int status = jsonObjectAnswers.getInt("status");

                answerData.add(new AnswerData(id, question_id, answer_id, answer_name, answer_selfIntro, contextText, status));
            }
            return answerData;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return answerData;
    }
}
