package com.example.shichaonie.eduassist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.AnswerData;
import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.UserData.User;
import com.example.shichaonie.eduassist.Utils.ConstantContract;
import com.example.shichaonie.eduassist.Utils.GetQuestionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.shichaonie.eduassist.R.string.gender;

/**
 * Created by Shichao Nie on 2017/1/28.
 */

public class QuestionDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<AnswerData> {
    private int questionId;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_detail_activity);

        Intent intent = getIntent();
        questionId = intent.getIntExtra("questionId", -1);

        iniView();
        iniQuestion();

    }
    private void iniView(){
        ImageView imageBack = (ImageView) findViewById(R.id.question_detail_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.question_detail_list);
        View headView = getLayoutInflater().inflate(R.layout.question_detail_list_head_view, listView, false);
        listView.addHeaderView(headView);
    }
    private void iniQuestion(){
        String url = ConstantContract.URL_QUESTIONS_BASE + questionId + "/";
        GetQuestionUtil getQuestionUtil = new GetQuestionUtil(url, QuestionDetailActivity.this);
        String questionInfo = getQuestionUtil.returnInfo();
        QuestionData questionData = extractFeatureFromJson(questionInfo);
    }
    private QuestionData extractFeatureFromJson(String questionJSON) {
        if (TextUtils.isEmpty(questionJSON)) {
            return new QuestionData();
        }
        try {
            JSONObject jsonObject = new JSONObject(questionJSON);
            JSONObject questionInfo = jsonObject.getJSONObject("Questions_data");
            String question_title = questionInfo.getString("title");
            String content_text = questionInfo.getString("content_text");
            String content_image = questionInfo.getString("content_image");
            String content_voice = questionInfo.getString("content_voice");
            String refuse_resaon = questionInfo.getString("refuse_reason");
            int id;
            int ask_id;
            int invited_id;
            int category;
            int attribute;
            int question_status;
            int invite_status;
            float value;
            if(questionInfo.getString("id") != null){
                id = questionInfo.getInt("id");
            }else {
                id = -1;
            }
            if(questionInfo.getString("ask_id") != null) {
                ask_id = questionInfo.getInt("title");
            }else{
                ask_id = -1;
            }
            if(questionInfo.getString("category") != null){
                category = questionInfo.getInt("category");
            }else {
                category = 0;
            }
            if(questionInfo.getString("question_status") != null){
                question_status = questionInfo.getInt("question_status");
            }else {
                question_status = 1;
            }
            if(questionInfo.getString("value") != null){
                value = Float.parseFloat(questionInfo.getString("value"));
            }else {
                value = (float) 0.0;
            }
            question_title = questionInfo.getString("question_title");
            content_text = questionInfo.getString("content_text");
            content_image = questionInfo.getString("content_iamge");
            content_voice = questionInfo.getString("content_voice");

            if(questionInfo.getString("attribute") != null){
                attribute = questionInfo.getInt("attribute");
                if(questionInfo.getString("attribute").equals("0")){ //if private mode
                    if(questionInfo.getString("invited_id") != null){
                        invited_id = questionInfo.getInt("invited_id");
                    }else {
                        invited_id = -1;
                    }
                    if(questionInfo.getString("invite_status") != null){
                        invite_status = questionInfo.getInt("invite_status");
                        if(invite_status == 1){ // if refused
                            if(questionInfo.getString("refuse_reason") != null){
                                refuse_resaon = questionInfo.getString("refuse_reason");
                            }else {
                                refuse_resaon = "";
                            }
                            return new QuestionData(id, ask_id, invited_id, category, question_title, content_text, content_image, content_voice, attribute, question_status, invite_status, refuse_resaon, value);
                        }
                    }
                }else {
                    return new QuestionData(id, ask_id,category, question_title, content_text, content_image, content_voice, attribute, question_status, value);
                }
            }else {
                return new QuestionData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new QuestionData();
    }

    @Override
    public Loader<AnswerData> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<AnswerData> loader, AnswerData data) {

    }

    @Override
    public void onLoaderReset(Loader<AnswerData> loader) {

    }
}
