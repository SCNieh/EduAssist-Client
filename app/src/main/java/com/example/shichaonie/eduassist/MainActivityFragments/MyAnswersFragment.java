package com.example.shichaonie.eduassist.MainActivityFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shichaonie.eduassist.Activities.QuestionDetailActivity;
import com.example.shichaonie.eduassist.AnswerListUtils.AnswerAdapter;
import com.example.shichaonie.eduassist.MainActivityFragments.ContentFragmentUtils.QuestionAdapter;
import com.example.shichaonie.eduassist.MainActivityFragments.MyAnswersUtil.MyAnswerAdapter;
import com.example.shichaonie.eduassist.MainActivityFragments.MyAnswersUtil.MyAnswersLoader;
import com.example.shichaonie.eduassist.MainActivityFragments.MyQuestionUtil.MyQuestionLoader;
import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.AnswerData;
import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.Utils.ConstantContract;
import com.example.shichaonie.eduassist.Utils.DataFilter;
import com.example.shichaonie.eduassist.Utils.NiceSpinner.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.example.shichaonie.eduassist.Utils.DataFilter.filterData;

/**
 * Created by Shichao Nie on 2017/2/14.
 */

public class MyAnswersFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<AnswerData>> {
    public View rootView;
    public FragmentManager fm;
    public SharedPreferences sp;
    public String userId = null;
    private TextView emptyView;

    public MyAnswersFragment(){}

    public void iniId(){
        userId = sp.getString(ConstantContract.SP_USER_ID, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_answers_fragment, container, false);


        sp = getActivity().getSharedPreferences(ConstantContract.SP_TAG, Context.MODE_PRIVATE);
        emptyView = (TextView) rootView.findViewById(R.id.my_answer_empty);
        emptyView.setVisibility(View.GONE);
        fm = getFragmentManager();
        ImageView imageView = (ImageView) rootView.findViewById(R.id.my_answer_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.popBackStack();
                getActivity().findViewById(R.id.camera_fab).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.main_body).setVisibility(View.VISIBLE);
            }
        });
        iniId();

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.answer_list_swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLoaderManager().restartLoader(0, null, MyAnswersFragment.this).forceLoad();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getLoaderManager().initLoader(0, null, this).forceLoad();
        return rootView;
    }

    @Override
    public Loader<ArrayList<AnswerData>> onCreateLoader(int id, Bundle args) {
        return new MyAnswersLoader(this.getContext(), userId);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<AnswerData>> loader, ArrayList<AnswerData> data) {
        updateUI(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<AnswerData>> loader) {
        updateUI(new ArrayList<AnswerData>());
    }
    private void updateUI(ArrayList<AnswerData> data){
        if(data.isEmpty()){
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            MyAnswerAdapter adapter = new MyAnswerAdapter(this.getContext(), data);
            final ListView answerList = (ListView) rootView.findViewById(R.id.my_answer_list);
            answerList.setAdapter(adapter);

            answerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (id < -1) { //headview || footview
                        return;
                    }
                    MyAnswerAdapter adapter = (MyAnswerAdapter) answerList.getAdapter();
                    AnswerData answer = adapter.getItem((int) id);
                    int questionId = 0;
                    int questionAttr = 0;
                    float questionValue = 0;
                    int answerStatus = 0;
                    int invitedId = -1;

                    if (answer != null) {
                        questionId = answer.getmQuestion_Id();
                        questionAttr = answer.getmQuestionAttr();
                        questionValue = answer.getmQuestionValue();
                        answerStatus = 1; // have answered
                        if(questionAttr == 0){ // private
                            invitedId = Integer.parseInt(userId);
                        }
                    }
                    Intent intent = new Intent(getContext(), QuestionDetailActivity.class);
                    intent.putExtra("questionId", questionId);
                    intent.putExtra("questionAttr", questionAttr);
                    intent.putExtra("questionValue", questionValue);
                    intent.putExtra("answerStatus", answerStatus);
                    intent.putExtra("invitedId", invitedId);
                    startActivity(intent);

                }
            });
        }
    }
}
