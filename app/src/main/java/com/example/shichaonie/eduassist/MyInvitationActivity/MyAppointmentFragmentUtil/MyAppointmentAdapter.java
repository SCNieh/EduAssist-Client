package com.example.shichaonie.eduassist.MyInvitationActivity.MyAppointmentFragmentUtil;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.AppointmentData;
import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.Utils.ConvertUtil;

import java.util.ArrayList;

/**
 * Created by Shichao Nie on 2017/1/1.
 */

public class MyAppointmentAdapter extends ArrayAdapter<AppointmentData> {

    public MyAppointmentAdapter(Context context, ArrayList<AppointmentData> arrayList){
        super(context, 0, arrayList);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.invitation_appointment_list_item, parent, false);
        }
        AppointmentData appointmentData = getItem(position);

        TextView userName = (TextView) listItemView.findViewById(R.id.invitation_appointment_user_name);
        userName.setText(appointmentData.getmUserName());

        TextView selfIntro = (TextView) listItemView.findViewById(R.id.invitation_appointment_user_selfIntro);
        selfIntro.setText(appointmentData.getmSelfIntro());

        TextView msg = (TextView) listItemView.findViewById(R.id.invitation_appointment_msg);
        msg.setText(appointmentData.getmInfo());

        return listItemView;
    }


}
