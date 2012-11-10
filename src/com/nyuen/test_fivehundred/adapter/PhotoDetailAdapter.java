package com.nyuen.test_fivehundred.adapter;

import java.util.List;

import com.nyuen.test_fivehundred.R;
import com.nyuen.test_fivehundred.structure.Comment;
import com.nyuen.test_fivehundred.structure.CommentResponse;
import com.nyuen.test_fivehundred.structure.Photo;
import com.nyuen.test_fivehundred.util.ImageFetcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;

public class PhotoDetailAdapter extends BaseAdapter {
    
    private final LayoutInflater mInflater;
    private final ImageFetcher mImageFetcher;
    
    private Photo mPhoto;
    private List<Comment> mComments;
    
    private int mListItemCount;
    
    public PhotoDetailAdapter(Context context, ImageFetcher imageFetcher) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        
        mImageFetcher = imageFetcher;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mListItemCount;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public void setDetails(CommentResponse response) {
        // TODO Auto-generated method stub
        
    }
    
    public void setComments(List<Comment> asList) {
        // TODO Auto-generated method stub
        
    }
    
    public void appendComments(List<Comment> asList) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoDetailHolder holder;
        
        // TODO Auto-generated method stub
        if (convertView == null) {
            holder = new PhotoDetailHolder();
            convertView = mInflater.inflate(R.layout.comment_list_item, null, false);
        }
        
        return null;
    }
        
    private class PhotoDetailHolder {
        
    }
 
}
