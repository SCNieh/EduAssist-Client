package com.example.shichaonie.eduassist.MainActivityFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shichaonie.eduassist.Activities.QuestionDetailActivity;
import com.example.shichaonie.eduassist.MainActivityFragments.ContentFragmentUtils.QuestionAdapter;
import com.example.shichaonie.eduassist.MainActivityFragments.MyQuestionUtil.MyQuestionLoader;
import com.example.shichaonie.eduassist.R;
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

public class MyQuestionFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<QuestionData>> {
    public View rootView;
    public FragmentManager fm;
    public SharedPreferences sp;
    public String userId = null;
    private int questionStatus = -1;
    private int questionCategory = -1;
    private int questionAttr = -1;
    private ArrayList<QuestionData> filteredQuestionList = new ArrayList<>();
    private ArrayList<QuestionData> questionList = new ArrayList<>();
    private TextView emptyView;

    public MyQuestionFragment(){}

    public void iniId(){
        userId = sp.getString(ConstantContract.SP_USER_ID, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_question_fragment, container, false);

        initFilterView();

        sp = getActivity().getSharedPreferences(ConstantContract.SP_TAG, Context.MODE_PRIVATE);
        emptyView = (TextView) rootView.findViewById(R.id.my_question_empty);
        emptyView.setVisibility(View.GONE);
        fm = getFragmentManager();
        ImageView imageView = (ImageView) rootView.findViewById(R.id.my_question_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.popBackStack();
                getActivity().findViewById(R.id.camera_fab).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.main_body).setVisibility(View.VISIBLE);
            }
        });
        iniId();

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.question_list_swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLoaderManager().restartLoader(0, null, MyQuestionFragment.this).forceLoad();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getLoaderManager().initLoader(0, null, this).forceLoad();
        return rootView;
    }



    private void initFilterView(){
        NiceSpinner niceSpinnerStatus = (NiceSpinner) rootView.findViewById(R.id.my_question_nice_spinner_status);
        NiceSpinner niceSpinnerCategory = (NiceSpinner) rootView.findViewById(R.id.my_question_nice_spinner_category);
        NiceSpinner niceSpinnerAttr = (NiceSpinner) rootView.findViewById(R.id.my_question_nice_spinner_attr);

        List<String> status = new LinkedList<>(Arrays.asList(this.getResources().getStringArray(R.array.array_status)));
        List<String> category = new LinkedList<>(Arrays.asList(this.getResources().getStringArray(R.array.array_category)));
        List<String> attr = new LinkedList<>(Arrays.asList(this.getResources().getStringArray(R.array.array_attr)));

        niceSpinnerStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
        niceSpinnerStatus.attachDataSource(status);
        niceSpinnerStatus.setOnItemSelectedListener(filterSelectedStatus);


        niceSpinnerCategory.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
        niceSpinnerCategory.attachDataSource(category);
        niceSpinnerCategory.setOnItemSelectedListener(filterSelectedCategory);

        niceSpinnerAttr.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
        niceSpinnerAttr.attachDataSource(attr);
        niceSpinnerAttr.setOnItemSelectedListener(filterSelectedAttr);

    }
    private AdapterView.OnItemSelectedListener  filterSelectedStatus = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            questionStatus = position - 1;
            filteredQuestionList = DataFilter.filterData(questionList, questionStatus, questionCategory, questionAttr);
            updateUI(filteredQuestionList);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private AdapterView.OnItemSelectedListener filterSelectedCategory = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            questionCategory = position - 1;
            filteredQuestionList = filterData(questionList, questionStatus, questionCategory, questionAttr);
            updateUI(filteredQuestionList);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private AdapterView.OnItemSelectedListener filterSelectedAttr = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            questionAttr = position - 1;
            filteredQuestionList = filterData(questionList, questionStatus, questionCategory, questionAttr);
            updateUI(filteredQuestionList);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public Loader<ArrayList<QuestionData>> onCreateLoader(int id, Bundle args) {
        return new MyQuestionLoader(this.getContext(), userId);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<QuestionData>> loader, ArrayList<QuestionData> data) {
        questionList = data;
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
            final ListView questionList = (ListView) rootView.findViewById(R.id.my_question_list);
            questionList.setAdapter(adapter);

            questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (id < -1) { //headview || footview
                        return;
                    }
                    QuestionAdapter adapter = (QuestionAdapter) questionList.getAdapter();
                    QuestionData question = adapter.getItem((int) id);
                    int askId = -1;
                    int questionId = 0;
                    int questionAttr = 0;
                    float questionValue = 0;
                    int answerStatus = 0;
                    int invitedId = -1;
                    if (question != null) {
                        askId = question.getmAsk_id();
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
                    intent.putExtra("askId", askId);
                    startActivity(intent);

                }
            });
        }
    }
}
