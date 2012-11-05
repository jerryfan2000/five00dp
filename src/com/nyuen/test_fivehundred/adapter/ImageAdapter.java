package com.nyuen.test_fivehundred.adapter;

import com.nyuen.test_fivehundred.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        return mPhotos.length;
    }

    public Object getItem(int position) {
        return mPhotos[position]; 
    }
    
    public void setPhotos(Bitmap[] photos) {
        mPhotos = photos;
    }

    public long getItemId(int position) {
        return position;    
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageHolder holder;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.two_by_two, null, false);
            holder = new ImageHolder();
            holder.view[0] = (ImageView) convertView.findViewById(R.id.imageView1);
            holder.view[1] = (ImageView) convertView.findViewById(R.id.imageView2);
            holder.view[2] = (ImageView) convertView.findViewById(R.id.imageView3);
            holder.view[3] = (ImageView) convertView.findViewById(R.id.imageView4);
        } else {
            holder = (ImageHolder) convertView.getTag();
        }
        
        for(int i = 0; i < getCount(); i++ ) {
            holder.view[i].setImageBitmap(mPhotos[i]);
        }
        
        return convertView;
    }

    private class ImageHolder {
        ImageView[] view;
    }
}
