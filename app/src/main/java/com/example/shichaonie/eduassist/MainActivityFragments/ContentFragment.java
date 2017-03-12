package com.example.shichaonie.eduassist.MainActivityFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shichaonie.eduassist.Activities.QuestionDetailActivity;
import com.example.shichaonie.eduassist.MainActivityFragments.ContentFragmentUtils.QuestionAdapter;
import com.example.shichaonie.eduassist.MainActivityFragments.ContentFragmentUtils.QuestionLoader;
import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.QuestionData;
import com.example.shichaonie.eduassist.UserData.User;
import com.example.shichaonie.eduassist.UserListUtils.UserAdapter;
import com.example.shichaonie.eduassist.Utils.DateUtil;
import com.example.shichaonie.eduassist.Utils.NiceSpinner.NiceSpinner;
import com.example.shichaonie.eduassist.Utils.DataFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.example.shichaonie.eduassist.Utils.DataFilter.filterData;

/**
 * Created by Shichao Nie on 2016/12/31.
 */

public class ContentFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<QuestionData>> {
    private View rootView;
    private int questionStatus = -1;
    private int questionCategory = -1;
    private int questionAttr = -1;
    private ProgressBar mProgressBar;
    private TextView emptyView;
    private EditText searchEdit;
    private ArrayList<QuestionData> questionList = new ArrayList<>();
    private ArrayList<QuestionData> filteredQuestionList = new ArrayList<>();
    public String keywords = null;

    public ContentFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_fragment, container, false);
        initTitleView();
        initFilterView();

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.question_list_swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchEdit.clearFocus();
                keywords = searchEdit.getText().toString();
                getLoaderManager().restartLoader(0, null, ContentFragment.this).forceLoad();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getLoaderManager().initLoader(0, null, this).forceLoad();

        return rootView;
    }
    private void initTitleView(){
        searchEdit = (EditText) rootView.findViewById(R.id.search_edit);
        searchEdit.clearFocus();
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    keywords = searchEdit.getText().toString();
                    getLoaderManager().restartLoader(0, null, ContentFragment.this).forceLoad();
                }
                return true;
            }
        });
    }
    private void initFilterView(){
        NiceSpinner niceSpinnerStatus = (NiceSpinner) rootView.findViewById(R.id.nice_spinner_status);
        NiceSpinner niceSpinnerCategory = (NiceSpinner) rootView.findViewById(R.id.nice_spinner_category);
        NiceSpinner niceSpinnerAttr = (NiceSpinner) rootView.findViewById(R.id.nice_spinner_attr);

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
            searchEdit.clearFocus();
            questionStatus = position - 1;
            filteredQuestionList = filterData(questionList, questionStatus, questionCategory, questionAttr);
            updateUi(filteredQuestionList);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private AdapterView.OnItemSelectedListener filterSelectedCategory = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            searchEdit.clearFocus();
            questionCategory = position - 1;
            filteredQuestionList = filterData(questionList, questionStatus, questionCategory, questionAttr);
            updateUi(filteredQuestionList);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private AdapterView.OnItemSelectedListener filterSelectedAttr = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            searchEdit.clearFocus();
            questionAttr = position - 1;
            filteredQuestionList = filterData(questionList, questionStatus, questionCategory, questionAttr);
            updateUi(filteredQuestionList);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    @Override
    public Loader<ArrayList<QuestionData>> onCreateLoader(int id, Bundle args) {
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        emptyView.setVisibility(View.GONE);
        return new QuestionLoader(this.getContext(), keywords);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<QuestionData>> loader, ArrayList<QuestionData> data) {
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);
        questionList = data;
        updateUi(data);
    }

    private void updateUi(final ArrayList<QuestionData> data){
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        emptyView.setText(R.string.noData);
        if(data == null || data.isEmpty()){
            emptyView.setVisibility(View.VISIBLE);
        }else {
            emptyView.setVisibility(View.GONE);
        }
        ArrayList<QuestionData> newData = DateUtil.sortByDate_Q(data);

        QuestionAdapter adapter = new QuestionAdapter(this.getContext(), newData);
        final ListView questionList = (ListView) rootView.findViewById(R.id.question_list);
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
                int askId = 0;
                int questionAttr = 0;
                float questionValue = 0;
                int answerStatus = 0;
                int invitedId = -1;
                int questionStatus = 1;
                if (question != null) {
                    questionId = question.getmId();
                    askId = question.getmAsk_id();
                    questionAttr = question.getmAttribute();
                    questionValue = question.getmValue();
                    answerStatus = question.getmAnswerStatus();
                    invitedId  = question.getmInvited_id();
                    questionStatus = question.getmQuestion_status();
                }
                Intent intent = new Intent(getContext(), QuestionDetailActivity.class);
                intent.putExtra("questionId", questionId);
                intent.putExtra("askId", askId);
                intent.putExtra("questionAttr", questionAttr);
                intent.putExtra("questionValue", questionValue);
                intent.putExtra("answerStatus", answerStatus);
                intent.putExtra("invitedId", invitedId);
                intent.putExtra("questionStatus", questionStatus);
                startActivity(intent);

                searchEdit.clearFocus();
            }
        });

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<QuestionData>> loader) {
        updateUi(new ArrayList<QuestionData>());
    }
}
