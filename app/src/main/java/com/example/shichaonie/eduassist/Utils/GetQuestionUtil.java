package com.example.shichaonie.eduassist.Utils;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shichaonie.eduassist.Activities.QuestionDetailActivity;
import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.QuestionData;

import org.json.JSONException;
import org.json.JSONObject;

import static android.R.id.list;

/**
 * Created by Shichao Nie on 2017/1/2.
 */

public class GetQuestionUtil {
    private static String mUrl;
    private static myAsyncTask task;
    private ListView listView;
    private static QuestionDetailActivity activity;

    public GetQuestionUtil(String url, QuestionDetailActivity appCompatActivity){
        mUrl = url;
        activity = appCompatActivity;
    }

    public void activate(){
        if(task == null){
            task = new myAsyncTask();
            task.execute(mUrl);
        }
        else {
            task.cancel(true);
            task = new myAsyncTask();
            task.execute(mUrl);
        }
    }

    private class myAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            return HttpUtil.myConnectionGET(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            RelativeLayout QuestionDetailShelter = (RelativeLayout) activity.findViewById(R.id.question_detail_shelter);
            QuestionDetailShelter.setVisibility(View.GONE);
            if(s == null || s.isEmpty()){
                showSuccessDialog(activity.getString(R.string.loadFailed), finishListener, finishListener);
            }
            QuestionData questionData = extractFeatureFromJson(s);
            upHeadView(questionData);
        }
    }
    private void upHeadView(QuestionData data){
        listView = activity.listView;
        View headView = activity.getLayoutInflater().inflate(R.layout.question_detail_list_head_view, listView, false);
        TextView questionTitle = (TextView) headView.findViewById(R.id.question_detail_title);
        questionTitle.setText(data.getmTitle());
        TextView questionText = (TextView) headView.findViewById(R.id.question_detail_text);
        questionText.setText(data.getmContent_text());

        listView.addHeaderView(headView);
    }
    private void showSuccessDialog(String msg, DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.check, positiveClickListener);
        builder.setNegativeButton(R.string.cancel, negativeClickListener);
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private DialogInterface.OnClickListener finishListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            activity.finish();
        }
    };
    public QuestionData extractFeatureFromJson(String questionJSON) {
        if (TextUtils.isEmpty(questionJSON)) {
            return new QuestionData();
        }
        try {
            JSONObject jsonObject = new JSONObject(questionJSON);
            JSONObject questionInfo = jsonObject.getJSONObject("Questions_data");
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
                ask_id = questionInfo.getInt("ask_id");
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
            String question_title = questionInfo.getString("title");
            String content_text = questionInfo.getString("content_text");
            String content_image = questionInfo.getString("content_image");
            String content_voice = questionInfo.getString("content_voice");
            String refuse_reason;

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
                                refuse_reason = questionInfo.getString("refuse_reason");
                            }else {
                                refuse_reason = "";
                            }
                            return new QuestionData(id, ask_id, invited_id, category, question_title, content_text, content_image, content_voice, attribute, question_status, invite_status, refuse_reason, value);
                        }else { //wait for response
                            return new QuestionData(id, ask_id, invited_id, category, question_title, content_text, content_image, content_voice, attribute, question_status, invite_status, value);
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
}
