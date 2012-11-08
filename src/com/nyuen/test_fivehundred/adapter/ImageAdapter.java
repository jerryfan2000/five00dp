package com.nyuen.test_fivehundred.adapter;

import com.nyuen.test_fivehundred.R;
import com.nyuen.test_fivehundred.structure.Photo;
import com.nyuen.test_fivehundred.util.ImageFetcher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private ImageFetcher mImageFetcher;
    private int mImageWidth;

    private Photo[] mPhotos;    

    public ImageAdapter(Context context, ImageFetcher imageFetcher) {
        mContext = context;    
        mImageFetcher = imageFetcher;
        mInflater = LayoutInflater.from(context);
        calculateItemSize(context);
    }
    
    private void calculateItemSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        //display.getSize(size);
        int width = display.getWidth();
        //int columnNum = context.getResources().getInteger(R.integer.grid_column_num);

        mImageWidth = width / 2;
    }

    public int getCount() {
        return 1;
    }

    public Object getItem(int position) {
        return position; 
    }
    
    public void setPhotos(Photo[] photo) {
        mPhotos = photo;
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
            
            holder = new ImageHolder();
            holder.view = new ImageView[4];
            holder.view[0] = (ImageView) convertView.findViewById(R.id.imageView1);
            holder.view[1] = (ImageView) convertView.findViewById(R.id.imageView2);
            holder.view[2] = (ImageView) convertView.findViewById(R.id.imageView3);
            holder.view[3] = (ImageView) convertView.findViewById(R.id.imageView4);
                
            convertView.setTag(holder);
        } else {
            holder = (ImageHolder) convertView.getTag();
        }
        
        for(int i = 0; i < holder.view.length; i++ ) {
            mImageFetcher.loadImage(getItem(position), holder.view[i]);
            
            //holder.view[i].setImageBitmap(mPhotos[i]);
            //holder.view[i].setLayoutParams(new LayoutParams(holder.view[i].getMeasuredWidth(), holder.view[i].getMeasuredWidth()));
        }
        
        return convertView;
    }

    private class ImageHolder {
        ImageView[] view;
    }
}
