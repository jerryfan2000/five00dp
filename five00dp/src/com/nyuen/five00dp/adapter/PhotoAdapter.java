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
import com.nyuen.five00dp.structure.PhotoTestPatternContainer.Pattern;
import com.nyuen.five00dp.structure.PhotoTestPatternContainer;
import com.nyuen.five00dp.util.ImageFetcher;

public class PhotoAdapter extends BaseAdapter {

    private final int WIDTH_ONE;
    private final int MARGIN_ONE;

    private final LayoutInflater mInflater;
    private final ImageFetcher mImageFetcher;

    private final int[] IMAGEVIEW_IDS = { 
            R.id.imageView0, 
            R.id.imageView1, 
            R.id.imageView2,
            R.id.imageView3 
    };

    private List<Photo> mPhotos;
    private List<PhotoTestPatternContainer> mContainers;

    private int mListItemCount;
    private int mPhotoCount;
    private OnClickListener mOnPhotoClickListener;

    public PhotoAdapter(final Context context, ImageFetcher imageFetcher) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Resources resources = context.getResources();
        
        // Calculate sizes
        WIDTH_ONE = Math.min(display.getWidth(), display.getHeight());
        MARGIN_ONE = resources.getDimensionPixelSize(R.dimen.image_grid_margin);

        // Instantiate members
        mImageFetcher = imageFetcher;
        mInflater = LayoutInflater.from(context);
        mContainers = new ArrayList<PhotoTestPatternContainer>();
        mPhotos = new ArrayList<Photo>();

        // Attach click listener for each photo
        mOnPhotoClickListener = new OnClickListener() {
            public void onClick(View v) {
                Photo loadPhoto = (Photo) v.getTag();
                Intent photoDetailIntent = new Intent(context, PhotoDetailActivity.class);
                photoDetailIntent.putExtra(PhotoDetailFragment.INTENT_EXTRA_PHOTO, loadPhoto);
                context.startActivity(photoDetailIntent);
            }
        };   

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
            PhotoTestPatternContainer c = new PhotoTestPatternContainer(p);
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
        PhotoTestPatternContainer photoContainer = mContainers.get(position);
        Pattern pattern = photoContainer.getPattern();
        RelativeLayout.LayoutParams[] params = pattern.getParams(WIDTH_ONE, MARGIN_ONE);
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
        for (int i = 0; i < params.length; i++) {
            imageViews[i].setLayoutParams(params[i]);
        }
        
        return convertView;
    }

    private class ImageHolder {
        ImageView[] imageViews;
    }
}
