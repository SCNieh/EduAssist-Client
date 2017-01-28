package com.example.shichaonie.eduassist.Utils;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.shichaonie.eduassist.R;

/**
 * Created by Shichao Nie on 2017/1/2.
 */

public class GetQuestionUtil {
    private static String mUrl;
    private static myAsyncTask task;
    private static String mIs = null;
    private static AppCompatActivity activity;

    public GetQuestionUtil(String url, AppCompatActivity appCompatActivity){
        mUrl = url;
        activity = appCompatActivity;
    }

    public String returnInfo(){
        if(task == null){
            task = new myAsyncTask();
            task.execute(mUrl);
        }
        else {
            task.cancel(true);
            task = new myAsyncTask();
            task.execute(mUrl);
        }
        return mIs;
    }

    private class myAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            return HttpUtil.myConnectionGET(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            mIs = s;
            RelativeLayout QuestionDetailShelter = (RelativeLayout) activity.findViewById(R.id.question_detail_shelter);
            if(mIs == null || mIs.isEmpty()){
                QuestionDetailShelter.setVisibility(View.GONE);
                showSuccessDialog(activity.getString(R.string.loadFailed), finishListener, finishListener);
            }
        }
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
}
