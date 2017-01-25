package com.example.shichaonie.eduassist.Utils;

import com.example.shichaonie.eduassist.UserData.QuestionData;

import java.util.ArrayList;

import static android.R.attr.filter;

/**
 * Created by Shichao Nie on 2017/1/2.
 */

public class DataFilter {

    public static ArrayList<QuestionData> filterData(ArrayList<QuestionData> questionList, int status, int category, int attr){
        QuestionData questionData;
        ArrayList<QuestionData> tempQuestionList = new ArrayList<>();
        for(int i = 0; i < questionList.size(); i++) {
            questionData = questionList.get(i);
            if(status == -1 && category == -1 && attr == -1){
                tempQuestionList.add(questionData);
            }
            else if(status != -1 && category == -1 && attr == -1){
                if(questionData.getmQuestion_status() == status){
                    tempQuestionList.add(questionData);
                }
            }
            else if(status == -1 && category != -1 && attr == -1){
                if(questionData.getmCategory() == category){
                    tempQuestionList.add(questionData);
                }
            }
            else if(status == -1 && category == -1 && attr != -1){
                if(questionData.getmAttribute() == attr){
                    tempQuestionList.add(questionData);
                }
            }
            else if(status != -1 && category != -1 && attr == -1){
                if(questionData.getmQuestion_status() == status && questionData.getmCategory() == category){
                    tempQuestionList.add(questionData);
                }
            }
            else if(status == -1 && category != -1 && attr != -1){
                if(questionData.getmCategory() == category && questionData.getmAttribute() == attr){
                    tempQuestionList.add(questionData);
                }
            }
            else if(status != -1 && category == -1 && attr != -1){
                if(questionData.getmQuestion_status() == status && questionData.getmAttribute() == attr){
                    tempQuestionList.add(questionData);
                }
            }
            else {
                if (questionData.getmQuestion_status() == status && questionData.getmCategory() == category && questionData.getmAttribute() == attr) {
                    tempQuestionList.add(questionData);
                }
            }

        }
        return tempQuestionList;
    }
}
