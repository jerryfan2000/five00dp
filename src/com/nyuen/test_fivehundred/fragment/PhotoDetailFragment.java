package com.nyuen.test_fivehundred.fragment;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nyuen.test_fivehundred.R;
import com.nyuen.test_fivehundred.adapter.PhotoDetailAdapter;
import com.nyuen.test_fivehundred.api.ApiHelper;
import com.nyuen.test_fivehundred.structure.CommentResponse;
import com.nyuen.test_fivehundred.structure.Photo;
import com.nyuen.test_fivehundred.util.DateHelper;
import com.nyuen.test_fivehundred.util.ImageFetcher;
import com.nyuen.test_fivehundred.util.UIUtils;

@SuppressLint("NewApi")
public class PhotoDetailFragment extends ListFragment implements AbsListView.OnScrollListener {
    
    private static final String TAG = PhotoDetailFragment.class.getSimpleName();
    
    public static final String INTENT_EXTRA_PHOTO = TAG + ".INTENT_EXTRA_PHOTO";

    private PhotoDetailAdapter mPhotoDetailAdapter;
    private ImageFetcher mImageFetcher;
    
    private boolean mLoading = false;
    private View mHeaderView;
    private View mLoadingView;
    private Photo mPhoto;
    private int mPage = 1;
    private int mTotalPage = 2;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        
        mImageFetcher = UIUtils.getImageFetcher(getActivity());
        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.photo_header, null);
        mLoadingView = LayoutInflater.from(getActivity()).inflate(R.layout.loading_footer, null);
        mPhoto = (Photo) getArguments().getParcelable(INTENT_EXTRA_PHOTO);
        mPhotoDetailAdapter = new PhotoDetailAdapter(getActivity(), mImageFetcher);
        
        getActivity().getActionBar().setTitle(mPhoto.name);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setDisplayUseLogoEnabled(true);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photo_detail_fragment, container, false);   
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().addFooterView(mLoadingView);
        getListView().addHeaderView(mHeaderView, null, false);
       
        updateHeaderView();
        
        if(UIUtils.isNetworkAvailable(getActivity()))
            new LoadPhotoCommentsTask().execute();
        else {
            ((ProgressBar) getActivity().findViewById(R.id.emptyProgressBar)).setVisibility(View.GONE);
            ((TextView) getActivity().findViewById(R.id.emptyErrorView)).setVisibility(View.VISIBLE);
        }        
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_photo_details, menu);
    }  
     
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            getActivity().finish();
            return true;
        case R.id.menu_share:
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareTitle = mPhoto.name;
            String shareBody = "http://500px.com/photo/" + mPhoto.id;
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareTitle);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "" + getString(R.string.share_via)));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
        boolean loadMore = firstVisibleItem + visibleItemCount + 2 >= totalItemCount;
        
        if(loadMore && !mLoading && (mPage <= mTotalPage)) {
            new LoadPhotoCommentsTask().execute();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING
                || scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            mImageFetcher.setPauseWork(true);
        } else {
            mImageFetcher.setPauseWork(false);
        }
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
    
    private void updateHeaderView() {
        ImageView headerPhotoView = (ImageView) mHeaderView.findViewById(R.id.headerPhotoView);
        ImageView headerUserPhotoView = (ImageView) mHeaderView.findViewById(R.id.headerUserPhotoView);
        TextView headerUserNameView = (TextView) mHeaderView.findViewById(R.id.headerUserNameView);
        TextView headerDescriptionView = (TextView) mHeaderView.findViewById(R.id.headerDescriptionView);
        TextView headerDateView = (TextView) mHeaderView.findViewById(R.id.headerDateView);
        TextView viewsCountView = (TextView) mHeaderView.findViewById(R.id.viewsCountView);
        TextView votesCountView = (TextView) mHeaderView.findViewById(R.id.votesCountView);
        TextView favsCountView = (TextView) mHeaderView.findViewById(R.id.favsCountView);
        TextView ratingView = (TextView) mHeaderView.findViewById(R.id.ratingView);

        mImageFetcher.loadImage(mPhoto.image_url, headerPhotoView);
        if(!mPhoto.user.userpic_url.equals("/graphics/userpic.png"))
            mImageFetcher.loadImage(mPhoto.user.userpic_url, headerUserPhotoView);
        headerUserNameView.setText(mPhoto.user.fullname);
        viewsCountView.setText("" + mPhoto.times_viewed + " " + getString(R.string.views));
        votesCountView.setText("" + mPhoto.votes_count + " " + getString(R.string.votes));
        favsCountView.setText("" + mPhoto.favorites_count + " " + getString(R.string.favorites));
        headerDescriptionView.setText(Html.fromHtml(mPhoto.description));
        headerDateView.setText(DateHelper.DateDifference(DateHelper.parseISO8601(mPhoto.created_at)));
        ratingView.setText("" + mPhoto.rating);
        
        headerPhotoView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://500px.com/photo/" + mPhoto.id));
                startActivity(intent);
            }
        });
    }
    
    private void updateList(CommentResponse response) {
        if(mPage == 1) {
            mPhotoDetailAdapter.setComments(Arrays.asList(response.comments));
            mTotalPage = response.total_pages;
            setListAdapter(mPhotoDetailAdapter);    
            getListView().setOnScrollListener(this);
        } else {   
            mPhotoDetailAdapter.appendComments(Arrays.asList(response.comments));
            mPhotoDetailAdapter.notifyDataSetChanged();
        }
    }
    
    private class LoadPhotoCommentsTask extends AsyncTask<Void, Void, CommentResponse> {
        protected void onPreExecute() {
            mLoading = true;
            mLoadingView.setVisibility(View.VISIBLE);
        }

        protected CommentResponse doInBackground(Void... params) {
            return ApiHelper.getComments(mPhoto.id, mPage);
        }

        protected void onPostExecute(CommentResponse response) {
            mLoadingView.setVisibility(View.GONE);
            if(response != null) {
                updateList(response);
                mPage++;
                mLoading = false;
            }
        }
    }

}
