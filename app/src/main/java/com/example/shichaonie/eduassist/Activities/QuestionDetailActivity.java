package com.example.shichaonie.eduassist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
    public ListView listView;

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
        listView = (ListView) findViewById(R.id.question_detail_list);
        ImageView imageBack = (ImageView) findViewById(R.id.question_detail_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.question_detail_add_answer);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionDetailActivity.this, NewAnswerActivity.class);
                intent.putExtra("questionId", questionId);
                startActivity(intent);
            }
        });
    }
    private void iniQuestion(){
        String url = ConstantContract.URL_QUESTIONS_BASE + questionId + "/";
        RelativeLayout progressBar = (RelativeLayout) findViewById(R.id.question_detail_shelter);
        progressBar.setVisibility(View.VISIBLE);
        GetQuestionUtil getQuestionUtil = new GetQuestionUtil(url, listView,  QuestionDetailActivity.this);
        getQuestionUtil.activate();
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
