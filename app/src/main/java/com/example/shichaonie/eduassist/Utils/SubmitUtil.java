package com.example.shichaonie.eduassist.Utils;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.Utils.HttpUtil;
import com.google.gson.Gson;

import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Shichao Nie on 2017/1/2.
 */

public class SubmitUtil {
    private static String mInfo;
    private static String mUrl;
    private static myAsyncTask task;
    private static String mIs = null;
    private RelativeLayout shelterLayout;
    private static AppCompatActivity activity;

    public SubmitUtil(String Info, String url, RelativeLayout layout, AppCompatActivity appCompatActivity){
        shelterLayout = layout;
        mInfo = Info;
        mUrl = url;
        activity = appCompatActivity;
    }

    public String returnInfo(){
        if(task == null){
            task = new myAsyncTask();
            task.execute(mInfo, mUrl);
        }
        else {
            task.cancel(true);
            task = new myAsyncTask();
            task.execute(mInfo, mUrl);
        }
        return mIs;
    }

    private class myAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {

            return HttpUtil.myConnectionPOST(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            mIs = s;
            if(mIs != null && mIs.equals("Success")){
                shelterLayout.setVisibility(View.GONE);
                showSuccessDialog(activity.getString(R.string.success), finishListener, finishListener);
            }else {
                shelterLayout.setVisibility(View.GONE);
                showSuccessDialog(activity.getString(R.string.failed), stayListener, stayListener);
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
    private DialogInterface.OnClickListener stayListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(dialog != null){
                dialog.dismiss();
            }
        }
    };
    private DialogInterface.OnClickListener finishListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            activity.finish();

        }
    };

}
