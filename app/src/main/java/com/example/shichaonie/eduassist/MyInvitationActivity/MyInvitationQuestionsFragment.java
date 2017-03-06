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
import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.Utils.ConstantContract;

import java.util.ArrayList;

/**
 * Created by Shichao Nie on 2017/3/5.
 */

public class MyInvitationQuestionsFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<QuestionData>> {
    public View rootView;
    public FragmentManager fm;
    public SharedPreferences sp;
    public String userId = null;
    private TextView emptyView;

    public MyInvitationQuestionsFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_invitation_questions, container, false);

        sp = getActivity().getSharedPreferences(ConstantContract.SP_TAG, Context.MODE_PRIVATE);
        emptyView = (TextView) rootView.findViewById(R.id.my_invitation_question_empty);
        emptyView.setVisibility(View.GONE);
        fm = getFragmentManager();

        userId = sp.getString(ConstantContract.SP_USER_ID, null);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.my_invitation_question_swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLoaderManager().restartLoader(0, null, MyInvitationQuestionsFragment.this).forceLoad();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getLoaderManager().initLoader(0, null, this).forceLoad();
        return rootView;
    }

    @Override
    public Loader<ArrayList<QuestionData>> onCreateLoader(int id, Bundle args) {
        return new MyInvitationQuestionLoader(this.getContext(), userId);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<QuestionData>> loader, ArrayList<QuestionData> data) {
        updateUI(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<QuestionData>> loader) {
        updateUI(new ArrayList<QuestionData>());
    }
    private void updateUI(ArrayList<QuestionData> data){
        if(data.isEmpty()){
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            QuestionAdapter adapter = new QuestionAdapter(this.getContext(), data);
            final ListView questionList = (ListView) rootView.findViewById(R.id.my_invitation_question_list);
            questionList.setAdapter(adapter);

            questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (id < -1) { //headview || footview
                        return;
                    }
                    QuestionAdapter adapter = (QuestionAdapter) questionList.getAdapter();
                    QuestionData question = adapter.getItem((int) id);
                    int questionId = 0;
                    int questionAttr = 0;
                    float questionValue = 0;
                    int answerStatus = 0;
                    int invitedId = -1;
                    if (question != null) {
                        questionId = question.getmId();
                        questionAttr = question.getmAttribute();
                        questionValue = question.getmValue();
                        answerStatus = question.getmAnswerStatus();
                        invitedId  =question.getmInvited_id();
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
