package com.example.shichaonie.eduassist.MainActivityFragments.MyAnswersUtil;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.AnswerData;

import java.util.ArrayList;

/**
 * Created by Shichao Nie on 2017/1/29.
 */

public class MyAnswerAdapter extends ArrayAdapter<AnswerData> {

    public MyAnswerAdapter(Context context, ArrayList<AnswerData> arrayList){
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if(listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.my_answer_list_item, parent, false);
        }
        AnswerData answerData = getItem(position);

        TextView questionTitle = (TextView) listView.findViewById(R.id.my_answer_question_title);
        questionTitle.setText(answerData != null ? answerData.getmQuestionTitle() : "");

        TextView answerText = (TextView) listView.findViewById(R.id.my_answer_content_text);
        answerText.setText(answerData != null ? answerData.getmContent_text() : "");

        ImageView answerStatus = (ImageView) listView.findViewById(R.id.my_answer_status);
        assert answerData != null;
        if(answerData.getmStatus() == 0){ // accept
            answerStatus.setVisibility(View.VISIBLE);
        }else {
            answerStatus.setVisibility(View.GONE);
        }

        return listView;
    }
}
