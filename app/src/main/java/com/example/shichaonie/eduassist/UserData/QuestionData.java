package com.example.shichaonie.eduassist.UserData;

import static android.R.attr.category;
import static android.R.attr.id;
import static android.R.attr.text;
import static android.R.attr.value;

/**
 * Created by Shichao Nie on 2017/1/1.
 */

public class QuestionData {

    private int mId;
    private int mAsk_id;
    private int mInvited_id;
    private int mCategory;
    private String mTitle;
    private String mContent_text;
    private String mContent_image;
    private String mContent_voice;
    private int mAttribute; // 0: private, 1: public
    private int mQuestion_status; // completed: 0, processing: 1
    private int mInvite_status; // answered: 0, refused: 1, wait-for-response: None
    private String mRefuse_reason;
    private float mValue;

    public  QuestionData(){}
    public QuestionData(int id, int ask_id, int category, String title, String content_text,  int attribute, int question_status, float value){
        mId = id;
        mAsk_id = ask_id;
        mCategory = category;
        mTitle = title;
        mContent_text = content_text;
        mAttribute = attribute;
        mQuestion_status = question_status;
        mValue = value;
    }
    public QuestionData(int ask_id, int category, String title, String content_text,  int attribute, int invited_id, int question_status, float value){
        mAsk_id = ask_id;
        mCategory = category;
        mTitle = title;
        mContent_text = content_text;
        mAttribute = attribute;
        mInvited_id = invited_id;
        mQuestion_status = question_status;
        mValue = value;
    }

    //private mode: refused
    public QuestionData(int id, int ask_id, int invited_id, int category, String title, String content_text, String content_image,
                        String content_voice, int attribute, int question_status, int invite_status, String refuse_reason, float value){
        mId = id;
        mAsk_id = ask_id;
        mInvited_id = invited_id;
        mCategory = category;
        mTitle = title;
        mContent_text = content_text;
        mContent_image = content_image;
        mContent_voice = content_voice;
        mAttribute = attribute;
        mQuestion_status = question_status;
        mInvite_status = invite_status;
        mRefuse_reason = refuse_reason;
        mValue = value;
    }
    //private mode: wait for response
    public QuestionData(int id, int ask_id, int invited_id, int category, String title, String content_text, String content_image,
                        String content_voice, int attribute, int question_status, int invite_status, float value){
        mId = id;
        mAsk_id = ask_id;
        mInvited_id = invited_id;
        mCategory = category;
        mTitle = title;
        mContent_text = content_text;
        mContent_image = content_image;
        mContent_voice = content_voice;
        mAttribute = attribute;
        mQuestion_status = question_status;
        mInvite_status = invite_status;
        mValue = value;
    }
    //public mode
    public QuestionData(int id, int ask_id, int category, String title, String content_text, String content_image,
                        String content_voice, int attribute, int question_status, float value){
        mId = id;
        mAsk_id = ask_id;
        mCategory = category;
        mTitle = title;
        mContent_text = content_text;
        mContent_image = content_image;
        mContent_voice = content_voice;
        mAttribute = attribute;
        mQuestion_status = question_status;
        mValue = value;
    }

    public int getmId(){
        return mId;
    }
    public int getmAsk_id() {
        return mAsk_id;
    }
    public int getmInvited_id() {
        return mInvited_id;
    }
    public int getmCategory() {
        return mCategory;
    }
    public String getmTitle() {
        return mTitle;
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
    public int getmAttribute() {
        return mAttribute;
    }
    public int getmQuestion_status() {
        return mQuestion_status;
    }
    public int getmInvite_status() {
        return mInvite_status;
    }
    public String getmRefuse_reason() {
        return mRefuse_reason;
    }
    public float getmValue() {
        return mValue;
    }
}
