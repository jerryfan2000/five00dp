package com.nyuen.test_fivehundred;

import android.support.v4.app.Fragment;

import com.nyuen.test_fivehundred.base.BaseActivity;
import com.nyuen.test_fivehundred.fragment.PhotoDetailFragment;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected Fragment onCreatePane() {
        return new PhotoDetailFragment();
    }

}
