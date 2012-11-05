package com.nyuen.test_fivehundred;

import com.nyuen.test_fivehundred.adapter.ImageAdapter;

import android.R;
import android.os.Bundle;
import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MainActivity extends ListActivity {

    private ImageAdapter test_adapter;
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_content);
        
        test_adapter = new ImageAdapter(this);
        Bitmap[] mPhotos = null;
        
        mPhotos[0] = BitmapFactory.decodeResource(getResources(), R.drawable.btn_plus);
        mPhotos[1] = BitmapFactory.decodeResource(getResources(), R.drawable.btn_plus);
        mPhotos[2] = BitmapFactory.decodeResource(getResources(), R.drawable.btn_plus);
        mPhotos[3] = BitmapFactory.decodeResource(getResources(), R.drawable.btn_plus);
        
        
//                new BitmapDrawable(getResources(), R.dr);
        test_adapter.setPhotos(mPhotos);
        setListAdapter(this.test_adapter);
        
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu., menu);
//        return true;
//    }
}
