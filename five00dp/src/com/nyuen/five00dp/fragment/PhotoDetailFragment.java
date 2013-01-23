package com.nyuen.five00dp.fragment;

import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nyuen.five00dp.ProfileActivity;
import com.nyuen.five00dp.R;
import com.nyuen.five00dp.adapter.CommentAdapter;
import com.nyuen.five00dp.api.ApiHelper;
import com.nyuen.five00dp.structure.Comment;
import com.nyuen.five00dp.structure.CommentResponse;
import com.nyuen.five00dp.structure.Photo;
import com.nyuen.five00dp.structure.Photo.Category;
import com.nyuen.five00dp.structure.PhotoDetailResponse;
import com.nyuen.five00dp.structure.PhotoResponse;
import com.nyuen.five00dp.util.AccountUtils;
import com.nyuen.five00dp.util.DateHelper;
import com.nyuen.five00dp.util.FontUtils;
import com.nyuen.five00dp.util.ImageFetcher;
import com.nyuen.five00dp.util.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class PhotoDetailFragment extends SherlockListFragment implements AbsListView.OnScrollListener {

    private static final String TAG = PhotoDetailFragment.class.getSimpleName();

    public static final String INTENT_EXTRA_PHOTO = TAG + ".INTENT_EXTRA_PHOTO";

    private CommentAdapter mPhotoDetailAdapter;
    private ImageFetcher mImageFetcher;

    private boolean mLoadingComments = false;
    private View mHeaderView;
    private View mHeaderExifView;
    private View mLoadingView;
    private View mUserActionBar;
    private Photo mPhoto;
    private int mPage = 1;
    private int mTotalPage = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        
        mImageFetcher = UIUtils.getImageFetcher(getActivity());
        mHeaderView = inflater.inflate(R.layout.header_photo_detail, null);
        mHeaderExifView = inflater.inflate(R.layout.header_photo_exif, null);
        mLoadingView = inflater.inflate(R.layout.list_footer, null);
        mPhoto = (Photo) getArguments().getParcelable(INTENT_EXTRA_PHOTO);
        mPhotoDetailAdapter = new CommentAdapter(getActivity(), mImageFetcher);

        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mPhoto.name);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
        }
        
        getSherlockActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_detail, container, false);   
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().addHeaderView(mHeaderView, null, false);
        getListView().addHeaderView(mHeaderExifView, null, false);
        getListView().addFooterView(mLoadingView);
        
        updateHeaderView();
        updateHeaderExifView();
        updateUserActionBar();
        
        setListAdapter(mPhotoDetailAdapter);

        new LoadPhotoDetailTask().execute();
    }

    private void updateUserActionBar() {
        if(AccountUtils.hasAccount(getActivity())) {
            mUserActionBar = getActivity().findViewById(R.id.userActionBar);
            mUserActionBar.setVisibility(View.VISIBLE);

            ImageButton btnLike = (ImageButton) getActivity().findViewById(R.id.btnPhotoLike);
            ImageButton btnFav = (ImageButton) getActivity().findViewById(R.id.btnPhotoFav);
            
            btnLike.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    new VotePhotoTask().execute();
                }
            });
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
        case R.id.menu_photo_share:
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

        if (loadMore && !mLoadingComments && (mPage <= mTotalPage)) {
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
        ImageView imageViewStatus = (ImageView) mHeaderView.findViewById(R.id.imageViewStatus);
        TextView headerUserNameView = (TextView) mHeaderView.findViewById(R.id.headerUserNameView);
        TextView headerDescriptionView = (TextView) mHeaderView.findViewById(R.id.headerDescriptionView);
        TextView viewsCountView = (TextView) mHeaderView.findViewById(R.id.viewsCountView);
        TextView votesCountView = (TextView) mHeaderView.findViewById(R.id.votesCountView);
        TextView favsCountView = (TextView) mHeaderView.findViewById(R.id.favsCountView);
        TextView ratingView = (TextView) mHeaderView.findViewById(R.id.ratingView);
        
        // Calculate min height for the photo
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int minHeight = mPhoto.height * width / mPhoto.width;
        
        headerPhotoView.setMinimumHeight(minHeight);
                
        mImageFetcher.loadImage(mPhoto.image_url, headerPhotoView);
        headerUserPhotoView.setOnClickListener(new OnClickListener() { 
            @Override
            public void onClick(View v) {
                Intent photoDetailIntent = new Intent(getActivity(), ProfileActivity.class);
                photoDetailIntent.putExtra(ProfileFragment.INTENT_PROFILE_ID, mPhoto.user.id);
                startActivity(photoDetailIntent);
            }
        });
        
        if (!mPhoto.user.userpic_url.equals("/graphics/userpic.png")) {
            mImageFetcher.loadImage(mPhoto.user.userpic_url, headerUserPhotoView, R.drawable.ic_userpic);
        }

        if (mPhoto.user.upgrade_status > 0) {
            if (mPhoto.user.upgrade_status == 1) {
                imageViewStatus.setImageResource(R.drawable.ic_plus);
            } else if (mPhoto.user.upgrade_status == 2) 
                imageViewStatus.setImageResource(R.drawable.ic_awesome);
            imageViewStatus.setVisibility(View.VISIBLE);
        } else {
            imageViewStatus.setVisibility(View.GONE);
        }
        
        headerUserNameView.setText(mPhoto.user.fullname);
        viewsCountView.setText(getString(R.string.num_views, mPhoto.times_viewed));
        votesCountView.setText(getString(R.string.num_votes, mPhoto.votes_count));
        favsCountView.setText(getString(R.string.num_favorites, mPhoto.favorites_count));
        if(!TextUtils.isEmpty(mPhoto.description)) {
            headerDescriptionView.setText(Html.fromHtml(mPhoto.description));
            headerDescriptionView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            headerDescriptionView.setVisibility(View.GONE);
        }
        ratingView.setText(String.valueOf(mPhoto.rating));

        FontUtils.setTypefaceRobotoLight(getActivity(), headerDescriptionView, ratingView);

        headerPhotoView.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse("http://500px.com/photo/" + mPhoto.id));
//                startActivity(intent);
                
//                PhotoDialogFragment photoDialog = new PhotoDialogFragment(mPhoto.image_url);           
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.setCustomAnimations(R.anim.cardstack_open_enter, R.anim.cardstack_open_exit);             
//                photoDialog.show(ft, "photo_dialog");  
                
                
                WebDialogFragment webDialog = new WebDialogFragment("http://mobilesyrup.com/");
                FragmentManager fm = getFragmentManager();         
                webDialog.show(fm, "photo_dialog");  
                
            }
        });
    }
    
    private void updateHeaderExifView() {
        Button moreInfoButton = (Button) mHeaderExifView.findViewById(R.id.buttonMoreInfo);

        moreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parent = (View)v.getParent();
                View infoBox = parent.findViewById(R.id.moreInfoBox);
                Drawable icon;
                if (infoBox.getVisibility() == View.GONE) {                
                    icon = v.getResources().getDrawable(R.drawable.ic_arrow_up);
                    parent.findViewById(R.id.moreInfoBox).setVisibility(View.VISIBLE);
                } else {
                    icon= v.getResources().getDrawable(R.drawable.ic_arrow_down);         
                    parent.findViewById(R.id.moreInfoBox).setVisibility(View.GONE);
                }
                ((Button) v).setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
            }
        });
        
        TextView textView;
        if (!TextUtils.isEmpty(mPhoto.camera)) {
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewCamera);
            textView.setText(mPhoto.camera);
            mHeaderExifView.findViewById(R.id.tableRowCamera).setVisibility(View.VISIBLE);
        }
        
        if (!TextUtils.isEmpty(mPhoto.lens)) {
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewLens);
            textView.setText(mPhoto.lens);
            mHeaderExifView.findViewById(R.id.tableRowLens).setVisibility(View.VISIBLE);
        }
        
        if (!TextUtils.isEmpty(mPhoto.focal_length)) {
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewFocalLength);
            textView.setText(mPhoto.focal_length);
            mHeaderExifView.findViewById(R.id.tableRowFocalLength).setVisibility(View.VISIBLE);
        }
        
        if (!TextUtils.isEmpty(mPhoto.shutter_speed)) {
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewShutterSpeed);
            textView.setText(mPhoto.shutter_speed);
            mHeaderExifView.findViewById(R.id.tableRowShutterSpeed).setVisibility(View.VISIBLE);
        }
        
        if (!TextUtils.isEmpty(mPhoto.aperture)) {
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewAperture);
            textView.setText(mPhoto.aperture);
            mHeaderExifView.findViewById(R.id.tableRowAperture).setVisibility(View.VISIBLE);
        }
        
        if (!TextUtils.isEmpty(mPhoto.iso)) {
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewISO);
            textView.setText(mPhoto.iso);
            mHeaderExifView.findViewById(R.id.tableRowISO).setVisibility(View.VISIBLE);
        }
        
        if (!TextUtils.isEmpty("" + mPhoto.category)) {
            String cat = Category.values()[mPhoto.category].getString();
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewCategory);
            textView.setText(cat);
            mHeaderExifView.findViewById(R.id.tableRowCategory).setVisibility(View.VISIBLE);
        }
        
        if (!TextUtils.isEmpty(mPhoto.created_at)) {
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewUploaded);
            textView.setText(DateHelper.DateDifference(mPhoto.created_at));
            mHeaderExifView.findViewById(R.id.tableRowUploaded).setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(mPhoto.taken_at)) {
            textView = (TextView) mHeaderExifView.findViewById(R.id.textViewUploaded);
            textView.setText(DateHelper.getHeaderDate(mPhoto.taken_at));
            mHeaderExifView.findViewById(R.id.tableRowUploaded).setVisibility(View.VISIBLE);
        }
        
//       Copy Right?

        
    }
    

    private void updateCommentsList(ArrayList<Comment> comments, int totalPages) {
        mPhotoDetailAdapter.appendComments(comments);
        mPhotoDetailAdapter.notifyDataSetChanged();
        mTotalPage = totalPages;
        getListView().setOnScrollListener(this);
    }

    private class VotePhotoTask extends AsyncTask<Void, Void, PhotoResponse> {

        protected PhotoResponse doInBackground(Void... params) {
            return ApiHelper.votePhoto(getActivity(), mPhoto.id, (!mPhoto.voted)? 1:0);
        }

        protected void onPostExecute(PhotoResponse response) {
            if(getActivity() == null)
                return;
            
            if (response != null) {
                mPhoto = response.photo;
            } 
        }
    }
    
    private class LoadPhotoDetailTask extends AsyncTask<Void, Void, PhotoDetailResponse> {

        protected PhotoDetailResponse doInBackground(Void... params) {
            return ApiHelper.getFullPhoto(mPhoto.id);
        }

        protected void onPostExecute(PhotoDetailResponse response) {
            if(getActivity() == null)
                return;
            
            if (response != null) {
                mPhoto = response.photo;
                updateHeaderView();
                updateHeaderExifView();
                updateCommentsList(response.comments, 2);
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
            if(getActivity() == null)
                return;
            
            mLoadingView.setVisibility(View.GONE);
            mLoadingComments = false;
            if (response != null) {
                updateCommentsList(response.comments, response.total_pages);
                mPage++;
            }
        }
    }

}
