package com.nyuen.five00dp.fragment;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.nyuen.five00dp.R;
import com.nyuen.five00dp.api.ApiHelper;
import com.nyuen.five00dp.structure.User;
import com.nyuen.five00dp.util.ImageFetcher;
import com.nyuen.five00dp.util.UIUtils;

public class ProfileFragment extends SherlockFragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private ImageFetcher mImageFetcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        
        mImageFetcher = UIUtils.getImageFetcher(getActivity());
       
        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Profile");
        }
        
        getSherlockActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.profile_fragment, container, false);
        return inflater.inflate(R.layout.fragment_coming_soon, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new LoadProfileTask().execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_photo_details, menu);
    }  


    @Override
    public void onPause() {
        super.onPause();
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }
    
    
    private class LoadProfileTask extends AsyncTask<Void, Void, User> {
        protected void onPreExecute() {
            
        }

        protected User doInBackground(Void... params) {
            return ApiHelper.getProfile(501056);
        }

        protected void onPostExecute(User response) {

        }
    }
}
