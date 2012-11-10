package com.nyuen.test_fivehundred.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nyuen.test_fivehundred.R;
import com.nyuen.test_fivehundred.structure.ImagePatternContainer;
import com.nyuen.test_fivehundred.structure.ImagePatternContainer.Pattern;
import com.nyuen.test_fivehundred.structure.Photo;
import com.nyuen.test_fivehundred.util.ImageFetcher;

public class ImageAdapter extends BaseAdapter {

	private final LayoutInflater mInflater;
	private ImageFetcher mImageFetcher;
	private int mImageWidth;
	private int mMarginSize;
	private int mListItemCount;
	private int photoCount;

	private List<Photo> mPhotos;
	private List<ImagePatternContainer> mContainers;
	private List<Pattern> mPatterns;
	private List<ImageHolder> mHolders;

	
	private RelativeLayout.LayoutParams[] mParams;
	
	
	public ImageAdapter(Context context, ImageFetcher imageFetcher) {
		mImageFetcher = imageFetcher;
		mInflater = LayoutInflater.from(context);
		calculateItemSize(context);

		mPatterns = Pattern.getPatternList();
		mContainers = new ArrayList<ImagePatternContainer>();
		mHolders = new ArrayList<ImageHolder>();
		mPhotos = new ArrayList<Photo>();
		mParams = new RelativeLayout.LayoutParams[16];
		setParams();
	}

	private void calculateItemSize(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int width = display.getWidth();
		mImageWidth = width / 2;
		mMarginSize = context.getResources().getDimensionPixelSize(R.dimen.image_grid_margin);
	}

	public int getCount() {
		return mListItemCount;
	}

	public Object getItem(int position) {
		return position;
	}

	public void setPhotos(List<Photo> photos) {
	    for(Photo ph:photos) {
            mPhotos.add(ph); 
        }
		photoCount = 0;// photo.length;

		Iterator<Pattern> iterator = mPatterns.iterator();
		while (iterator.hasNext()) {
			Pattern p = iterator.next();
			Log.d("setPhotos", "List: " + mListItemCount + " Pattern size: " + p.getCount());
			ImagePatternContainer c = new ImagePatternContainer(p);
			mContainers.add(c);

			for (int i = 0; i < p.getCount(); i++) {
				mContainers.get(mListItemCount).addPhotoID(photoCount);
				photoCount++;
				Log.d("setPhotos", "Load Photo: " + photoCount);
			}

			mListItemCount++;
		}
		Log.d("ImageAdapter", "List: " + mListItemCount + " Photo: " + photoCount);
	}

	public void appendPhotos(List<Photo> photos) {
	    for(Photo ph:photos) {
	        mPhotos.add(ph); 
	    }
	    
	    Iterator<Pattern> iterator = mPatterns.iterator();
        while (iterator.hasNext()) {
            Pattern p = iterator.next();
            Log.d("setPhotos", "List: " + mListItemCount + " Pattern size: " + p.getCount());
            ImagePatternContainer c = new ImagePatternContainer(p);
            mContainers.add(c);

            for (int i = 0; i < p.getCount(); i++) {
                mContainers.get(mListItemCount).addPhotoID(photoCount);
                photoCount++;
                Log.d("setPhotos", "Load Photo: " + photoCount);
            }

            mListItemCount++;
        }
	}
	
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	public void setParams() {	    
	    //Pattern.ONE
	    mParams[0] = new RelativeLayout.LayoutParams(mImageWidth * 2, mImageWidth * 2);
	    
	    mParams[0].setMargins(mMarginSize, mMarginSize, mMarginSize, 0);
	    
	    //Pattern.TWO_VERT
	    mParams[1] = new RelativeLayout.LayoutParams(mImageWidth - (mMarginSize/2*3),
                mImageWidth * 2);
	    mParams[2] = new RelativeLayout.LayoutParams(mImageWidth, mImageWidth*2);
	    
	    mParams[1].setMargins(mMarginSize, mMarginSize, mMarginSize/2, 0);
        mParams[2].setMargins(mMarginSize/2, mMarginSize, mMarginSize, 0);
        
        mParams[2].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
	    
	    //Pattern.TWO_HOR
	    mParams[3] = new RelativeLayout.LayoutParams(mImageWidth, mImageWidth);
	    mParams[4] = new RelativeLayout.LayoutParams(mImageWidth, mImageWidth);
	    
	    mParams[3] = new RelativeLayout.LayoutParams(mImageWidth * 2, mImageWidth
                - (mMarginSize / 2 * 3));
	    mParams[4] = new RelativeLayout.LayoutParams(mImageWidth * 2, mImageWidth);

	    mParams[3].setMargins(mMarginSize, mMarginSize, mMarginSize, mMarginSize / 2);
        mParams[4].setMargins(mMarginSize, mMarginSize / 2, mMarginSize, 0);
        
        mParams[4].addRule(RelativeLayout.BELOW, R.id.imageView0);
	    
	    //Pattern.THREE_HOR    
	    mParams[5] = new RelativeLayout.LayoutParams(mImageWidth * 2, mImageWidth
                - (mMarginSize / 2 * 3));
	    mParams[6] = new RelativeLayout.LayoutParams(mImageWidth - (mMarginSize / 2 * 3),
                mImageWidth);    
	    mParams[7] = new RelativeLayout.LayoutParams(mImageWidth, mImageWidth);

	    mParams[5].setMargins(mMarginSize, mMarginSize, mMarginSize, mMarginSize / 2);
	    mParams[6].setMargins(mMarginSize, mMarginSize / 2, mMarginSize / 2, 0);
	    mParams[7].setMargins(mMarginSize / 2, mMarginSize / 2, mMarginSize, 0);
        
	    mParams[6].addRule(RelativeLayout.BELOW, R.id.imageView0);
        mParams[7].addRule(RelativeLayout.BELOW, R.id.imageView0);
        mParams[7].addRule(RelativeLayout.RIGHT_OF, R.id.imageView1);
	    
	    //Pattern.THREE_VERT
	    mParams[8] = new RelativeLayout.LayoutParams(mImageWidth - (mMarginSize / 2 * 3),
                mImageWidth * 2);
	    mParams[9] = new RelativeLayout.LayoutParams(mImageWidth, mImageWidth
                - (mMarginSize / 2 * 3));
	    mParams[10] = new RelativeLayout.LayoutParams(mImageWidth, mImageWidth);

	    mParams[8].setMargins(mMarginSize, mMarginSize, mMarginSize / 2, 0);
	    mParams[9].setMargins(mMarginSize / 2, mMarginSize, mMarginSize, mMarginSize / 2);
	    mParams[10].setMargins(mMarginSize / 2, mMarginSize / 2, mMarginSize, 0);

	    mParams[9].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
	    mParams[10].addRule(RelativeLayout.BELOW, R.id.imageView1);
	    mParams[10].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
  
	    //Pattern.FOUR
	    mParams[11]= new RelativeLayout.LayoutParams(mImageWidth, mImageWidth);
	    mParams[12]= new RelativeLayout.LayoutParams(mImageWidth, mImageWidth);
	    mParams[13]= new RelativeLayout.LayoutParams(mImageWidth, mImageWidth);
	    mParams[14]= new RelativeLayout.LayoutParams(mImageWidth, mImageWidth);
	    
	    mParams[11] = new RelativeLayout.LayoutParams(mImageWidth - (mMarginSize / 2 * 3),
                mImageWidth - (mMarginSize / 2 * 3));
	    mParams[12] = new RelativeLayout.LayoutParams(mImageWidth, mImageWidth
                - (mMarginSize / 2 * 3));
	    mParams[13] = new RelativeLayout.LayoutParams(mImageWidth - (mMarginSize / 2 * 3),
                mImageWidth);
	    mParams[14] = new RelativeLayout.LayoutParams(mImageWidth, mImageWidth);

	    mParams[11].setMargins(mMarginSize, mMarginSize, mMarginSize / 2, mMarginSize / 2);
	    mParams[12].setMargins(mMarginSize / 2, mMarginSize, mMarginSize, mMarginSize / 2);
	    mParams[13].setMargins(mMarginSize, mMarginSize / 2, mMarginSize / 2, 0);
	    mParams[14].setMargins(mMarginSize / 2, mMarginSize / 2, mMarginSize, 0);

	    mParams[12].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
	    mParams[13].addRule(RelativeLayout.BELOW, R.id.imageView0);
	    mParams[14].addRule(RelativeLayout.BELOW, R.id.imageView1);
	    mParams[14].addRule(RelativeLayout.RIGHT_OF, R.id.imageView2);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageHolder holder = new ImageHolder();

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.image_grid, null, false);
			RelativeLayout r = (RelativeLayout) convertView.findViewById(R.id.imageGrid);
			r.setLayoutParams(new AbsListView.LayoutParams(mImageWidth * 2, mImageWidth * 2));

			holder.view = new ImageView[4];

			int[] viewId = { R.id.imageView0, R.id.imageView1, R.id.imageView2, R.id.imageView3 };

			for (int i = 0; i < holder.view.length; i++) {
				holder.view[i] = (ImageView) convertView.findViewById(viewId[i]);
			}

			convertView.setTag(holder);

		} else {
			holder = (ImageHolder) convertView.getTag();
		}

		mHolders.add(holder);

		ImageView[] iv = holder.view;
		ImagePatternContainer ipc = mContainers.get(position);
		Pattern pattern = ipc.getPattern();
		List<Integer> li = ipc.getPhotosID();
		for (int i = 0; i < 4; i++) {
			if (i < li.size()) {
				iv[i].setVisibility(View.VISIBLE);
				Log.d("ImageAdapter", "Item: " + position + ", Fetch image: " + li.get(i)
						+ " view: " + i);
				String url = mPhotos.get(li.get(i)).image_url;
				
				Log.e("URL", url);
				url = url.replace("3.jpg", pattern.getSizes()[i] + ".jpg");
				Log.e("URL-After", url);
				
				iv[i].setBackgroundColor(Color.WHITE);
				mImageFetcher.loadImage(url, iv[i]);
			} else {
				iv[i].setVisibility(View.GONE);
			}
		}

		if (pattern == Pattern.ONE) {
		    iv[0].setLayoutParams(mParams[0]);
		} else if (pattern == Pattern.TWO_VERT) {	
			iv[0].setLayoutParams(mParams[1]);
            iv[1].setLayoutParams(mParams[2]);
		} else if (pattern == Pattern.TWO_HOR) {
			iv[0].setLayoutParams(mParams[3]);
			iv[1].setLayoutParams(mParams[4]);
		} else if (pattern == Pattern.THREE_HOR) {
			iv[0].setLayoutParams(mParams[5]);
            iv[1].setLayoutParams(mParams[6]);
            iv[2].setLayoutParams(mParams[7]);
		} else if (pattern == Pattern.THREE_VERT) {
			iv[0].setLayoutParams(mParams[8]);
            iv[1].setLayoutParams(mParams[9]);
            iv[2].setLayoutParams(mParams[10]);
		} else {			
			iv[0].setLayoutParams(mParams[11]);
            iv[1].setLayoutParams(mParams[12]);
            iv[2].setLayoutParams(mParams[13]);
            iv[3].setLayoutParams(mParams[14]);
		}

		return convertView;
	}

	private class ImageHolder {
		ImageView[] view;
	}

	// public View getView(int position, View convertView, ViewGroup parent) {
	// Iterator<ImagePatternContainer> iterator = mContainers.iterator();
	// while (iterator.hasNext()) {
	// ImageHolder holder = new ImageHolder();
	// if(convertView == null) {
	// convertView = mInflater.inflate(R.layout.two_by_two, null, false);
	// ImagePatternContainer ipc = iterator.next();
	//
	// holder.view = new ImageView[ipc.getPattern().getCount()];
	//
	// //temp
	// int[] viewId = {R.id.imageView0, R.id.imageView1, R.id.imageView2,
	// R.id.imageView3};
	//
	// for(int i = 0; i < holder.view.length; i++) {
	// holder.view[i] = (ImageView) convertView.findViewById(viewId[i]);
	// holder.view[i].setLayoutParams(new FrameLayout.LayoutParams(mImageWidth,
	// mImageWidth));
	// }
	//
	// convertView.setTag(holder);
	//
	// }else {
	// holder = (ImageHolder) convertView.getTag();
	// }
	//
	// mHolders.add(holder);
	//
	// }
	//
	// for(int j = 0; j < 5; j++){
	// ImageView[] iv = mHolders.get(j).view;
	// ImagePatternContainer ipc = mContainers.get(j);
	// for(int i = 0; i < iv.length; i++) {
	// List<Integer> li = ipc.getPhotosID();
	// Log.d("ImageAdapter", "Fetch image: " + li.get(i));
	// mImageFetcher.loadImage(mPhotos.get(li.get(i)).getImage_url(), iv[i]);
	// }
	//
	// }
	//
	//
	//
	// return convertView;
	// }

}
