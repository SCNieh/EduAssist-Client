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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shichaonie.eduassist.MainActivityFragments.ContentFragmentUtils.QuestionAdapter;
import com.example.shichaonie.eduassist.MainActivityFragments.ContentFragmentUtils.QuestionLoader;
import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.QuestionData;
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

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.question_list_swip_layout);
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
        niceSpinnerCategory.setOnItemSelectedListener(filterSelectedCatetory);

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
    private AdapterView.OnItemSelectedListener filterSelectedCatetory = new AdapterView.OnItemSelectedListener() {
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

        QuestionAdapter adapter = new QuestionAdapter(this.getContext(), data);
        ListView questionList = (ListView) rootView.findViewById(R.id.question_list);
        questionList.setAdapter(adapter);

        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(Integer.toString(data.get(position).getmId())));
//                startActivity(intent);
                searchEdit.clearFocus();
            }
        });

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<QuestionData>> loader) {
        updateUi(new ArrayList<QuestionData>());
    }
}
