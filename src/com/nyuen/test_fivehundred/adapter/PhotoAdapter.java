package com.nyuen.test_fivehundred.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nyuen.test_fivehundred.MainActivity;
import com.nyuen.test_fivehundred.PhotoDetailActivity;
import com.nyuen.test_fivehundred.R;
import com.nyuen.test_fivehundred.fragment.PhotoDetailFragment;
import com.nyuen.test_fivehundred.fragment.PhotoListFragment;
import com.nyuen.test_fivehundred.structure.PhotoPatternContainer;
import com.nyuen.test_fivehundred.structure.PhotoPatternContainer.Pattern;
import com.nyuen.test_fivehundred.structure.Photo;
import com.nyuen.test_fivehundred.util.ImageFetcher;

public class PhotoAdapter extends BaseAdapter {

	private final int WIDTH_ONE;
	private final int WIDTH_HALF;
	private final int MARGIN_ONE;
	private final int MARGIN_TWO;
	private final int MARGIN_HALF;
	private final int MARGIN_THREE_HALF;
	
	private final LayoutInflater mInflater;
	private final ImageFetcher mImageFetcher;

	private final int[] IMAGEVIEW_IDS = { 
			R.id.imageView0, 
			R.id.imageView1, 
			R.id.imageView2,
			R.id.imageView3 
	};

	private List<Photo> mPhotos;
	private List<PhotoPatternContainer> mContainers;
	private RelativeLayout.LayoutParams[] mParams;
	
	private int mListItemCount;
	private int mPhotoCount;
	private OnClickListener mOnPhotoClickListener;

	public PhotoAdapter(final Context context, ImageFetcher imageFetcher) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Resources resources = context.getResources();

		// Calculate sizes
		WIDTH_ONE = display.getWidth();
		WIDTH_HALF = WIDTH_ONE / 2;
		MARGIN_ONE = resources.getDimensionPixelSize(R.dimen.image_grid_margin);
		MARGIN_TWO = MARGIN_ONE + MARGIN_ONE;
		MARGIN_HALF = MARGIN_ONE / 2;
		MARGIN_THREE_HALF = MARGIN_HALF * 3;
		
		// Instantiate members
		mImageFetcher = imageFetcher;
		mInflater = LayoutInflater.from(context);
		mContainers = new ArrayList<PhotoPatternContainer>();
		mPhotos = new ArrayList<Photo>();
		mParams = new RelativeLayout.LayoutParams[16];

		mOnPhotoClickListener = new OnClickListener() {
	        public void onClick(View v) {
	            Photo loadPhoto = (Photo) v.getTag();
	            Intent photoDetailIntent = new Intent(context, PhotoDetailActivity.class);
	            photoDetailIntent.putExtra(PhotoDetailFragment.INTENT_EXTRA_PHOTO, loadPhoto);
	            context.startActivity(photoDetailIntent);
	        }
	    };   
		
		// Instantiate layout params
		setParams();
	}

	public int getCount() {
		return mListItemCount;
	}

	public Object getItem(int position) {
		return position;
	}

	public void clearPhotos() {
	    mPhotoCount = 0;
	    //mListItemCount = 0;
	    mPhotos.clear();
	}
	
	public void setPhotos(List<Photo> photos) {
	    //clearPhotos();
	    mPhotos.clear();
		appendPhotos(photos);
	}

	public void appendPhotos(List<Photo> photos) {  
		for (Photo ph : photos) {
			mPhotos.add(ph);
		}
		
		List<Pattern> patterns = Pattern.getPatternList();
		Iterator<Pattern> iterator = patterns.iterator();
		while (iterator.hasNext()) {
			Pattern p = iterator.next();
			PhotoPatternContainer c = new PhotoPatternContainer(p);
			mContainers.add(c);

			for (int i = 0; i < p.getCount(); i++) {
				mContainers.get(mListItemCount).addPhotoID(mPhotoCount);
				mPhotoCount++;
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
		// Pattern.ONE
		mParams[0] = new RelativeLayout.LayoutParams(WIDTH_ONE - MARGIN_TWO, WIDTH_ONE - MARGIN_TWO);

		mParams[0].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_ONE, 0);

		// Pattern.TWO_VERT
		mParams[1] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_ONE - MARGIN_TWO);
		mParams[2] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_ONE - MARGIN_TWO);

		mParams[1].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_HALF, 0);
		mParams[2].setMargins(MARGIN_HALF, MARGIN_ONE, MARGIN_ONE, 0);

		mParams[2].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);

		// Pattern.TWO_HOR
		mParams[3] = new RelativeLayout.LayoutParams(WIDTH_ONE - MARGIN_TWO, WIDTH_HALF - MARGIN_THREE_HALF);
		mParams[4] = new RelativeLayout.LayoutParams(WIDTH_ONE - MARGIN_TWO, WIDTH_HALF - MARGIN_THREE_HALF);

		mParams[3].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_ONE, MARGIN_HALF);
		mParams[4].setMargins(MARGIN_ONE, MARGIN_HALF, MARGIN_ONE, 0);

		mParams[4].addRule(RelativeLayout.BELOW, R.id.imageView0);

		// Pattern.THREE_HOR
		mParams[5] = new RelativeLayout.LayoutParams(WIDTH_ONE - MARGIN_TWO, WIDTH_HALF - MARGIN_THREE_HALF);
		mParams[6] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_HALF - MARGIN_THREE_HALF);
		mParams[7] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_HALF - MARGIN_THREE_HALF);

		mParams[5].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_ONE, MARGIN_HALF);
		mParams[6].setMargins(MARGIN_ONE, MARGIN_HALF, MARGIN_HALF, 0);
		mParams[7].setMargins(MARGIN_HALF, MARGIN_HALF, MARGIN_ONE, 0);

		mParams[6].addRule(RelativeLayout.BELOW, R.id.imageView0);
		mParams[7].addRule(RelativeLayout.BELOW, R.id.imageView0);
		mParams[7].addRule(RelativeLayout.RIGHT_OF, R.id.imageView1);

		// Pattern.THREE_VERT
		mParams[8] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_ONE - MARGIN_TWO);
		mParams[9] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_HALF - MARGIN_THREE_HALF);
		mParams[10] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_HALF - MARGIN_THREE_HALF);

		mParams[8].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_HALF, 0);
		mParams[9].setMargins(MARGIN_HALF, MARGIN_ONE, MARGIN_ONE, MARGIN_HALF);
		mParams[10].setMargins(MARGIN_HALF, MARGIN_HALF, MARGIN_ONE, 0);

		mParams[9].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
		mParams[10].addRule(RelativeLayout.BELOW, R.id.imageView1);
		mParams[10].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);

		// Pattern.FOUR
		mParams[11] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_HALF - MARGIN_THREE_HALF);
		mParams[12] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_HALF - MARGIN_THREE_HALF);
		mParams[13] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_HALF - MARGIN_THREE_HALF);
		mParams[14] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_HALF - MARGIN_THREE_HALF);

		mParams[11].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_HALF, MARGIN_HALF);
		mParams[12].setMargins(MARGIN_HALF, MARGIN_ONE, MARGIN_ONE, MARGIN_HALF);
		mParams[13].setMargins(MARGIN_ONE, MARGIN_HALF, MARGIN_HALF, 0);
		mParams[14].setMargins(MARGIN_HALF, MARGIN_HALF, MARGIN_ONE, 0);

		mParams[12].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
		mParams[13].addRule(RelativeLayout.BELOW, R.id.imageView0);
		mParams[14].addRule(RelativeLayout.BELOW, R.id.imageView1);
		mParams[14].addRule(RelativeLayout.RIGHT_OF, R.id.imageView2);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageHolder holder;

		if (convertView == null) {
			holder = new ImageHolder();
			convertView = mInflater.inflate(R.layout.image_grid, null, false);

			holder.imageViews = new ImageView[4];

			for (int i = 0; i < holder.imageViews.length; i++) {
				holder.imageViews[i] = (ImageView) convertView.findViewById(IMAGEVIEW_IDS[i]);
				holder.imageViews[i].setOnClickListener(mOnPhotoClickListener);
			}

			convertView.setTag(holder);

		} else {
			holder = (ImageHolder) convertView.getTag();
		}

		ImageView[] iv = holder.imageViews;
		PhotoPatternContainer ipc = mContainers.get(position);
		Pattern pattern = ipc.getPattern();
		List<Integer> li = ipc.getPhotosID();
		for (int i = 0; i < 4; i++) {
			if (i < li.size()) {
				iv[i].setVisibility(View.VISIBLE);
				Photo loadPhoto = mPhotos.get(li.get(i));
				String url = loadPhoto.image_url;
				url = url.replace(PhotoListFragment.IMAGE_SIZE + ".jpg", pattern.getSizes()[i] + ".jpg");
				iv[i].setBackgroundColor(Color.WHITE);
				iv[i].setTag(loadPhoto);
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
		ImageView[] imageViews;
	}
}
