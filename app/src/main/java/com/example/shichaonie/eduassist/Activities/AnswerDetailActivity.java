package com.example.shichaonie.eduassist.Activities;

import android.animation.IntArrayEvaluator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.Voice;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
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
import com.example.shichaonie.eduassist.Utils.SubmitUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shichao Nie on 2017/2/1.
 */

public class AnswerDetailActivity extends AppCompatActivity {
    private TextView answerName;
    private TextView answerSelfIntro;
    private TextView answerText;
    private int askId;
    private int id;
    private int questionStatus;
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_detail_activity);

        Intent intent = getIntent();
        id = intent.getIntExtra("answerId", -1);
        askId = intent.getIntExtra("askId", -1);
        questionStatus = intent.getIntExtra("questionStatus", 1);

        iniView();

        String url = ConstantContract.URL_ANSWER_BASE + id + "/";
        myAsyncTaskGet task = new myAsyncTaskGet();
        task.execute(url);
    }
    private void iniView(){
        sp = getSharedPreferences(ConstantContract.SP_TAG, MODE_PRIVATE);
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
        ImageView imageAccept = (ImageView) findViewById(R.id.answer_detail_accept);
        if(sp.getString(ConstantContract.SP_USER_ID, "") .equals(Integer.toString(askId)) && questionStatus == 1){
            imageAccept.setVisibility(View.VISIBLE);
        }else {
            imageAccept.setVisibility(View.GONE);
        }
        imageAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = getResources().getString(R.string.accept_answer);
                SubmitUtil.showSuccessDialog(AnswerDetailActivity.this, msg, positive, negative);
            }
        });
    }
    private DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String url = ConstantContract.BASE_URL + "accept/" + id + "/";
            myAsyncTaskAccept task = new myAsyncTaskAccept();
            task.execute(url);
        }
    };
    private DialogInterface.OnClickListener negative = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(dialog != null){
                dialog.dismiss();
            }
        }
    };

    private class myAsyncTaskAccept extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            return HttpUtil.myConnectionGET(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            String msg;
            if(s.equals("Success")){
                msg = getResources().getString(R.string.accept_sucess);
            }else {
                msg = getResources().getString(R.string.accept_failed);
            }
            SubmitUtil.showSuccessDialog(AnswerDetailActivity.this, msg, negative, negative);
        }
    }

    private class myAsyncTaskGet extends AsyncTask<String, Void, String>{

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
