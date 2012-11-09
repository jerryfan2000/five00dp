package com.nyuen.test_fivehundred.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nyuen.test_fivehundred.R;
import com.nyuen.test_fivehundred.structure.ImagePatternContainer;
import com.nyuen.test_fivehundred.structure.ImagePatternContainer.Pattern;
import com.nyuen.test_fivehundred.structure.Photo;
import com.nyuen.test_fivehundred.util.ImageFetcher;

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
    
    private int mPosition;
    
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
            ImageHolder holder = new ImageHolder();
            
            if(convertView == null) { 
                convertView = mInflater.inflate(R.layout.image_grid, null, false);
                ImagePatternContainer ipc = mContainers.get(position);
                RelativeLayout r = (RelativeLayout) convertView.findViewById(R.id.imageGrid);
                r.setLayoutParams(new AbsListView.LayoutParams(mImageWidth*2, mImageWidth*2));
                
                holder.view = new ImageView[4];//ipc.getPattern().getCount()];

                //temp
                int[] viewId =  {R.id.imageView0, R.id.imageView1, R.id.imageView2, R.id.imageView3};

                for(int i = 0; i < holder.view.length; i++) {
                    holder.view[i] = (ImageView) convertView.findViewById(viewId[i]);
                    //holder.view[i].setLayoutParams(new FrameLayout.LayoutParams(mImageWidth, mImageWidth));
                }

                convertView.setTag(holder);

            }else {
                holder = (ImageHolder) convertView.getTag();
            }

            mHolders.add(holder);


            ImageView[] iv = holder.view;
            ImagePatternContainer ipc = mContainers.get(position);
            Pattern pattern = ipc.getPattern();
            List<Integer> li = ipc.getPhotosID();
            for(int i = 0; i < 4; i++) {
            	if (i < li.size()) {
            		iv[i].setVisibility(View.VISIBLE);
	                Log.d("ImageAdapter", "Item: " + position + ", Fetch image: " + li.get(i) + " view: " + i);
	                String url = mPhotos.get(li.get(i)).getImage_url();
	                
	                if(pattern == Pattern.FOUR) {
	                    url.replace("4.jpg", "3.jpg");
	                }
	                
    	                mImageFetcher.loadImage(url, iv[i]);
                	} else {
            		iv[i].setVisibility(View.GONE);
            	}
            }
            
            RelativeLayout.LayoutParams param0 = new RelativeLayout.LayoutParams(mImageWidth, mImageWidth);            
            RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(mImageWidth, mImageWidth);
            RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(mImageWidth, mImageWidth);
            RelativeLayout.LayoutParams param3 = new RelativeLayout.LayoutParams(mImageWidth, mImageWidth);
              
            
            
            
            if (pattern == Pattern.ONE) {
                param0 = new RelativeLayout.LayoutParams(mImageWidth*2, mImageWidth*2);   
                
            	iv[0].setLayoutParams(param0);	
            } else if (pattern == Pattern.TWO_VERT) {
                param0 = new RelativeLayout.LayoutParams(mImageWidth,  mImageWidth*2);   
                param1 = new RelativeLayout.LayoutParams(mImageWidth, mImageWidth*2);
                
                param1.addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
   
            	iv[0].setLayoutParams(param0);
            	iv[1].setLayoutParams(param1);
            } else if (pattern == Pattern.TWO_HOR) {
                param0 = new RelativeLayout.LayoutParams(mImageWidth*2,  mImageWidth);   
                param1 = new RelativeLayout.LayoutParams(mImageWidth*2, mImageWidth);
                
                param1.addRule(RelativeLayout.BELOW, R.id.imageView0);
   
                iv[0].setLayoutParams(param0);
                iv[1].setLayoutParams(param1);
            } else if (pattern == Pattern.THREE_HOR) {
                param0 = new RelativeLayout.LayoutParams(mImageWidth*2,  mImageWidth);   
                
                param1.addRule(RelativeLayout.BELOW, R.id.imageView0);
                param2.addRule(RelativeLayout.BELOW, R.id.imageView0);
                param2.addRule(RelativeLayout.RIGHT_OF, R.id.imageView1);
                
            	iv[0].setLayoutParams(param0);
            	iv[1].setLayoutParams(param1);
            	iv[2].setLayoutParams(param2);
            } else if (pattern == Pattern.THREE_VERT) {
                param0 = new RelativeLayout.LayoutParams(mImageWidth,  mImageWidth*2);
                
                param1.addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
                param2.addRule(RelativeLayout.BELOW, R.id.imageView1);
                param2.addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
                
                iv[0].setLayoutParams(param0);
                iv[1].setLayoutParams(param1);
                iv[2].setLayoutParams(param2);
            } else {
                param1.addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
                param2.addRule(RelativeLayout.BELOW, R.id.imageView0);
                param3.addRule(RelativeLayout.BELOW, R.id.imageView1);
                param3.addRule(RelativeLayout.RIGHT_OF, R.id.imageView2);
                
            	iv[0].setLayoutParams(param0);
            	iv[1].setLayoutParams(param1);
            	iv[2].setLayoutParams(param2);
            	iv[3].setLayoutParams(param3);
            }
            
//            iv[0].setBackgroundResource(R.drawable.picture_frame);
//            iv[0].setPadding(4, 4, 4, 4);
//            //iv[1].setBackgroundResource(R.drawable.picture_frame);
//            iv[1].setPadding(1, 1, 1, 1);
//            //iv[2].setBackgroundResource(R.drawable.picture_frame);
//            iv[2].setPadding(1, 4, 4, 4);
//            //iv[3].setBackgroundResource(R.drawable.picture_frame);
//            iv[3].setPadding(4, 4, 4, 4);
            
        return convertView;
    }
    
//    public View getView(int position, View convertView, ViewGroup parent) {  
//        Iterator<ImagePatternContainer> iterator = mContainers.iterator();
//        while (iterator.hasNext()) {
//            ImageHolder holder = new ImageHolder();
//            if(convertView == null) { 
//                convertView = mInflater.inflate(R.layout.two_by_two, null, false);
//                ImagePatternContainer ipc = iterator.next();
//
//                holder.view = new ImageView[ipc.getPattern().getCount()];
//
//                //temp
//                int[] viewId =  {R.id.imageView0, R.id.imageView1, R.id.imageView2, R.id.imageView3};
//
//                for(int i = 0; i < holder.view.length; i++) {
//                    holder.view[i] = (ImageView) convertView.findViewById(viewId[i]);
//                    holder.view[i].setLayoutParams(new FrameLayout.LayoutParams(mImageWidth, mImageWidth));
//                }
//
//                convertView.setTag(holder);
//
//            }else {
//                holder = (ImageHolder) convertView.getTag();
//            }
//
//            mHolders.add(holder);
//
//        } 
//
//        for(int j = 0; j < 5; j++){
//            ImageView[] iv = mHolders.get(j).view;
//            ImagePatternContainer ipc = mContainers.get(j);
//            for(int i = 0; i < iv.length; i++) {
//                List<Integer> li = ipc.getPhotosID();
//                Log.d("ImageAdapter", "Fetch image: " + li.get(i));
//                mImageFetcher.loadImage(mPhotos.get(li.get(i)).getImage_url(), iv[i]);
//            }
//
//        }
//
//        
//        
//        return convertView;
//    }

    
}
