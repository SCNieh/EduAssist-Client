package com.example.shichaonie.eduassist.MainActivityFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shichaonie.eduassist.MainActivityFragments.UserInfoFragmentUtil.UserInfoLoader;
import com.example.shichaonie.eduassist.R;
import com.example.shichaonie.eduassist.UserData.User;
import com.example.shichaonie.eduassist.Utils.ConstantContract;
import com.example.shichaonie.eduassist.Utils.ConvertUtil;

import java.util.concurrent.CopyOnWriteArrayList;

import static android.R.attr.dashGap;
import static android.R.attr.data;
import static android.R.attr.numberPickerStyle;
import static android.R.attr.updatePeriodMillis;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.view.View.GONE;

/**
 * Created by Shichao Nie on 2017/1/4.
 */

public class UserInfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<User>{
    public View rootView;
    public FragmentManager fm;
    public SharedPreferences sp;
    public String userId = null;

    public UserInfoFragment(){

    }

    public void iniId(){
        userId = sp.getString(ConstantContract.SP_USER_ID, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.user_info_fragment, container, false);

        sp = getActivity().getSharedPreferences(ConstantContract.SP_TAG, Context.MODE_PRIVATE);
        fm = getFragmentManager();
        ImageView imageView = (ImageView) rootView.findViewById(R.id.user_info_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.popBackStack();
                getActivity().findViewById(R.id.camera_fab).setVisibility(View.VISIBLE);
            }
        });
        iniId();
        getLoaderManager().initLoader(0, null, this).forceLoad();
        return rootView;
    }

    @Override
    public Loader<User> onCreateLoader(int id, Bundle args) {
        return new UserInfoLoader(getContext(), userId);
    }

    @Override
    public void onLoadFinished(Loader<User> loader, User data) {
        updateUI(data);
    }

    @Override
    public void onLoaderReset(Loader<User> loader) {
        updateUI(null);
    }

    public void updateUI(User data){
        TextView userInfoName = (TextView) rootView.findViewById(R.id.user_info_name);
        TextView userInfoTitle = (TextView) rootView.findViewById(R.id.user_info_title);
        TextView userInfoId = (TextView) rootView.findViewById(R.id.user_info_id);
        TextView userInfoEmail = (TextView) rootView.findViewById(R.id.user_info_email);
        TextView userInfoPrivateMode = (TextView) rootView.findViewById(R.id.user_info_private_mode);
        TextView userInfoSelfIntro = (TextView) rootView.findViewById(R.id.user_info_self_intro);

        if(data != null){
            userInfoName.setText(data.getmName());
            userInfoTitle.setText(ConvertUtil.toTitle(data.getTitle()));
            userInfoId.setText(Integer.toString(data.getId()));
            if(data.getEmail() == null || data.getEmail().isEmpty() || data.getEmail().equals("null")){
                userInfoEmail.setVisibility(GONE);
            }else {
                userInfoEmail.setText(data.getEmail());
            }
            userInfoPrivateMode.setText(ConvertUtil.toPrivateMode(data.getmPrivateCode()));
            if(data.getmSelfIntro() == null || data.getmSelfIntro().isEmpty() || data.getmSelfIntro().equals("null")){
                userInfoSelfIntro.setText("无");
            }else {
                userInfoSelfIntro.setText(data.getmSelfIntro());
            }
        }


    }
}
