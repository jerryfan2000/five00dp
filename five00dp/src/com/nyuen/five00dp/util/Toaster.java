package com.nyuen.five00dp.util;

import android.content.Context;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Toaster {

    private static Toaster sInstance;
    private static Context mContext;
    
    private Map<String, String> mCachedMessages;
    
    public static void init(Context context) {
        sInstance = new Toaster();
        mContext = context;
    }
    
    public static Toaster get() {
        if (sInstance == null) {
            throw new IllegalStateException("Call init() before first use.");
        }
        return sInstance;
    }
    
    public Toaster() {
        mCachedMessages = new HashMap<String, String>();
    }
    
    public void showShortText(CharSequence s) {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }

    public void showShortText(int id) {
        Toast.makeText(mContext, id, Toast.LENGTH_SHORT).show();
    }
    
    public void showLongText(CharSequence s) {
        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
    }

    public void showLongText(int id) {
        Toast.makeText(mContext, id, Toast.LENGTH_LONG).show();
    }
    
    public void showShortError(Throwable t) {
        if (t != null) showShortText(t.getMessage());
    }
    
    public void showLongError(Throwable t) {
        if (t != null) showLongText(t.getMessage());
    }
    
    public void addCachedMessage(String key, String value) {
        mCachedMessages.put(key, value);
    }
    
    public String getCachedMessage(String key) {
        return mCachedMessages.get(key);
    }
}