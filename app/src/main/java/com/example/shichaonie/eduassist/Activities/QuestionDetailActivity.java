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
import android.widget.TextView;

import com.example.shichaonie.eduassist.AnswerListUtils.AnswerAdapter;
import com.example.shichaonie.eduassist.AnswerListUtils.AnswerLoader;
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

import static com.example.shichaonie.eduassist.R.id.question_title;
import static com.example.shichaonie.eduassist.R.string.gender;

/**
 * Created by Shichao Nie on 2017/1/28.
 */

public class QuestionDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<AnswerData>> {
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

        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }
    private void iniView(){
        ImageView imageBack = (ImageView) findViewById(R.id.question_detail_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void iniQuestion(){
        String url = ConstantContract.URL_QUESTIONS_BASE + questionId + "/";
        GetQuestionUtil getQuestionUtil = new GetQuestionUtil(url, QuestionDetailActivity.this);
        String questionInfo = getQuestionUtil.returnInfo();
        QuestionData questionData = getQuestionUtil.extractFeatureFromJson(questionInfo);
        upHeadView(questionData);
    }
    private void upHeadView(QuestionData data){
        listView = (ListView) findViewById(R.id.question_detail_list);
        View headView = getLayoutInflater().inflate(R.layout.question_detail_list_head_view, listView, false);
        TextView questionTitle = (TextView) headView.findViewById(R.id.question_detail_title);
        questionTitle.setText(data.getmTitle());
        TextView questionText = (TextView) headView.findViewById(R.id.question_detail_text);
        questionText.setText(data.getmContent_text());

        listView.addHeaderView(headView);
    }


    @Override
    public Loader<ArrayList<AnswerData>> onCreateLoader(int id, Bundle args) {
        return new AnswerLoader(this, questionId);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<AnswerData>> loader, ArrayList<AnswerData> data) {
        updateAnswers(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<AnswerData>> loader) {
        updateAnswers(new ArrayList<AnswerData>());
    }

    private void updateAnswers(ArrayList<AnswerData> data){
        AnswerAdapter adapter = new AnswerAdapter(this, data);
        listView.setAdapter(adapter);
    }
}
