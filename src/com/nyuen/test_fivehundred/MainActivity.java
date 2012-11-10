package com.nyuen.test_fivehundred;

import android.support.v4.app.Fragment;

import com.nyuen.test_fivehundred.base.BaseActivity;


public class MainActivity extends BaseActivity {

    @Override
    protected Fragment onCreatePane() {
        
        return new ImageListFragment();
    }
    
}
