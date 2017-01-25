package com.example.shichaonie.eduassist.MainActivityFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shichaonie.eduassist.R;


/**
 * Created by Shichao Nie on 2016/12/25.
 */

public class TitleFragment extends Fragment{
    public TitleFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.title_fragment, container, false);
        final EditText searchEdit = (EditText) rootView.findViewById(R.id.search_edit);
        searchEdit.clearFocus();

        final String searchText = searchEdit.getText().toString();
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    ContentFragment contentFragment = (ContentFragment) getFragmentManager().findFragmentById(R.id.content_fragment);
                    contentFragment.keywords = searchText;
                    contentFragment.getLoaderManager().restartLoader(0, null, contentFragment);
                }
                return true;
            }
        });

        return rootView;
    }


}
