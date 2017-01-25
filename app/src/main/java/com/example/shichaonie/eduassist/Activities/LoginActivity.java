package com.example.shichaonie.eduassist.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.User;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;

import static com.example.shichaonie.eduassist.Utils.ConstantContract.BASE_URL;
import static com.example.shichaonie.eduassist.Utils.ConstantContract.INIT_CODE;
import static com.example.shichaonie.eduassist.Utils.ConstantContract.SP_AUTO_LOGIN;
import static com.example.shichaonie.eduassist.Utils.ConstantContract.SP_NAME;
import static com.example.shichaonie.eduassist.Utils.ConstantContract.SP_PASSWORD;
import static com.example.shichaonie.eduassist.Utils.ConstantContract.SP_REMEMBER_PASSWORD;
import static com.example.shichaonie.eduassist.Utils.ConstantContract.SP_TAG;
import static com.example.shichaonie.eduassist.Utils.ConstantContract.SP_USER_ID;
import static com.example.shichaonie.eduassist.Utils.HttpUtil.createUrl;
import static com.example.shichaonie.eduassist.Utils.HttpUtil.myConnectionPOST;
import static com.example.shichaonie.eduassist.UserData.User.md5;
import static com.example.shichaonie.eduassist.Utils.HttpUtil.myConnectionPOST;
import com.example.shichaonie.eduassist.Utils.ConstantContract;

/**
 * Created by Shichao Nie on 2016/12/13.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private static final String LOG_TAG = LoginActivity.class.getName();
    private Toast toast = null;
    private String verifyInfo = "";
    private SharedPreferences sp = null;
    private CheckBox rememPassword;
    private CheckBox autoLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        Button signIn = (Button) findViewById(R.id.sign_in);
        TextView register = (TextView) findViewById(R.id.register);
        rememPassword = (CheckBox) findViewById(R.id.check_box_remember_password);
        autoLogin = (CheckBox) findViewById(R.id.check_box_auto_login);
        signIn.setOnClickListener(signInListener);
        sp = getSharedPreferences(SP_TAG, MODE_PRIVATE);
        if(sp.getBoolean(SP_REMEMBER_PASSWORD, false)){
            username.setText(sp.getString(SP_NAME, null));
            password.setText(sp.getString(SP_PASSWORD, null));
        }
        rememPassword.setChecked(sp.getBoolean(SP_REMEMBER_PASSWORD, false));
        autoLogin.setChecked(sp.getBoolean(SP_AUTO_LOGIN, false));
        Intent intent = getIntent();
        if(intent.getIntExtra("mark", -1) == -1){ //user logout
            if(autoLogin.isChecked()){
                loginThread newThread = new loginThread();
                newThread.start();
            }
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rememPassword.setChecked(true);
                }
            }
        });
    }

    private View.OnClickListener signInListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loginThread newThread = new loginThread();
            newThread.start();
        }
    };

    private void makeToast(String msg){
        if(toast == null){
            toast = Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT);
        }
        else {
            toast.setText(msg);
        }
    }

    private class loginThread extends Thread{ //change to asynctask later
        @Override
        public void run() {
            String Info = null;
            String is = null;
            try{
                JSONObject json = new JSONObject();
                json.put("mStatusCode", INIT_CODE);
                json.put("mUsername", username.getText().toString());
                Info = json.toString();
            }catch (JSONException e){
                e.printStackTrace();
            }
            String url = BASE_URL + "login/";
            is = myConnectionPOST(Info, url); //First connection -- get random integer.
            int randomInteger;
            if(is != null && !is.equals("-1")){
                randomInteger = Integer.parseInt(is);
                String responseString = updateUserInfo(randomInteger);
                verifyInfo = myConnectionPOST(responseString, url); //Second connection -- get response String.
                if(!verifyInfo.equals("False")){
                    sp.edit().putString(SP_USER_ID, verifyInfo).apply();
                    if(autoLogin.isChecked()){
                        sp.edit().putBoolean(SP_AUTO_LOGIN,true).apply();
                    }else {
                        sp.edit().putBoolean(SP_AUTO_LOGIN,false).apply();
                    }
                    if(rememPassword.isChecked()){
                        sp.edit().putBoolean(SP_REMEMBER_PASSWORD, true).apply();
                        if(!username.getText().toString().equals(sp.getString(SP_NAME, null))){
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(SP_NAME, username.getText().toString());
                            editor.putString(SP_PASSWORD, md5(password.getText().toString()));
                            editor.apply();
                        }else if(!password.getText().toString().equals(sp.getString(SP_PASSWORD, null))){
                            sp.edit().putString(SP_PASSWORD, md5(password.getText().toString())).apply();
                        }
                    }else {
                        sp.edit().putString(SP_NAME, username.getText().toString());
                        sp.edit().putBoolean(SP_REMEMBER_PASSWORD, false).apply();
                    }

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    Looper.prepare();
                    makeToast("Password is wrong");
                    toast.show();
                    Looper.loop();
                }
            }
            else if(is == null){
                Looper.prepare();
                Toast.makeText(LoginActivity.this, "Server failed", Toast.LENGTH_SHORT).show();
                Looper.loop();
                return;
            }else if(is.equals("-1")){
                Looper.prepare();
                Toast.makeText(LoginActivity.this, "User name doesn't exists", Toast.LENGTH_SHORT).show();
                Looper.loop();
                return;
            }

        }
    }

    private String updateUserInfo(int integer){
        User userInfo = new User(username.getText().toString(), password.getText().toString());
        byte[] array;
        if(sp.getString(SP_PASSWORD, null) != null && password.getText().toString().equals(sp.getString(SP_PASSWORD, null))){
            array = password.getText().toString().getBytes();
        }else {
            array = userInfo.getPassword().getBytes();
        }
        for(int i = 0; i < array.length; i++){
            array[i] =(byte) (array[i] ^ integer);
        }
        userInfo.setPassword(new String(array));
        Gson gson = new Gson();
        return  gson.toJson(userInfo);
    }
}
