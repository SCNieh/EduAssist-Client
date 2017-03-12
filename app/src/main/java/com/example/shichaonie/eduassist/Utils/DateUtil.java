package com.example.shichaonie.eduassist.Utils;

import com.example.shichaonie.eduassist.UserData.AnswerData;
import com.example.shichaonie.eduassist.UserData.QuestionData;

import java.util.ArrayList;

import static android.R.attr.data;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Shichao Nie on 2017/3/8.
 */

public final class DateUtil {

    public static ArrayList<QuestionData> sortByDate_Q(ArrayList<QuestionData> data){
        ArrayList<QuestionData> tempList = new ArrayList<>();
        for(int i = data.size() - 1; i > 0; i--){
            tempList.add(data.get(i));
        }
        return tempList;
    }
    public static ArrayList<AnswerData> sortByDate_A(ArrayList<AnswerData> data){
        ArrayList<AnswerData> tempList = new ArrayList<>();
        for(int i = data.size() - 1; i > 0; i--){
            tempList.add(data.get(i));
        }
        return tempList;
    }

}
