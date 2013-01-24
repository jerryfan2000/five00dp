package com.nyuen.five00dp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

public class CustomTextSwitcher extends TextSwitcher implements ViewFactory {
    
    private TextView mTextView;

    public CustomTextSwitcher(Context context) {
        this(context, null);
    }

    public CustomTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTextView = (TextView) findViewById(android.R.id.text1);
    }

    @Override
    public View makeView() {
        return mTextView;
    }
}
