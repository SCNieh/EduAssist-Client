package com.example.shichaonie.eduassist.UserData;

import java.util.ArrayList;

import static com.example.shichaonie.eduassist.R.string.answerText;

/**
 * Created by Shichao Nie on 2017/1/28.
 */

public class AnswerData {

    private int mId;
    private int mQuestion_id;
    private int mAnswers_id;
    private String mAnswer_name;
    private String mAnswer_selfIntro;
    private String mContent_text;
    private String mContent_image;
    private String mContent_voice;
    private String mQuestionTitle;
    private int mStatus; // 0: accepted
    private int mQuestionAttr;
    private float mQuestionValue;

    public AnswerData(){
    }
    public AnswerData(int id, int question_id, String questionTitle, String answerText, int answer_status, int questionAttr, float questionValue){
        mId = id;
        mQuestion_id = question_id;
        mQuestionTitle = questionTitle;
        mContent_text = answerText;
        mStatus = answer_status;
        mQuestionAttr = questionAttr;
        mQuestionValue = questionValue;
    }
    public AnswerData(int id, int question_id, int answers_id, String answer_name, String answer_selfIntro, String content_text, String content_image, String content_voice, int status){
        mId = id;
        mQuestion_id = question_id;
        mAnswers_id = answers_id;
        mAnswer_name = answer_name;
        mAnswer_selfIntro = answer_selfIntro;
        mContent_text = content_text;
        mContent_image = content_image;
        mContent_voice = content_voice;
        mStatus = status;
    }
    public AnswerData(int id, int question_id, int answers_id, String answer_name, String answer_selfIntro, String content_text, int status){
        mId = id;
        mQuestion_id = question_id;
        mAnswers_id = answers_id;
        mAnswer_name = answer_name;
        mAnswer_selfIntro = answer_selfIntro;
        mContent_text = content_text;
        mStatus = status;
    }
    public AnswerData(int question_id, int answers_id, String content_text, int status){
        mQuestion_id = question_id;
        mAnswers_id = answers_id;
        mContent_text = content_text;
        mStatus = status;
    }
    public AnswerData(String answer_name, String answer_selfIntro, String content_text){
        mAnswer_name = answer_name;
        mAnswer_selfIntro = answer_selfIntro;
        mContent_text = content_text;
    }

    public int getmId() {
        return mId;
    }

    public int getmQuestion_Id() {
        return mQuestion_id;
    }

    public int getmAnswers_id() {
        return mAnswers_id;
    }

    public String getmAnswer_name() {
        return mAnswer_name;
    }

    public String getmAnswer_selfIntro() {
        return mAnswer_selfIntro;
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

    public String getmQuestionTitle(){
        return mQuestionTitle;
    }

    public int getmStatus() {
        return mStatus;
    }

    public int getmQuestionAttr() {
        return mQuestionAttr;
    }

    public float getmQuestionValue() {
        return mQuestionValue;
    }
}
