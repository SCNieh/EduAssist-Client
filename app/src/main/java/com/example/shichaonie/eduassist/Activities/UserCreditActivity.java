package com.example.shichaonie.eduassist.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.Utils.CircleProgressView;
import com.example.shichaonie.eduassist.Utils.ConstantContract;

/**
 * Created by Shichao Nie on 2017/3/12.
 */

public class UserCreditActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_credit);

        Intent intent = getIntent();

        int userCredit = intent.getIntExtra("userCredit", 0);
        CircleProgressView circleProgressView = (CircleProgressView) findViewById(R.id.my_credit_progress_view);
        circleProgressView.setProgress(userCredit);
        if(userCredit >= 90){
            circleProgressView.setmColor(ContextCompat.getColor(this, R.color.credit_high));
            circleProgressView.setmTxtHint1(getResources().getString(R.string.credit_high));
        }else if(userCredit >= 80){
            circleProgressView.setmColor(ContextCompat.getColor(this, R.color.credit_mid));
            circleProgressView.setmTxtHint1(getResources().getString(R.string.credit_mid));
        }else{
            circleProgressView.setmColor(ContextCompat.getColor(this, R.color.credit_low));
            circleProgressView.setmTxtHint1(getResources().getString(R.string.credit_low));
        }

        ImageView back = (ImageView) findViewById(R.id.my_credit_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
