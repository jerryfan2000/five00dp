package com.nyuen.five00dp;

import android.support.v4.app.Fragment;

import com.nyuen.five00dp.base.BaseActivity;
import com.nyuen.five00dp.fragment.PhotoListFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected Fragment onCreatePane() {
        
        return new PhotoListFragment();
    }
    
}
