package com.nyuen.five00dp.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nyuen.five00dp.R;
import com.nyuen.five00dp.structure.Comment;
import com.nyuen.five00dp.structure.CommentResponse;
import com.nyuen.five00dp.structure.Photo;
import com.nyuen.five00dp.structure.User;
import com.nyuen.five00dp.util.ImageFetcher;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
    
    private List<Comment> mComments;
    
    public PhotoDetailAdapter(Context context, ImageFetcher imageFetcher) {
               
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
        
        if (convertView == null) {
            holder = new PhotoDetailHolder();
            convertView = mInflater.inflate(R.layout.comment_list_item, null, false);
            
            holder.commentUserPhotoView = (ImageView) convertView.findViewById(R.id.commentUserPhotoView);
            holder.imageViewUserAwesome = (ImageView) convertView.findViewById(R.id.imageViewUserStatus);
            
            holder.commentBodyView = (TextView) convertView.findViewById(R.id.commentBodyView);
            holder.commentBodyView.setMovementMethod(LinkMovementMethod.getInstance());
            holder.commentUserNameView = (TextView) convertView.findViewById(R.id.commentUserNameView);
            holder.likeCountView = (TextView) convertView.findViewById(R.id.likeCountView);
            
            convertView.setTag(holder);
        } else {
            holder = (PhotoDetailHolder) convertView.getTag();
        }
        
        Comment comment = getItem(position);
        User user = getItem(position).user;
        mImageFetcher.loadImage(user.userpic_url, holder.commentUserPhotoView, R.drawable.ic_userpic);
        
        if(user.upgrade_status > 0) {
            if(user.upgrade_status == 1)
                holder.imageViewUserAwesome.setImageResource(R.drawable.ic_plus);
            holder.imageViewUserAwesome.setVisibility(View.VISIBLE);
        }
        
        holder.commentBodyView.setText(Html.fromHtml(comment.body));
        holder.commentUserNameView.setText(user.fullname);
        holder.likeCountView.setText(""+0);
        
        return convertView;
    }
        
    private class PhotoDetailHolder {
        ImageView commentUserPhotoView, imageViewUserAwesome;
        TextView likeCountView, commentUserNameView, commentBodyView;
    }
 
}
