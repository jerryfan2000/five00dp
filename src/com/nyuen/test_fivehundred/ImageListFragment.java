package com.nyuen.test_fivehundred;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.fivehundredpx.api.PxApi;
import com.google.gson.Gson;
import com.nyuen.test_fivehundred.adapter.ImageAdapter;
import com.nyuen.test_fivehundred.structure.PhotoResponse;
import com.nyuen.test_fivehundred.util.ImageFetcher;
import com.nyuen.test_fivehundred.util.UIUtils;

@SuppressLint("NewApi")
public class ImageListFragment extends ListFragment implements AbsListView.OnScrollListener {
    
    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageAdapter mImageAdapter;
    private ImageFetcher mImageFetcher;
    
    
    private boolean mLoading = false;
    private View mLoadingView;
    private int mPage = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        //setContentView(R.layout.activity_main);
        
        getActivity().getActionBar().setDisplayUseLogoEnabled(true);
        getActivity().getActionBar().setTitle("Popular");
        
        mImageFetcher = UIUtils.getImageFetcher(getActivity());
        mLoadingView = LayoutInflater.from(getActivity()).inflate(R.layout.loading_footer, null);
        
        Log.d(TAG, "Ran!!");
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().addFooterView(mLoadingView);
        new LoadPhotoTask().execute();
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
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
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) { 
        boolean loadMore = firstVisibleItem + visibleItemCount + 1 >= totalItemCount;
        
        if(loadMore && !mLoading) {
            new LoadPhotoTask().execute();
        }
    }
    
    private void updateList(PhotoResponse response) {
        Log.d(TAG, ""+mPage);
        if(mPage == 1) {
            mImageAdapter = new ImageAdapter(getActivity(), mImageFetcher);
            mImageAdapter.setPhotos(Arrays.asList(response.getPhotos()));
            setListAdapter(mImageAdapter);    
            getListView().setOnScrollListener(this);
        } else {   
            mImageAdapter.appendPhotos(Arrays.asList(response.getPhotos()));
            mImageAdapter.notifyDataSetChanged();
        }
    }
    
    public static PhotoResponse getPhotosResponse(int pageNumber) {
        PxApi pxapi = new PxApi(FiveHundred.CONSUMER_KEY);
        
        PhotoResponse photoResponse;
        try {               
            photoResponse = new Gson().fromJson(
                    pxapi.get("/photos?feature=popular&rpp=15&image_size=4&page="+pageNumber).toString(), 
                    PhotoResponse.class);
            
            Log.d(TAG, photoResponse.getPhotos()[0].image_url );
            
            return photoResponse;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }   
    }

    private class LoadPhotoTask extends AsyncTask<Void, Void, PhotoResponse> {
        protected void onPreExecute() {
            mLoading = true;
            mLoadingView.setVisibility(View.VISIBLE);
        }

        protected PhotoResponse doInBackground(Void... params) {
            return getPhotosResponse(mPage);
        }

        protected void onPostExecute(PhotoResponse response) {
            mLoadingView.setVisibility(View.GONE);
            if(response != null) {
                updateList(response);
                mPage++;
                mLoading = false;
            }
        }
    }
}
