package com.nyuen.five00dp.api;

import android.util.Log;

import com.fivehundredpx.api.PxApi;
import com.google.gson.Gson;
import com.nyuen.five00dp.structure.CommentResponse;
import com.nyuen.five00dp.structure.PhotoResponse;

public class ApiHelper {
    private static final String TAG = ApiHelper.class.getSimpleName();

    public static PhotoResponse getPhotoStream(String feature, int rpp, int size, int page){
        PxApi pxapi = new PxApi(FiveHundred.CONSUMER_KEY);
        String url = "/photos?feature=" + feature + "&rpp=" + rpp + 
                "&image_size=" + size +
                "&page="+page;
        try {
            PhotoResponse out = new Gson().fromJson(
                    pxapi.get(url).toString(), 
                            PhotoResponse.class);
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

}
