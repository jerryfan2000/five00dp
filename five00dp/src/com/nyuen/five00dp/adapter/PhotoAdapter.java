package com.nyuen.five00dp.adapter;

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

import com.nyuen.five00dp.MainActivity;
import com.nyuen.five00dp.PhotoDetailActivity;
import com.nyuen.five00dp.R;
import com.nyuen.five00dp.fragment.PhotoDetailFragment;
import com.nyuen.five00dp.fragment.PhotoListFragment;
import com.nyuen.five00dp.structure.Photo;
import com.nyuen.five00dp.structure.PhotoPatternContainer;
import com.nyuen.five00dp.structure.PhotoPatternContainer.Pattern;
import com.nyuen.five00dp.util.ImageFetcher;

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
        WIDTH_ONE = Math.min(display.getWidth(), display.getHeight());
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

        // Attach click listener for each photo
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

    public void setPhotos(ArrayList<Photo> photos) {
        mPhotoCount = 0;
        mPhotos.clear();
        appendPhotos(photos);
    }

    public void appendPhotos(ArrayList<Photo> photos) {  
        mPhotos.addAll(photos);

        // Retrieve a list of patterns 
        List<Pattern> patterns = Pattern.getPatternList();

        // Loop through the patterns, 
        // create a container object that store a pattern and the respective photo index
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

    // Set the LayoutParams that will be used in getView() 
    public void setParams() {
        // Pattern.ONE
        mParams[0] = new RelativeLayout.LayoutParams(WIDTH_ONE - MARGIN_TWO, WIDTH_ONE - MARGIN_TWO);

        mParams[0].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_ONE, 0);

        // Pattern.TWO_VERTICAL
        mParams[1] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_ONE - MARGIN_TWO);
        mParams[2] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_ONE - MARGIN_TWO);

        mParams[1].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_HALF, 0);
        mParams[2].setMargins(MARGIN_HALF, MARGIN_ONE, MARGIN_ONE, 0);

        mParams[2].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);

        // Pattern.TWO_HORIZONTAL
        mParams[3] = new RelativeLayout.LayoutParams(WIDTH_ONE - MARGIN_TWO, WIDTH_HALF - MARGIN_THREE_HALF);
        mParams[4] = new RelativeLayout.LayoutParams(WIDTH_ONE - MARGIN_TWO, WIDTH_HALF - MARGIN_THREE_HALF);

        mParams[3].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_ONE, MARGIN_HALF);
        mParams[4].setMargins(MARGIN_ONE, MARGIN_HALF, MARGIN_ONE, 0);

        mParams[4].addRule(RelativeLayout.BELOW, R.id.imageView0);

        // Pattern.THREE_VERTICAL
        mParams[8] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_ONE - MARGIN_TWO);
        mParams[9] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_HALF - MARGIN_THREE_HALF);
        mParams[10] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_HALF - MARGIN_THREE_HALF);

        mParams[8].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_HALF, 0);
        mParams[9].setMargins(MARGIN_HALF, MARGIN_ONE, MARGIN_ONE, MARGIN_HALF);
        mParams[10].setMargins(MARGIN_HALF, MARGIN_HALF, MARGIN_ONE, 0);

        mParams[9].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
        mParams[10].addRule(RelativeLayout.BELOW, R.id.imageView1);
        mParams[10].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);

        // Pattern.THREE_HORIZONTAL
        mParams[5] = new RelativeLayout.LayoutParams(WIDTH_ONE - MARGIN_TWO, WIDTH_HALF - MARGIN_THREE_HALF);
        mParams[6] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_HALF - MARGIN_THREE_HALF);
        mParams[7] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, WIDTH_HALF - MARGIN_THREE_HALF);

        mParams[5].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_ONE, MARGIN_HALF);
        mParams[6].setMargins(MARGIN_ONE, MARGIN_HALF, MARGIN_HALF, 0);
        mParams[7].setMargins(MARGIN_HALF, MARGIN_HALF, MARGIN_ONE, 0);

        mParams[6].addRule(RelativeLayout.BELOW, R.id.imageView0);
        mParams[7].addRule(RelativeLayout.BELOW, R.id.imageView0);
        mParams[7].addRule(RelativeLayout.RIGHT_OF, R.id.imageView1);


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
            // Infate the imageViews
            holder = new ImageHolder();
            holder.imageViews = new ImageView[4];
            convertView = mInflater.inflate(R.layout.image_grid, null, false);	

            // Set click listener for each imageView
            for (int i = 0; i < holder.imageViews.length; i++) {
                holder.imageViews[i] = (ImageView) convertView.findViewById(IMAGEVIEW_IDS[i]);
                holder.imageViews[i].setOnClickListener(mOnPhotoClickListener);
            }

            convertView.setTag(holder);
        } else {
            holder = (ImageHolder) convertView.getTag();
        }

        ImageView[] imageViews = holder.imageViews;
        PhotoPatternContainer photoContainer = mContainers.get(position);
        Pattern pattern = photoContainer.getPattern();
        List<Integer> photoIndices = photoContainer.getPhotosIdx();

        // Loop through each imageView and retrieve the specific photo url
        // Then, load the image with imageFetcher
        for (int i = 0; i < 4; i++) {
            if (i < photoIndices.size()) {
                imageViews[i].setVisibility(View.VISIBLE);
                Photo loadPhoto = mPhotos.get(photoIndices.get(i));
                String url = loadPhoto.image_url;
                url = url.replace(PhotoListFragment.IMAGE_SIZE + ".jpg", pattern.getSizes()[i] + ".jpg");
                imageViews[i].setBackgroundColor(Color.WHITE);
                imageViews[i].setTag(loadPhoto);
                mImageFetcher.loadImage(url, imageViews[i]);
            } else {
                imageViews[i].setVisibility(View.GONE);
            }
        }

        // Set LayoutParams based on the pattern
        if (pattern == Pattern.ONE) {
            imageViews[0].setLayoutParams(mParams[0]);
        } else if (pattern == Pattern.TWO_VERTICAL) {
            imageViews[0].setLayoutParams(mParams[1]);
            imageViews[1].setLayoutParams(mParams[2]);
        } else if (pattern == Pattern.TWO_HORIZONTAL) {
            imageViews[0].setLayoutParams(mParams[3]);
            imageViews[1].setLayoutParams(mParams[4]);
        } else if (pattern == Pattern.THREE_HORIZONTAL) {
            imageViews[0].setLayoutParams(mParams[5]);
            imageViews[1].setLayoutParams(mParams[6]);
            imageViews[2].setLayoutParams(mParams[7]);
        } else if (pattern == Pattern.THREE_VERTICAL) {
            imageViews[0].setLayoutParams(mParams[8]);
            imageViews[1].setLayoutParams(mParams[9]);
            imageViews[2].setLayoutParams(mParams[10]);
        } else {
            imageViews[0].setLayoutParams(mParams[11]);
            imageViews[1].setLayoutParams(mParams[12]);
            imageViews[2].setLayoutParams(mParams[13]);
            imageViews[3].setLayoutParams(mParams[14]);
        }

        return convertView;
    }

    private class ImageHolder {
        ImageView[] imageViews;
    }
}
