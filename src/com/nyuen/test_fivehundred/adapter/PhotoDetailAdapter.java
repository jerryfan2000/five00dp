package com.nyuen.test_fivehundred.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nyuen.test_fivehundred.R;
import com.nyuen.test_fivehundred.structure.Comment;
import com.nyuen.test_fivehundred.structure.CommentResponse;
import com.nyuen.test_fivehundred.structure.Photo;
import com.nyuen.test_fivehundred.structure.User;
import com.nyuen.test_fivehundred.util.ImageFetcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoDetailAdapter extends BaseAdapter {
    
    private final LayoutInflater mInflater;
    private final ImageFetcher mImageFetcher;
    
    private Photo mPhoto;
    private List<Comment> mComments;
    
    public PhotoDetailAdapter(Context context, ImageFetcher imageFetcher) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        
        mImageFetcher = imageFetcher;
        mInflater = LayoutInflater.from(context);
        mComments = new ArrayList<Comment>();
    }

    @Override
    public int getCount() {
        return mComments.size();
    }

    @Override
    public Comment getItem(int position) {
        return mComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    public void setDetails(CommentResponse response) {
        
    }
    
    public void setComments(List<Comment> comments) {
        mComments.clear();
        appendComments(comments);
    }
    
    public void appendComments(List<Comment> comments) {
        for (Comment c : comments) {
            mComments.add(c);
        }
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoDetailHolder holder;
        
        // TODO Auto-generated method stub
        if (convertView == null) {
            holder = new PhotoDetailHolder();
            convertView = mInflater.inflate(R.layout.comment_list_item, null, false);
            
            holder.commentUserPhotoView = (ImageView) convertView.findViewById(R.id.commentUserPhotoView);
            holder.likeImageView = (ImageView) convertView.findViewById(R.id.likeImageView);
            
            holder.commentBodyView = (TextView) convertView.findViewById(R.id.commentBodyView);
            holder.commentUserNameView = (TextView) convertView.findViewById(R.id.commentUserNameView);
            holder.likeCountView = (TextView) convertView.findViewById(R.id.likeCountView);
            
            convertView.setTag(holder);
        } else {
            holder = (PhotoDetailHolder) convertView.getTag();
        }
        
        //holder.commentUserPhotoView
        Comment comment = getItem(position);
        User user = getItem(position).user;
        mImageFetcher.loadImage(user.userpic_url, holder.commentUserPhotoView, R.drawable.ic_userpic);
        
        holder.commentBodyView.setText(comment.body);
        holder.commentUserNameView.setText(user.fullname);
        //holder.likeCountView.setText(comment.);
        
        return convertView;
    }
        
    private class PhotoDetailHolder {
        ImageView commentUserPhotoView, likeImageView;
        TextView likeCountView, commentUserNameView, commentBodyView;
    }
 
}
