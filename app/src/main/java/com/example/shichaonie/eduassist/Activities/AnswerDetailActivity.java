package com.example.shichaonie.eduassist.Activities;

import android.animation.IntArrayEvaluator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.Voice;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.AnswerData;
import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.Utils.ConstantContract;
import com.example.shichaonie.eduassist.Utils.HttpUtil;
import com.example.shichaonie.eduassist.Utils.SubmitUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shichao Nie on 2017/2/1.
 */

public class AnswerDetailActivity extends AppCompatActivity {
    private TextView answerName;
    private TextView answerSelfIntro;
    private TextView answerText;
    private RelativeLayout answerTextEditable;
    private int questionId;
    private int answererId;
    private int askId;
    private int id;
    private int questionStatus;
    private int mode; // 0: guest, 1: editable
    private SubmitUtil submitUtil;
    private ImageView imageAccept;
    private ImageView imageSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_detail_activity);

        Intent intent = getIntent();

        questionId = intent.getIntExtra("questionId", -1);
        answererId = intent.getIntExtra("answererId", -1);

        id = intent.getIntExtra("answerId", -1);
        if(answererId == -1 && questionId == -1){
            mode = 0; // guest mode
        }else {
            mode = 1; // editable mode
        }
        askId = intent.getIntExtra("askId", -1);
        questionStatus = intent.getIntExtra("questionStatus", 1);

        iniView();

        String url;

        if(mode == 0){
            url = ConstantContract.URL_ANSWER_BASE + id + "/";
        }else {
            url = ConstantContract.URL_ANSWER_BASE + answererId + "/" + questionId + "/";
        }

        myAsyncTaskGet task = new myAsyncTaskGet();
        task.execute(url);

        imageAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = getResources().getString(R.string.accept_answer);
                SubmitUtil.showSuccessDialog(AnswerDetailActivity.this, msg, positive, negative);
            }
        });
        imageSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.answer_detail_edit);
                String textString = editText.getText().toString();
                String url = ConstantContract.URL_ANSWER_BASE + "edit/";
                RelativeLayout shelterLayout = (RelativeLayout) findViewById(R.id.answer_detail_shelter);
                if(submitUtil == null){
                    String newAnswer = makeAnswer(textString);
                    submitUtil = new SubmitUtil(newAnswer, url, shelterLayout, AnswerDetailActivity.this);
                    submitUtil.returnInfo();
                }else {
                    submitUtil.returnInfo();
                }
            }
        });
    }
    private void iniView(){
        SharedPreferences sp = getSharedPreferences(ConstantContract.SP_TAG, MODE_PRIVATE);
        answerName = (TextView) findViewById(R.id.answer_detail_name);
        answerSelfIntro = (TextView) findViewById(R.id.answer_detail_selfIntro);
        answerText = (TextView) findViewById(R.id.answer_detail_content);
        answerTextEditable = (RelativeLayout) findViewById(R.id.answer_detail_content_editable);

        ImageView imageBack = (ImageView) findViewById(R.id.answer_detail_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageAccept = (ImageView) findViewById(R.id.answer_detail_accept);
        if(sp.getString(ConstantContract.SP_USER_ID, "") .equals(Integer.toString(askId)) && questionStatus == 1){
            imageAccept.setVisibility(View.VISIBLE);
        }else {
            imageAccept.setVisibility(View.GONE);
        }

        imageSubmit = (ImageView) findViewById(R.id.answer_detail_submit);
        if(mode == 1){
            imageSubmit.setVisibility(View.VISIBLE);
        }else {
            imageSubmit.setVisibility(View.GONE);
        }

    }
    private String makeAnswer(String text){
        AnswerData answerData = new AnswerData(questionId, answererId, text, 1);
        Gson gson = new Gson();

        return gson.toJson(answerData);
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
                msg = getResources().getString(R.string.accept_success);
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
            if(mode == 0){
                updateUI(answerData);
            }
            else {
                updateUI_Editable(answerData);
            }
        }
        private AnswerData extractFeatureFromJson(String s){
            if(TextUtils.isEmpty(s)){
                return new AnswerData();
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                String name = jsonObject.getString("answer_name");
                String selfIntro = jsonObject.getString("answer_selfIntro");
                String content_text = jsonObject.getString("content_text");

                return new AnswerData(name, selfIntro, content_text);
            }catch (JSONException e){
                e.printStackTrace();
            }
            return new AnswerData();
        }
        private void updateUI(AnswerData data){
            answerText.setVisibility(View.VISIBLE);
            answerTextEditable.setVisibility(View.GONE);

            String name = data.getmAnswer_name();
            answerName.setText((name != null && !name.equals("null")) ? name : "Unknown");

            String selfIntro = data.getmAnswer_selfIntro();
            answerSelfIntro.setText((selfIntro != null && !selfIntro.equals("null")) ? selfIntro : "");
            answerText.setText(data.getmContent_text());
        }
        private void updateUI_Editable(AnswerData data){
            answerText.setVisibility(View.GONE);
            answerTextEditable.setVisibility(View.VISIBLE);

            String name = data.getmAnswer_name();
            answerName.setText((name != null && !name.equals("null")) ? name : "Unknown");

            String selfIntro = data.getmAnswer_selfIntro();
            answerSelfIntro.setText((selfIntro != null && !selfIntro.equals("null")) ? selfIntro : "");
            EditText editText = (EditText) findViewById(R.id.answer_detail_edit);
            editText.setText(data.getmContent_text());
        }
    }
}
