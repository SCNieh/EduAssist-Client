package com.example.shichaonie.eduassist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.User;
import com.example.shichaonie.eduassist.Utils.ConstantContract;
import com.google.gson.Gson;

import java.net.URL;

import static com.example.shichaonie.eduassist.Utils.ConstantContract.BASE_URL;
import static com.example.shichaonie.eduassist.Utils.HttpUtil.createUrl;
import static com.example.shichaonie.eduassist.Utils.HttpUtil.myConnectionPOST;

/**
 * Created by Shichao Nie on 2016/12/14.
 */

public class RegisterActivity extends AppCompatActivity{

    private static final String LOG_TAG = RegisterActivity.class.getName();
    private Toast toast = null;
    private String responseString = "";
    private EditText usernameEditor;
    private EditText passwordEditor;
    private EditText emailEditor;
    private EditText telEditor;
    private RadioButton student;
    private RadioButton teacher;
    private Spinner genderSpinner;
    private ImageView registerBack;
    private int mTitle;
    private int mGender;
    private boolean isCorrect = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        initView();

//        registerBtn.setEnabled(false);
//        if(isUserInfoCorrect()){
//            registerBtn.setEnabled(true);
//        }   //User Info Check... Accomplish later

    }
    private void initView(){
        usernameEditor = (EditText) findViewById(R.id.register_name);
        passwordEditor = (EditText) findViewById(R.id.register_password);
        emailEditor = (EditText) findViewById(R.id.register_Email);
        genderSpinner = (Spinner) findViewById(R.id.register_gender);
        telEditor = (EditText) findViewById(R.id.register_Tel);
        student = (RadioButton) findViewById(R.id.register_student);
        teacher = (RadioButton) findViewById(R.id.register_teacher);
        registerBack = (ImageView) findViewById(R.id.register_back);
        registerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnLogin();
            }
        });
        student.setChecked(true);
        student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    teacher.setChecked(false);
                }
            }
        });
        teacher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    student.setChecked(false);
                }
            }
        });
        Button registerBtn = (Button) findViewById(R.id.register_submit);
        registerBtn.setOnClickListener(registerListener);
        setupSpinner();
    }
    private boolean isUserInfoCorrect(){
        return isCorrect;
    }

    @Override
    public void onBackPressed() {
        returnLogin();
        super.onBackPressed();
    }


    private View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RegisterActivity.registerThread thread = new RegisterActivity.registerThread();
            thread.start();
        }
    };
    private void returnLogin(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void setupSpinner(){
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        genderSpinner.setAdapter(genderSpinnerAdapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = User.genderInfo.GNDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = User.genderInfo.GENDER_FEMALE; // Female
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0; // Unknown
            }
        });
    }

    private class registerThread extends Thread{
        @Override
        public void run() {
            String url = BASE_URL + "register/";
            if(student.isChecked()){
                mTitle = 0;
            }else {
                mTitle = 1;
            }
            User newUser = new User(usernameEditor.getText().toString(), passwordEditor.getText().toString(), mTitle, telEditor.getText().toString(), emailEditor.getText().toString(), mGender, 0, (float) 0);
            Gson gson = new Gson();
            responseString = myConnectionPOST(gson.toJson(newUser), url);
            Looper.prepare();
            if(responseString.equals("Success")){
                makeToast("Successful registered!");
                returnLogin();
            }else if(responseString.equals("User Exists")){
                makeToast("User already exists");
            }else {
                makeToast("Register Failed");
            }
            toast.show();
            Looper.loop();

        }
    }
    private void makeToast(String msg){
        if(toast == null){
            toast = Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT);
        }
        else {
            toast.setText(msg);
        }
    }

}
