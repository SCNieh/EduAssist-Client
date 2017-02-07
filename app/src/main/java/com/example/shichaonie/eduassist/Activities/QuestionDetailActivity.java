package com.example.shichaonie.eduassist.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shichaonie.eduassist.AnswerListUtils.AnswerAdapter;
import com.example.shichaonie.eduassist.AnswerListUtils.AnswerLoader;
import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.AnswerData;
import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.UserData.User;
import com.example.shichaonie.eduassist.Utils.ConstantContract;
import com.example.shichaonie.eduassist.Utils.GetQuestionUtil;
import com.example.shichaonie.eduassist.Utils.HttpUtil;
import com.example.shichaonie.eduassist.Utils.SubmitUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.shichaonie.eduassist.R.id.action_menu_presenter;
import static com.example.shichaonie.eduassist.R.id.question_title;
import static com.example.shichaonie.eduassist.R.id.user_info_private_mode;
import static com.example.shichaonie.eduassist.R.string.gender;
import static com.example.shichaonie.eduassist.Utils.HttpUtil.myConnectionGET;

/**
 * Created by Shichao Nie on 2017/1/28.
 */

public class QuestionDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<AnswerData>> {
    private int questionId;
    private int invitedId;
    private int questionAttr;
    private float questionValue;
    private int answerStatus; // 0:  no answer
    public ListView listView;
    private SharedPreferences sp;
    private RelativeLayout  privateMode;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_detail_activity);

        sp = getSharedPreferences(ConstantContract.SP_TAG, MODE_PRIVATE);
        Intent intent = getIntent();
        questionId = intent.getIntExtra("questionId", -1);
        questionAttr = intent.getIntExtra("questionAttr", -1);
        questionValue = intent.getFloatExtra("questionValue", (float) 0.0);
        answerStatus = intent.getIntExtra("answerStatus", -1);
        invitedId = intent.getIntExtra("invitedId", -1);
        iniView();
        iniQuestion();
    }
    private void getPermissionStatus(){
        permissionThread thread = new permissionThread();
        thread.start();
    }

    private Handler infoHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String info = (String) msg.obj;
            if(info.equals("Success")){
                getSupportLoaderManager().initLoader(0, null, QuestionDetailActivity.this).forceLoad();
                privateMode.setVisibility(View.GONE);
            }else {
                Toast.makeText(QuestionDetailActivity.this, getResources().getString(R.string.payment_failed), Toast.LENGTH_LONG).show();
            }
        }
    };
    private Handler permissionHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int permission = msg.what;
            if(permission == 1 && !(sp.getString(ConstantContract.SP_USER_ID, null).equals(Integer.toString(invitedId)))){ //paid and user is not invited object
                privateMode.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                getSupportLoaderManager().initLoader(0, null, QuestionDetailActivity.this).forceLoad();
            }else if(sp.getString(ConstantContract.SP_USER_ID, null).equals(Integer.toString(invitedId))){
                privateMode.setVisibility(View.GONE);
                getSupportLoaderManager().initLoader(0, null, QuestionDetailActivity.this).forceLoad();
            }
            else {
                privateMode.setVisibility(View.VISIBLE);
                AnswerAdapter adapter = new AnswerAdapter(QuestionDetailActivity.this, new ArrayList<AnswerData>());
                listView.setAdapter(adapter);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        privateMode = (RelativeLayout) findViewById(R.id.question_detail_private_mode);
        TextView clickHere = (TextView) findViewById(R.id.question_detail_click_here);
        clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = getResources().getString(R.string.confirm_payment) + questionValue / 2.0 + getResources().getString(R.string.continue_or_not);
                SubmitUtil.showSuccessDialog(QuestionDetailActivity.this, msg, positive, negative);
            }
        });
        if(questionAttr == 1 || answerStatus == 0){//public or no answer
            privateMode.setVisibility(View.GONE);
            if(getSupportLoaderManager().getLoader(0) == null){
                getSupportLoaderManager().initLoader(0, null, this).forceLoad();
            }else {
                getSupportLoaderManager().restartLoader(0, null, this).forceLoad();
            }
        }else { //private
            getPermissionStatus();

        }

    }
    private DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            myThread thread = new myThread();
            thread.start();

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
    private class myThread extends Thread{
        @Override
        public void run() {
            String url = ConstantContract.URL_USER_BASE + "payment/" + sp.getString(ConstantContract.SP_USER_ID, null) + "/" + (questionValue / 2.0) + "/" + questionId + "/";
            Message message = infoHandler.obtainMessage();
            message.obj = HttpUtil.myConnectionGET(url);
            infoHandler.sendMessage(message);
        }
    }
    private class permissionThread extends Thread{
        @Override
        public void run() {
            String url = ConstantContract.URL_USER_BASE + "query/" + sp.getString(ConstantContract.SP_USER_ID, null) + "/" + questionId + "/";
            int permissionInfo;
            String text = myConnectionGET(url);
            if(text.equals("True")){
                permissionInfo = 1;
            }else {
                permissionInfo = 0;
            }
            permissionHandler.sendEmptyMessage(permissionInfo);
        }
    }

    private void iniView(){
        listView = (ListView) findViewById(R.id.question_detail_list);
        fab = (FloatingActionButton) findViewById(R.id.question_detail_add_answer);
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
        GetQuestionUtil getQuestionUtil = new GetQuestionUtil(url, QuestionDetailActivity.this);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    AnswerData answerData = (AnswerData) parent.getAdapter().getItem(position);
                    Intent intent = new Intent(QuestionDetailActivity.this, AnswerDetailActivity.class);
                    intent.putExtra("answerId", answerData.getmId());
                    startActivity(intent);
                }
            }
        });
    }
}
