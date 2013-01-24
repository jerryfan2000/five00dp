package com.nyuen.five00dp.api;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.fivehundredpx.api.PxApi;
import com.google.gson.Gson;
import com.nyuen.five00dp.response.CommentResponse;
import com.nyuen.five00dp.response.PostResponse;
import com.nyuen.five00dp.response.PhotoDetailResponse;
import com.nyuen.five00dp.response.PhotoListResponse;
import com.nyuen.five00dp.response.PhotoResponse;
import com.nyuen.five00dp.response.ProfileResponse;
import com.nyuen.five00dp.util.AccountUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ApiHelper {
    private static final String TAG = ApiHelper.class.getSimpleName();
    private static final String KEY_TOKEN = "auth_token";
    
    private static PxApi getApi(Context context){
        if(AccountUtils.hasAccount(context))
            return new PxApi(AccountUtils.getAccessToken(context), FiveHundred.CONSUMER_KEY, FiveHundred.CONSUMER_SECRET);
        else
            return new PxApi(FiveHundred.CONSUMER_KEY);  
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
    

    public static PhotoDetailResponse getFullPhoto(Context context, int photoId){
        PxApi pxapi = getApi(context);
        String url = "/photos/" + photoId + "?";

        try {
            String a = pxapi.get(url).toString();
            Log.e(TAG, a);
            PhotoDetailResponse out = new Gson().fromJson(
                    a, 
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
    
    public static ProfileResponse getUserProfileWithAccount(int userID, Context context){        
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
    
    public static ProfileResponse getUserProfile(int userID){        
        PxApi pxapi = new PxApi(FiveHundred.CONSUMER_KEY);
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
    
    //POST Section
    
    public static PhotoResponse votePhoto(Context context, int photoID, int vote) {
        PxApi pxapi = new PxApi(AccountUtils.getAccessToken(context), FiveHundred.CONSUMER_KEY, FiveHundred.CONSUMER_SECRET);
        String url = "/photos/" + photoID +"/vote";
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("vote", "" + vote));
        try {
            String json = pxapi.post(url, params).toString();
            PhotoResponse out = new Gson().fromJson(json, PhotoResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public static PostResponse favPhoto(Context context, int photoID) {
        PxApi pxapi = new PxApi(AccountUtils.getAccessToken(context), FiveHundred.CONSUMER_KEY, FiveHundred.CONSUMER_SECRET);
        String url = "/photos/" + photoID +"/favorite";
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            String json = pxapi.post(url, params).toString();
            PostResponse out = new Gson().fromJson(json, PostResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    
    public static PostResponse commentPhoto(Context context, int photoID, String comment) {
        PxApi pxapi = new PxApi(AccountUtils.getAccessToken(context), FiveHundred.CONSUMER_KEY, FiveHundred.CONSUMER_SECRET);
        String url = "/photos/" + photoID +"/comments";
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("body", comment));
        try {
            String json = pxapi.post(url, params).toString();
            PostResponse out = new Gson().fromJson(json, PostResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    
    //DELETE
    
    public static PostResponse removeFavPhoto(Context context, int photoID) {
        PxApi pxapi = new PxApi(AccountUtils.getAccessToken(context), FiveHundred.CONSUMER_KEY, FiveHundred.CONSUMER_SECRET);
        String url = "/photos/" + photoID +"/favorite";
        try {
            //pxapi.get(url)
            String json = pxapi.delete(url).toString();
            PostResponse out = new Gson().fromJson(json, PostResponse.class);
            return out;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    
}
