package com.nyuen.test_fivehundred.adapter;

import com.nyuen.test_fivehundred.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;
    
    private Bitmap[] mPhotos;

    public ImageAdapter(Context context) {
        mContext = context;    
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return 1;
    }

    public Object getItem(int position) {
        return position; 
    }
    
    public void setPhotos(Bitmap[] photos) {
        mPhotos = photos;
    }

    public long getItemId(int position) {
        return position;    
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageHolder holder;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.two_by_two, null, false);
//            convertView = mInflater.inflate(R.layout.relative_two_by_two, null, false);
            holder = new ImageHolder();
            holder.view = new ImageView[4];
            holder.view[0] = (ImageView) convertView.findViewById(R.id.imageView1);
            holder.view[1] = (ImageView) convertView.findViewById(R.id.imageView2);
            holder.view[2] = (ImageView) convertView.findViewById(R.id.imageView3);
            holder.view[3] = (ImageView) convertView.findViewById(R.id.imageView4);
            
//            holder.view[0] = (ImageView) convertView.findViewById(R.id.imageView5);
//            holder.view[1] = (ImageView) convertView.findViewById(R.id.imageView6);
//            holder.view[2] = (ImageView) convertView.findViewById(R.id.imageView7);
//            holder.view[3] = (ImageView) convertView.findViewById(R.id.imageView8);
//            
            
            convertView.setTag(holder);
        } else {
            holder = (ImageHolder) convertView.getTag();
        }
        
        for(int i = 0; i < mPhotos.length; i++ ) {
            holder.view[i].setImageBitmap(mPhotos[i]);
            //holder.view[i].setLayoutParams(new LayoutParams(holder.view[i].getMeasuredWidth(), holder.view[i].getMeasuredWidth()));
        }
        
        return convertView;
    }

    private class ImageHolder {
        ImageView[] view;
    }
}
