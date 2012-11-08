package com.nyuen.test_fivehundred.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nyuen.test_fivehundred.R;
import com.nyuen.test_fivehundred.structure.ImagePatternContainer;
import com.nyuen.test_fivehundred.structure.ImagePatternContainer.Pattern;
import com.nyuen.test_fivehundred.structure.Photo;
import com.nyuen.test_fivehundred.util.ImageFetcher;

import android.content.ClipData.Item;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private ImageFetcher mImageFetcher;
    private int mImageWidth;
    private int mListItemCount;
    private int photoCount;

    private List<Photo> mPhotos;    
    private List<ImagePatternContainer> mContainers;
    private List<Pattern> mPatterns;
    private List<ImageHolder> mHolders;
    
    private class ImageHolder {
        ImageView[] view;
    }
    
    public ImageAdapter(Context context, ImageFetcher imageFetcher) {
        mContext = context;    
        mImageFetcher = imageFetcher;
        mInflater = LayoutInflater.from(context);
        calculateItemSize(context);
        
        mPatterns = Pattern.getPatternList();
        mContainers = new ArrayList<ImagePatternContainer>();
        mHolders = new ArrayList<ImageHolder>();
    }
    
    private void calculateItemSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        mImageWidth = width / 2;
    }

    public int getCount() {
        return mListItemCount;
    }

    public Object getItem(int position) {
        return position; 
    }
    
    public void setPhotos(List<Photo> photos) {
        mPhotos = photos;
        photoCount = 0;// photo.length;
        
        Iterator<Pattern> iterator = mPatterns.iterator();
        while (iterator.hasNext()) {
            Pattern p = iterator.next();
            Log.d("setPhotos", "List: " + mListItemCount + " Pattern size: " + p.getCount());
            ImagePatternContainer c = new ImagePatternContainer(p);
            
            mContainers.add(c);
            
            for(int i = 0; i < p.getCount(); i++) {
                mContainers.get(mListItemCount).addPhotoID(photoCount);
                photoCount++;
                Log.d("setPhotos", "Load Photo: " + photoCount);
            }
            
            mListItemCount++;
        }
        Log.d("ImageAdapter", "List: " + mListItemCount + " Photo: " + photoCount);
    }
    
    public long getItemId(int position) {
        return position;    
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {  


        Iterator<ImagePatternContainer> iterator = mContainers.iterator();
        while (iterator.hasNext()) {
            ImageHolder holder = new ImageHolder();
            if(convertView == null) {
                convertView = mInflater.inflate(R.layout.two_by_two, null, false);


                ImagePatternContainer ipc = iterator.next();

                holder.view = new ImageView[ipc.getPattern().getCount()];

                //temp
                int[] viewId =  {R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4};

                for(int i = 0; i < holder.view.length; i++) {
                    holder.view[i] = (ImageView) convertView.findViewById(viewId[i]);
                    holder.view[i].setLayoutParams(new FrameLayout.LayoutParams(mImageWidth, mImageWidth));
                }

                convertView.setTag(holder);

            }else {
                holder = (ImageHolder) convertView.getTag();
            }

            mHolders.add(holder);

        } 

        for(int j = 0; j < mListItemCount; j++){
            ImageView[] iv = mHolders.get(j).view;
            ImagePatternContainer ipc = mContainers.get(j);
            for(int i = 0; i < iv.length; i++) {
                List<Integer> li = ipc.getPhotosID();
                Log.d("ImageAdapter", "Fetch image: " + li.get(i));
                mImageFetcher.loadImage(mPhotos.get(li.get(i)).getImage_url(), iv[i]);
            }

        }

        
        
        return convertView;
    }

    
}
