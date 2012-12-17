package com.nyuen.five00dp;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.nyuen.five00dp.R;
import com.nyuen.five00dp.base.SlidingBaseActivity;
import com.nyuen.five00dp.fragment.MenuFragment;
import com.nyuen.five00dp.fragment.PhotoListFragment;
import com.slidingmenu.lib.SlidingMenu;

public class MainActivity extends SlidingBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
    }

    @Override
    protected Fragment onCreatePane() {
        return new PhotoListFragment();
    }
    
    @Override
    protected Fragment onCreateMenuPane() {
        return new MenuFragment();
    }

}
