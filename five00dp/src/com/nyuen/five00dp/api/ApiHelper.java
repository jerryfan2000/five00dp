package com.nyuen.five00dp.api;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fivehundredpx.api.PxApi;
import com.google.gson.Gson;
import com.nyuen.five00dp.structure.CommentResponse;
import com.nyuen.five00dp.structure.PhotoDetailResponse;
import com.nyuen.five00dp.structure.PhotoListResponse;
import com.nyuen.five00dp.structure.ProfileResponse;
import com.nyuen.five00dp.structure.User;
import com.nyuen.five00dp.util.AccountUtils;

public class ApiHelper {
    private static final String TAG = ApiHelper.class.getSimpleName();
    private static final String KEY_TOKEN = "auth_token";

    private static PxApi getApi(Context context){
        PxApi pxapi = new PxApi(FiveHundred.CONSUMER_KEY);
        
        if(AccountUtils.hasAccount(context)) {
        }        
        return pxapi;
    }
    
    public static PhotoListResponse getPhotoStream(String feature, int rpp, int size, int page){
        PxApi pxapi = new PxApi(FiveHundred.CONSUMER_KEY);
        String url = "/photos?feature=" + feature + "&rpp=" + rpp + 
                "&image_size=" + size +
                "&page="+page;
        try {
            PhotoListResponse out = new Gson().fromJson(
                    pxapi.get(url).toString(), 
                            PhotoListResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    
    public static PhotoListResponse getProfilePhotos(int user_id, int rpp, int size, int page){
        PxApi pxapi = new PxApi(FiveHundred.CONSUMER_KEY);
        String url = "/photos?feature=user&user_id=" + user_id + "&rpp=" + rpp + 
                "&image_size=" + size +
                "&page="+page;
        
        try {
            PhotoListResponse out = new Gson().fromJson(
                    pxapi.get(url).toString(), 
                            PhotoListResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    

    public static PhotoDetailResponse getFullPhoto(int photoId){
        PxApi pxapi = new PxApi(FiveHundred.CONSUMER_KEY);
        String url = "/photos/" + photoId + "?comments";
        
        try {
            PhotoDetailResponse out = new Gson().fromJson(
                    pxapi.get(url).toString(), 
                    PhotoDetailResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public static CommentResponse getComments(int photoId, int page){
        PxApi pxapi = new PxApi(FiveHundred.CONSUMER_KEY);
        String url = "/photos/" + photoId + "/comments?page=" + page;
        try {
            //20 comments per page
            CommentResponse out = new Gson().fromJson(
                    pxapi.get(url).toString(), 
                            CommentResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    
    public static ProfileResponse getMyProfile(Context context){        
        PxApi pxapi = new PxApi(AccountUtils.getAccessToken(context), FiveHundred.CONSUMER_KEY, FiveHundred.CONSUMER_SECRET);
        String url = "/users";
        try {
            String json = pxapi.get(url).toString();
            ProfileResponse out = new Gson().fromJson(json, ProfileResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    
    public static ProfileResponse getUserProfile(int userID, Context context){        
        PxApi pxapi = new PxApi(AccountUtils.getAccessToken(context), FiveHundred.CONSUMER_KEY, FiveHundred.CONSUMER_SECRET);
        String url = "/users/show?id=" + userID;
        try {
            String json = pxapi.get(url).toString();
            ProfileResponse out = new Gson().fromJson(json, ProfileResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

}
