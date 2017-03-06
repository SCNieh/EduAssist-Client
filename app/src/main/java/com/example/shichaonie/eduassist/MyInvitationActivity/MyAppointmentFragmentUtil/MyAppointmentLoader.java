package com.example.shichaonie.eduassist.MyInvitationActivity.MyAppointmentFragmentUtil;

import android.content.Context;
import android.text.TextUtils;

import com.example.shichaonie.eduassist.UserData.AppointmentData;
import com.example.shichaonie.eduassist.Utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.shichaonie.eduassist.Utils.ConstantContract.URL_APPOINTMENT_BASE;

/**
 * Created by Shichao Nie on 2017/3/5.
 */

public class MyAppointmentLoader extends android.support.v4.content.AsyncTaskLoader<ArrayList<AppointmentData>> {

    private String mUserId;

    public MyAppointmentLoader(Context context, String userId) {
        super(context);
        mUserId = userId;
    }

    @Override
    public ArrayList<AppointmentData> loadInBackground() {
        String is;
        String newUrl = URL_APPOINTMENT_BASE + mUserId + "/";

        is = HttpUtil.myConnectionGET(newUrl);

        return extractFeatureFromJson(is);
    }

    private ArrayList<AppointmentData> extractFeatureFromJson(String questionJSON) {
        ArrayList<AppointmentData> appointmentData = new ArrayList<>();
        if (TextUtils.isEmpty(questionJSON)) {
            return appointmentData;
        }
        try {
            JSONObject jsonObject = new JSONObject(questionJSON);
            JSONArray jsonArray = jsonObject.getJSONArray("Appointment_data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectQuestion = jsonArray.getJSONObject(i);
                int id = jsonObjectQuestion.getInt("id");
                int stu_id = jsonObjectQuestion.getInt("stu_id");
                int teach_id = jsonObjectQuestion.getInt("teac_id");
                int status = jsonObjectQuestion.getInt("status");
                String info = jsonObjectQuestion.getString("info");
                String refuseReason;
                if(status == 1){ // refused
                    refuseReason = jsonObjectQuestion.getString("refuse_reason");
                }else {
                    refuseReason = null;
                }
                String userName = jsonObjectQuestion.getString("user_name");
                String selfIntro = jsonObjectQuestion.getString("self_intro");

                appointmentData.add(new AppointmentData(id, stu_id, teach_id, status, info, refuseReason, userName, selfIntro));
            }
            return appointmentData;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return appointmentData;
    }
}

