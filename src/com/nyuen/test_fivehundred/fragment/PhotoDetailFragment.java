package com.nyuen.test_fivehundred.fragment;

import java.util.Arrays;

import com.nyuen.test_fivehundred.MainActivity;
import com.nyuen.test_fivehundred.R;
import com.nyuen.test_fivehundred.adapter.PhotoDetailAdapter;
import com.nyuen.test_fivehundred.api.ApiHelper;
import com.nyuen.test_fivehundred.structure.CommentResponse;
import com.nyuen.test_fivehundred.util.ImageFetcher;
import com.nyuen.test_fivehundred.util.UIUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

public class PhotoDetailFragment extends ListFragment implements AbsListView.OnScrollListener {
    
    private static final String TAG = MainActivity.class.getSimpleName();

    private PhotoDetailAdapter mPhotoDetailAdapter;
    private ImageFetcher mImageFetcher;
    
    private boolean mLoading = false;
    private View mHeaderView;
    private View mLoadingView;
    private int mPage = 1;
    private int mPhotoId;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        
        mImageFetcher = UIUtils.getImageFetcher(getActivity());
        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.photo_header, null);
        mLoadingView = LayoutInflater.from(getActivity()).inflate(R.layout.loading_footer, null);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().addFooterView(mLoadingView);
        getListView().addHeaderView(mHeaderView);
        new LoadPhotoDetailTask().execute();
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_main, menu);
    }
      
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
        // TODO Auto-generated method stub
        boolean loadMore = firstVisibleItem + visibleItemCount + 2 >= totalItemCount;
        
        if(loadMore && !mLoading) {
            new LoadPhotoDetailTask().execute();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING
                || scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            mImageFetcher.setPauseWork(true);
        } else {
            mImageFetcher.setPauseWork(false);
        }
    }
    
    private void updateList(CommentResponse response) {
        if(mPage == 1) {
            mPhotoDetailAdapter = new PhotoDetailAdapter(getActivity(), mImageFetcher);
            //TODO
            //mPhotoDetailAdapter.setDetails(response);
            mPhotoDetailAdapter.setComments(Arrays.asList(response.comments));
            setListAdapter(mPhotoDetailAdapter);    
            getListView().setOnScrollListener(this);
        } else {   
            mPhotoDetailAdapter.appendComments(Arrays.asList(response.comments));
            mPhotoDetailAdapter.notifyDataSetChanged();
        }
    }
    
    private class LoadPhotoDetailTask extends AsyncTask<Void, Void, CommentResponse> {
        protected void onPreExecute() {
            mLoading = true;
            mLoadingView.setVisibility(View.VISIBLE);
        }

        protected CommentResponse doInBackground(Void... params) {
            return ApiHelper.getComments(mPhotoId, mPage);
        }

        protected void onPostExecute(CommentResponse response) {
            mLoadingView.setVisibility(View.GONE);
            if(response != null) {
                //updateList(response);
                mPage++;
                mLoading = false;
            }
        }
    }

}
