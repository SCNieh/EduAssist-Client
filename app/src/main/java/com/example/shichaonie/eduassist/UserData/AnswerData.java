package com.example.shichaonie.eduassist.UserData;

import java.util.ArrayList;

/**
 * Created by Shichao Nie on 2017/1/28.
 */

public class AnswerData {

    private int mId;
    private int mQuestion_Id;
    private int mAnswers_id;
    private String mContent_text;
    private String mContent_image;
    private String mContent_voice;
    private int mStatus; // 0: accepted

    public AnswerData(){
    }
    public AnswerData(int id, int question_id, int answers_id, String content_text, String content_image, String content_voice, int status){
        mId = id;
        mQuestion_Id = question_id;
        mAnswers_id = answers_id;
        mContent_text = content_text;
        mContent_image = content_image;
        mContent_voice = content_voice;
        mStatus = status;
    }
    public AnswerData(int id, int question_id, int answers_id, String content_text, int status){
        mId = id;
        mQuestion_Id = question_id;
        mAnswers_id = answers_id;
        mContent_text = content_text;
        mStatus = status;
    }

    public int getmId() {
        return mId;
    }

    public int getmQuestion_Id() {
        return mQuestion_Id;
    }

    public int getmAnswers_id() {
        return mAnswers_id;
    }

    public String getmContent_text() {
        return mContent_text;
    }

    public String getmContent_image() {
        return mContent_image;
    }

    public String getmContent_voice() {
        return mContent_voice;
    }

    public int getmStatus() {
        return mStatus;
    }
}
