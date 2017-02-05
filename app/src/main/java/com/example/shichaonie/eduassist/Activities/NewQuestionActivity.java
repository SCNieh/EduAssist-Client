package com.example.shichaonie.eduassist.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.Utils.HttpUtil;

import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.Utils.NiceSpinner.NiceSpinner;
import com.example.shichaonie.eduassist.Utils.SubmitUtil;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static android.R.attr.targetId;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static android.view.View.GONE;

/**
 * Created by Shichao Nie on 2017/1/2.
 */

public class NewQuestionActivity extends AppCompatActivity {
    private String titleString;
    private String textString;
    private SharedPreferences sp;
    private EditText newQuestionValue;
    private SubmitUtil submitUtil;
    private RelativeLayout newQuestionShelter;
    private int newQuestionCategory;
    EditText newQuestionTitle;
    EditText newQuestionText;
    private int questionAttr; //0: private, 1: public
    private String targetName;
    private int targetId;
    private static final String URL_SEND_PUBLIC = "http://10.0.2.2:5000/questions/add/public/";
    private static final String URL_SEND_PRIVATE = "http://10.0.2.2:5000/questions/add/private/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_question);

        Intent intent = getIntent();
        if(intent != null && intent.getStringExtra("targetName") != null){
            questionAttr = 0;
            targetName = intent.getStringExtra("targetName");
            targetId = intent.getIntExtra("targetId", -1);
        }else {
            questionAttr = 1;
        }
        iniView();
    }
    private void iniView(){
        LinearLayout targetObject = (LinearLayout) findViewById(R.id.new_question_target);
        TextView username = (TextView) findViewById(R.id.new_question_target_username);
        if(questionAttr == 1){
            targetObject.setVisibility(GONE);
        }else {
            targetObject.setVisibility(View.VISIBLE);
            username.setText(targetName);
        }

        ImageView backBtn = (ImageView) findViewById(R.id.new_question_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        NiceSpinner newQuestionCategory = (NiceSpinner) findViewById(R.id.new_question_category);
        List<String> category = new LinkedList<>(Arrays.asList(this.getResources().getStringArray(R.array.new_question_category)));

        newQuestionCategory.setTextColor(ContextCompat.getColor(this, R.color.dark_gray));
        newQuestionCategory.attachDataSource(category);
        newQuestionCategory.setOnItemSelectedListener(categorySelectedListener);

        newQuestionTitle = (EditText) findViewById(R.id.new_question_title);
        newQuestionText = (EditText) findViewById(R.id.new_question_text);
        newQuestionValue = (EditText) findViewById(R.id.new_question_value);
        newQuestionValue.clearFocus();
        newQuestionText.clearFocus();
        newQuestionTitle.clearFocus();

        ImageView submitQuestion = (ImageView) findViewById(R.id.new_question_submit);
        submitQuestion.setOnClickListener(submitListener);

        newQuestionShelter = (RelativeLayout) findViewById(R.id.new_question_shelter);

    }
    private AdapterView.OnItemSelectedListener categorySelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            newQuestionCategory = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //String newQuestion = makeQuestion(titleString, textString);
            String newQuestion = null;
            titleString = newQuestionTitle.getText().toString();
            textString = newQuestionText.getText().toString();
            String is = "mark";
            if(newQuestionShelter.getVisibility() == GONE || newQuestionShelter.getVisibility() == View.INVISIBLE){
                newQuestionShelter.setVisibility(View.VISIBLE);
            }else{
                newQuestionShelter.setVisibility(GONE);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                newQuestionShelter.setVisibility(View.VISIBLE);

            }
            RelativeLayout shelterLayout = (RelativeLayout) findViewById(R.id.new_question_shelter);
            if(submitUtil == null){
                newQuestion = makeQuestion(titleString, textString);
                if(questionAttr == 1){
                    submitUtil = new SubmitUtil(newQuestion, URL_SEND_PUBLIC, shelterLayout, NewQuestionActivity.this);
                }else {
                    submitUtil = new SubmitUtil(newQuestion, URL_SEND_PRIVATE, shelterLayout, NewQuestionActivity.this);
                }
                submitUtil.returnInfo();
            }else {
                submitUtil.returnInfo();
            }

        }
    };


    private String makeQuestion(String title, String text){
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        int userId = Integer.parseInt(sp.getString("userId", null));
        float value = Float.parseFloat(newQuestionValue.getText().toString());
        QuestionData questionData = new QuestionData(userId, newQuestionCategory, title, text, questionAttr, targetId, 1, value, 0);
        Gson gson = new Gson();
        return gson.toJson(questionData);
    }
}
