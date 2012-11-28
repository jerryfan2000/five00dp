package com.nyuen.five00dp.api;

import android.util.Log;

import com.fivehundredpx.api.PxApi;
import com.google.gson.Gson;
import com.nyuen.five00dp.structure.CommentResponse;
import com.nyuen.five00dp.structure.PhotoDetailResponse;
import com.nyuen.five00dp.structure.PhotoListResponse;
import com.nyuen.five00dp.structure.User;
import com.nyuen.five00dp.util.AccountUtils;

public class ApiHelper {
    private static final String TAG = ApiHelper.class.getSimpleName();

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
    
    public static User getProfile(int userId){        
        PxApi pxapi = new PxApi(FiveHundred.CONSUMER_KEY);
        String url = "/user/" + userId + "?";
        try {
            //20 comments per page
            User out = new Gson().fromJson(
                    pxapi.get(url).toString(), 
                    User.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

}
