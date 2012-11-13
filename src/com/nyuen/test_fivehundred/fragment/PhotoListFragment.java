package com.nyuen.test_fivehundred.fragment;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nyuen.test_fivehundred.PhotoDetailActivity;
import com.nyuen.test_fivehundred.R;
import com.nyuen.test_fivehundred.adapter.PhotoAdapter;
import com.nyuen.test_fivehundred.api.ApiHelper;
import com.nyuen.test_fivehundred.structure.Photo;
import com.nyuen.test_fivehundred.structure.PhotoResponse;
import com.nyuen.test_fivehundred.util.ImageFetcher;
import com.nyuen.test_fivehundred.util.UIUtils;

@SuppressLint("NewApi")
public class PhotoListFragment extends ListFragment implements AbsListView.OnScrollListener {
    
    private static final String TAG = PhotoListFragment.class.getSimpleName();
    public final int IMAGE_PER_PAGE = 15;
    public static final int IMAGE_SIZE = 4;
    
    private PhotoAdapter mImageAdapter;
    private ImageFetcher mImageFetcher;
    
    private boolean mLoading = false;
    private View mLoadingView;
    private int mPage = 1;
    private String mFeature ;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        
        getActivity().getActionBar().setDisplayUseLogoEnabled(true);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);
        
        mFeature = "Popular";
        String[] featureString = {getString(R.string.popular), getString(R.string.editor), getString(R.string.upcoming), getString(R.string.fresh)};
        ArrayAdapter<String> featureOptionAdapter = new ArrayAdapter<String>(getActivity(), R.layout.feature_action_bar, featureString);
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getActivity().getActionBar().setListNavigationCallbacks(featureOptionAdapter, new OnNavigationListener() {
            
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                mPage = 1;
                switch (itemPosition) {
                case 0:
                    mFeature = "popular";
                    break;
                case 1:
                    mFeature = "editors";
                    break;
                case 2:
                    mFeature = "upcoming";
                    break;
                case 3:
                    mFeature = "fresh_today";
                    break;
                }
                mImageAdapter = new PhotoAdapter(getActivity(), mImageFetcher);
                new LoadPhotoTask().execute();
                //mImageAdapter.
                return false;
            }
        });
        
        mImageFetcher = UIUtils.getImageFetcher(getActivity());
        mLoadingView = LayoutInflater.from(getActivity()).inflate(R.layout.loading_footer, null);
        mImageAdapter = new PhotoAdapter(getActivity(), mImageFetcher);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().addFooterView(mLoadingView);
        
        if(UIUtils.isNetworkAvailable(getActivity()))
            new LoadPhotoTask().execute();
        else {
            ((ProgressBar) getActivity().findViewById(R.id.emptyProgressBar)).setVisibility(View.GONE);
            ((TextView) getActivity().findViewById(R.id.emptyErrorView)).setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_main, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_settings:
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) { 
        boolean loadMore = firstVisibleItem + visibleItemCount + 1 >= totalItemCount;
        
        if(loadMore && !mLoading) {
            new LoadPhotoTask().execute();
        }
    }
    
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
		// Pause disk cache access to ensure smoother scrolling
		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING
				|| scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
			mImageFetcher.setPauseWork(true);
		} else {
			mImageFetcher.setPauseWork(false);
		}
    }  
    
    private void updateList(PhotoResponse response) {
        if(mPage == 1) {
            mImageAdapter.setPhotos(Arrays.asList(response.photos));
            
            setListAdapter(mImageAdapter);    
            getListView().setOnScrollListener(this);
        } else {   
            mImageAdapter.appendPhotos(Arrays.asList(response.photos));
            mImageAdapter.notifyDataSetChanged();
        }
    }
    
    private class LoadPhotoTask extends AsyncTask<Void, Void, PhotoResponse> {
        protected void onPreExecute() {
            mLoading = true;
            mLoadingView.setVisibility(View.VISIBLE);
            if(mPage == 1) {
                getActivity().findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
                getActivity().findViewById(android.R.id.list).setVisibility(View.INVISIBLE);
            }
        }

        protected PhotoResponse doInBackground(Void... params) {
            return ApiHelper.getPhotoStream(mFeature, IMAGE_PER_PAGE, IMAGE_SIZE, mPage);
        }

        protected void onPostExecute(PhotoResponse response) {
            mLoadingView.setVisibility(View.GONE);
            getActivity().findViewById(android.R.id.empty).setVisibility(View.GONE);
            getActivity().findViewById(android.R.id.list).setVisibility(View.VISIBLE);
            mLoading = false;
            if(response != null) {
                updateList(response);
                mPage++;
                
            } else {
               this.cancel(false);
               mLoadingView.setVisibility(View.GONE);
            }
        }
    }
}
