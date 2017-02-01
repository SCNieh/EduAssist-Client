package com.example.shichaonie.eduassist.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.AnswerData;
import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.Utils.ConstantContract;
import com.example.shichaonie.eduassist.Utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shichao Nie on 2017/2/1.
 */

public class AnswerDetailActivity extends AppCompatActivity {
    private TextView answerName;
    private TextView answerSelfIntro;
    private TextView answerText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_detail_activity);

        Intent intent = getIntent();
        int id = intent.getIntExtra("answerId", -1);

        iniView();

        String url = ConstantContract.URL_ANSWER_BASE + id + "/";
        myAsyncTask task = new myAsyncTask();
        task.execute(url);
    }
    private void iniView(){
        answerName = (TextView) findViewById(R.id.answer_detail_name);
        answerSelfIntro = (TextView) findViewById(R.id.answer_detail_selfIntro);
        answerText = (TextView) findViewById(R.id.answer_detail_content);
        ImageView imageBack = (ImageView) findViewById(R.id.answer_detail_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private class myAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            return HttpUtil.myConnectionGET(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            AnswerData answerData = extractFeatureFromJson(s);
            updateUI(answerData);
        }
        private AnswerData extractFeatureFromJson(String s){
            if(TextUtils.isEmpty(s)){
                return new AnswerData();
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                String answerName = jsonObject.getString("answer_name");
                String answerSelfIntro = jsonObject.getString("answer_selfIntro");
                String content_text = jsonObject.getString("content_text");

                return new AnswerData(answerName, answerSelfIntro, content_text);
            }catch (JSONException e){
                e.printStackTrace();
            }
            return new AnswerData();
        }
        private void updateUI(AnswerData data){
            answerName.setText(data.getmAnswer_name());
            answerSelfIntro.setText(data.getmAnswer_selfIntro());
            answerText.setText(data.getmContent_text());
        }
    }
}
