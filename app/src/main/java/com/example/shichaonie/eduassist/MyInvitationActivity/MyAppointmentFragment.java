package com.example.shichaonie.eduassist.MyInvitationActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shichaonie.eduassist.Activities.QuestionDetailActivity;
import com.example.shichaonie.eduassist.MainActivityFragments.ContentFragmentUtils.QuestionAdapter;
import com.example.shichaonie.eduassist.MyInvitationActivity.MyAppointmentFragmentUtil.MyAppointmentAdapter;
import com.example.shichaonie.eduassist.MyInvitationActivity.MyAppointmentFragmentUtil.MyAppointmentLoader;
import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.AppointmentData;
import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.Utils.ConstantContract;

import java.util.ArrayList;

/**
 * Created by Shichao Nie on 2017/3/5.
 */

public class MyAppointmentFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<AppointmentData>> {
    public View rootView;
    public FragmentManager fm;
    public SharedPreferences sp;
    public String userId = null;
    private TextView emptyView;

    public MyAppointmentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_invitation_appointment, container, false);

        sp = getActivity().getSharedPreferences(ConstantContract.SP_TAG, Context.MODE_PRIVATE);
        emptyView = (TextView) rootView.findViewById(R.id.my_invitation_appointment_empty);
        emptyView.setVisibility(View.GONE);
        fm = getFragmentManager();

        userId = sp.getString(ConstantContract.SP_USER_ID, null);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.my_invitation_appointment_swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLoaderManager().restartLoader(0, null, MyAppointmentFragment.this).forceLoad();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getLoaderManager().initLoader(0, null, this).forceLoad();
        return rootView;
    }

    @Override
    public Loader<ArrayList<AppointmentData>> onCreateLoader(int id, Bundle args) {
        return new MyAppointmentLoader(this.getContext(), userId);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<AppointmentData>> loader, ArrayList<AppointmentData> data) {
        updateUI(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<AppointmentData>> loader) {
        updateUI(new ArrayList<AppointmentData>());
    }

    private void updateUI(ArrayList<AppointmentData> data) {
        if (data.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            MyAppointmentAdapter adapter = new MyAppointmentAdapter(this.getContext(), data);
            final ListView appointmentList = (ListView) rootView.findViewById(R.id.my_invitation_appointment_list);
            appointmentList.setAdapter(adapter);

            appointmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (id < -1) { //headview || footview
                        return;
                    }
                    MyAppointmentAdapter adapter = (MyAppointmentAdapter) appointmentList.getAdapter();
                    AppointmentData question = adapter.getItem((int) id);

                }
            });
        }
    }
}
