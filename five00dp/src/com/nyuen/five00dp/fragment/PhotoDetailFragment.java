package com.nyuen.five00dp.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nyuen.five00dp.R;
import com.nyuen.five00dp.adapter.PhotoDetailAdapter;
import com.nyuen.five00dp.api.ApiHelper;
import com.nyuen.five00dp.structure.CommentResponse;
import com.nyuen.five00dp.structure.Photo;
import com.nyuen.five00dp.structure.PhotoDetailResponse;
import com.nyuen.five00dp.util.DateHelper;
import com.nyuen.five00dp.util.ImageFetcher;
import com.nyuen.five00dp.util.UIUtils;

import java.util.Arrays;

public class PhotoDetailFragment extends SherlockListFragment implements AbsListView.OnScrollListener {

    private static final String TAG = PhotoDetailFragment.class.getSimpleName();

    public static final String INTENT_EXTRA_PHOTO = TAG + ".INTENT_EXTRA_PHOTO";

    private PhotoDetailAdapter mPhotoDetailAdapter;
    private ImageFetcher mImageFetcher;

    private boolean mLoadingComments = false;
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

        getSherlockActivity().getSupportActionBar().setTitle(mPhoto.name);
        getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSherlockActivity().getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSherlockActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        
        setListAdapter(mPhotoDetailAdapter);

        //        if(UIUtils.isNetworkAvailable(getActivity()))
        new LoadPhotoCommentsTask().execute();
        //        else {
        //            ((ProgressBar) getActivity().findViewById(R.id.emptyProgressBar)).setVisibility(View.GONE);
        //            ((TextView) getActivity().findViewById(R.id.emptyErrorView)).setVisibility(View.VISIBLE);
        //        }        
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

        if(loadMore && !mLoadingComments && (mPage <= mTotalPage)) {
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
        Button moreInfoButton = (Button) mHeaderView.findViewById(R.id.headerMoreInfoButton);
        
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

        moreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parent = (View)v.getParent();
                View infoBox = parent.findViewById(R.id.moreInfoBox);
                Drawable icon;
                if(infoBox.getVisibility() == View.GONE) {                
                    icon = v.getResources().getDrawable(R.drawable.ic_arrow_up);
                    parent.findViewById(R.id.moreInfoBox).setVisibility(View.VISIBLE);
                } else {
                    icon= v.getResources().getDrawable(R.drawable.ic_arrow_down);         
                    parent.findViewById(R.id.moreInfoBox).setVisibility(View.GONE);
                }
                ((Button) v).setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
            }
        });
    }

    private void updateDetails(PhotoDetailResponse response) {
        Photo photo = response.photo;        
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

    private class LoadPhotoDetailTask extends AsyncTask<Void, Void, PhotoDetailResponse> {
        protected void onPreExecute() {
            
        }

        protected PhotoDetailResponse doInBackground(Void... params) {
            return ApiHelper.getFullPhoto(mPhoto.id);
        }

        protected void onPostExecute(PhotoDetailResponse response) {
            if(response != null) {
                updateDetails(response);
            } 
        }
    }
    
    private class LoadPhotoCommentsTask extends AsyncTask<Void, Void, CommentResponse> {
        protected void onPreExecute() {
            mLoadingComments = true;
            mLoadingView.setVisibility(View.VISIBLE);
        }

        protected CommentResponse doInBackground(Void... params) {
            return ApiHelper.getComments(mPhoto.id, mPage);
        }

        protected void onPostExecute(CommentResponse response) {
            mLoadingView.setVisibility(View.GONE);
            mLoadingComments = false;
            if(response != null) {
                updateList(response);
                mPage++;
            }
        }
    }

}
