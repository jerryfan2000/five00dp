package com.nyuen.test_fivehundred.api;

import android.util.Log;

import com.fivehundredpx.api.PxApi;
import com.google.gson.Gson;
import com.nyuen.test_fivehundred.structure.CommentResponse;
import com.nyuen.test_fivehundred.structure.PhotoResponse;

public class ApiHelper {
    private static final String TAG = ApiHelper.class.getSimpleName();

    public static PhotoResponse getPhotoStream(String feature, int rpp, int size, int page){
        PxApi pxapi = new PxApi(FiveHundred.CONSUMER_KEY);

        try {
            PhotoResponse out = new Gson().fromJson(
                    pxapi.get("/photos?feature=" + feature + 
                            "&rpp=" + rpp + 
                            "&image_size=" + size +
                            "&page="+page).toString(), 
                            PhotoResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }


    public static CommentResponse getComments(int photoId, int page){
        PxApi pxapi = new PxApi(FiveHundred.CONSUMER_KEY);

        try {
            //20 comments per page
            CommentResponse out = new Gson().fromJson(
                    pxapi.get("v1/photos/" + photoId + 
                            "/comments?page="+page).toString(), 
                            CommentResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

}
