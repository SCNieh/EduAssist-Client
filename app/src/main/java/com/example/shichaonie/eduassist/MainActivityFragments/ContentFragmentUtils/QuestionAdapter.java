package com.example.shichaonie.eduassist.MainActivityFragments.ContentFragmentUtils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.Utils.ConvertUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shichao Nie on 2017/1/1.
 */

public class QuestionAdapter extends ArrayAdapter<QuestionData> {

    public QuestionAdapter(Context context, ArrayList<QuestionData> arrayList){
        super(context, 0, arrayList);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.content_list_item, parent, false);
        }
        QuestionData questionData = getItem(position);

        TextView questionTitle = (TextView) listItemView.findViewById(R.id.question_title);
        questionTitle.setText(questionData.getmTitle());


        TextView categoryTag = (TextView) listItemView.findViewById(R.id.category_tag);
        categoryTag.setText(ConvertUtil.toCategory(questionData.getmCategory()));

        TextView questionValue = (TextView) listItemView.findViewById(R.id.question_value);
        questionValue.setText(Float.toString(questionData.getmValue()));

        ImageView imageLock = (ImageView) listItemView.findViewById(R.id.question_attr);
        if(questionData.getmAttribute() == 1){
            imageLock.setVisibility(View.INVISIBLE);
        }else {
            imageLock.setVisibility(View.VISIBLE);
        }

        ImageView questionStatus = (ImageView) listItemView.findViewById(R.id.question_status);
        if(questionData.getmQuestion_status() == 0){
            questionStatus.setImageResource(R.drawable.ic_complete);
        }else {
            questionStatus.setImageResource(R.drawable.ic_processing);

        }

        return listItemView;
    }


}
