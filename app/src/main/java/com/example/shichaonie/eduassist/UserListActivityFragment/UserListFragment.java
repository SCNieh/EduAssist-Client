package com.example.shichaonie.eduassist.UserListActivityFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
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

import com.example.shichaonie.eduassist.Activities.UserListActivity;
import com.example.shichaonie.eduassist.MainActivityFragments.UserInfoFragment;
import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.User;
import com.example.shichaonie.eduassist.UserListUtils.UserAdapter;
import com.example.shichaonie.eduassist.UserListUtils.UserLoader;
import com.example.shichaonie.eduassist.Utils.ConstantContract;

import java.util.ArrayList;
import java.util.Iterator;

import static android.content.Context.MODE_PRIVATE;
import static com.example.shichaonie.eduassist.R.string.searchUser;
import static com.example.shichaonie.eduassist.R.string.tel;

/**
 * Created by Shichao Nie on 2017/1/23.
 */

public class UserListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<User>>{
    private View rootView;
    private EditText searchUser;
    private String keywords = null;
    private ProgressBar mProgressBar;
    private TextView emptyView;
    private FragmentManager fm;

    public UserListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.user_list_fragment, container, false);
        searchUser = (EditText) rootView.findViewById(R.id.user_list_search);
        TextView searchView = (TextView) rootView.findViewById(R.id.user_list_search_view);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keywords = searchUser.getText().toString();

                if (getLoaderManager().getLoader(0) == null) {
                    getLoaderManager().initLoader(0, null, UserListFragment.this).forceLoad();
                } else {
                    getLoaderManager().restartLoader(0, null, UserListFragment.this);
                }

            }
        });

        searchUser.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                keywords = searchUser.getText().toString();

                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if(getLoaderManager().getLoader(0) == null){
                        getLoaderManager().initLoader(0, null, UserListFragment.this).forceLoad();
                    }else {
                        getLoaderManager().restartLoader(0, null, UserListFragment.this);
                    }
                }
                return true;
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.user_list_swip_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLoaderManager().restartLoader(0, null, UserListFragment.this).forceLoad();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getLoaderManager().initLoader(0, null, this).forceLoad();

        return rootView;
    }

    @Override
    public Loader<ArrayList<User>> onCreateLoader(int id, Bundle args) {
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.user_list_progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        emptyView = (TextView) rootView.findViewById(R.id.user_list_empty_view);
        emptyView.setVisibility(View.GONE);
        return new UserLoader(getContext(), keywords);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<User>> loader, ArrayList<User> data) {
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.user_list_progress_bar);
        mProgressBar.setVisibility(View.GONE);
        updateUI(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<User>> loader) {
        updateUI(new ArrayList<User>());
    }

    private void updateUI(final ArrayList<User> users){

        SharedPreferences sp = getActivity().getSharedPreferences("userInfo", MODE_PRIVATE);
        String userId = sp.getString(ConstantContract.SP_USER_ID, null);
        Iterator<User> iter = users.iterator();

        while (iter.hasNext()){
            if(Integer.toString(iter.next().getId()).equals(userId)){
                iter.remove(); //remove user himself
            }
        }

        emptyView = (TextView) rootView.findViewById(R.id.user_list_empty_view);
        emptyView.setText(R.string.noData);
        if(users == null || users.isEmpty()){
            emptyView.setVisibility(View.VISIBLE);
        }else {
            emptyView.setVisibility(View.GONE);
        }

        final UserAdapter adapter = new UserAdapter(getContext(), users);

        ListView userList = (ListView) rootView.findViewById(R.id.user_list_list);
        userList.setAdapter(adapter);

    }
}
