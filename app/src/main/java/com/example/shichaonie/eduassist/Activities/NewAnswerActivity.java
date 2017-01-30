package com.example.shichaonie.eduassist.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.AnswerData;
import com.example.shichaonie.eduassist.Utils.ConstantContract;
import com.example.shichaonie.eduassist.Utils.SubmitUtil;
import com.google.gson.Gson;

/**
 * Created by Shichao Nie on 2017/1/30.
 */

public class NewAnswerActivity extends AppCompatActivity {
    private int questionId;
    private SubmitUtil submitUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_answer);

        Intent intent = getIntent();
        questionId = intent.getIntExtra("questionId", -1);

        ImageView submit = (ImageView) findViewById(R.id.new_answer_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.new_answer_text);
                String textString = editText.getText().toString();
                String url = ConstantContract.URL_ANSWER_BASE + "add/";

                if(submitUtil == null){
                    String newAnswer = makeAnswer(textString);
                    submitUtil = new SubmitUtil(newAnswer, url, NewAnswerActivity.this);
                    submitUtil.returnInfo();
                }else {
                    submitUtil.returnInfo();
                }
            }
        });
    }
    private String makeAnswer(String text){
        SharedPreferences sp = getSharedPreferences(ConstantContract.SP_TAG, MODE_PRIVATE);
        int userId = Integer.parseInt(sp.getString("userId", null));
        AnswerData answerData = new AnswerData(questionId, userId, text);
        Gson gson = new Gson();

        return gson.toJson(answerData);
    }
}
